package fotostore

class Foto {

    String type
    byte[] image
    String contentType

    static constraints = {
        type nullable: false, blank: false
        image nullable: false, maxSize: 1024 * 1024 * 20
        contentType nullable: true, blank: true
    }
}
