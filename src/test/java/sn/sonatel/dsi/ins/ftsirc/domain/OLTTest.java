package sn.sonatel.dsi.ins.ftsirc.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.sonatel.dsi.ins.ftsirc.domain.OLTTestSamples.*;
import static sn.sonatel.dsi.ins.ftsirc.domain.ONTTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import sn.sonatel.dsi.ins.ftsirc.web.rest.TestUtil;

class OLTTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OLT.class);
        OLT oLT1 = getOLTSample1();
        OLT oLT2 = new OLT();
        assertThat(oLT1).isNotEqualTo(oLT2);

        oLT2.setId(oLT1.getId());
        assertThat(oLT1).isEqualTo(oLT2);

        oLT2 = getOLTSample2();
        assertThat(oLT1).isNotEqualTo(oLT2);
    }

    @Test
    void ontTest() throws Exception {
        OLT oLT = getOLTRandomSampleGenerator();
        ONT oNTBack = getONTRandomSampleGenerator();

        oLT.addOnt(oNTBack);
        assertThat(oLT.getOnts()).containsOnly(oNTBack);
        assertThat(oNTBack.getOlt()).isEqualTo(oLT);

        oLT.removeOnt(oNTBack);
        assertThat(oLT.getOnts()).doesNotContain(oNTBack);
        assertThat(oNTBack.getOlt()).isNull();

        oLT.onts(new HashSet<>(Set.of(oNTBack)));
        assertThat(oLT.getOnts()).containsOnly(oNTBack);
        assertThat(oNTBack.getOlt()).isEqualTo(oLT);

        oLT.setOnts(new HashSet<>());
        assertThat(oLT.getOnts()).doesNotContain(oNTBack);
        assertThat(oNTBack.getOlt()).isNull();
    }
}
