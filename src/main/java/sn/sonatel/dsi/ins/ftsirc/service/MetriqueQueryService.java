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
import sn.sonatel.dsi.ins.ftsirc.domain.Metrique;
import sn.sonatel.dsi.ins.ftsirc.repository.MetriqueRepository;
import sn.sonatel.dsi.ins.ftsirc.service.criteria.MetriqueCriteria;
import sn.sonatel.dsi.ins.ftsirc.service.dto.MetriqueDTO;
import sn.sonatel.dsi.ins.ftsirc.service.mapper.MetriqueMapper;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Metrique} entities in the database.
 * The main input is a {@link MetriqueCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MetriqueDTO} or a {@link Page} of {@link MetriqueDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MetriqueQueryService extends QueryService<Metrique> {

    private final Logger log = LoggerFactory.getLogger(MetriqueQueryService.class);

    private final MetriqueRepository metriqueRepository;

    private final MetriqueMapper metriqueMapper;

    public MetriqueQueryService(MetriqueRepository metriqueRepository, MetriqueMapper metriqueMapper) {
        this.metriqueRepository = metriqueRepository;
        this.metriqueMapper = metriqueMapper;
    }

    /**
     * Return a {@link List} of {@link MetriqueDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MetriqueDTO> findByCriteria(MetriqueCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Metrique> specification = createSpecification(criteria);
        return metriqueMapper.toDto(metriqueRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MetriqueDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MetriqueDTO> findByCriteria(MetriqueCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Metrique> specification = createSpecification(criteria);
        return metriqueRepository.findAll(specification, page).map(metriqueMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MetriqueCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Metrique> specification = createSpecification(criteria);
        return metriqueRepository.count(specification);
    }

    /**
     * Function to convert {@link MetriqueCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Metrique> createSpecification(MetriqueCriteria criteria) {
        Specification<Metrique> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Metrique_.id));
            }
            if (criteria.getOltPower() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOltPower(), Metrique_.oltPower));
            }
            if (criteria.getOntPower() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOntPower(), Metrique_.ontPower));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), Metrique_.createdAt));
            }
            if (criteria.getOntId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getOntId(), root -> root.join(Metrique_.ont, JoinType.LEFT).get(ONT_.id))
                    );
            }
        }
        return specification;
    }
}
