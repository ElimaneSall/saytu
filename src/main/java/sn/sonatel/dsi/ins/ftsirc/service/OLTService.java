package sn.sonatel.dsi.ins.ftsirc.service;

import java.util.Optional;
import sn.sonatel.dsi.ins.ftsirc.service.dto.OLTDTO;

/**
 * Service Interface for managing {@link sn.sonatel.dsi.ins.ftsirc.domain.OLT}.
 */
public interface OLTService {
    /**
     * Save a oLT.
     *
     * @param oLTDTO the entity to save.
     * @return the persisted entity.
     */
    OLTDTO save(OLTDTO oLTDTO);

    /**
     * Updates a oLT.
     *
     * @param oLTDTO the entity to update.
     * @return the persisted entity.
     */
    OLTDTO update(OLTDTO oLTDTO);

    /**
     * Partially updates a oLT.
     *
     * @param oLTDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OLTDTO> partialUpdate(OLTDTO oLTDTO);

    /**
     * Get the "id" oLT.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OLTDTO> findOne(Long id);

    /**
     * Delete the "id" oLT.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
