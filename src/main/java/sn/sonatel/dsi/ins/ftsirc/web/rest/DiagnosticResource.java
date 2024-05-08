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
import sn.sonatel.dsi.ins.ftsirc.repository.DiagnosticRepository;
import sn.sonatel.dsi.ins.ftsirc.service.DiagnosticQueryService;
import sn.sonatel.dsi.ins.ftsirc.service.DiagnosticService;
import sn.sonatel.dsi.ins.ftsirc.service.criteria.DiagnosticCriteria;
import sn.sonatel.dsi.ins.ftsirc.service.dto.DiagnosticDTO;
import sn.sonatel.dsi.ins.ftsirc.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.sonatel.dsi.ins.ftsirc.domain.Diagnostic}.
 */
@RestController
@RequestMapping("/api/diagnostics")
public class DiagnosticResource {

    private final Logger log = LoggerFactory.getLogger(DiagnosticResource.class);

    private static final String ENTITY_NAME = "saytuBackendDiagnostic";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DiagnosticService diagnosticService;

    private final DiagnosticRepository diagnosticRepository;

    private final DiagnosticQueryService diagnosticQueryService;

    public DiagnosticResource(
        DiagnosticService diagnosticService,
        DiagnosticRepository diagnosticRepository,
        DiagnosticQueryService diagnosticQueryService
    ) {
        this.diagnosticService = diagnosticService;
        this.diagnosticRepository = diagnosticRepository;
        this.diagnosticQueryService = diagnosticQueryService;
    }

    /**
     * {@code POST  /diagnostics} : Create a new diagnostic.
     *
     * @param diagnosticDTO the diagnosticDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new diagnosticDTO, or with status {@code 400 (Bad Request)} if the diagnostic has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DiagnosticDTO> createDiagnostic(@Valid @RequestBody DiagnosticDTO diagnosticDTO) throws URISyntaxException {
        log.debug("REST request to save Diagnostic : {}", diagnosticDTO);
        if (diagnosticDTO.getId() != null) {
            throw new BadRequestAlertException("A new diagnostic cannot already have an ID", ENTITY_NAME, "idexists");
        }
        diagnosticDTO = diagnosticService.save(diagnosticDTO);
        return ResponseEntity.created(new URI("/api/diagnostics/" + diagnosticDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, diagnosticDTO.getId().toString()))
            .body(diagnosticDTO);
    }

    /**
     * {@code PUT  /diagnostics/:id} : Updates an existing diagnostic.
     *
     * @param id the id of the diagnosticDTO to save.
     * @param diagnosticDTO the diagnosticDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated diagnosticDTO,
     * or with status {@code 400 (Bad Request)} if the diagnosticDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the diagnosticDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DiagnosticDTO> updateDiagnostic(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DiagnosticDTO diagnosticDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Diagnostic : {}, {}", id, diagnosticDTO);
        if (diagnosticDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, diagnosticDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!diagnosticRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        diagnosticDTO = diagnosticService.update(diagnosticDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, diagnosticDTO.getId().toString()))
            .body(diagnosticDTO);
    }

    /**
     * {@code PATCH  /diagnostics/:id} : Partial updates given fields of an existing diagnostic, field will ignore if it is null
     *
     * @param id the id of the diagnosticDTO to save.
     * @param diagnosticDTO the diagnosticDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated diagnosticDTO,
     * or with status {@code 400 (Bad Request)} if the diagnosticDTO is not valid,
     * or with status {@code 404 (Not Found)} if the diagnosticDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the diagnosticDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DiagnosticDTO> partialUpdateDiagnostic(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DiagnosticDTO diagnosticDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Diagnostic partially : {}, {}", id, diagnosticDTO);
        if (diagnosticDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, diagnosticDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!diagnosticRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DiagnosticDTO> result = diagnosticService.partialUpdate(diagnosticDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, diagnosticDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /diagnostics} : get all the diagnostics.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of diagnostics in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DiagnosticDTO>> getAllDiagnostics(
        DiagnosticCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Diagnostics by criteria: {}", criteria);

        Page<DiagnosticDTO> page = diagnosticQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /diagnostics/count} : count all the diagnostics.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countDiagnostics(DiagnosticCriteria criteria) {
        log.debug("REST request to count Diagnostics by criteria: {}", criteria);
        return ResponseEntity.ok().body(diagnosticQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /diagnostics/:id} : get the "id" diagnostic.
     *
     * @param id the id of the diagnosticDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the diagnosticDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DiagnosticDTO> getDiagnostic(@PathVariable("id") Long id) {
        log.debug("REST request to get Diagnostic : {}", id);
        Optional<DiagnosticDTO> diagnosticDTO = diagnosticService.findOne(id);
        return ResponseUtil.wrapOrNotFound(diagnosticDTO);
    }

    /**
     * {@code DELETE  /diagnostics/:id} : delete the "id" diagnostic.
     *
     * @param id the id of the diagnosticDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDiagnostic(@PathVariable("id") Long id) {
        log.debug("REST request to delete Diagnostic : {}", id);
        diagnosticService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code GET  /diagnostics/diagnostic/:serviceId} : count all the diagnostics.
     *
     * @param serviceId the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/diagnostic/{serviceId}")
    public ResponseEntity<String> diagnosticFibre(@PathVariable("serviceId") String serviceId) {
        log.debug("REST request to diagnose by serviceId: {}", serviceId);
        return ResponseEntity.ok().body(diagnosticService.diagnosticFiberCut(serviceId));
    }
}
