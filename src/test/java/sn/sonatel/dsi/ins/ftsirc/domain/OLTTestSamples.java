package sn.sonatel.dsi.ins.ftsirc.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class OLTTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static OLT getOLTSample1() {
        return new OLT().id(1L).nom("nom1").ip("ip1").vendeur("vendeur1").etat("etat1");
    }

    public static OLT getOLTSample2() {
        return new OLT().id(2L).nom("nom2").ip("ip2").vendeur("vendeur2").etat("etat2");
    }

    public static OLT getOLTRandomSampleGenerator() {
        return new OLT()
            .id(longCount.incrementAndGet())
            .nom(UUID.randomUUID().toString())
            .ip(UUID.randomUUID().toString())
            .vendeur(UUID.randomUUID().toString())
            .etat(UUID.randomUUID().toString());
    }
}
