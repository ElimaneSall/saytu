package sn.sonatel.dsi.ins.ftsirc.service.mapper;

import static sn.sonatel.dsi.ins.ftsirc.domain.MetriqueAsserts.*;
import static sn.sonatel.dsi.ins.ftsirc.domain.MetriqueTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MetriqueMapperTest {

    private MetriqueMapper metriqueMapper;

    @BeforeEach
    void setUp() {
        metriqueMapper = new MetriqueMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMetriqueSample1();
        var actual = metriqueMapper.toEntity(metriqueMapper.toDto(expected));
        assertMetriqueAllPropertiesEquals(expected, actual);
    }
}
