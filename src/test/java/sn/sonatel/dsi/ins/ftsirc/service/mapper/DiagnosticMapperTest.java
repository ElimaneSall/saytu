package sn.sonatel.dsi.ins.ftsirc.service.mapper;

import static sn.sonatel.dsi.ins.ftsirc.domain.DiagnosticAsserts.*;
import static sn.sonatel.dsi.ins.ftsirc.domain.DiagnosticTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DiagnosticMapperTest {

    private DiagnosticMapper diagnosticMapper;

    @BeforeEach
    void setUp() {
        diagnosticMapper = new DiagnosticMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDiagnosticSample1();
        var actual = diagnosticMapper.toEntity(diagnosticMapper.toDto(expected));
        assertDiagnosticAllPropertiesEquals(expected, actual);
    }
}
