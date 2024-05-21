package sn.sonatel.dsi.ins.ftsirc.service;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.sonatel.dsi.ins.ftsirc.domain.Anomalie;
import sn.sonatel.dsi.ins.ftsirc.domain.Diagnostic;
import sn.sonatel.dsi.ins.ftsirc.domain.ONT;
import sn.sonatel.dsi.ins.ftsirc.service.dto.DiagnosticDTO;

/**
 * Service Interface for managing {@link sn.sonatel.dsi.ins.ftsirc.domain.Diagnostic}.
 */
public interface DiagnosticService {
    /**
     * Save a diagnostic.
     *
     * @param diagnosticDTO the entity to save.
     * @return the persisted entity.
     */
    DiagnosticDTO save(DiagnosticDTO diagnosticDTO);

    /**
     * Updates a diagnostic.
     *
     * @param diagnosticDTO the entity to update.
     * @return the persisted entity.
     */
    DiagnosticDTO update(DiagnosticDTO diagnosticDTO);

    /**
     * Partially updates a diagnostic.
     *
     * @param diagnosticDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DiagnosticDTO> partialUpdate(DiagnosticDTO diagnosticDTO);

    /**
     * Get all the diagnostics.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DiagnosticDTO> findAll(Pageable pageable);

    /**
     * Get all the diagnostics with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DiagnosticDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" diagnostic.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DiagnosticDTO> findOne(Long id);

    /**
     * Delete the "id" diagnostic.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    public  Map<String, Object> diagnosticOLTPowerUnderLimit(ONT ont) throws IOException;

    public  Map<String, Object>  diagnosticONTPowerUnderLimit(ONT ont) throws IOException;

    public Anomalie diagnosticDebit(ONT ont) throws IOException;

    public Anomalie diagnosticPowerSupply(ONT ont) throws IOException;

    public Anomalie diagnosticFiberCut(ONT ont);
    public Diagnostic diagnosticFiber(String serviceId) throws IOException;

}
