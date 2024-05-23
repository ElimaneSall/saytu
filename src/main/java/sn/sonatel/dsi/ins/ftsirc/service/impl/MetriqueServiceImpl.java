package sn.sonatel.dsi.ins.ftsirc.service.impl;

import java.io.IOException;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.sonatel.dsi.ins.ftsirc.domain.Metrique;
import sn.sonatel.dsi.ins.ftsirc.domain.ONT;
import sn.sonatel.dsi.ins.ftsirc.repository.MetriqueRepository;
import sn.sonatel.dsi.ins.ftsirc.repository.ONTRepository;
import sn.sonatel.dsi.ins.ftsirc.scripts.ScriptsDiagnostic;
import sn.sonatel.dsi.ins.ftsirc.service.MetriqueService;
import sn.sonatel.dsi.ins.ftsirc.service.dto.MetriqueDTO;
import sn.sonatel.dsi.ins.ftsirc.service.mapper.MetriqueMapper;

/**
 * Service Implementation for managing {@link sn.sonatel.dsi.ins.ftsirc.domain.Metrique}.
 */
@Service
@Transactional
public class MetriqueServiceImpl implements MetriqueService {

    private final Logger log = LoggerFactory.getLogger(MetriqueServiceImpl.class);

    private final MetriqueRepository metriqueRepository;

    private final MetriqueMapper metriqueMapper;
    private final ONTRepository ontRepository;
    public MetriqueServiceImpl(
        MetriqueRepository metriqueRepository,
        MetriqueMapper metriqueMapper,
        ONTRepository ontRepository
    ) {
        this.metriqueRepository = metriqueRepository;
        this.metriqueMapper = metriqueMapper;
        this.ontRepository = ontRepository;
    }

    @Override
    public MetriqueDTO save(MetriqueDTO metriqueDTO) {
        log.debug("Request to save Metrique : {}", metriqueDTO);
        Metrique metrique = metriqueMapper.toEntity(metriqueDTO);
        metrique = metriqueRepository.save(metrique);
        return metriqueMapper.toDto(metrique);
    }

    @Override
    public MetriqueDTO update(MetriqueDTO metriqueDTO) {
        log.debug("Request to update Metrique : {}", metriqueDTO);
        Metrique metrique = metriqueMapper.toEntity(metriqueDTO);
        metrique = metriqueRepository.save(metrique);
        return metriqueMapper.toDto(metrique);
    }

    @Override
    public Optional<MetriqueDTO> partialUpdate(MetriqueDTO metriqueDTO) {
        log.debug("Request to partially update Metrique : {}", metriqueDTO);

        return metriqueRepository
            .findById(metriqueDTO.getId())
            .map(existingMetrique -> {
                metriqueMapper.partialUpdate(existingMetrique, metriqueDTO);

                return existingMetrique;
            })
            .map(metriqueRepository::save)
            .map(metriqueMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MetriqueDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Metriques");
        return metriqueRepository.findAll(pageable).map(metriqueMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MetriqueDTO> findOne(Long id) {
        log.debug("Request to get Metrique : {}", id);
        return metriqueRepository.findById(id).map(metriqueMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Metrique : {}", id);
        metriqueRepository.deleteById(id);
    }

//    public Map<String, Object> getAllMetrics(Long id) throws IOException {
//        ONT ont = ontRepository.findById(id).get();
//        Map<String, Object> listMetrics = new HashMap<>();
//        Long oltPower_dBm, ontPower_dBm;
//
//        System.out.println("debut de l'inventaire Metrics");
//
//        if (ont != null) {
//            Long oltPower = scriptsDiagnostic.getOLTRxPower(
//                ont.getOlt().getVendeur(),
//                ont.getIndex(),
//                ont.getOlt().getIp(),
//                ont.getOntID()
//            );
//            Long ontPower = scriptsDiagnostic.getONTRxPower(
//                ont.getOlt().getVendeur(),
//                ont.getIndex(),
//                ont.getOlt().getIp(),
//                ont.getOntID()
//            );
//            if (ont.getOlt().getVendeur().equalsIgnoreCase("NOKIA")) {
//                if (oltPower == 65534) {
//                    listMetrics.put("oltPower", 0);
//                } else if (oltPower != 65534) {
//                    oltPower_dBm = oltPower / 10;
//                    listMetrics.put("oltPower", oltPower_dBm);
//                }
//                if (ontPower == 32768) {
//                    listMetrics.put("ontPower", 0);
//                } else if (ontPower != 32768) {
//                    ontPower_dBm = (ontPower * 2) / 1000;
//                    listMetrics.put("ontPower", ontPower_dBm);
//                }
//            } else if (ont.getOlt().getVendeur().equalsIgnoreCase("HUAWEI")) {
//                if (oltPower == 2147483647) {
//                    listMetrics.put("olt_power", 0);
//                } else if (oltPower != 2147483647) {
//                    oltPower_dBm = (oltPower - 10000) / 100;
//                    listMetrics.put("olt_power", oltPower_dBm);
//                }
//                if (ontPower == 2147483647) {
//                    listMetrics.put("ont_power", 0);
//                } else if (ontPower != 2147483647) {
//                    ontPower_dBm = ontPower / 100;
//                    listMetrics.put("ont_power", ontPower_dBm);
//                }
//            }
//        }
//        return listMetrics;
//    }
}
