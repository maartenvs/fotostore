package fotostore


import java.security.cert.X509Certificate
import java.security.cert.CertificateException

import grails.transaction.Transactional
import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.HttpResponseException
import groovyx.net.http.Method
import org.apache.http.conn.scheme.Scheme
import org.apache.http.conn.ssl.SSLSocketFactory
import org.apache.http.conn.ssl.TrustStrategy
import static org.springframework.http.HttpStatus.*


@Transactional(readOnly = true)
class EncounterController {

    static scaffold = true
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE", addFoto: "POST"]

    def limitedAccessService

    @Transactional
    def addFoto(AddFotoCommand command) {
        def contentType = params.image?.contentType

        Encounter encounter = Encounter.get(command.id)
        if (encounter == null) {
            notFound()
            return
        }

        command.validate()
        if (command.hasErrors()) {
            respond command.errors, view:'show'
            return
        }

        def foto = new Foto(type: command.type, image: command.image, contentType: contentType)
        encounter.addToFotos(foto)
        encounter.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Encounter.label', default: 'Encounter'), encounter.id])
                redirect encounter
            }
            '*'{ respond encounter, [status: OK] }
        }
    }

    /**
     * Temporary mock for the external image processing server.
     */
    def log() {
        def now = new Date()
        def jobId = now.getTime() % 100000
        def message = "Action ${params.action} @ $now with q=${params.q}, job $jobId"
        println "request " + request.JSON
        println message
        respond message: message, id: jobId
    }

    def process(Encounter encounter) {
        def baseUrl = grailsApplication.config.fotostore.imageService.baseUrl
        def path    = grailsApplication.config.fotostore.imageService.path
        if (!baseUrl || !path) {
            def message = "baseUrl and/or path for image service not configured"
            log.error message
            flash.message = message
            render(view: "process", model: [encounterInstance: encounter], status: INTERNAL_SERVER_ERROR)
            return
        }

        if (encounter == null) {
            notFound()
            return
        }

        Date now = new Date()
        def processedFotos = encounter.fotos.collect { foto ->
            [
                foto: foto,
                temporaryAccess: limitedAccessService.newAccess(now, foto)
            ]
        }

        def requestBody = [
            name: encounter.name,
            images: processedFotos.collect {
                [
                    name: it.foto.type,
                    url: createLink(action: 'image', controller: 'limitedAccess', params: [key: it.temporaryAccess.key], absolute: true)
                ]
            }
        ]
        def httpResult = httpRequest(baseUrl, path, requestBody)

        respond(
            encounter,
            model: [
                processedFotos: processedFotos,
                httpResult:     httpResult.status,
                jobId:          httpResult.json?.id
            ]
        )
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'encounter.label', default: 'Encounter'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }

    static httpRequest(String baseUrl, String path, Map query) {
        def result
        try {
            def http = new HTTPBuilder(baseUrl)
            setIgnoreSSLIssues(http)
            http.request(Method.POST, ContentType.JSON) {
                uri.path = path
                body = query
                response.success = { resp, json ->
                    result = [ status: "${resp.status} ${resp.statusLine}", json: json ]
                }
                response.failure = { resp, reader ->
                    result = [ status: "${resp.status} ${resp.statusLine}", json: null ]
                }
            }
        }
        catch (java.net.ConnectException e) {
            result = [ status: "ConnectException, ${e.message}", json: null ]
        }
        return result
    }

    static setIgnoreSSLIssues(HTTPBuilder builder) {
        TrustStrategy trustStrat = new TrustStrategy() {
            public boolean isTrusted(X509Certificate[] chain, String authtype) throws CertificateException {
                 return true;
            }
        }
        SSLSocketFactory sslSocketFactory = new SSLSocketFactory(trustStrat, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        builder.client.connectionManager.schemeRegistry.register(new Scheme("https", 443, sslSocketFactory))
    }
}

@grails.validation.Validateable
class AddFotoCommand {
    long id
    String type
    byte[] image

    static constraints = {
        type nullable: false, blank: false
        image nullable: false, maxSize: 1024 * 1024 * 20
    }
}
