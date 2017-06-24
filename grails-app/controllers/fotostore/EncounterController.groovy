package fotostore



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

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

    def log() {
        def now = new Date()
        println "Action ${params.action} @ $now with q=${params.q}"
        render "logged @ $now with q=${params.q}"
    }

    def process(Encounter encounter) {
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

        withHttp(uri: "https://localhost:8443") {
            def html = get(path : '/fotostore/encounter/log', query : [q:'Groovy'])
            assert html.HEAD.size() == 1
            assert html.BODY.size() == 1
        }

        respond encounter, model: [ processedFotos: processedFotos ]
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
