package sn.sonatel.dsi.ins.ftsirc.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.*;
import org.snmp4j.event.*;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.sonatel.dsi.ins.ftsirc.domain.Diagnostic;
import sn.sonatel.dsi.ins.ftsirc.domain.ONT;
import sn.sonatel.dsi.ins.ftsirc.repository.DiagnosticRepository;
import sn.sonatel.dsi.ins.ftsirc.repository.ONTRepository;
import sn.sonatel.dsi.ins.ftsirc.scripts.ScriptsDiagnostic;
import sn.sonatel.dsi.ins.ftsirc.service.DiagnosticService;
import sn.sonatel.dsi.ins.ftsirc.service.dto.DiagnosticDTO;
import sn.sonatel.dsi.ins.ftsirc.service.mapper.DiagnosticMapper;

/**
 * Service Implementation for managing {@link sn.sonatel.dsi.ins.ftsirc.domain.Diagnostic}.
 */
@Service
@Transactional
public class DiagnosticServiceImpl implements DiagnosticService {

    private final Logger log = LoggerFactory.getLogger(DiagnosticServiceImpl.class);

    private final DiagnosticRepository diagnosticRepository;

    private final DiagnosticMapper diagnosticMapper;
    private final ONTRepository ontRepository;

    ScriptsDiagnostic scriptsDiagnostic = new ScriptsDiagnostic();

    public DiagnosticServiceImpl(
        DiagnosticRepository diagnosticRepository,
        DiagnosticMapper diagnosticMapper,
        ONTRepository ontRepository
    ) {
        this.diagnosticRepository = diagnosticRepository;
        this.diagnosticMapper = diagnosticMapper;
        this.ontRepository = ontRepository;
    }

    @Override
    public DiagnosticDTO save(DiagnosticDTO diagnosticDTO) {
        log.debug("Request to save Diagnostic : {}", diagnosticDTO);
        Diagnostic diagnostic = diagnosticMapper.toEntity(diagnosticDTO);
        diagnostic = diagnosticRepository.save(diagnostic);
        return diagnosticMapper.toDto(diagnostic);
    }

    @Override
    public DiagnosticDTO update(DiagnosticDTO diagnosticDTO) {
        log.debug("Request to update Diagnostic : {}", diagnosticDTO);
        Diagnostic diagnostic = diagnosticMapper.toEntity(diagnosticDTO);
        diagnostic = diagnosticRepository.save(diagnostic);
        return diagnosticMapper.toDto(diagnostic);
    }

    @Override
    public Optional<DiagnosticDTO> partialUpdate(DiagnosticDTO diagnosticDTO) {
        log.debug("Request to partially update Diagnostic : {}", diagnosticDTO);

        return diagnosticRepository
            .findById(diagnosticDTO.getId())
            .map(existingDiagnostic -> {
                diagnosticMapper.partialUpdate(existingDiagnostic, diagnosticDTO);

                return existingDiagnostic;
            })
            .map(diagnosticRepository::save)
            .map(diagnosticMapper::toDto);
    }

    public Page<DiagnosticDTO> findAllWithEagerRelationships(Pageable pageable) {
        return diagnosticRepository.findAllWithEagerRelationships(pageable).map(diagnosticMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DiagnosticDTO> findOne(Long id) {
        log.debug("Request to get Diagnostic : {}", id);
        return diagnosticRepository.findOneWithEagerRelationships(id).map(diagnosticMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Diagnostic : {}", id);
        diagnosticRepository.deleteById(id);
    }

    public String diagnosticFiberCut(String serviceId) {
        try {
            ONT ont = ontRepository.findByServiceId(serviceId);
            if (ont != null) {
                //Verifer que les elements ne sont pas null

                String index = ont.getIndex();
                String ip = ont.getOlt().getIp();
                String ontId = ont.getOntIP();
                String vendeur = ont.getOlt().getVendeur();

                String currentAlarmList = scriptsDiagnostic.getCurrentAlarms(ip, index, ontId, vendeur);
                String ontpower = scriptsDiagnostic.getRxOpticalPower(ip, index, ontId, vendeur);

                if (ontpower.equals("KO") && currentAlarmList.equals("KO")) {
                    System.out.println(
                        "{'status': 'ko', 'anomalie': 'Coupure fibre', 'etat': 'Critique', 'description': 'Coupure de fibre optique', 'Recommandation': ['Remonter l'anomalie à l'équipe intervention terrain', 'Faire une mesure de réflectometrie pour localisation du point de coupure.']}"
                    );
                } else {
                    System.out.println("{'status': 'ok', 'description': 'coupure fibre Non'}");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
