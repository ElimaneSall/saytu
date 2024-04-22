package sn.sonatel.dsi.ins.ftsirc.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.sonatel.dsi.ins.ftsirc.domain.DiagnosticTestSamples.*;
import static sn.sonatel.dsi.ins.ftsirc.domain.TypeDiagnosticTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import sn.sonatel.dsi.ins.ftsirc.web.rest.TestUtil;

class TypeDiagnosticTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeDiagnostic.class);
        TypeDiagnostic typeDiagnostic1 = getTypeDiagnosticSample1();
        TypeDiagnostic typeDiagnostic2 = new TypeDiagnostic();
        assertThat(typeDiagnostic1).isNotEqualTo(typeDiagnostic2);

        typeDiagnostic2.setId(typeDiagnostic1.getId());
        assertThat(typeDiagnostic1).isEqualTo(typeDiagnostic2);

        typeDiagnostic2 = getTypeDiagnosticSample2();
        assertThat(typeDiagnostic1).isNotEqualTo(typeDiagnostic2);
    }

    @Test
    void diagnosticTest() throws Exception {
        TypeDiagnostic typeDiagnostic = getTypeDiagnosticRandomSampleGenerator();
        Diagnostic diagnosticBack = getDiagnosticRandomSampleGenerator();

        typeDiagnostic.addDiagnostic(diagnosticBack);
        assertThat(typeDiagnostic.getDiagnostics()).containsOnly(diagnosticBack);
        assertThat(diagnosticBack.getTypeDiagnostic()).isEqualTo(typeDiagnostic);

        typeDiagnostic.removeDiagnostic(diagnosticBack);
        assertThat(typeDiagnostic.getDiagnostics()).doesNotContain(diagnosticBack);
        assertThat(diagnosticBack.getTypeDiagnostic()).isNull();

        typeDiagnostic.diagnostics(new HashSet<>(Set.of(diagnosticBack)));
        assertThat(typeDiagnostic.getDiagnostics()).containsOnly(diagnosticBack);
        assertThat(diagnosticBack.getTypeDiagnostic()).isEqualTo(typeDiagnostic);

        typeDiagnostic.setDiagnostics(new HashSet<>());
        assertThat(typeDiagnostic.getDiagnostics()).doesNotContain(diagnosticBack);
        assertThat(diagnosticBack.getTypeDiagnostic()).isNull();
    }
}
