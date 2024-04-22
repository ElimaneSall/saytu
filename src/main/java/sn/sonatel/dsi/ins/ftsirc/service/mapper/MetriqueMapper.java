package sn.sonatel.dsi.ins.ftsirc.service.mapper;

import org.mapstruct.*;
import sn.sonatel.dsi.ins.ftsirc.domain.Metrique;
import sn.sonatel.dsi.ins.ftsirc.domain.ONT;
import sn.sonatel.dsi.ins.ftsirc.service.dto.MetriqueDTO;
import sn.sonatel.dsi.ins.ftsirc.service.dto.ONTDTO;

/**
 * Mapper for the entity {@link Metrique} and its DTO {@link MetriqueDTO}.
 */
@Mapper(componentModel = "spring")
public interface MetriqueMapper extends EntityMapper<MetriqueDTO, Metrique> {
    @Mapping(target = "ont", source = "ont", qualifiedByName = "oNTId")
    MetriqueDTO toDto(Metrique s);

    @Named("oNTId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ONTDTO toDtoONTId(ONT oNT);
}
