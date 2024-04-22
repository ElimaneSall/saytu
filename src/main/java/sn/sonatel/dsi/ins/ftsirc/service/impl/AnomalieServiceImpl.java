package sn.sonatel.dsi.ins.ftsirc.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.sonatel.dsi.ins.ftsirc.domain.Anomalie;
import sn.sonatel.dsi.ins.ftsirc.repository.AnomalieRepository;
import sn.sonatel.dsi.ins.ftsirc.service.AnomalieService;
import sn.sonatel.dsi.ins.ftsirc.service.dto.AnomalieDTO;
import sn.sonatel.dsi.ins.ftsirc.service.mapper.AnomalieMapper;

/**
 * Service Implementation for managing {@link sn.sonatel.dsi.ins.ftsirc.domain.Anomalie}.
 */
@Service
@Transactional
public class AnomalieServiceImpl implements AnomalieService {

    private final Logger log = LoggerFactory.getLogger(AnomalieServiceImpl.class);

    private final AnomalieRepository anomalieRepository;

    private final AnomalieMapper anomalieMapper;

    public AnomalieServiceImpl(AnomalieRepository anomalieRepository, AnomalieMapper anomalieMapper) {
        this.anomalieRepository = anomalieRepository;
        this.anomalieMapper = anomalieMapper;
    }

    @Override
    public AnomalieDTO save(AnomalieDTO anomalieDTO) {
        log.debug("Request to save Anomalie : {}", anomalieDTO);
        Anomalie anomalie = anomalieMapper.toEntity(anomalieDTO);
        anomalie = anomalieRepository.save(anomalie);
        return anomalieMapper.toDto(anomalie);
    }

    @Override
    public AnomalieDTO update(AnomalieDTO anomalieDTO) {
        log.debug("Request to update Anomalie : {}", anomalieDTO);
        Anomalie anomalie = anomalieMapper.toEntity(anomalieDTO);
        anomalie = anomalieRepository.save(anomalie);
        return anomalieMapper.toDto(anomalie);
    }

    @Override
    public Optional<AnomalieDTO> partialUpdate(AnomalieDTO anomalieDTO) {
        log.debug("Request to partially update Anomalie : {}", anomalieDTO);

        return anomalieRepository
            .findById(anomalieDTO.getId())
            .map(existingAnomalie -> {
                anomalieMapper.partialUpdate(existingAnomalie, anomalieDTO);

                return existingAnomalie;
            })
            .map(anomalieRepository::save)
            .map(anomalieMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AnomalieDTO> findOne(Long id) {
        log.debug("Request to get Anomalie : {}", id);
        return anomalieRepository.findById(id).map(anomalieMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Anomalie : {}", id);
        anomalieRepository.deleteById(id);
    }
}
