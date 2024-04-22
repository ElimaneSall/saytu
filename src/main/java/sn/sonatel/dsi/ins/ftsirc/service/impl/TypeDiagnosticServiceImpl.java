package sn.sonatel.dsi.ins.ftsirc.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.sonatel.dsi.ins.ftsirc.domain.TypeDiagnostic;
import sn.sonatel.dsi.ins.ftsirc.repository.TypeDiagnosticRepository;
import sn.sonatel.dsi.ins.ftsirc.service.TypeDiagnosticService;
import sn.sonatel.dsi.ins.ftsirc.service.dto.TypeDiagnosticDTO;
import sn.sonatel.dsi.ins.ftsirc.service.mapper.TypeDiagnosticMapper;

/**
 * Service Implementation for managing {@link sn.sonatel.dsi.ins.ftsirc.domain.TypeDiagnostic}.
 */
@Service
@Transactional
public class TypeDiagnosticServiceImpl implements TypeDiagnosticService {

    private final Logger log = LoggerFactory.getLogger(TypeDiagnosticServiceImpl.class);

    private final TypeDiagnosticRepository typeDiagnosticRepository;

    private final TypeDiagnosticMapper typeDiagnosticMapper;

    public TypeDiagnosticServiceImpl(TypeDiagnosticRepository typeDiagnosticRepository, TypeDiagnosticMapper typeDiagnosticMapper) {
        this.typeDiagnosticRepository = typeDiagnosticRepository;
        this.typeDiagnosticMapper = typeDiagnosticMapper;
    }

    @Override
    public TypeDiagnosticDTO save(TypeDiagnosticDTO typeDiagnosticDTO) {
        log.debug("Request to save TypeDiagnostic : {}", typeDiagnosticDTO);
        TypeDiagnostic typeDiagnostic = typeDiagnosticMapper.toEntity(typeDiagnosticDTO);
        typeDiagnostic = typeDiagnosticRepository.save(typeDiagnostic);
        return typeDiagnosticMapper.toDto(typeDiagnostic);
    }

    @Override
    public TypeDiagnosticDTO update(TypeDiagnosticDTO typeDiagnosticDTO) {
        log.debug("Request to update TypeDiagnostic : {}", typeDiagnosticDTO);
        TypeDiagnostic typeDiagnostic = typeDiagnosticMapper.toEntity(typeDiagnosticDTO);
        typeDiagnostic = typeDiagnosticRepository.save(typeDiagnostic);
        return typeDiagnosticMapper.toDto(typeDiagnostic);
    }

    @Override
    public Optional<TypeDiagnosticDTO> partialUpdate(TypeDiagnosticDTO typeDiagnosticDTO) {
        log.debug("Request to partially update TypeDiagnostic : {}", typeDiagnosticDTO);

        return typeDiagnosticRepository
            .findById(typeDiagnosticDTO.getId())
            .map(existingTypeDiagnostic -> {
                typeDiagnosticMapper.partialUpdate(existingTypeDiagnostic, typeDiagnosticDTO);

                return existingTypeDiagnostic;
            })
            .map(typeDiagnosticRepository::save)
            .map(typeDiagnosticMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TypeDiagnosticDTO> findOne(Long id) {
        log.debug("Request to get TypeDiagnostic : {}", id);
        return typeDiagnosticRepository.findById(id).map(typeDiagnosticMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TypeDiagnostic : {}", id);
        typeDiagnosticRepository.deleteById(id);
    }
}
