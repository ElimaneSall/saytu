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
import sn.sonatel.dsi.ins.ftsirc.repository.AnomalieRepository;
import sn.sonatel.dsi.ins.ftsirc.service.AnomalieQueryService;
import sn.sonatel.dsi.ins.ftsirc.service.AnomalieService;
import sn.sonatel.dsi.ins.ftsirc.service.criteria.AnomalieCriteria;
import sn.sonatel.dsi.ins.ftsirc.service.dto.AnomalieDTO;
import sn.sonatel.dsi.ins.ftsirc.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.sonatel.dsi.ins.ftsirc.domain.Anomalie}.
 */
@RestController
@RequestMapping("/api/anomalies")
public class AnomalieResource {

    private final Logger log = LoggerFactory.getLogger(AnomalieResource.class);

    private static final String ENTITY_NAME = "saytuBackendAnomalie";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AnomalieService anomalieService;

    private final AnomalieRepository anomalieRepository;

    private final AnomalieQueryService anomalieQueryService;

    public AnomalieResource(
        AnomalieService anomalieService,
        AnomalieRepository anomalieRepository,
        AnomalieQueryService anomalieQueryService
    ) {
        this.anomalieService = anomalieService;
        this.anomalieRepository = anomalieRepository;
        this.anomalieQueryService = anomalieQueryService;
    }

    /**
     * {@code POST  /anomalies} : Create a new anomalie.
     *
     * @param anomalieDTO the anomalieDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new anomalieDTO, or with status {@code 400 (Bad Request)} if the anomalie has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AnomalieDTO> createAnomalie(@Valid @RequestBody AnomalieDTO anomalieDTO) throws URISyntaxException {
        log.debug("REST request to save Anomalie : {}", anomalieDTO);
        if (anomalieDTO.getId() != null) {
            throw new BadRequestAlertException("A new anomalie cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AnomalieDTO result = anomalieService.save(anomalieDTO);
        return ResponseEntity
            .created(new URI("/api/anomalies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /anomalies/:id} : Updates an existing anomalie.
     *
     * @param id the id of the anomalieDTO to save.
     * @param anomalieDTO the anomalieDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated anomalieDTO,
     * or with status {@code 400 (Bad Request)} if the anomalieDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the anomalieDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AnomalieDTO> updateAnomalie(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AnomalieDTO anomalieDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Anomalie : {}, {}", id, anomalieDTO);
        if (anomalieDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, anomalieDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!anomalieRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AnomalieDTO result = anomalieService.update(anomalieDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, anomalieDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /anomalies/:id} : Partial updates given fields of an existing anomalie, field will ignore if it is null
     *
     * @param id the id of the anomalieDTO to save.
     * @param anomalieDTO the anomalieDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated anomalieDTO,
     * or with status {@code 400 (Bad Request)} if the anomalieDTO is not valid,
     * or with status {@code 404 (Not Found)} if the anomalieDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the anomalieDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AnomalieDTO> partialUpdateAnomalie(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AnomalieDTO anomalieDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Anomalie partially : {}, {}", id, anomalieDTO);
        if (anomalieDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, anomalieDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!anomalieRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AnomalieDTO> result = anomalieService.partialUpdate(anomalieDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, anomalieDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /anomalies} : get all the anomalies.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of anomalies in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AnomalieDTO>> getAllAnomalies(
        AnomalieCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Anomalies by criteria: {}", criteria);

        Page<AnomalieDTO> page = anomalieQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /anomalies/count} : count all the anomalies.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAnomalies(AnomalieCriteria criteria) {
        log.debug("REST request to count Anomalies by criteria: {}", criteria);
        return ResponseEntity.ok().body(anomalieQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /anomalies/:id} : get the "id" anomalie.
     *
     * @param id the id of the anomalieDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the anomalieDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AnomalieDTO> getAnomalie(@PathVariable("id") Long id) {
        log.debug("REST request to get Anomalie : {}", id);
        Optional<AnomalieDTO> anomalieDTO = anomalieService.findOne(id);
        return ResponseUtil.wrapOrNotFound(anomalieDTO);
    }

    /**
     * {@code DELETE  /anomalies/:id} : delete the "id" anomalie.
     *
     * @param id the id of the anomalieDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnomalie(@PathVariable("id") Long id) {
        log.debug("REST request to delete Anomalie : {}", id);
        anomalieService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
