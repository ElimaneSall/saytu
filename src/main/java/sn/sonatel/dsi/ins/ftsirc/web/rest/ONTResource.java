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
import sn.sonatel.dsi.ins.ftsirc.repository.ONTRepository;
import sn.sonatel.dsi.ins.ftsirc.service.ONTQueryService;
import sn.sonatel.dsi.ins.ftsirc.service.ONTService;
import sn.sonatel.dsi.ins.ftsirc.service.criteria.ONTCriteria;
import sn.sonatel.dsi.ins.ftsirc.service.dto.ONTDTO;
import sn.sonatel.dsi.ins.ftsirc.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.sonatel.dsi.ins.ftsirc.domain.ONT}.
 */
@RestController
@RequestMapping("/api/onts")
public class ONTResource {

    private final Logger log = LoggerFactory.getLogger(ONTResource.class);

    private static final String ENTITY_NAME = "saytuBackendOnt";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ONTService oNTService;

    private final ONTRepository oNTRepository;

    private final ONTQueryService oNTQueryService;

    public ONTResource(ONTService oNTService, ONTRepository oNTRepository, ONTQueryService oNTQueryService) {
        this.oNTService = oNTService;
        this.oNTRepository = oNTRepository;
        this.oNTQueryService = oNTQueryService;
    }

    /**
     * {@code POST  /onts} : Create a new oNT.
     *
     * @param oNTDTO the oNTDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new oNTDTO, or with status {@code 400 (Bad Request)} if the oNT has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ONTDTO> createONT(@Valid @RequestBody ONTDTO oNTDTO) throws URISyntaxException {
        log.debug("REST request to save ONT : {}", oNTDTO);
        if (oNTDTO.getId() != null) {
            throw new BadRequestAlertException("A new oNT cannot already have an ID", ENTITY_NAME, "idexists");
        }
        oNTDTO = oNTService.save(oNTDTO);
        return ResponseEntity.created(new URI("/api/onts/" + oNTDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, oNTDTO.getId().toString()))
            .body(oNTDTO);
    }

    /**
     * {@code PUT  /onts/:id} : Updates an existing oNT.
     *
     * @param id the id of the oNTDTO to save.
     * @param oNTDTO the oNTDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated oNTDTO,
     * or with status {@code 400 (Bad Request)} if the oNTDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the oNTDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ONTDTO> updateONT(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody ONTDTO oNTDTO)
        throws URISyntaxException {
        log.debug("REST request to update ONT : {}, {}", id, oNTDTO);
        if (oNTDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, oNTDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!oNTRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        oNTDTO = oNTService.update(oNTDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, oNTDTO.getId().toString()))
            .body(oNTDTO);
    }

    /**
     * {@code PATCH  /onts/:id} : Partial updates given fields of an existing oNT, field will ignore if it is null
     *
     * @param id the id of the oNTDTO to save.
     * @param oNTDTO the oNTDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated oNTDTO,
     * or with status {@code 400 (Bad Request)} if the oNTDTO is not valid,
     * or with status {@code 404 (Not Found)} if the oNTDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the oNTDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ONTDTO> partialUpdateONT(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ONTDTO oNTDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ONT partially : {}, {}", id, oNTDTO);
        if (oNTDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, oNTDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!oNTRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ONTDTO> result = oNTService.partialUpdate(oNTDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, oNTDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /onts} : get all the oNTS.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of oNTS in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ONTDTO>> getAllONTS(
        ONTCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get ONTS by criteria: {}", criteria);

        Page<ONTDTO> page = oNTQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /onts/count} : count all the oNTS.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countONTS(ONTCriteria criteria) {
        log.debug("REST request to count ONTS by criteria: {}", criteria);
        return ResponseEntity.ok().body(oNTQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /onts/:id} : get the "id" oNT.
     *
     * @param id the id of the oNTDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the oNTDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ONTDTO> getONT(@PathVariable("id") Long id) {
        log.debug("REST request to get ONT : {}", id);
        Optional<ONTDTO> oNTDTO = oNTService.findOne(id);
        return ResponseUtil.wrapOrNotFound(oNTDTO);
    }

    /**
     * {@code DELETE  /onts/:id} : delete the "id" oNT.
     *
     * @param id the id of the oNTDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteONT(@PathVariable("id") Long id) {
        log.debug("REST request to delete ONT : {}", id);
        oNTService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
