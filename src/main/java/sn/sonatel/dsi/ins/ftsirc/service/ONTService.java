package sn.sonatel.dsi.ins.ftsirc.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.sonatel.dsi.ins.ftsirc.domain.ONT;
import sn.sonatel.dsi.ins.ftsirc.service.dto.ONTDTO;

/**
 * Service Interface for managing {@link sn.sonatel.dsi.ins.ftsirc.domain.ONT}.
 */
public interface ONTService {
    /**
     * Save a oNT.
     *
     * @param oNTDTO the entity to save.
     * @return the persisted entity.
     */
    ONTDTO save(ONTDTO oNTDTO);
    /**
     * Save a oNT list.
     *
     * @param listONTs the entities to save.
     * @return the persisted entity.
     */
    public List<ONTDTO> saveListONT(List<ONT> listONTs);

    /**
     * Updates a oNT.
     *
     * @param oNTDTO the entity to update.
     * @return the persisted entity.
     */
    ONTDTO update(ONTDTO oNTDTO);


    /**
     * Partially updates a oNT.
     *
     * @param oNTDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ONTDTO> partialUpdate(ONTDTO oNTDTO);

    /**
     * Get all the oNTS.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ONTDTO> findAll(Pageable pageable);

    /**
     * Get all the oNTS with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ONTDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" oNT.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ONTDTO> findOne(Long id);

    /**
     * Delete the "id" oNT.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
    public void updateALLONTS();

}
