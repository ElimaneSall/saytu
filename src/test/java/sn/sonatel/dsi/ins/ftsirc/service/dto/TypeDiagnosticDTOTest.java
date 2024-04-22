package sn.sonatel.dsi.ins.ftsirc.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.sonatel.dsi.ins.ftsirc.web.rest.TestUtil;

class TypeDiagnosticDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeDiagnosticDTO.class);
        TypeDiagnosticDTO typeDiagnosticDTO1 = new TypeDiagnosticDTO();
        typeDiagnosticDTO1.setId(1L);
        TypeDiagnosticDTO typeDiagnosticDTO2 = new TypeDiagnosticDTO();
        assertThat(typeDiagnosticDTO1).isNotEqualTo(typeDiagnosticDTO2);
        typeDiagnosticDTO2.setId(typeDiagnosticDTO1.getId());
        assertThat(typeDiagnosticDTO1).isEqualTo(typeDiagnosticDTO2);
        typeDiagnosticDTO2.setId(2L);
        assertThat(typeDiagnosticDTO1).isNotEqualTo(typeDiagnosticDTO2);
        typeDiagnosticDTO1.setId(null);
        assertThat(typeDiagnosticDTO1).isNotEqualTo(typeDiagnosticDTO2);
    }
}
