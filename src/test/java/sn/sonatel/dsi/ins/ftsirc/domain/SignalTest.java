package sn.sonatel.dsi.ins.ftsirc.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.sonatel.dsi.ins.ftsirc.domain.DiagnosticTestSamples.*;
import static sn.sonatel.dsi.ins.ftsirc.domain.SignalTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import sn.sonatel.dsi.ins.ftsirc.web.rest.TestUtil;

class SignalTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Signal.class);
        Signal signal1 = getSignalSample1();
        Signal signal2 = new Signal();
        assertThat(signal1).isNotEqualTo(signal2);

        signal2.setId(signal1.getId());
        assertThat(signal1).isEqualTo(signal2);

        signal2 = getSignalSample2();
        assertThat(signal1).isNotEqualTo(signal2);
    }

    @Test
    void diagnosticTest() throws Exception {
        Signal signal = getSignalRandomSampleGenerator();
        Diagnostic diagnosticBack = getDiagnosticRandomSampleGenerator();

        signal.addDiagnostic(diagnosticBack);
        assertThat(signal.getDiagnostics()).containsOnly(diagnosticBack);
        assertThat(diagnosticBack.getSignal()).isEqualTo(signal);

        signal.removeDiagnostic(diagnosticBack);
        assertThat(signal.getDiagnostics()).doesNotContain(diagnosticBack);
        assertThat(diagnosticBack.getSignal()).isNull();

        signal.diagnostics(new HashSet<>(Set.of(diagnosticBack)));
        assertThat(signal.getDiagnostics()).containsOnly(diagnosticBack);
        assertThat(diagnosticBack.getSignal()).isEqualTo(signal);

        signal.setDiagnostics(new HashSet<>());
        assertThat(signal.getDiagnostics()).doesNotContain(diagnosticBack);
        assertThat(diagnosticBack.getSignal()).isNull();
    }
}
