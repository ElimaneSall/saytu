package sn.sonatel.dsi.ins.ftsirc.service.mapper;

import static sn.sonatel.dsi.ins.ftsirc.domain.ONTAsserts.*;
import static sn.sonatel.dsi.ins.ftsirc.domain.ONTTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ONTMapperTest {

    private ONTMapper oNTMapper;

    @BeforeEach
    void setUp() {
        oNTMapper = new ONTMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getONTSample1();
        var actual = oNTMapper.toEntity(oNTMapper.toDto(expected));
        assertONTAllPropertiesEquals(expected, actual);
    }
}
