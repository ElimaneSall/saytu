package sn.sonatel.dsi.ins.ftsirc.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.sonatel.dsi.ins.ftsirc.domain.ClientTestSamples.*;
import static sn.sonatel.dsi.ins.ftsirc.domain.DiagnosticTestSamples.*;
import static sn.sonatel.dsi.ins.ftsirc.domain.MetriqueTestSamples.*;
import static sn.sonatel.dsi.ins.ftsirc.domain.OLTTestSamples.*;
import static sn.sonatel.dsi.ins.ftsirc.domain.ONTTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import sn.sonatel.dsi.ins.ftsirc.web.rest.TestUtil;

class ONTTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ONT.class);
        ONT oNT1 = getONTSample1();
        ONT oNT2 = new ONT();
        assertThat(oNT1).isNotEqualTo(oNT2);

        oNT2.setId(oNT1.getId());
        assertThat(oNT1).isEqualTo(oNT2);

        oNT2 = getONTSample2();
        assertThat(oNT1).isNotEqualTo(oNT2);
    }

    @Test
    void clientTest() throws Exception {
        ONT oNT = getONTRandomSampleGenerator();
        Client clientBack = getClientRandomSampleGenerator();

        oNT.setClient(clientBack);
        assertThat(oNT.getClient()).isEqualTo(clientBack);

        oNT.client(null);
        assertThat(oNT.getClient()).isNull();
    }

    @Test
    void oltTest() throws Exception {
        ONT oNT = getONTRandomSampleGenerator();
        OLT oLTBack = getOLTRandomSampleGenerator();

        oNT.setOlt(oLTBack);
        assertThat(oNT.getOlt()).isEqualTo(oLTBack);

        oNT.olt(null);
        assertThat(oNT.getOlt()).isNull();
    }

    @Test
    void diagnosticTest() throws Exception {
        ONT oNT = getONTRandomSampleGenerator();
        Diagnostic diagnosticBack = getDiagnosticRandomSampleGenerator();

        oNT.addDiagnostic(diagnosticBack);
        assertThat(oNT.getDiagnostics()).containsOnly(diagnosticBack);
        assertThat(diagnosticBack.getOnt()).isEqualTo(oNT);

        oNT.removeDiagnostic(diagnosticBack);
        assertThat(oNT.getDiagnostics()).doesNotContain(diagnosticBack);
        assertThat(diagnosticBack.getOnt()).isNull();

        oNT.diagnostics(new HashSet<>(Set.of(diagnosticBack)));
        assertThat(oNT.getDiagnostics()).containsOnly(diagnosticBack);
        assertThat(diagnosticBack.getOnt()).isEqualTo(oNT);

        oNT.setDiagnostics(new HashSet<>());
        assertThat(oNT.getDiagnostics()).doesNotContain(diagnosticBack);
        assertThat(diagnosticBack.getOnt()).isNull();
    }

    @Test
    void metriqueTest() throws Exception {
        ONT oNT = getONTRandomSampleGenerator();
        Metrique metriqueBack = getMetriqueRandomSampleGenerator();

        oNT.addMetrique(metriqueBack);
        assertThat(oNT.getMetriques()).containsOnly(metriqueBack);
        assertThat(metriqueBack.getOnt()).isEqualTo(oNT);

        oNT.removeMetrique(metriqueBack);
        assertThat(oNT.getMetriques()).doesNotContain(metriqueBack);
        assertThat(metriqueBack.getOnt()).isNull();

        oNT.metriques(new HashSet<>(Set.of(metriqueBack)));
        assertThat(oNT.getMetriques()).containsOnly(metriqueBack);
        assertThat(metriqueBack.getOnt()).isEqualTo(oNT);

        oNT.setMetriques(new HashSet<>());
        assertThat(oNT.getMetriques()).doesNotContain(metriqueBack);
        assertThat(metriqueBack.getOnt()).isNull();
    }
}
