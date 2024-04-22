package sn.sonatel.dsi.ins.ftsirc.service.mapper;

import static sn.sonatel.dsi.ins.ftsirc.domain.OLTAsserts.*;
import static sn.sonatel.dsi.ins.ftsirc.domain.OLTTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OLTMapperTest {

    private OLTMapper oLTMapper;

    @BeforeEach
    void setUp() {
        oLTMapper = new OLTMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getOLTSample1();
        var actual = oLTMapper.toEntity(oLTMapper.toDto(expected));
        assertOLTAllPropertiesEquals(expected, actual);
    }
}
