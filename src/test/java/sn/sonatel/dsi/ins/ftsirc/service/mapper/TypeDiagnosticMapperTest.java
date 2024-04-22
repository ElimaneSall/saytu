package sn.sonatel.dsi.ins.ftsirc.service.mapper;

import static sn.sonatel.dsi.ins.ftsirc.domain.TypeDiagnosticAsserts.*;
import static sn.sonatel.dsi.ins.ftsirc.domain.TypeDiagnosticTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TypeDiagnosticMapperTest {

    private TypeDiagnosticMapper typeDiagnosticMapper;

    @BeforeEach
    void setUp() {
        typeDiagnosticMapper = new TypeDiagnosticMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTypeDiagnosticSample1();
        var actual = typeDiagnosticMapper.toEntity(typeDiagnosticMapper.toDto(expected));
        assertTypeDiagnosticAllPropertiesEquals(expected, actual);
    }
}
