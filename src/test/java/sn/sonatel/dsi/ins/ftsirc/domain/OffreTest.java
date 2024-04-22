package sn.sonatel.dsi.ins.ftsirc.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.sonatel.dsi.ins.ftsirc.domain.ClientTestSamples.*;
import static sn.sonatel.dsi.ins.ftsirc.domain.OffreTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import sn.sonatel.dsi.ins.ftsirc.web.rest.TestUtil;

class OffreTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Offre.class);
        Offre offre1 = getOffreSample1();
        Offre offre2 = new Offre();
        assertThat(offre1).isNotEqualTo(offre2);

        offre2.setId(offre1.getId());
        assertThat(offre1).isEqualTo(offre2);

        offre2 = getOffreSample2();
        assertThat(offre1).isNotEqualTo(offre2);
    }

    @Test
    void clientTest() throws Exception {
        Offre offre = getOffreRandomSampleGenerator();
        Client clientBack = getClientRandomSampleGenerator();

        offre.addClient(clientBack);
        assertThat(offre.getClients()).containsOnly(clientBack);
        assertThat(clientBack.getOffre()).isEqualTo(offre);

        offre.removeClient(clientBack);
        assertThat(offre.getClients()).doesNotContain(clientBack);
        assertThat(clientBack.getOffre()).isNull();

        offre.clients(new HashSet<>(Set.of(clientBack)));
        assertThat(offre.getClients()).containsOnly(clientBack);
        assertThat(clientBack.getOffre()).isEqualTo(offre);

        offre.setClients(new HashSet<>());
        assertThat(offre.getClients()).doesNotContain(clientBack);
        assertThat(clientBack.getOffre()).isNull();
    }
}
