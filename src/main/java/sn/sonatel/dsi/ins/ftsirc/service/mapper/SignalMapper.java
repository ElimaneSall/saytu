package sn.sonatel.dsi.ins.ftsirc.service.mapper;

import org.mapstruct.*;
import sn.sonatel.dsi.ins.ftsirc.domain.Signal;
import sn.sonatel.dsi.ins.ftsirc.service.dto.SignalDTO;

/**
 * Mapper for the entity {@link Signal} and its DTO {@link SignalDTO}.
 */
@Mapper(componentModel = "spring")
public interface SignalMapper extends EntityMapper<SignalDTO, Signal> {}
