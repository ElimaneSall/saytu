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
import sn.sonatel.dsi.ins.ftsirc.domain.Signal;
import sn.sonatel.dsi.ins.ftsirc.repository.SignalRepository;
import sn.sonatel.dsi.ins.ftsirc.service.criteria.SignalCriteria;
import sn.sonatel.dsi.ins.ftsirc.service.dto.SignalDTO;
import sn.sonatel.dsi.ins.ftsirc.service.mapper.SignalMapper;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Signal} entities in the database.
 * The main input is a {@link SignalCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link SignalDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SignalQueryService extends QueryService<Signal> {

    private final Logger log = LoggerFactory.getLogger(SignalQueryService.class);

    private final SignalRepository signalRepository;

    private final SignalMapper signalMapper;

    public SignalQueryService(SignalRepository signalRepository, SignalMapper signalMapper) {
        this.signalRepository = signalRepository;
        this.signalMapper = signalMapper;
    }

    /**
     * Return a {@link Page} of {@link SignalDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SignalDTO> findByCriteria(SignalCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Signal> specification = createSpecification(criteria);
        return signalRepository.findAll(specification, page).map(signalMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SignalCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Signal> specification = createSpecification(criteria);
        return signalRepository.count(specification);
    }

    /**
     * Function to convert {@link SignalCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Signal> createSpecification(SignalCriteria criteria) {
        Specification<Signal> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Signal_.id));
            }
            if (criteria.getLibelle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLibelle(), Signal_.libelle));
            }
            if (criteria.getSeuilMin() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSeuilMin(), Signal_.seuilMin));
            }
            if (criteria.getSeuilMax() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSeuilMax(), Signal_.seuilMax));
            }
            if (criteria.getDiagnosticId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getDiagnosticId(),
                        root -> root.join(Signal_.diagnostics, JoinType.LEFT).get(Diagnostic_.id)
                    )
                );
            }
        }
        return specification;
    }
}
