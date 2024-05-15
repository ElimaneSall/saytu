package sn.sonatel.dsi.ins.ftsirc.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.sonatel.dsi.ins.ftsirc.service.dto.SignalDTO;

/**
 * Service Interface for managing {@link sn.sonatel.dsi.ins.ftsirc.domain.Signal}.
 */
public interface SignalService {
    /**
     * Save a signal.
     *
     * @param signalDTO the entity to save.
     * @return the persisted entity.
     */
    SignalDTO save(SignalDTO signalDTO);

    /**
     * Updates a signal.
     *
     * @param signalDTO the entity to update.
     * @return the persisted entity.
     */
    SignalDTO update(SignalDTO signalDTO);

    /**
     * Partially updates a signal.
     *
     * @param signalDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SignalDTO> partialUpdate(SignalDTO signalDTO);

    /**
     * Get all the signals.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SignalDTO> findAll(Pageable pageable);

    /**
     * Get the "id" signal.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SignalDTO> findOne(Long id);

    /**
     * Delete the "id" signal.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
