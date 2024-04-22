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
import sn.sonatel.dsi.ins.ftsirc.repository.TypeDiagnosticRepository;
import sn.sonatel.dsi.ins.ftsirc.service.TypeDiagnosticQueryService;
import sn.sonatel.dsi.ins.ftsirc.service.TypeDiagnosticService;
import sn.sonatel.dsi.ins.ftsirc.service.criteria.TypeDiagnosticCriteria;
import sn.sonatel.dsi.ins.ftsirc.service.dto.TypeDiagnosticDTO;
import sn.sonatel.dsi.ins.ftsirc.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.sonatel.dsi.ins.ftsirc.domain.TypeDiagnostic}.
 */
@RestController
@RequestMapping("/api/type-diagnostics")
public class TypeDiagnosticResource {

    private final Logger log = LoggerFactory.getLogger(TypeDiagnosticResource.class);

    private static final String ENTITY_NAME = "saytuBackendTypeDiagnostic";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TypeDiagnosticService typeDiagnosticService;

    private final TypeDiagnosticRepository typeDiagnosticRepository;

    private final TypeDiagnosticQueryService typeDiagnosticQueryService;

    public TypeDiagnosticResource(
        TypeDiagnosticService typeDiagnosticService,
        TypeDiagnosticRepository typeDiagnosticRepository,
        TypeDiagnosticQueryService typeDiagnosticQueryService
    ) {
        this.typeDiagnosticService = typeDiagnosticService;
        this.typeDiagnosticRepository = typeDiagnosticRepository;
        this.typeDiagnosticQueryService = typeDiagnosticQueryService;
    }

    /**
     * {@code POST  /type-diagnostics} : Create a new typeDiagnostic.
     *
     * @param typeDiagnosticDTO the typeDiagnosticDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new typeDiagnosticDTO, or with status {@code 400 (Bad Request)} if the typeDiagnostic has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TypeDiagnosticDTO> createTypeDiagnostic(@Valid @RequestBody TypeDiagnosticDTO typeDiagnosticDTO)
        throws URISyntaxException {
        log.debug("REST request to save TypeDiagnostic : {}", typeDiagnosticDTO);
        if (typeDiagnosticDTO.getId() != null) {
            throw new BadRequestAlertException("A new typeDiagnostic cannot already have an ID", ENTITY_NAME, "idexists");
        }
        typeDiagnosticDTO = typeDiagnosticService.save(typeDiagnosticDTO);
        return ResponseEntity.created(new URI("/api/type-diagnostics/" + typeDiagnosticDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, typeDiagnosticDTO.getId().toString()))
            .body(typeDiagnosticDTO);
    }

    /**
     * {@code PUT  /type-diagnostics/:id} : Updates an existing typeDiagnostic.
     *
     * @param id the id of the typeDiagnosticDTO to save.
     * @param typeDiagnosticDTO the typeDiagnosticDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeDiagnosticDTO,
     * or with status {@code 400 (Bad Request)} if the typeDiagnosticDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the typeDiagnosticDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TypeDiagnosticDTO> updateTypeDiagnostic(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TypeDiagnosticDTO typeDiagnosticDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TypeDiagnostic : {}, {}", id, typeDiagnosticDTO);
        if (typeDiagnosticDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typeDiagnosticDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typeDiagnosticRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        typeDiagnosticDTO = typeDiagnosticService.update(typeDiagnosticDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typeDiagnosticDTO.getId().toString()))
            .body(typeDiagnosticDTO);
    }

    /**
     * {@code PATCH  /type-diagnostics/:id} : Partial updates given fields of an existing typeDiagnostic, field will ignore if it is null
     *
     * @param id the id of the typeDiagnosticDTO to save.
     * @param typeDiagnosticDTO the typeDiagnosticDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeDiagnosticDTO,
     * or with status {@code 400 (Bad Request)} if the typeDiagnosticDTO is not valid,
     * or with status {@code 404 (Not Found)} if the typeDiagnosticDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the typeDiagnosticDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TypeDiagnosticDTO> partialUpdateTypeDiagnostic(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TypeDiagnosticDTO typeDiagnosticDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TypeDiagnostic partially : {}, {}", id, typeDiagnosticDTO);
        if (typeDiagnosticDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typeDiagnosticDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typeDiagnosticRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TypeDiagnosticDTO> result = typeDiagnosticService.partialUpdate(typeDiagnosticDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typeDiagnosticDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /type-diagnostics} : get all the typeDiagnostics.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of typeDiagnostics in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TypeDiagnosticDTO>> getAllTypeDiagnostics(
        TypeDiagnosticCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get TypeDiagnostics by criteria: {}", criteria);

        Page<TypeDiagnosticDTO> page = typeDiagnosticQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /type-diagnostics/count} : count all the typeDiagnostics.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countTypeDiagnostics(TypeDiagnosticCriteria criteria) {
        log.debug("REST request to count TypeDiagnostics by criteria: {}", criteria);
        return ResponseEntity.ok().body(typeDiagnosticQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /type-diagnostics/:id} : get the "id" typeDiagnostic.
     *
     * @param id the id of the typeDiagnosticDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the typeDiagnosticDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TypeDiagnosticDTO> getTypeDiagnostic(@PathVariable("id") Long id) {
        log.debug("REST request to get TypeDiagnostic : {}", id);
        Optional<TypeDiagnosticDTO> typeDiagnosticDTO = typeDiagnosticService.findOne(id);
        return ResponseUtil.wrapOrNotFound(typeDiagnosticDTO);
    }

    /**
     * {@code DELETE  /type-diagnostics/:id} : delete the "id" typeDiagnostic.
     *
     * @param id the id of the typeDiagnosticDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTypeDiagnostic(@PathVariable("id") Long id) {
        log.debug("REST request to delete TypeDiagnostic : {}", id);
        typeDiagnosticService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
