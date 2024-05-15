package sn.sonatel.dsi.ins.ftsirc.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.sonatel.dsi.ins.ftsirc.domain.Signal;
import sn.sonatel.dsi.ins.ftsirc.repository.SignalRepository;
import sn.sonatel.dsi.ins.ftsirc.service.SignalService;
import sn.sonatel.dsi.ins.ftsirc.service.dto.SignalDTO;
import sn.sonatel.dsi.ins.ftsirc.service.mapper.SignalMapper;

/**
 * Service Implementation for managing {@link sn.sonatel.dsi.ins.ftsirc.domain.Signal}.
 */
@Service
@Transactional
public class SignalServiceImpl implements SignalService {

    private final Logger log = LoggerFactory.getLogger(SignalServiceImpl.class);

    private final SignalRepository signalRepository;

    private final SignalMapper signalMapper;

    public SignalServiceImpl(SignalRepository signalRepository, SignalMapper signalMapper) {
        this.signalRepository = signalRepository;
        this.signalMapper = signalMapper;
    }

    @Override
    public SignalDTO save(SignalDTO signalDTO) {
        log.debug("Request to save Signal : {}", signalDTO);
        Signal signal = signalMapper.toEntity(signalDTO);
        signal = signalRepository.save(signal);
        return signalMapper.toDto(signal);
    }

    @Override
    public SignalDTO update(SignalDTO signalDTO) {
        log.debug("Request to update Signal : {}", signalDTO);
        Signal signal = signalMapper.toEntity(signalDTO);
        signal = signalRepository.save(signal);
        return signalMapper.toDto(signal);
    }

    @Override
    public Optional<SignalDTO> partialUpdate(SignalDTO signalDTO) {
        log.debug("Request to partially update Signal : {}", signalDTO);

        return signalRepository
            .findById(signalDTO.getId())
            .map(existingSignal -> {
                signalMapper.partialUpdate(existingSignal, signalDTO);

                return existingSignal;
            })
            .map(signalRepository::save)
            .map(signalMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SignalDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Signals");
        return signalRepository.findAll(pageable).map(signalMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SignalDTO> findOne(Long id) {
        log.debug("Request to get Signal : {}", id);
        return signalRepository.findById(id).map(signalMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Signal : {}", id);
        signalRepository.deleteById(id);
    }
}
