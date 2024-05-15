package sn.sonatel.dsi.ins.ftsirc.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.sonatel.dsi.ins.ftsirc.domain.Diagnostic;
import sn.sonatel.dsi.ins.ftsirc.repository.DiagnosticRepository;
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

    public DiagnosticServiceImpl(DiagnosticRepository diagnosticRepository, DiagnosticMapper diagnosticMapper) {
        this.diagnosticRepository = diagnosticRepository;
        this.diagnosticMapper = diagnosticMapper;
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
    @Transactional(readOnly = true)
    public Page<DiagnosticDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Diagnostics");
        return diagnosticRepository.findAll(pageable).map(diagnosticMapper::toDto);
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
}
