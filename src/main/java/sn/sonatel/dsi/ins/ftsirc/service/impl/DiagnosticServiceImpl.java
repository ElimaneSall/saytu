package sn.sonatel.dsi.ins.ftsirc.service.impl;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.sonatel.dsi.ins.ftsirc.domain.Anomalie;
import sn.sonatel.dsi.ins.ftsirc.domain.Diagnostic;
import sn.sonatel.dsi.ins.ftsirc.domain.ONT;
import sn.sonatel.dsi.ins.ftsirc.domain.Signal;
import sn.sonatel.dsi.ins.ftsirc.domain.enumeration.StatutONT;
import sn.sonatel.dsi.ins.ftsirc.repository.AnomalieRepository;
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
    private final AnomalieRepository anomalieRepository;

    ScriptsDiagnostic scriptsDiagnostic = new ScriptsDiagnostic();

    public DiagnosticServiceImpl(
        DiagnosticRepository diagnosticRepository,
        DiagnosticMapper diagnosticMapper,
        ONTRepository ontRepository,
        AnomalieRepository anomalieRepository) {
        this.diagnosticRepository = diagnosticRepository;
        this.diagnosticMapper = diagnosticMapper;
        this.ontRepository = ontRepository;
        this.anomalieRepository = anomalieRepository;
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

    @Override
    public Page<DiagnosticDTO> findAll(Pageable pageable) {
        return null;
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

    public Anomalie diagnosticPowerSupply(ONT ont) {
        try {
            if (ont != null) {
                //Verifer que les elements ne sont pas null

                String index = ont.getIndex();
                String ip = ont.getOlt().getIp();
                String ontId = ont.getOntID();
                String vendeur = ont.getOlt().getVendeur();

                String currentAlarmList = scriptsDiagnostic.getCurrentAlarms(ip, index, ontId, vendeur);
                String operStatus = scriptsDiagnostic.getOperStatus(ip, index, ontId, vendeur);
                String rowStatus = scriptsDiagnostic.getRowStatus(ip, index, ontId, vendeur);
                String ranging = scriptsDiagnostic.getRanging(ip, index, ontId, vendeur);

                if (operStatus.equals("KO") && rowStatus.equals("OK") && ranging.equals("KO") && currentAlarmList.equals("KO")) {
                    return anomalieRepository.findAllByEtatAndLibelle("Critique", "modem");

                } else {
                    System.out.println("{'status': 'ok', 'description': 'Pas de defaut alimentation electrique'}");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Anomalie diagnosticFiberCut(ONT ont) {
        try {
            if (ont != null) {
                //Verifer que les elements ne sont pas null

                String index = ont.getIndex();
                String ip = ont.getOlt().getIp();
                String ontId = ont.getOntID();
                String vendeur = ont.getOlt().getVendeur();

                String currentAlarmList = scriptsDiagnostic.getCurrentAlarms(ip, index, ontId, vendeur);
                String ontpower = scriptsDiagnostic.getRxOpticalPower(ip, index, ontId, vendeur);

                if (ontpower.equals("KO") && currentAlarmList.equals("KO")) {
                  return anomalieRepository.findAllByEtatAndLibelle("Critique", "coupure fibre");

                } else {
                    System.out.println("{'status': 'ok', 'description': 'coupure fibre Non'}");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Diagnostic diagnosticFiber(String serviceId) throws IOException {
        ONT ont = ontRepository.findByServiceId(serviceId);
        Anomalie anomaliePowerSupply = this.diagnosticPowerSupply(ont);
        Anomalie anomalieFiberCut = this.diagnosticFiberCut(ont);
        Anomalie anomaliePowerOLT = this.diagnosticOLTPowerUnderLimit(ont);
        Anomalie anomaliePowerONT =  this.diagnosticONTPowerUnderLimit(ont);
        Diagnostic diagnostic = new Diagnostic();
        diagnostic.setOnt(ont);
        diagnostic.setStatutONT(scriptsDiagnostic.getOperStatus(ont.getOlt().getIp(), ont.getIndex(), ont.getOntID(),
            ont.getOlt().getVendeur()).equalsIgnoreCase("OK")? StatutONT.ACTIF: StatutONT.INACTIF);

        diagnostic.setAnomalies(Set.of( anomaliePowerSupply, anomalieFiberCut,  anomaliePowerONT, anomaliePowerOLT));
        return diagnostic;
    }

    public Anomalie  diagnosticOLTPowerUnderLimit(ONT ont) throws IOException {
        Diagnostic diagnostic = new Diagnostic();
        if (ont != null) {
            Double oltPower = scriptsDiagnostic.getOLTRxPower(
                ont.getOlt().getVendeur(),
                ont.getIndex(),
                ont.getOlt().getIp(),
                ont.getOntID()
            );
            Double sfpclass = scriptsDiagnostic.getPowerOLT(
                ont.getOlt().getVendeur(),
                ont.getIndex(),
                ont.getOlt().getIp(),
                ont.getOntID(),
                Integer.parseInt(ont.getSlot()),
                ont.getPon()
            );

            if (ont.getOlt().getVendeur().equalsIgnoreCase("NOKIA")) {
                if (oltPower == 32768) {
                    diagnostic.setOnt(ont);
                    return anomalieRepository.findAllByEtatAndLibelle("Critique", "OLT");
                } else if (oltPower != 32768) {
                    Double oltPower_dbm = oltPower / 10;
                    if ((sfpclass == 7 || sfpclass == 8) && oltPower_dbm <= -30) {
                        return anomalieRepository.findAllByEtatAndLibelle("Majeur", "OLT");

                    } else if (((sfpclass == 7 || sfpclass == 8) && (oltPower_dbm > -30 && oltPower_dbm <= -27)) || oltPower_dbm > 10) {
                        return anomalieRepository.findAllByEtatAndLibelle("Moyen", "OLT");
                    } else {
                        //                       'status': 'ok',
                        //                       'description': 'Puissance signal ONT reçu par OLT OK',
                        //                       'signal_optique': oltpower_dbm
                    }
                }
            } else if (ont.getOlt().getVendeur().equalsIgnoreCase("HUAWEI")) {
                if (oltPower == 2147483647) {
         diagnostic.setOnt(ont);
                    return anomalieRepository.findAllByEtatAndLibelle("Critique", "OLT");

                } else if (oltPower != 2147483647) {
                    Double oltPower_dbm = (oltPower - 10000) / 100;
                    if (sfpclass != 102 && oltPower_dbm <= -30) {
                        return anomalieRepository.findAllByEtatAndLibelle("Majeur", "OLT");

                    } else if (
                        ((sfpclass != 102 && (oltPower_dbm > -30 && oltPower_dbm <= -27)) || oltPower_dbm > 10) ||
                        (sfpclass == 102 && (oltPower_dbm < -30 || oltPower_dbm > 10))
                    ) {
                        return anomalieRepository.findAllByEtatAndLibelle("Moyen", "OLT");

                    } else {
                        //                       'status': 'ok',
                        //                       'description': 'Puissance signal ONT reçu par OLT OK',
                        //                       'signal_optique': oltpower_dbm
                    }
                }
            }
        }
        return null;
    }

    public Anomalie diagnosticONTPowerUnderLimit(ONT ont) throws IOException {
        Diagnostic diagnostic = new Diagnostic();

        if (ont != null) {
            Double oltPower = scriptsDiagnostic.getONTRxPower(
                ont.getOlt().getVendeur(),
                ont.getIndex(),
                ont.getOlt().getIp(),
                ont.getOntID()
            );
            Double sfpclass = scriptsDiagnostic.getPowerOLT(
                ont.getOlt().getVendeur(),
                ont.getIndex(),
                ont.getOlt().getIp(),
                ont.getOntID(),
                Integer.parseInt(ont.getSlot()),
                ont.getPon()
            );

            if (ont.getOlt().getVendeur().equalsIgnoreCase("NOKIA")) {
                if (oltPower == 32768) {
                    return anomalieRepository.findAllByEtatAndLibelle("Critique", "ONT");
                } else if (oltPower != 32768) {
                    Double oltPower_dbm = (oltPower * 2) / 10;
                    if (oltPower_dbm <= -30) {
                        return anomalieRepository.findAllByEtatAndLibelle("Majeur", "ONT");
                    } else if ((oltPower_dbm > -30 && oltPower_dbm <= -27) || oltPower_dbm > 10) {
                        return anomalieRepository.findAllByEtatAndLibelle("Moyen", "ONT");
                    } else {
                        //                       'status': 'ok',
                        //                       'description': 'Puissance signal ONT OK',
                        //                       'signal_optique': oltpower_dbm
                    }
                }
            } else if (ont.getOlt().getVendeur().equalsIgnoreCase("HUAWEI")) {
                if (oltPower == 2147483647) {
                    diagnostic.setOnt(ont);
                    return anomalieRepository.findAllByEtatAndLibelle("Critique", "ONT");
                } else if (oltPower != 2147483647) {
                    Double oltPower_dbm = oltPower / 100;
                    if (sfpclass != 102 && oltPower_dbm <= -30) {
                        return anomalieRepository.findAllByEtatAndLibelle("Majeur", "ONT");
                    } else if (
                        ((sfpclass != 102 && (oltPower_dbm > -30 && oltPower_dbm <= -27)) || oltPower_dbm > 10) ||
                        (sfpclass == 102 && (oltPower_dbm < -30 || oltPower_dbm > 10))
                    ) {
                        return anomalieRepository.findAllByEtatAndLibelle("Moyen", "ONT");
                    } else {
                        //                       'status': 'ok',
                        //                       'description': 'Puissance signal ONT reçu par OLT OK',
                        //                       'signal_optique': oltpower_dbm
                    }
                }
            }
        }
        return null;
    }

    @Override
    public String diagnosticDebit(ONT ont) throws IOException {
        return null;
    }
}
