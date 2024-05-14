package sn.sonatel.dsi.ins.ftsirc.service;

import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.sonatel.dsi.ins.ftsirc.domain.Anomalie;
import sn.sonatel.dsi.ins.ftsirc.domain.Anomalie_;
import sn.sonatel.dsi.ins.ftsirc.domain.Diagnostic_;
import sn.sonatel.dsi.ins.ftsirc.repository.AnomalieRepository;
import sn.sonatel.dsi.ins.ftsirc.service.criteria.AnomalieCriteria;
import sn.sonatel.dsi.ins.ftsirc.service.dto.AnomalieDTO;
import sn.sonatel.dsi.ins.ftsirc.service.mapper.AnomalieMapper;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Anomalie} entities in the database.
 * The main input is a {@link AnomalieCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AnomalieDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AnomalieQueryService extends QueryService<Anomalie> {

    private final Logger log = LoggerFactory.getLogger(AnomalieQueryService.class);

    private final AnomalieRepository anomalieRepository;

    private final AnomalieMapper anomalieMapper;

    public AnomalieQueryService(AnomalieRepository anomalieRepository, AnomalieMapper anomalieMapper) {
        this.anomalieRepository = anomalieRepository;
        this.anomalieMapper = anomalieMapper;
    }

    /**
     * Return a {@link Page} of {@link AnomalieDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AnomalieDTO> findByCriteria(AnomalieCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Anomalie> specification = createSpecification(criteria);
        return anomalieRepository.findAll(specification, page).map(anomalieMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AnomalieCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Anomalie> specification = createSpecification(criteria);
        return anomalieRepository.count(specification);
    }

    /**
     * Function to convert {@link AnomalieCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Anomalie> createSpecification(AnomalieCriteria criteria) {
        Specification<Anomalie> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Anomalie_.id));
            }
            if (criteria.getLibelle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLibelle(), Anomalie_.libelle));
            }
            if (criteria.getDiagnosticId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getDiagnosticId(),
                        root -> root.join(Anomalie_.diagnostics, JoinType.LEFT).get(Diagnostic_.id)
                    )
                );
            }
        }
        return specification;
    }
}
