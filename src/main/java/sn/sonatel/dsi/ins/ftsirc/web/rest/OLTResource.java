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
import sn.sonatel.dsi.ins.ftsirc.repository.OLTRepository;
import sn.sonatel.dsi.ins.ftsirc.service.OLTQueryService;
import sn.sonatel.dsi.ins.ftsirc.service.OLTService;
import sn.sonatel.dsi.ins.ftsirc.service.criteria.OLTCriteria;
import sn.sonatel.dsi.ins.ftsirc.service.dto.OLTDTO;
import sn.sonatel.dsi.ins.ftsirc.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.sonatel.dsi.ins.ftsirc.domain.OLT}.
 */
@RestController
@RequestMapping("/api/olts")
public class OLTResource {

    private final Logger log = LoggerFactory.getLogger(OLTResource.class);

    private static final String ENTITY_NAME = "saytuBackendOlt";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OLTService oLTService;

    private final OLTRepository oLTRepository;

    private final OLTQueryService oLTQueryService;

    public OLTResource(OLTService oLTService, OLTRepository oLTRepository, OLTQueryService oLTQueryService) {
        this.oLTService = oLTService;
        this.oLTRepository = oLTRepository;
        this.oLTQueryService = oLTQueryService;
    }

    /**
     * {@code POST  /olts} : Create a new oLT.
     *
     * @param oLTDTO the oLTDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new oLTDTO, or with status {@code 400 (Bad Request)} if the oLT has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<OLTDTO> createOLT(@Valid @RequestBody OLTDTO oLTDTO) throws URISyntaxException {
        log.debug("REST request to save OLT : {}", oLTDTO);
        if (oLTDTO.getId() != null) {
            throw new BadRequestAlertException("A new oLT cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OLTDTO result = oLTService.save(oLTDTO);
        return ResponseEntity
            .created(new URI("/api/olts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /olts/:id} : Updates an existing oLT.
     *
     * @param id the id of the oLTDTO to save.
     * @param oLTDTO the oLTDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated oLTDTO,
     * or with status {@code 400 (Bad Request)} if the oLTDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the oLTDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OLTDTO> updateOLT(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody OLTDTO oLTDTO)
        throws URISyntaxException {
        log.debug("REST request to update OLT : {}, {}", id, oLTDTO);
        if (oLTDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, oLTDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!oLTRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OLTDTO result = oLTService.update(oLTDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, oLTDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /olts/:id} : Partial updates given fields of an existing oLT, field will ignore if it is null
     *
     * @param id the id of the oLTDTO to save.
     * @param oLTDTO the oLTDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated oLTDTO,
     * or with status {@code 400 (Bad Request)} if the oLTDTO is not valid,
     * or with status {@code 404 (Not Found)} if the oLTDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the oLTDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OLTDTO> partialUpdateOLT(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OLTDTO oLTDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update OLT partially : {}, {}", id, oLTDTO);
        if (oLTDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, oLTDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!oLTRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OLTDTO> result = oLTService.partialUpdate(oLTDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, oLTDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /olts} : get all the oLTS.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of oLTS in body.
     */
    @GetMapping("")
    public ResponseEntity<List<OLTDTO>> getAllOLTS(
        OLTCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get OLTS by criteria: {}", criteria);

        Page<OLTDTO> page = oLTQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /olts/count} : count all the oLTS.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countOLTS(OLTCriteria criteria) {
        log.debug("REST request to count OLTS by criteria: {}", criteria);
        return ResponseEntity.ok().body(oLTQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /olts/:id} : get the "id" oLT.
     *
     * @param id the id of the oLTDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the oLTDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OLTDTO> getOLT(@PathVariable("id") Long id) {
        log.debug("REST request to get OLT : {}", id);
        Optional<OLTDTO> oLTDTO = oLTService.findOne(id);
        return ResponseUtil.wrapOrNotFound(oLTDTO);
    }

    /**
     * {@code DELETE  /olts/:id} : delete the "id" oLT.
     *
     * @param id the id of the oLTDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOLT(@PathVariable("id") Long id) {
        log.debug("REST request to delete OLT : {}", id);
        oLTService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
