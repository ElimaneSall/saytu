package sn.sonatel.dsi.ins.ftsirc.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TypeDiagnosticTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TypeDiagnostic getTypeDiagnosticSample1() {
        return new TypeDiagnostic().id(1L).libelle("libelle1");
    }

    public static TypeDiagnostic getTypeDiagnosticSample2() {
        return new TypeDiagnostic().id(2L).libelle("libelle2");
    }

    public static TypeDiagnostic getTypeDiagnosticRandomSampleGenerator() {
        return new TypeDiagnostic().id(longCount.incrementAndGet()).libelle(UUID.randomUUID().toString());
    }
}
