package sn.sonatel.dsi.ins.ftsirc.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ONTTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ONT getONTSample1() {
        return new ONT()
            .id(1L)
            .index("index1")
            .ontID("ontID1")
            .serviceId("serviceId1")
            .slot("slot1")
            .pon("pon1")
            .ponIndex("ponIndex1")
            .maxUp("maxUp1")
            .maxDown("maxDown1");
    }

    public static ONT getONTSample2() {
        return new ONT()
            .id(2L)
            .index("index2")
            .ontID("ontID2")
            .serviceId("serviceId2")
            .slot("slot2")
            .pon("pon2")
            .ponIndex("ponIndex2")
            .maxUp("maxUp2")
            .maxDown("maxDown2");
    }

    public static ONT getONTRandomSampleGenerator() {
        return new ONT()
            .id(longCount.incrementAndGet())
            .index(UUID.randomUUID().toString())
            .ontID(UUID.randomUUID().toString())
            .serviceId(UUID.randomUUID().toString())
            .slot(UUID.randomUUID().toString())
            .pon(UUID.randomUUID().toString())
            .ponIndex(UUID.randomUUID().toString())
            .maxUp(UUID.randomUUID().toString())
            .maxDown(UUID.randomUUID().toString());
    }
}
