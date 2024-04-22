package sn.sonatel.dsi.ins.ftsirc.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SignalTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Signal getSignalSample1() {
        return new Signal().id(1L).libelle("libelle1");
    }

    public static Signal getSignalSample2() {
        return new Signal().id(2L).libelle("libelle2");
    }

    public static Signal getSignalRandomSampleGenerator() {
        return new Signal().id(longCount.incrementAndGet()).libelle(UUID.randomUUID().toString());
    }
}
