package sn.sonatel.dsi.ins.ftsirc.service.impl;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.sonatel.dsi.ins.ftsirc.domain.Anomalie;
import sn.sonatel.dsi.ins.ftsirc.domain.Diagnostic;
import sn.sonatel.dsi.ins.ftsirc.domain.Metrique;
import sn.sonatel.dsi.ins.ftsirc.domain.ONT;
import sn.sonatel.dsi.ins.ftsirc.domain.enumeration.StatutONT;
import sn.sonatel.dsi.ins.ftsirc.domain.enumeration.TypeDiagnostic;
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
        AnomalieRepository anomalieRepository
    ) {
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
        Anomalie anomaliePowerSupply = new Anomalie();
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
                    anomaliePowerSupply = anomalieRepository.findByCode("500");
                } else {
                    anomaliePowerSupply = anomalieRepository.findByCode("205");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return anomaliePowerSupply;
    }

    public Anomalie diagnosticFiberCut(ONT ont) {
        Anomalie anomalieFiberCut = new Anomalie();
        try {
            if (ont != null) {
                String index = ont.getIndex();
                String ip = ont.getOlt().getIp();
                String ontId = ont.getOntID();
                String vendeur = ont.getOlt().getVendeur();

                String currentAlarmList = scriptsDiagnostic.getCurrentAlarms(ip, index, ontId, vendeur);
                String ontpower = scriptsDiagnostic.getRxOpticalPower(ip, index, ontId, vendeur);

                if (ontpower.equals("KO") && currentAlarmList.equals("KO")) {
                    anomalieFiberCut = anomalieRepository.findByCode("400");
                } else {
                    anomalieFiberCut = anomalieRepository.findByCode("204");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return anomalieFiberCut;
    }

    public Map<String, Object> diagnosticOLTPowerUnderLimit(ONT ont) throws IOException {
        Map<String, Object> result = new HashMap<>();
        Long oltPower_dbm;

        if (ont != null) {
            Long oltPower = scriptsDiagnostic.getOLTRxPower(
                ont.getOlt().getVendeur(),
                ont.getIndex(),
                ont.getOlt().getIp(),
                ont.getOntID()
            );
            Long sfpclass = scriptsDiagnostic.getSfpClass(
                ont.getOlt().getVendeur(),
                ont.getIndex(),
                ont.getOlt().getIp(),
                Integer.parseInt(ont.getSlot()),
                ont.getPon()
            );

            if (ont.getOlt().getVendeur().equalsIgnoreCase("NOKIA")) {
                if (oltPower == 65534) {
                    result.put("anomalie", anomalieRepository.findByCode("300"));
                    result.put("signal", 0);
                } else if (oltPower != 65534) {
                    oltPower_dbm = oltPower / 10;
                    result.put("signal", oltPower_dbm);
                    if ((sfpclass == 7 || sfpclass == 8) && oltPower_dbm <= -30) {
                        result.put("anomalie", anomalieRepository.findByCode("301"));
                    } else if (((sfpclass == 7 || sfpclass == 8) && (oltPower_dbm > -30 && oltPower_dbm <= -27)) || oltPower_dbm > 10) {
                        result.put("anomalie", anomalieRepository.findByCode("302"));
                    } else {
                        result.put("anomalie", anomalieRepository.findByCode("203"));
                    }
                }
            } else if (ont.getOlt().getVendeur().equalsIgnoreCase("HUAWEI")) {
                if (oltPower == 2147483647) {
                    result.put("signal", 0);
                    result.put("anomalie", anomalieRepository.findByCode("300"));
                } else if (oltPower != 2147483647) {
                    oltPower_dbm = (oltPower - 10000) / 100;
                    result.put("signal", oltPower_dbm);
                    if (sfpclass != 102 && oltPower_dbm <= -30) {
                        result.put("anomalie", anomalieRepository.findByCode("301"));
                    } else if (
                        ((sfpclass != 102 && (oltPower_dbm > -30 && oltPower_dbm <= -27)) || oltPower_dbm > 10) ||
                        (sfpclass == 102 && (oltPower_dbm < -30 || oltPower_dbm > 10))
                    ) {
                        result.put("anomalie", anomalieRepository.findByCode("302"));
                    } else {
                        result.put("anomalie", anomalieRepository.findByCode("203"));
                    }
                }
            }
        }
        return result;
    }

    public Map<String, Object> diagnosticONTPowerUnderLimit(ONT ont) throws IOException {
        Map<String, Object> result = new HashMap<>();
        Long ontPower_dbm;

        if (ont != null) {
            Long ontPower = scriptsDiagnostic.getONTRxPower(
                ont.getOlt().getVendeur(),
                ont.getIndex(),
                ont.getOlt().getIp(),
                ont.getOntID()
            );
            Long sfpclass = scriptsDiagnostic.getSfpClass(
                ont.getOlt().getVendeur(),
                ont.getIndex(),
                ont.getOlt().getIp(),
                Integer.parseInt(ont.getSlot()),
                ont.getPon()
            );

            if (ont.getOlt().getVendeur().equalsIgnoreCase("NOKIA")) {
                if (ontPower == 32768) {
                    result.put("anomalie", anomalieRepository.findByCode("100"));
                    result.put("signal", 0);
                } else if (ontPower != 32768) {
                    ontPower_dbm = (ontPower * 2) / 1000;
                    result.put("signal", ontPower_dbm);
                    if (ontPower_dbm <= -30) {
                        result.put("anomalie", anomalieRepository.findByCode("101"));
                    } else if ((ontPower_dbm > -30 && ontPower_dbm <= -27) || ontPower_dbm > 10) {
                        result.put("anomalie", anomalieRepository.findByCode("102"));
                    } else {
                        result.put("anomalie", anomalieRepository.findByCode("201"));
                    }
                }
            } else if (ont.getOlt().getVendeur().equalsIgnoreCase("HUAWEI")) {
                if (ontPower == 2147483647) {
                    result.put("anomalie", anomalieRepository.findByCode("100"));
                    result.put("signal", 0);
                } else if (ontPower != 2147483647) {
                    ontPower_dbm = ontPower / 100;
                    result.put("signal", ontPower_dbm);
                    if (sfpclass != 102 && ontPower_dbm <= -30) {
                        result.put("anomalie", anomalieRepository.findByCode("101"));
                        return result;
                    } else if (
                        ((sfpclass != 102 && (ontPower_dbm > -30 && ontPower_dbm <= -27)) || ontPower_dbm > 10) ||
                        (sfpclass == 102 && (ontPower_dbm < -30 || ontPower_dbm > 10))
                    ) {
                        result.put("anomalie", anomalieRepository.findByCode("102"));
                        return result;
                    } else {
                        result.put("anomalie", anomalieRepository.findByCode("201"));
                        return result;
                    }
                }
            }
        }

        return result;
    }

    @Override
    public Diagnostic diagnosticFiberManuel(String serviceId) throws IOException {
        Diagnostic diagnosticResult = new Diagnostic();
        Set<Anomalie> anomalieSet = new HashSet<>();

        ONT ont = ontRepository.findByServiceId(serviceId);
        if (ont != null) {
            Anomalie anomaliePowerSupply = this.diagnosticPowerSupply(ont);
            anomalieSet.add(anomaliePowerSupply);

            if (anomaliePowerSupply.getCode() == 205) {
                Anomalie anomalieFiberCut = this.diagnosticFiberCut(ont);
                anomalieSet.add(anomalieFiberCut);
                Map<String, Object> resultOLTPowerUnderLimit = this.diagnosticOLTPowerUnderLimit(ont);
                Map<String, Object> resultONTPowerUnderLimit = this.diagnosticONTPowerUnderLimit(ont);

                anomalieSet.add((Anomalie) resultOLTPowerUnderLimit.get("anomalie"));
                anomalieSet.add((Anomalie) resultONTPowerUnderLimit.get("anomalie"));
                diagnosticResult.setPowerOLT(resultOLTPowerUnderLimit.get("signal").toString());
                diagnosticResult.setPowerONT(resultONTPowerUnderLimit.get("signal").toString());
            }

            diagnosticResult.setTypeDiagnostic(TypeDiagnostic.MANUEL);
            diagnosticResult.setAnomalies(anomalieSet);
            diagnosticResult.setOnt(ont);
            diagnosticResult.setStatutONT(
                scriptsDiagnostic
                        .getOperStatus(ont.getOlt().getIp(), ont.getIndex(), ont.getOntID(), ont.getOlt().getVendeur())
                        .equalsIgnoreCase("Ok")
                    ? StatutONT.ACTIF
                    : StatutONT.INACTIF
            );
            LocalDateTime currentDateTime = LocalDateTime.now();
            diagnosticResult.setDateDiagnostic(LocalDate.from(currentDateTime));
            //        diagnosticResult.setSignal();
                    diagnosticResult.setDebitDown(scriptsDiagnostic.getDebitDown(ont.getOlt().getVendeur(), ont.getIndex(),
                        ont.getOlt().getIp()));
            //        diagnosticResult.setDebitUp();

            diagnosticRepository.save(diagnosticResult);
        }
        return diagnosticResult;
    }

    @Override
    public void diagnosticFiberAutomatique() throws IOException {
        Diagnostic diagnosticResult = new Diagnostic();
        Set<Anomalie> anomalieSet = new HashSet<>();

        List<ONT> onts = ontRepository.findAll();
        for (ONT ont : onts) {
            if (ont != null) {
                Anomalie anomaliePowerSupply = this.diagnosticPowerSupply(ont);
                anomalieSet.add(anomaliePowerSupply);

                if (anomaliePowerSupply.getCode() == 205) {
                    Anomalie anomalieFiberCut = this.diagnosticFiberCut(ont);
                    anomalieSet.add(anomalieFiberCut);
                }

                Map<String, Object> resultOLTPowerUnderLimit = this.diagnosticOLTPowerUnderLimit(ont);
                Map<String, Object> resultONTPowerUnderLimit = this.diagnosticONTPowerUnderLimit(ont);

                anomalieSet.add((Anomalie) resultOLTPowerUnderLimit.get("anomalie"));
                anomalieSet.add((Anomalie) resultONTPowerUnderLimit.get("anomalie"));
                diagnosticResult.setPowerOLT(resultOLTPowerUnderLimit.get("signal").toString());
                diagnosticResult.setPowerONT(resultONTPowerUnderLimit.get("signal").toString());

                diagnosticResult.setTypeDiagnostic(TypeDiagnostic.AUTOMATIQUE);
                diagnosticResult.setAnomalies(anomalieSet);
                diagnosticResult.setOnt(ont);
                diagnosticResult.setStatutONT(
                    scriptsDiagnostic
                            .getOperStatus(ont.getOlt().getIp(), ont.getIndex(), ont.getOntID(), ont.getOlt().getVendeur())
                            .equalsIgnoreCase("Ok")
                        ? StatutONT.ACTIF
                        : StatutONT.INACTIF
                );
                LocalDateTime currentDateTime = LocalDateTime.now();
                diagnosticResult.setDateDiagnostic(LocalDate.from(currentDateTime));

                diagnosticRepository.save(diagnosticResult);
            }
        }
        // envoie mail equipe DRPS
    }

    @Override
    public Anomalie diagnosticDebit(ONT ont) throws IOException {
        return null;
    }
}
