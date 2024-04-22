package sn.sonatel.dsi.ins.ftsirc.service;

import java.util.List;
import java.util.Optional;
import sn.sonatel.dsi.ins.ftsirc.service.dto.AdresseDTO;

/**
 * Service Interface for managing {@link sn.sonatel.dsi.ins.ftsirc.domain.Adresse}.
 */
public interface AdresseService {
    /**
     * Save a adresse.
     *
     * @param adresseDTO the entity to save.
     * @return the persisted entity.
     */
    AdresseDTO save(AdresseDTO adresseDTO);

    /**
     * Updates a adresse.
     *
     * @param adresseDTO the entity to update.
     * @return the persisted entity.
     */
    AdresseDTO update(AdresseDTO adresseDTO);

    /**
     * Partially updates a adresse.
     *
     * @param adresseDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AdresseDTO> partialUpdate(AdresseDTO adresseDTO);

    /**
     * Get all the AdresseDTO where Olt is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<AdresseDTO> findAllWhereOltIsNull();

    /**
     * Get the "id" adresse.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AdresseDTO> findOne(Long id);

    /**
     * Delete the "id" adresse.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
