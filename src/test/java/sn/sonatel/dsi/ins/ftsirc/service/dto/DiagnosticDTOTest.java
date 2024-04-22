package sn.sonatel.dsi.ins.ftsirc.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.sonatel.dsi.ins.ftsirc.web.rest.TestUtil;

class DiagnosticDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DiagnosticDTO.class);
        DiagnosticDTO diagnosticDTO1 = new DiagnosticDTO();
        diagnosticDTO1.setId(1L);
        DiagnosticDTO diagnosticDTO2 = new DiagnosticDTO();
        assertThat(diagnosticDTO1).isNotEqualTo(diagnosticDTO2);
        diagnosticDTO2.setId(diagnosticDTO1.getId());
        assertThat(diagnosticDTO1).isEqualTo(diagnosticDTO2);
        diagnosticDTO2.setId(2L);
        assertThat(diagnosticDTO1).isNotEqualTo(diagnosticDTO2);
        diagnosticDTO1.setId(null);
        assertThat(diagnosticDTO1).isNotEqualTo(diagnosticDTO2);
    }
}
