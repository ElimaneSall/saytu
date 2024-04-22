package sn.sonatel.dsi.ins.ftsirc.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MetriqueTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Metrique getMetriqueSample1() {
        return new Metrique().id(1L).oltPower("oltPower1").ontPower("ontPower1");
    }

    public static Metrique getMetriqueSample2() {
        return new Metrique().id(2L).oltPower("oltPower2").ontPower("ontPower2");
    }

    public static Metrique getMetriqueRandomSampleGenerator() {
        return new Metrique().id(longCount.incrementAndGet()).oltPower(UUID.randomUUID().toString()).ontPower(UUID.randomUUID().toString());
    }
}
