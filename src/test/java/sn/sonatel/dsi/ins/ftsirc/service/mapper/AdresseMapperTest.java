package sn.sonatel.dsi.ins.ftsirc.service.mapper;

import static sn.sonatel.dsi.ins.ftsirc.domain.AdresseAsserts.*;
import static sn.sonatel.dsi.ins.ftsirc.domain.AdresseTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AdresseMapperTest {

    private AdresseMapper adresseMapper;

    @BeforeEach
    void setUp() {
        adresseMapper = new AdresseMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAdresseSample1();
        var actual = adresseMapper.toEntity(adresseMapper.toDto(expected));
        assertAdresseAllPropertiesEquals(expected, actual);
    }
}
