package sn.sonatel.dsi.ins.ftsirc.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.sonatel.dsi.ins.ftsirc.web.rest.TestUtil;

class AnomalieDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AnomalieDTO.class);
        AnomalieDTO anomalieDTO1 = new AnomalieDTO();
        anomalieDTO1.setId(1L);
        AnomalieDTO anomalieDTO2 = new AnomalieDTO();
        assertThat(anomalieDTO1).isNotEqualTo(anomalieDTO2);
        anomalieDTO2.setId(anomalieDTO1.getId());
        assertThat(anomalieDTO1).isEqualTo(anomalieDTO2);
        anomalieDTO2.setId(2L);
        assertThat(anomalieDTO1).isNotEqualTo(anomalieDTO2);
        anomalieDTO1.setId(null);
        assertThat(anomalieDTO1).isNotEqualTo(anomalieDTO2);
    }
}
