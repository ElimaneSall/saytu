package sn.sonatel.dsi.ins.ftsirc.service.mapper;

import org.mapstruct.*;
import sn.sonatel.dsi.ins.ftsirc.domain.Client;
import sn.sonatel.dsi.ins.ftsirc.domain.OLT;
import sn.sonatel.dsi.ins.ftsirc.domain.ONT;
import sn.sonatel.dsi.ins.ftsirc.service.dto.ClientDTO;
import sn.sonatel.dsi.ins.ftsirc.service.dto.OLTDTO;
import sn.sonatel.dsi.ins.ftsirc.service.dto.ONTDTO;

/**
 * Mapper for the entity {@link ONT} and its DTO {@link ONTDTO}.
 */
@Mapper(componentModel = "spring")
public interface ONTMapper extends EntityMapper<ONTDTO, ONT> {
    @Mapping(target = "client", source = "client", qualifiedByName = "clientNumeroFixe")
    @Mapping(target = "olt", source = "olt", qualifiedByName = "oLTId")
    ONTDTO toDto(ONT s);

    @Named("clientNumeroFixe")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "numeroFixe", source = "numeroFixe")
    ClientDTO toDtoClientNumeroFixe(Client client);

    @Named("oLTId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OLTDTO toDtoOLTId(OLT oLT);
}
