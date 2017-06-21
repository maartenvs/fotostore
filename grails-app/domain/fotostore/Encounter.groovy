package fotostore

class Encounter {

    String name
    Date date

    static hasMany = [
        fotos: Foto
    ]

    static constraints = {
        name nullable: false, blank: false
    }
}
