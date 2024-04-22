package sn.sonatel.dsi.ins.ftsirc.service.mapper;

import static sn.sonatel.dsi.ins.ftsirc.domain.SignalAsserts.*;
import static sn.sonatel.dsi.ins.ftsirc.domain.SignalTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SignalMapperTest {

    private SignalMapper signalMapper;

    @BeforeEach
    void setUp() {
        signalMapper = new SignalMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSignalSample1();
        var actual = signalMapper.toEntity(signalMapper.toDto(expected));
        assertSignalAllPropertiesEquals(expected, actual);
    }
}
