package sn.sonatel.dsi.ins.ftsirc.service.mapper;

import static sn.sonatel.dsi.ins.ftsirc.domain.AnomalieAsserts.*;
import static sn.sonatel.dsi.ins.ftsirc.domain.AnomalieTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AnomalieMapperTest {

    private AnomalieMapper anomalieMapper;

    @BeforeEach
    void setUp() {
        anomalieMapper = new AnomalieMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAnomalieSample1();
        var actual = anomalieMapper.toEntity(anomalieMapper.toDto(expected));
        assertAnomalieAllPropertiesEquals(expected, actual);
    }
}
