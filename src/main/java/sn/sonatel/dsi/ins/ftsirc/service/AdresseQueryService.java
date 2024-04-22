package sn.sonatel.dsi.ins.ftsirc.service;

import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.sonatel.dsi.ins.ftsirc.domain.*; // for static metamodels
import sn.sonatel.dsi.ins.ftsirc.domain.Adresse;
import sn.sonatel.dsi.ins.ftsirc.repository.AdresseRepository;
import sn.sonatel.dsi.ins.ftsirc.service.criteria.AdresseCriteria;
import sn.sonatel.dsi.ins.ftsirc.service.dto.AdresseDTO;
import sn.sonatel.dsi.ins.ftsirc.service.mapper.AdresseMapper;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Adresse} entities in the database.
 * The main input is a {@link AdresseCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AdresseDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AdresseQueryService extends QueryService<Adresse> {

    private final Logger log = LoggerFactory.getLogger(AdresseQueryService.class);

    private final AdresseRepository adresseRepository;

    private final AdresseMapper adresseMapper;

    public AdresseQueryService(AdresseRepository adresseRepository, AdresseMapper adresseMapper) {
        this.adresseRepository = adresseRepository;
        this.adresseMapper = adresseMapper;
    }

    /**
     * Return a {@link Page} of {@link AdresseDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AdresseDTO> findByCriteria(AdresseCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Adresse> specification = createSpecification(criteria);
        return adresseRepository.findAll(specification, page).map(adresseMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AdresseCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Adresse> specification = createSpecification(criteria);
        return adresseRepository.count(specification);
    }

    /**
     * Function to convert {@link AdresseCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Adresse> createSpecification(AdresseCriteria criteria) {
        Specification<Adresse> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Adresse_.id));
            }
            if (criteria.getRegion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRegion(), Adresse_.region));
            }
            if (criteria.getVille() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVille(), Adresse_.ville));
            }
            if (criteria.getCommune() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCommune(), Adresse_.commune));
            }
            if (criteria.getLatitude() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLatitude(), Adresse_.latitude));
            }
            if (criteria.getLongitude() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLongitude(), Adresse_.longitude));
            }
            if (criteria.getOltId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getOltId(), root -> root.join(Adresse_.olt, JoinType.LEFT).get(OLT_.id))
                );
            }
        }
        return specification;
    }
}
