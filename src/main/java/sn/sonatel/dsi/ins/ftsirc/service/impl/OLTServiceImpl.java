package sn.sonatel.dsi.ins.ftsirc.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.sonatel.dsi.ins.ftsirc.domain.OLT;
import sn.sonatel.dsi.ins.ftsirc.repository.OLTRepository;
import sn.sonatel.dsi.ins.ftsirc.service.OLTService;
import sn.sonatel.dsi.ins.ftsirc.service.dto.OLTDTO;
import sn.sonatel.dsi.ins.ftsirc.service.mapper.OLTMapper;

/**
 * Service Implementation for managing {@link sn.sonatel.dsi.ins.ftsirc.domain.OLT}.
 */
@Service
@Transactional
public class OLTServiceImpl implements OLTService {

    private final Logger log = LoggerFactory.getLogger(OLTServiceImpl.class);

    private final OLTRepository oLTRepository;

    private final OLTMapper oLTMapper;

    public OLTServiceImpl(OLTRepository oLTRepository, OLTMapper oLTMapper) {
        this.oLTRepository = oLTRepository;
        this.oLTMapper = oLTMapper;
    }

    @Override
    public OLTDTO save(OLTDTO oLTDTO) {
        log.debug("Request to save OLT : {}", oLTDTO);
        OLT oLT = oLTMapper.toEntity(oLTDTO);
        oLT = oLTRepository.save(oLT);
        return oLTMapper.toDto(oLT);
    }

    @Override
    public OLTDTO update(OLTDTO oLTDTO) {
        log.debug("Request to update OLT : {}", oLTDTO);
        OLT oLT = oLTMapper.toEntity(oLTDTO);
        oLT = oLTRepository.save(oLT);
        return oLTMapper.toDto(oLT);
    }

    @Override
    public Optional<OLTDTO> partialUpdate(OLTDTO oLTDTO) {
        log.debug("Request to partially update OLT : {}", oLTDTO);

        return oLTRepository
            .findById(oLTDTO.getId())
            .map(existingOLT -> {
                oLTMapper.partialUpdate(existingOLT, oLTDTO);

                return existingOLT;
            })
            .map(oLTRepository::save)
            .map(oLTMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OLTDTO> findOne(Long id) {
        log.debug("Request to get OLT : {}", id);
        return oLTRepository.findById(id).map(oLTMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete OLT : {}", id);
        oLTRepository.deleteById(id);
    }
}
