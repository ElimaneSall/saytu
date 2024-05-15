package sn.sonatel.dsi.ins.ftsirc.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.sonatel.dsi.ins.ftsirc.service.dto.MetriqueDTO;

/**
 * Service Interface for managing {@link sn.sonatel.dsi.ins.ftsirc.domain.Metrique}.
 */
public interface MetriqueService {
    /**
     * Save a metrique.
     *
     * @param metriqueDTO the entity to save.
     * @return the persisted entity.
     */
    MetriqueDTO save(MetriqueDTO metriqueDTO);

    /**
     * Updates a metrique.
     *
     * @param metriqueDTO the entity to update.
     * @return the persisted entity.
     */
    MetriqueDTO update(MetriqueDTO metriqueDTO);

    /**
     * Partially updates a metrique.
     *
     * @param metriqueDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MetriqueDTO> partialUpdate(MetriqueDTO metriqueDTO);

    /**
     * Get all the metriques.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MetriqueDTO> findAll(Pageable pageable);

    /**
     * Get the "id" metrique.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MetriqueDTO> findOne(Long id);

    /**
     * Delete the "id" metrique.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
