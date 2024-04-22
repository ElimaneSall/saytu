package sn.sonatel.dsi.ins.ftsirc.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AdresseTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Adresse getAdresseSample1() {
        return new Adresse().id(1L).region("region1").ville("ville1").commune("commune1");
    }

    public static Adresse getAdresseSample2() {
        return new Adresse().id(2L).region("region2").ville("ville2").commune("commune2");
    }

    public static Adresse getAdresseRandomSampleGenerator() {
        return new Adresse()
            .id(longCount.incrementAndGet())
            .region(UUID.randomUUID().toString())
            .ville(UUID.randomUUID().toString())
            .commune(UUID.randomUUID().toString());
    }
}
