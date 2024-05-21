package sn.sonatel.dsi.ins.ftsirc.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DiagnosticTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Diagnostic getDiagnosticSample1() {
        return new Diagnostic().id(1L).debitUp("debitUp1").debitDown("debitDown1").powerONT("powerONT1").powerOLT("powerOLT1");
    }

    public static Diagnostic getDiagnosticSample2() {
        return new Diagnostic().id(2L).debitUp("debitUp2").debitDown("debitDown2").powerONT("powerONT2").powerOLT("powerOLT2");
    }

    public static Diagnostic getDiagnosticRandomSampleGenerator() {
        return new Diagnostic()
            .id(longCount.incrementAndGet())
            .debitUp(UUID.randomUUID().toString())
            .debitDown(UUID.randomUUID().toString())
            .powerONT(UUID.randomUUID().toString())
            .powerOLT(UUID.randomUUID().toString());
    }
}
