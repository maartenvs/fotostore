package fotostore



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class EncounterController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE", addFoto: "POST"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Encounter.list(params), model:[encounterInstanceCount: Encounter.count()]
    }

    def show(Encounter encounterInstance) {
        respond encounterInstance
    }

    def create() {
        respond new Encounter(params)
    }

    @Transactional
    def save(Encounter encounterInstance) {
        if (encounterInstance == null) {
            notFound()
            return
        }

        if (encounterInstance.hasErrors()) {
            respond encounterInstance.errors, view:'create'
            return
        }

        encounterInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'encounter.label', default: 'Encounter'), encounterInstance.id])
                redirect encounterInstance
            }
            '*' { respond encounterInstance, [status: CREATED] }
        }
    }

    def edit(Encounter encounterInstance) {
        respond encounterInstance
    }

    @Transactional
    def update(Encounter encounterInstance) {
        if (encounterInstance == null) {
            notFound()
            return
        }

        if (encounterInstance.hasErrors()) {
            respond encounterInstance.errors, view:'edit'
            return
        }

        encounterInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Encounter.label', default: 'Encounter'), encounterInstance.id])
                redirect encounterInstance
            }
            '*'{ respond encounterInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(Encounter encounterInstance) {

        if (encounterInstance == null) {
            notFound()
            return
        }

        encounterInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Encounter.label', default: 'Encounter'), encounterInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    @Transactional
    def addFoto(AddFotoCommand command) {
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

        def foto = new Foto(type: command.type, image: command.image)
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
