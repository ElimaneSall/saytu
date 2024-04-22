package sn.sonatel.dsi.ins.ftsirc.service.mapper;

import org.mapstruct.*;
import sn.sonatel.dsi.ins.ftsirc.domain.Adresse;
import sn.sonatel.dsi.ins.ftsirc.domain.OLT;
import sn.sonatel.dsi.ins.ftsirc.service.dto.AdresseDTO;
import sn.sonatel.dsi.ins.ftsirc.service.dto.OLTDTO;

/**
 * Mapper for the entity {@link OLT} and its DTO {@link OLTDTO}.
 */
@Mapper(componentModel = "spring")
public interface OLTMapper extends EntityMapper<OLTDTO, OLT> {
    @Mapping(target = "adresse", source = "adresse", qualifiedByName = "adresseRegion")
    OLTDTO toDto(OLT s);

    @Named("adresseRegion")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "region", source = "region")
    AdresseDTO toDtoAdresseRegion(Adresse adresse);
}
