package sn.sonatel.dsi.ins.ftsirc.service.mapper;

import org.mapstruct.*;
import sn.sonatel.dsi.ins.ftsirc.domain.Adresse;
import sn.sonatel.dsi.ins.ftsirc.service.dto.AdresseDTO;

/**
 * Mapper for the entity {@link Adresse} and its DTO {@link AdresseDTO}.
 */
@Mapper(componentModel = "spring")
public interface AdresseMapper extends EntityMapper<AdresseDTO, Adresse> {}
