package sn.sonatel.dsi.ins.ftsirc.service.mapper;

import org.mapstruct.*;
import sn.sonatel.dsi.ins.ftsirc.domain.TypeDiagnostic;
import sn.sonatel.dsi.ins.ftsirc.service.dto.TypeDiagnosticDTO;

/**
 * Mapper for the entity {@link TypeDiagnostic} and its DTO {@link TypeDiagnosticDTO}.
 */
@Mapper(componentModel = "spring")
public interface TypeDiagnosticMapper extends EntityMapper<TypeDiagnosticDTO, TypeDiagnostic> {}
