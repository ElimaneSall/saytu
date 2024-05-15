package sn.sonatel.dsi.ins.ftsirc.service.mapper;

import org.mapstruct.*;
import sn.sonatel.dsi.ins.ftsirc.domain.Anomalie;
import sn.sonatel.dsi.ins.ftsirc.service.dto.AnomalieDTO;

/**
 * Mapper for the entity {@link Anomalie} and its DTO {@link AnomalieDTO}.
 */
@Mapper(componentModel = "spring")
public interface AnomalieMapper extends EntityMapper<AnomalieDTO, Anomalie> {}
