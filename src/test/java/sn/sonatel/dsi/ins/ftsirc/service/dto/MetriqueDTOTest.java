package sn.sonatel.dsi.ins.ftsirc.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.sonatel.dsi.ins.ftsirc.web.rest.TestUtil;

class MetriqueDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MetriqueDTO.class);
        MetriqueDTO metriqueDTO1 = new MetriqueDTO();
        metriqueDTO1.setId(1L);
        MetriqueDTO metriqueDTO2 = new MetriqueDTO();
        assertThat(metriqueDTO1).isNotEqualTo(metriqueDTO2);
        metriqueDTO2.setId(metriqueDTO1.getId());
        assertThat(metriqueDTO1).isEqualTo(metriqueDTO2);
        metriqueDTO2.setId(2L);
        assertThat(metriqueDTO1).isNotEqualTo(metriqueDTO2);
        metriqueDTO1.setId(null);
        assertThat(metriqueDTO1).isNotEqualTo(metriqueDTO2);
    }
}
