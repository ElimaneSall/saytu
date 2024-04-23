package sn.sonatel.dsi.ins.ftsirc.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.sonatel.dsi.ins.ftsirc.domain.AnomalieTestSamples.*;
import static sn.sonatel.dsi.ins.ftsirc.domain.DiagnosticTestSamples.*;
import static sn.sonatel.dsi.ins.ftsirc.domain.ONTTestSamples.*;
import static sn.sonatel.dsi.ins.ftsirc.domain.SignalTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import sn.sonatel.dsi.ins.ftsirc.web.rest.TestUtil;

class DiagnosticTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Diagnostic.class);
        Diagnostic diagnostic1 = getDiagnosticSample1();
        Diagnostic diagnostic2 = new Diagnostic();
        assertThat(diagnostic1).isNotEqualTo(diagnostic2);

        diagnostic2.setId(diagnostic1.getId());
        assertThat(diagnostic1).isEqualTo(diagnostic2);

        diagnostic2 = getDiagnosticSample2();
        assertThat(diagnostic1).isNotEqualTo(diagnostic2);
    }

    @Test
    void signalTest() throws Exception {
        Diagnostic diagnostic = getDiagnosticRandomSampleGenerator();
        Signal signalBack = getSignalRandomSampleGenerator();

        diagnostic.setSignal(signalBack);
        assertThat(diagnostic.getSignal()).isEqualTo(signalBack);

        diagnostic.signal(null);
        assertThat(diagnostic.getSignal()).isNull();
    }

    @Test
    void ontTest() throws Exception {
        Diagnostic diagnostic = getDiagnosticRandomSampleGenerator();
        ONT oNTBack = getONTRandomSampleGenerator();

        diagnostic.setOnt(oNTBack);
        assertThat(diagnostic.getOnt()).isEqualTo(oNTBack);

        diagnostic.ont(null);
        assertThat(diagnostic.getOnt()).isNull();
    }

    @Test
    void anomalieTest() throws Exception {
        Diagnostic diagnostic = getDiagnosticRandomSampleGenerator();
        Anomalie anomalieBack = getAnomalieRandomSampleGenerator();

        diagnostic.addAnomalie(anomalieBack);
        assertThat(diagnostic.getAnomalies()).containsOnly(anomalieBack);

        diagnostic.removeAnomalie(anomalieBack);
        assertThat(diagnostic.getAnomalies()).doesNotContain(anomalieBack);

        diagnostic.anomalies(new HashSet<>(Set.of(anomalieBack)));
        assertThat(diagnostic.getAnomalies()).containsOnly(anomalieBack);

        diagnostic.setAnomalies(new HashSet<>());
        assertThat(diagnostic.getAnomalies()).doesNotContain(anomalieBack);
    }
}
