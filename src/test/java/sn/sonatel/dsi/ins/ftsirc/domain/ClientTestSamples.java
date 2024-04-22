package sn.sonatel.dsi.ins.ftsirc.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ClientTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Client getClientSample1() {
        return new Client()
            .id(1L)
            .nclient(1L)
            .nom("nom1")
            .prenom("prenom1")
            .etat("etat1")
            .numeroFixe("numeroFixe1")
            .contactMobileClient("contactMobileClient1");
    }

    public static Client getClientSample2() {
        return new Client()
            .id(2L)
            .nclient(2L)
            .nom("nom2")
            .prenom("prenom2")
            .etat("etat2")
            .numeroFixe("numeroFixe2")
            .contactMobileClient("contactMobileClient2");
    }

    public static Client getClientRandomSampleGenerator() {
        return new Client()
            .id(longCount.incrementAndGet())
            .nclient(longCount.incrementAndGet())
            .nom(UUID.randomUUID().toString())
            .prenom(UUID.randomUUID().toString())
            .etat(UUID.randomUUID().toString())
            .numeroFixe(UUID.randomUUID().toString())
            .contactMobileClient(UUID.randomUUID().toString());
    }
}
