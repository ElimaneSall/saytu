package sn.sonatel.dsi.ins.ftsirc.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;
import sn.sonatel.dsi.ins.ftsirc.domain.Anomalie;
import sn.sonatel.dsi.ins.ftsirc.domain.Diagnostic;
import sn.sonatel.dsi.ins.ftsirc.service.dto.AnomalieDTO;
import sn.sonatel.dsi.ins.ftsirc.service.dto.DiagnosticDTO;

/**
 * Mapper for the entity {@link Anomalie} and its DTO {@link AnomalieDTO}.
 */
@Mapper(componentModel = "spring")
public interface AnomalieMapper extends EntityMapper<AnomalieDTO, Anomalie> {
    @Mapping(target = "diagnostics", source = "diagnostics", qualifiedByName = "diagnosticIdSet")
    AnomalieDTO toDto(Anomalie s);

    @Mapping(target = "diagnostics", ignore = true)
    @Mapping(target = "removeDiagnostic", ignore = true)
    Anomalie toEntity(AnomalieDTO anomalieDTO);

    @Named("diagnosticId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DiagnosticDTO toDtoDiagnosticId(Diagnostic diagnostic);

    @Named("diagnosticIdSet")
    default Set<DiagnosticDTO> toDtoDiagnosticIdSet(Set<Diagnostic> diagnostic) {
        return diagnostic.stream().map(this::toDtoDiagnosticId).collect(Collectors.toSet());
    }
}
