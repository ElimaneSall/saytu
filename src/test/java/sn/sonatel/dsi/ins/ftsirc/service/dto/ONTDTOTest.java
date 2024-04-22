package sn.sonatel.dsi.ins.ftsirc.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.sonatel.dsi.ins.ftsirc.web.rest.TestUtil;

class ONTDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ONTDTO.class);
        ONTDTO oNTDTO1 = new ONTDTO();
        oNTDTO1.setId(1L);
        ONTDTO oNTDTO2 = new ONTDTO();
        assertThat(oNTDTO1).isNotEqualTo(oNTDTO2);
        oNTDTO2.setId(oNTDTO1.getId());
        assertThat(oNTDTO1).isEqualTo(oNTDTO2);
        oNTDTO2.setId(2L);
        assertThat(oNTDTO1).isNotEqualTo(oNTDTO2);
        oNTDTO1.setId(null);
        assertThat(oNTDTO1).isNotEqualTo(oNTDTO2);
    }
}
