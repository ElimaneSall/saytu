package sn.sonatel.dsi.ins.ftsirc.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.sonatel.dsi.ins.ftsirc.domain.Metrique;
import sn.sonatel.dsi.ins.ftsirc.domain.OLT;
import sn.sonatel.dsi.ins.ftsirc.domain.ONT;
import sn.sonatel.dsi.ins.ftsirc.repository.MetriqueRepository;
import sn.sonatel.dsi.ins.ftsirc.repository.ONTRepository;
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

    public MetriqueServiceImpl(MetriqueRepository metriqueRepository, MetriqueMapper metriqueMapper, ONTRepository ontRepository) {
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

    public List<Metrique> getAllMetrics(Long id) {
        ONT ont = ontRepository.findById(id).get();
        List<Metrique> listMetrics = new ArrayList<>();
    }
}
