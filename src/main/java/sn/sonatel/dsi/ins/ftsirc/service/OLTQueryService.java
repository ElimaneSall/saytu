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
import sn.sonatel.dsi.ins.ftsirc.domain.OLT;
import sn.sonatel.dsi.ins.ftsirc.repository.OLTRepository;
import sn.sonatel.dsi.ins.ftsirc.service.criteria.OLTCriteria;
import sn.sonatel.dsi.ins.ftsirc.service.dto.OLTDTO;
import sn.sonatel.dsi.ins.ftsirc.service.mapper.OLTMapper;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link OLT} entities in the database.
 * The main input is a {@link OLTCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link OLTDTO} or a {@link Page} of {@link OLTDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OLTQueryService extends QueryService<OLT> {

    private final Logger log = LoggerFactory.getLogger(OLTQueryService.class);

    private final OLTRepository oLTRepository;

    private final OLTMapper oLTMapper;

    public OLTQueryService(OLTRepository oLTRepository, OLTMapper oLTMapper) {
        this.oLTRepository = oLTRepository;
        this.oLTMapper = oLTMapper;
    }

    /**
     * Return a {@link List} of {@link OLTDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<OLTDTO> findByCriteria(OLTCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<OLT> specification = createSpecification(criteria);
        return oLTMapper.toDto(oLTRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link OLTDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OLTDTO> findByCriteria(OLTCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<OLT> specification = createSpecification(criteria);
        return oLTRepository.findAll(specification, page).map(oLTMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OLTCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<OLT> specification = createSpecification(criteria);
        return oLTRepository.count(specification);
    }

    /**
     * Function to convert {@link OLTCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<OLT> createSpecification(OLTCriteria criteria) {
        Specification<OLT> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), OLT_.id));
            }
            if (criteria.getLibelle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLibelle(), OLT_.libelle));
            }
            if (criteria.getIp() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIp(), OLT_.ip));
            }
            if (criteria.getVendeur() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVendeur(), OLT_.vendeur));
            }
            if (criteria.getTypeEquipment() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTypeEquipment(), OLT_.typeEquipment));
            }
            if (criteria.getCodeEquipment() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCodeEquipment(), OLT_.codeEquipment));
            }
            if (criteria.getAdresse() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAdresse(), OLT_.adresse));
            }
            if (criteria.getEmplacement() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmplacement(), OLT_.emplacement));
            }
            if (criteria.getTypeCarte() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTypeCarte(), OLT_.typeCarte));
            }
            if (criteria.getLatitude() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLatitude(), OLT_.latitude));
            }
            if (criteria.getLongitude() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLongitude(), OLT_.longitude));
            }
            if (criteria.getCapacite() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCapacite(), OLT_.capacite));
            }
            if (criteria.getEtat() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEtat(), OLT_.etat));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), OLT_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), OLT_.updatedAt));
            }
            if (criteria.getOntId() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getOntId(), root -> root.join(OLT_.onts, JoinType.LEFT).get(ONT_.id)));
            }
        }
        return specification;
    }
}
