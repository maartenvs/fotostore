package fotostore



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class EncounterController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

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
