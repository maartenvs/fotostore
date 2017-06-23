package fotostore

class LimitedAccess {

    Date   start
    Foto   foto
    String key

    static constraints = {
        key nullable: false, blank: false
    }
}
