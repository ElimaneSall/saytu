package sn.sonatel.dsi.ins.ftsirc.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DiagnosticTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Diagnostic getDiagnosticSample1() {
        return new Diagnostic().id(1L).index("index1").debitUp("debitUp1").debitDown("debitDown1");
    }

    public static Diagnostic getDiagnosticSample2() {
        return new Diagnostic().id(2L).index("index2").debitUp("debitUp2").debitDown("debitDown2");
    }

    public static Diagnostic getDiagnosticRandomSampleGenerator() {
        return new Diagnostic()
            .id(longCount.incrementAndGet())
            .index(UUID.randomUUID().toString())
            .debitUp(UUID.randomUUID().toString())
            .debitDown(UUID.randomUUID().toString());
    }
}
