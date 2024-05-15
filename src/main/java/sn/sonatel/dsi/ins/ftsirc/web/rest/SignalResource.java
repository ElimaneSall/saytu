package sn.sonatel.dsi.ins.ftsirc.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import sn.sonatel.dsi.ins.ftsirc.repository.SignalRepository;
import sn.sonatel.dsi.ins.ftsirc.service.SignalQueryService;
import sn.sonatel.dsi.ins.ftsirc.service.SignalService;
import sn.sonatel.dsi.ins.ftsirc.service.criteria.SignalCriteria;
import sn.sonatel.dsi.ins.ftsirc.service.dto.SignalDTO;
import sn.sonatel.dsi.ins.ftsirc.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.sonatel.dsi.ins.ftsirc.domain.Signal}.
 */
@RestController
@RequestMapping("/api/signals")
public class SignalResource {

    private final Logger log = LoggerFactory.getLogger(SignalResource.class);

    private static final String ENTITY_NAME = "saytuBackendSignal";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SignalService signalService;

    private final SignalRepository signalRepository;

    private final SignalQueryService signalQueryService;

    public SignalResource(SignalService signalService, SignalRepository signalRepository, SignalQueryService signalQueryService) {
        this.signalService = signalService;
        this.signalRepository = signalRepository;
        this.signalQueryService = signalQueryService;
    }

    /**
     * {@code POST  /signals} : Create a new signal.
     *
     * @param signalDTO the signalDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new signalDTO, or with status {@code 400 (Bad Request)} if the signal has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SignalDTO> createSignal(@Valid @RequestBody SignalDTO signalDTO) throws URISyntaxException {
        log.debug("REST request to save Signal : {}", signalDTO);
        if (signalDTO.getId() != null) {
            throw new BadRequestAlertException("A new signal cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SignalDTO result = signalService.save(signalDTO);
        return ResponseEntity
            .created(new URI("/api/signals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /signals/:id} : Updates an existing signal.
     *
     * @param id the id of the signalDTO to save.
     * @param signalDTO the signalDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated signalDTO,
     * or with status {@code 400 (Bad Request)} if the signalDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the signalDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SignalDTO> updateSignal(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SignalDTO signalDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Signal : {}, {}", id, signalDTO);
        if (signalDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, signalDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!signalRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SignalDTO result = signalService.update(signalDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, signalDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /signals/:id} : Partial updates given fields of an existing signal, field will ignore if it is null
     *
     * @param id the id of the signalDTO to save.
     * @param signalDTO the signalDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated signalDTO,
     * or with status {@code 400 (Bad Request)} if the signalDTO is not valid,
     * or with status {@code 404 (Not Found)} if the signalDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the signalDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SignalDTO> partialUpdateSignal(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SignalDTO signalDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Signal partially : {}, {}", id, signalDTO);
        if (signalDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, signalDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!signalRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SignalDTO> result = signalService.partialUpdate(signalDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, signalDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /signals} : get all the signals.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of signals in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SignalDTO>> getAllSignals(
        SignalCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Signals by criteria: {}", criteria);

        Page<SignalDTO> page = signalQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /signals/count} : count all the signals.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countSignals(SignalCriteria criteria) {
        log.debug("REST request to count Signals by criteria: {}", criteria);
        return ResponseEntity.ok().body(signalQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /signals/:id} : get the "id" signal.
     *
     * @param id the id of the signalDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the signalDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SignalDTO> getSignal(@PathVariable("id") Long id) {
        log.debug("REST request to get Signal : {}", id);
        Optional<SignalDTO> signalDTO = signalService.findOne(id);
        return ResponseUtil.wrapOrNotFound(signalDTO);
    }

    /**
     * {@code DELETE  /signals/:id} : delete the "id" signal.
     *
     * @param id the id of the signalDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSignal(@PathVariable("id") Long id) {
        log.debug("REST request to delete Signal : {}", id);
        signalService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
