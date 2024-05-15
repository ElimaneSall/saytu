package sn.sonatel.dsi.ins.ftsirc.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AnomalieTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Anomalie getAnomalieSample1() {
        return new Anomalie().id(1L).libelle("libelle1").etat("etat1");
    }

    public static Anomalie getAnomalieSample2() {
        return new Anomalie().id(2L).libelle("libelle2").etat("etat2");
    }

    public static Anomalie getAnomalieRandomSampleGenerator() {
        return new Anomalie().id(longCount.incrementAndGet()).libelle(UUID.randomUUID().toString()).etat(UUID.randomUUID().toString());
    }
}
