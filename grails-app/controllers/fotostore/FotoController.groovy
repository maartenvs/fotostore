package fotostore



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class FotoController {

    static scaffold = true
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    @Transactional
    def save(Foto fotoInstance) {
        if (fotoInstance == null) {
            notFound()
            return
        }

        fotoInstance.contentType = params.image?.contentType

        fotoInstance.validate()
        if (fotoInstance.hasErrors()) {
            respond fotoInstance.errors, view:'create'
            return
        }

        fotoInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'foto.label', default: 'Foto'), fotoInstance.id])
                redirect fotoInstance
            }
            '*' { respond fotoInstance, [status: CREATED] }
        }
    }

    @Transactional
    def update(Foto fotoInstance) {
        if (fotoInstance == null) {
            notFound()
            return
        }

        fotoInstance.contentType = params.image?.contentType

        fotoInstance.validate()
        if (fotoInstance.hasErrors()) {
            respond fotoInstance.errors, view:'edit'
            return
        }

        fotoInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Foto.label', default: 'Foto'), fotoInstance.id])
                redirect fotoInstance
            }
            '*'{ respond fotoInstance, [status: OK] }
        }
    }

    /**
     * Returns the binary image data.
     */
    def image(Foto fotoInstance) {

        if (fotoInstance == null) {
            notFound()
            return
        }

        response.contentType =   fotoInstance.contentType ?: "image/jpeg"
        response.contentLength = fotoInstance.image?.length
        response.outputStream << fotoInstance.image
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'foto.label', default: 'Foto'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
