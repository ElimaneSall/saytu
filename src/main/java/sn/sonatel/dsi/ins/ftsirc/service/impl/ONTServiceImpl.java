package sn.sonatel.dsi.ins.ftsirc.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.sonatel.dsi.ins.ftsirc.domain.ONT;
import sn.sonatel.dsi.ins.ftsirc.repository.ONTRepository;
import sn.sonatel.dsi.ins.ftsirc.service.ONTService;
import sn.sonatel.dsi.ins.ftsirc.service.dto.ONTDTO;
import sn.sonatel.dsi.ins.ftsirc.service.mapper.ONTMapper;

/**
 * Service Implementation for managing {@link sn.sonatel.dsi.ins.ftsirc.domain.ONT}.
 */
@Service
@Transactional
public class ONTServiceImpl implements ONTService {

    private final Logger log = LoggerFactory.getLogger(ONTServiceImpl.class);

    private final ONTRepository oNTRepository;

    private final ONTMapper oNTMapper;

    public ONTServiceImpl(ONTRepository oNTRepository, ONTMapper oNTMapper) {
        this.oNTRepository = oNTRepository;
        this.oNTMapper = oNTMapper;
    }

    @Override
    public ONTDTO save(ONTDTO oNTDTO) {
        log.debug("Request to save ONT : {}", oNTDTO);
        ONT oNT = oNTMapper.toEntity(oNTDTO);
        oNT = oNTRepository.save(oNT);
        return oNTMapper.toDto(oNT);
    }

    @Override
    public ONTDTO update(ONTDTO oNTDTO) {
        log.debug("Request to update ONT : {}", oNTDTO);
        ONT oNT = oNTMapper.toEntity(oNTDTO);
        oNT = oNTRepository.save(oNT);
        return oNTMapper.toDto(oNT);
    }

    @Override
    public Optional<ONTDTO> partialUpdate(ONTDTO oNTDTO) {
        log.debug("Request to partially update ONT : {}", oNTDTO);

        return oNTRepository
            .findById(oNTDTO.getId())
            .map(existingONT -> {
                oNTMapper.partialUpdate(existingONT, oNTDTO);

                return existingONT;
            })
            .map(oNTRepository::save)
            .map(oNTMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ONTDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ONTS");
        return oNTRepository.findAll(pageable).map(oNTMapper::toDto);
    }

    public Page<ONTDTO> findAllWithEagerRelationships(Pageable pageable) {
        return oNTRepository.findAllWithEagerRelationships(pageable).map(oNTMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ONTDTO> findOne(Long id) {
        log.debug("Request to get ONT : {}", id);
        return oNTRepository.findOneWithEagerRelationships(id).map(oNTMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ONT : {}", id);
        oNTRepository.deleteById(id);
    }
}
