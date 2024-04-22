package sn.sonatel.dsi.ins.ftsirc.service;

import java.util.Optional;
import sn.sonatel.dsi.ins.ftsirc.service.dto.TypeDiagnosticDTO;

/**
 * Service Interface for managing {@link sn.sonatel.dsi.ins.ftsirc.domain.TypeDiagnostic}.
 */
public interface TypeDiagnosticService {
    /**
     * Save a typeDiagnostic.
     *
     * @param typeDiagnosticDTO the entity to save.
     * @return the persisted entity.
     */
    TypeDiagnosticDTO save(TypeDiagnosticDTO typeDiagnosticDTO);

    /**
     * Updates a typeDiagnostic.
     *
     * @param typeDiagnosticDTO the entity to update.
     * @return the persisted entity.
     */
    TypeDiagnosticDTO update(TypeDiagnosticDTO typeDiagnosticDTO);

    /**
     * Partially updates a typeDiagnostic.
     *
     * @param typeDiagnosticDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TypeDiagnosticDTO> partialUpdate(TypeDiagnosticDTO typeDiagnosticDTO);

    /**
     * Get the "id" typeDiagnostic.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TypeDiagnosticDTO> findOne(Long id);

    /**
     * Delete the "id" typeDiagnostic.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
