package sn.sonatel.dsi.ins.ftsirc.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class OffreTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Offre getOffreSample1() {
        return new Offre().id(1L).libelle("libelle1").debitMax("debitMax1");
    }

    public static Offre getOffreSample2() {
        return new Offre().id(2L).libelle("libelle2").debitMax("debitMax2");
    }

    public static Offre getOffreRandomSampleGenerator() {
        return new Offre().id(longCount.incrementAndGet()).libelle(UUID.randomUUID().toString()).debitMax(UUID.randomUUID().toString());
    }
}
