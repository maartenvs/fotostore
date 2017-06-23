package fotostore


import grails.transaction.Transactional

import java.security.SecureRandom


@Transactional
class LimitedAccessService {

    static final SecureRandom numberGenerator = new SecureRandom();

    def newAccess(Date now, Foto foto) {
        new LimitedAccess(start: now, foto: foto, key: generateKey()).save(flush: true)
    }

    static String generateKey() {
        byte[] randomBytes = new byte[16];
        numberGenerator.nextBytes(randomBytes);
        return randomBytes.encodeBase64().toString().minus("==").tr("+/", "-_")
    }
}
