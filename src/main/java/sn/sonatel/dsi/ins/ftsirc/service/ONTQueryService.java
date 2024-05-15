package sn.sonatel.dsi.ins.ftsirc.service;

import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.sonatel.dsi.ins.ftsirc.domain.*; // for static metamodels
import sn.sonatel.dsi.ins.ftsirc.domain.ONT;
import sn.sonatel.dsi.ins.ftsirc.repository.ONTRepository;
import sn.sonatel.dsi.ins.ftsirc.service.criteria.ONTCriteria;
import sn.sonatel.dsi.ins.ftsirc.service.dto.ONTDTO;
import sn.sonatel.dsi.ins.ftsirc.service.mapper.ONTMapper;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ONT} entities in the database.
 * The main input is a {@link ONTCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ONTDTO} or a {@link Page} of {@link ONTDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ONTQueryService extends QueryService<ONT> {

    private final Logger log = LoggerFactory.getLogger(ONTQueryService.class);

    private final ONTRepository oNTRepository;

    private final ONTMapper oNTMapper;

    public ONTQueryService(ONTRepository oNTRepository, ONTMapper oNTMapper) {
        this.oNTRepository = oNTRepository;
        this.oNTMapper = oNTMapper;
    }

    /**
     * Return a {@link List} of {@link ONTDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ONTDTO> findByCriteria(ONTCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ONT> specification = createSpecification(criteria);
        return oNTMapper.toDto(oNTRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ONTDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ONTDTO> findByCriteria(ONTCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ONT> specification = createSpecification(criteria);
        return oNTRepository.findAll(specification, page).map(oNTMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ONTCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ONT> specification = createSpecification(criteria);
        return oNTRepository.count(specification);
    }

    /**
     * Function to convert {@link ONTCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ONT> createSpecification(ONTCriteria criteria) {
        Specification<ONT> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ONT_.id));
            }
            if (criteria.getIndex() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIndex(), ONT_.index));
            }
            if (criteria.getOntID() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOntID(), ONT_.ontID));
            }
            if (criteria.getServiceId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getServiceId(), ONT_.serviceId));
            }
            if (criteria.getSlot() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSlot(), ONT_.slot));
            }
            if (criteria.getPon() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPon(), ONT_.pon));
            }
            if (criteria.getPonIndex() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPonIndex(), ONT_.ponIndex));
            }
            if (criteria.getMaxUp() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMaxUp(), ONT_.maxUp));
            }
            if (criteria.getMaxDown() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMaxDown(), ONT_.maxDown));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), ONT_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), ONT_.updatedAt));
            }
            if (criteria.getEtatOlt() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEtatOlt(), ONT_.etatOlt));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStatus(), ONT_.status));
            }
            if (criteria.getStatusAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStatusAt(), ONT_.statusAt));
            }
            if (criteria.getNbreLignesCouper() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNbreLignesCouper(), ONT_.nbreLignesCouper));
            }
            if (criteria.getClientId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getClientId(), root -> root.join(ONT_.client, JoinType.LEFT).get(Client_.id))
                    );
            }
            if (criteria.getOltId() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getOltId(), root -> root.join(ONT_.olt, JoinType.LEFT).get(OLT_.id)));
            }
            if (criteria.getDiagnosticId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDiagnosticId(),
                            root -> root.join(ONT_.diagnostics, JoinType.LEFT).get(Diagnostic_.id)
                        )
                    );
            }
            if (criteria.getMetriqueId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getMetriqueId(), root -> root.join(ONT_.metriques, JoinType.LEFT).get(Metrique_.id))
                    );
            }
        }
        return specification;
    }
}
