package sn.sonatel.dsi.ins.ftsirc.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.sonatel.dsi.ins.ftsirc.domain.ClientTestSamples.*;
import static sn.sonatel.dsi.ins.ftsirc.domain.ONTTestSamples.*;
import static sn.sonatel.dsi.ins.ftsirc.domain.OffreTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.sonatel.dsi.ins.ftsirc.web.rest.TestUtil;

class ClientTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Client.class);
        Client client1 = getClientSample1();
        Client client2 = new Client();
        assertThat(client1).isNotEqualTo(client2);

        client2.setId(client1.getId());
        assertThat(client1).isEqualTo(client2);

        client2 = getClientSample2();
        assertThat(client1).isNotEqualTo(client2);
    }

    @Test
    void offreTest() throws Exception {
        Client client = getClientRandomSampleGenerator();
        Offre offreBack = getOffreRandomSampleGenerator();

        client.setOffre(offreBack);
        assertThat(client.getOffre()).isEqualTo(offreBack);

        client.offre(null);
        assertThat(client.getOffre()).isNull();
    }

    @Test
    void ontTest() throws Exception {
        Client client = getClientRandomSampleGenerator();
        ONT oNTBack = getONTRandomSampleGenerator();

        client.setOnt(oNTBack);
        assertThat(client.getOnt()).isEqualTo(oNTBack);
        assertThat(oNTBack.getClient()).isEqualTo(client);

        client.ont(null);
        assertThat(client.getOnt()).isNull();
        assertThat(oNTBack.getClient()).isNull();
    }
}
