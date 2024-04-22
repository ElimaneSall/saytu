package sn.sonatel.dsi.ins.ftsirc.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.sonatel.dsi.ins.ftsirc.domain.AdresseTestSamples.*;
import static sn.sonatel.dsi.ins.ftsirc.domain.OLTTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.sonatel.dsi.ins.ftsirc.web.rest.TestUtil;

class AdresseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Adresse.class);
        Adresse adresse1 = getAdresseSample1();
        Adresse adresse2 = new Adresse();
        assertThat(adresse1).isNotEqualTo(adresse2);

        adresse2.setId(adresse1.getId());
        assertThat(adresse1).isEqualTo(adresse2);

        adresse2 = getAdresseSample2();
        assertThat(adresse1).isNotEqualTo(adresse2);
    }

    @Test
    void oltTest() throws Exception {
        Adresse adresse = getAdresseRandomSampleGenerator();
        OLT oLTBack = getOLTRandomSampleGenerator();

        adresse.setOlt(oLTBack);
        assertThat(adresse.getOlt()).isEqualTo(oLTBack);
        assertThat(oLTBack.getAdresse()).isEqualTo(adresse);

        adresse.olt(null);
        assertThat(adresse.getOlt()).isNull();
        assertThat(oLTBack.getAdresse()).isNull();
    }
}
