package fotostore


import static org.springframework.http.HttpStatus.*


class LimitedAccessController {

    static scaffold = true

    /** in milliseconds */
    static long validityDuration = 60 * 1000

    /**
     * Checks time period that is corresponds with the specified key, returns the
     * binary image data of the associated foto if the period is current at the
     * moment.
     */
    def image(String key) {
        Date now = new Date()

        def access = LimitedAccess.findByKey(key)
        if (access == null) {
            render status: NOT_FOUND, text: "Not found"
            return
        }

        def end = new Date(access.start.getTime() + validityDuration)

        if ((!now.before(access.start)) && now.before(end)) {
            def foto = access.foto
            response.contentType =   foto.contentType ?: "image/jpeg"
            response.contentLength = foto.image?.length
            response.outputStream << foto.image
        }
        else {
            render status: NOT_FOUND, text: "Not found"
            return
        }
    }
}
