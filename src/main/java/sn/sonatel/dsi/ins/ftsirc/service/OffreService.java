package sn.sonatel.dsi.ins.ftsirc.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.sonatel.dsi.ins.ftsirc.service.dto.OffreDTO;

/**
 * Service Interface for managing {@link sn.sonatel.dsi.ins.ftsirc.domain.Offre}.
 */
public interface OffreService {
    /**
     * Save a offre.
     *
     * @param offreDTO the entity to save.
     * @return the persisted entity.
     */
    OffreDTO save(OffreDTO offreDTO);

    /**
     * Updates a offre.
     *
     * @param offreDTO the entity to update.
     * @return the persisted entity.
     */
    OffreDTO update(OffreDTO offreDTO);

    /**
     * Partially updates a offre.
     *
     * @param offreDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OffreDTO> partialUpdate(OffreDTO offreDTO);

    /**
     * Get all the offres.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OffreDTO> findAll(Pageable pageable);

    /**
     * Get the "id" offre.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OffreDTO> findOne(Long id);

    /**
     * Delete the "id" offre.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
