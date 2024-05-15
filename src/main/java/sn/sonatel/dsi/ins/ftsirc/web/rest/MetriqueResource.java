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
import sn.sonatel.dsi.ins.ftsirc.repository.MetriqueRepository;
import sn.sonatel.dsi.ins.ftsirc.service.MetriqueQueryService;
import sn.sonatel.dsi.ins.ftsirc.service.MetriqueService;
import sn.sonatel.dsi.ins.ftsirc.service.criteria.MetriqueCriteria;
import sn.sonatel.dsi.ins.ftsirc.service.dto.MetriqueDTO;
import sn.sonatel.dsi.ins.ftsirc.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.sonatel.dsi.ins.ftsirc.domain.Metrique}.
 */
@RestController
@RequestMapping("/api/metriques")
public class MetriqueResource {

    private final Logger log = LoggerFactory.getLogger(MetriqueResource.class);

    private static final String ENTITY_NAME = "saytuBackendMetrique";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MetriqueService metriqueService;

    private final MetriqueRepository metriqueRepository;

    private final MetriqueQueryService metriqueQueryService;

    public MetriqueResource(
        MetriqueService metriqueService,
        MetriqueRepository metriqueRepository,
        MetriqueQueryService metriqueQueryService
    ) {
        this.metriqueService = metriqueService;
        this.metriqueRepository = metriqueRepository;
        this.metriqueQueryService = metriqueQueryService;
    }

    /**
     * {@code POST  /metriques} : Create a new metrique.
     *
     * @param metriqueDTO the metriqueDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new metriqueDTO, or with status {@code 400 (Bad Request)} if the metrique has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MetriqueDTO> createMetrique(@Valid @RequestBody MetriqueDTO metriqueDTO) throws URISyntaxException {
        log.debug("REST request to save Metrique : {}", metriqueDTO);
        if (metriqueDTO.getId() != null) {
            throw new BadRequestAlertException("A new metrique cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MetriqueDTO result = metriqueService.save(metriqueDTO);
        return ResponseEntity
            .created(new URI("/api/metriques/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /metriques/:id} : Updates an existing metrique.
     *
     * @param id the id of the metriqueDTO to save.
     * @param metriqueDTO the metriqueDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated metriqueDTO,
     * or with status {@code 400 (Bad Request)} if the metriqueDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the metriqueDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MetriqueDTO> updateMetrique(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MetriqueDTO metriqueDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Metrique : {}, {}", id, metriqueDTO);
        if (metriqueDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, metriqueDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!metriqueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MetriqueDTO result = metriqueService.update(metriqueDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, metriqueDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /metriques/:id} : Partial updates given fields of an existing metrique, field will ignore if it is null
     *
     * @param id the id of the metriqueDTO to save.
     * @param metriqueDTO the metriqueDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated metriqueDTO,
     * or with status {@code 400 (Bad Request)} if the metriqueDTO is not valid,
     * or with status {@code 404 (Not Found)} if the metriqueDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the metriqueDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MetriqueDTO> partialUpdateMetrique(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MetriqueDTO metriqueDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Metrique partially : {}, {}", id, metriqueDTO);
        if (metriqueDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, metriqueDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!metriqueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MetriqueDTO> result = metriqueService.partialUpdate(metriqueDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, metriqueDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /metriques} : get all the metriques.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of metriques in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MetriqueDTO>> getAllMetriques(
        MetriqueCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Metriques by criteria: {}", criteria);

        Page<MetriqueDTO> page = metriqueQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /metriques/count} : count all the metriques.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countMetriques(MetriqueCriteria criteria) {
        log.debug("REST request to count Metriques by criteria: {}", criteria);
        return ResponseEntity.ok().body(metriqueQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /metriques/:id} : get the "id" metrique.
     *
     * @param id the id of the metriqueDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the metriqueDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MetriqueDTO> getMetrique(@PathVariable("id") Long id) {
        log.debug("REST request to get Metrique : {}", id);
        Optional<MetriqueDTO> metriqueDTO = metriqueService.findOne(id);
        return ResponseUtil.wrapOrNotFound(metriqueDTO);
    }

    /**
     * {@code DELETE  /metriques/:id} : delete the "id" metrique.
     *
     * @param id the id of the metriqueDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMetrique(@PathVariable("id") Long id) {
        log.debug("REST request to delete Metrique : {}", id);
        metriqueService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
