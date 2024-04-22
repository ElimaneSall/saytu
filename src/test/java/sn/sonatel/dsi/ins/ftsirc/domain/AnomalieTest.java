package sn.sonatel.dsi.ins.ftsirc.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.sonatel.dsi.ins.ftsirc.domain.AnomalieTestSamples.*;
import static sn.sonatel.dsi.ins.ftsirc.domain.DiagnosticTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import sn.sonatel.dsi.ins.ftsirc.web.rest.TestUtil;

class AnomalieTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Anomalie.class);
        Anomalie anomalie1 = getAnomalieSample1();
        Anomalie anomalie2 = new Anomalie();
        assertThat(anomalie1).isNotEqualTo(anomalie2);

        anomalie2.setId(anomalie1.getId());
        assertThat(anomalie1).isEqualTo(anomalie2);

        anomalie2 = getAnomalieSample2();
        assertThat(anomalie1).isNotEqualTo(anomalie2);
    }

    @Test
    void diagnosticTest() throws Exception {
        Anomalie anomalie = getAnomalieRandomSampleGenerator();
        Diagnostic diagnosticBack = getDiagnosticRandomSampleGenerator();

        anomalie.addDiagnostic(diagnosticBack);
        assertThat(anomalie.getDiagnostics()).containsOnly(diagnosticBack);
        assertThat(diagnosticBack.getAnomalies()).containsOnly(anomalie);

        anomalie.removeDiagnostic(diagnosticBack);
        assertThat(anomalie.getDiagnostics()).doesNotContain(diagnosticBack);
        assertThat(diagnosticBack.getAnomalies()).doesNotContain(anomalie);

        anomalie.diagnostics(new HashSet<>(Set.of(diagnosticBack)));
        assertThat(anomalie.getDiagnostics()).containsOnly(diagnosticBack);
        assertThat(diagnosticBack.getAnomalies()).containsOnly(anomalie);

        anomalie.setDiagnostics(new HashSet<>());
        assertThat(anomalie.getDiagnostics()).doesNotContain(diagnosticBack);
        assertThat(diagnosticBack.getAnomalies()).doesNotContain(anomalie);
    }
}
