package sn.sonatel.dsi.ins.ftsirc.service.mapper;

import org.mapstruct.*;
import sn.sonatel.dsi.ins.ftsirc.domain.OLT;
import sn.sonatel.dsi.ins.ftsirc.service.dto.OLTDTO;

/**
 * Mapper for the entity {@link OLT} and its DTO {@link OLTDTO}.
 */
@Mapper(componentModel = "spring")
public interface OLTMapper extends EntityMapper<OLTDTO, OLT> {}
