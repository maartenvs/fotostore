package fotostore

class Foto {

    String type

    static constraints = {
        type nullable: false, blank: false
    }
}
