package sn.sonatel.dsi.ins.ftsirc.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.sonatel.dsi.ins.ftsirc.web.rest.TestUtil;

class SignalDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SignalDTO.class);
        SignalDTO signalDTO1 = new SignalDTO();
        signalDTO1.setId(1L);
        SignalDTO signalDTO2 = new SignalDTO();
        assertThat(signalDTO1).isNotEqualTo(signalDTO2);
        signalDTO2.setId(signalDTO1.getId());
        assertThat(signalDTO1).isEqualTo(signalDTO2);
        signalDTO2.setId(2L);
        assertThat(signalDTO1).isNotEqualTo(signalDTO2);
        signalDTO1.setId(null);
        assertThat(signalDTO1).isNotEqualTo(signalDTO2);
    }
}
