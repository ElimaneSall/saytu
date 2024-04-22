package sn.sonatel.dsi.ins.ftsirc.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.sonatel.dsi.ins.ftsirc.domain.MetriqueTestSamples.*;
import static sn.sonatel.dsi.ins.ftsirc.domain.ONTTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.sonatel.dsi.ins.ftsirc.web.rest.TestUtil;

class MetriqueTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Metrique.class);
        Metrique metrique1 = getMetriqueSample1();
        Metrique metrique2 = new Metrique();
        assertThat(metrique1).isNotEqualTo(metrique2);

        metrique2.setId(metrique1.getId());
        assertThat(metrique1).isEqualTo(metrique2);

        metrique2 = getMetriqueSample2();
        assertThat(metrique1).isNotEqualTo(metrique2);
    }

    @Test
    void ontTest() throws Exception {
        Metrique metrique = getMetriqueRandomSampleGenerator();
        ONT oNTBack = getONTRandomSampleGenerator();

        metrique.setOnt(oNTBack);
        assertThat(metrique.getOnt()).isEqualTo(oNTBack);

        metrique.ont(null);
        assertThat(metrique.getOnt()).isNull();
    }
}
