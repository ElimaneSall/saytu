package sn.sonatel.dsi.ins.ftsirc.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.sonatel.dsi.ins.ftsirc.web.rest.TestUtil;

class OLTDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OLTDTO.class);
        OLTDTO oLTDTO1 = new OLTDTO();
        oLTDTO1.setId(1L);
        OLTDTO oLTDTO2 = new OLTDTO();
        assertThat(oLTDTO1).isNotEqualTo(oLTDTO2);
        oLTDTO2.setId(oLTDTO1.getId());
        assertThat(oLTDTO1).isEqualTo(oLTDTO2);
        oLTDTO2.setId(2L);
        assertThat(oLTDTO1).isNotEqualTo(oLTDTO2);
        oLTDTO1.setId(null);
        assertThat(oLTDTO1).isNotEqualTo(oLTDTO2);
    }
}
