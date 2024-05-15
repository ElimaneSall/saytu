package sn.sonatel.dsi.ins.ftsirc.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.sonatel.dsi.ins.ftsirc.service.dto.AnomalieDTO;

/**
 * Service Interface for managing {@link sn.sonatel.dsi.ins.ftsirc.domain.Anomalie}.
 */
public interface AnomalieService {
    /**
     * Save a anomalie.
     *
     * @param anomalieDTO the entity to save.
     * @return the persisted entity.
     */
    AnomalieDTO save(AnomalieDTO anomalieDTO);

    /**
     * Updates a anomalie.
     *
     * @param anomalieDTO the entity to update.
     * @return the persisted entity.
     */
    AnomalieDTO update(AnomalieDTO anomalieDTO);

    /**
     * Partially updates a anomalie.
     *
     * @param anomalieDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AnomalieDTO> partialUpdate(AnomalieDTO anomalieDTO);

    /**
     * Get all the anomalies.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AnomalieDTO> findAll(Pageable pageable);

    /**
     * Get the "id" anomalie.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AnomalieDTO> findOne(Long id);

    /**
     * Delete the "id" anomalie.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
