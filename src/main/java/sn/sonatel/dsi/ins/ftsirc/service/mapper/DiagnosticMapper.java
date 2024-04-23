package sn.sonatel.dsi.ins.ftsirc.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;
import sn.sonatel.dsi.ins.ftsirc.domain.Anomalie;
import sn.sonatel.dsi.ins.ftsirc.domain.Diagnostic;
import sn.sonatel.dsi.ins.ftsirc.domain.ONT;
import sn.sonatel.dsi.ins.ftsirc.domain.Signal;
import sn.sonatel.dsi.ins.ftsirc.service.dto.AnomalieDTO;
import sn.sonatel.dsi.ins.ftsirc.service.dto.DiagnosticDTO;
import sn.sonatel.dsi.ins.ftsirc.service.dto.ONTDTO;
import sn.sonatel.dsi.ins.ftsirc.service.dto.SignalDTO;

/**
 * Mapper for the entity {@link Diagnostic} and its DTO {@link DiagnosticDTO}.
 */
@Mapper(componentModel = "spring")
public interface DiagnosticMapper extends EntityMapper<DiagnosticDTO, Diagnostic> {
    @Mapping(target = "signal", source = "signal", qualifiedByName = "signalId")
    @Mapping(target = "ont", source = "ont", qualifiedByName = "oNTId")
    @Mapping(target = "anomalies", source = "anomalies", qualifiedByName = "anomalieIdSet")
    DiagnosticDTO toDto(Diagnostic s);

    @Mapping(target = "removeAnomalie", ignore = true)
    Diagnostic toEntity(DiagnosticDTO diagnosticDTO);

    @Named("signalId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SignalDTO toDtoSignalId(Signal signal);

    @Named("oNTId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ONTDTO toDtoONTId(ONT oNT);

    @Named("anomalieId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AnomalieDTO toDtoAnomalieId(Anomalie anomalie);

    @Named("anomalieIdSet")
    default Set<AnomalieDTO> toDtoAnomalieIdSet(Set<Anomalie> anomalie) {
        return anomalie.stream().map(this::toDtoAnomalieId).collect(Collectors.toSet());
    }
}
