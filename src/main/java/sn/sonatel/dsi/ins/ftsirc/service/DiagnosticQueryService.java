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
import sn.sonatel.dsi.ins.ftsirc.domain.Diagnostic;
import sn.sonatel.dsi.ins.ftsirc.repository.DiagnosticRepository;
import sn.sonatel.dsi.ins.ftsirc.service.criteria.DiagnosticCriteria;
import sn.sonatel.dsi.ins.ftsirc.service.dto.DiagnosticDTO;
import sn.sonatel.dsi.ins.ftsirc.service.mapper.DiagnosticMapper;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Diagnostic} entities in the database.
 * The main input is a {@link DiagnosticCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DiagnosticDTO} or a {@link Page} of {@link DiagnosticDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DiagnosticQueryService extends QueryService<Diagnostic> {

    private final Logger log = LoggerFactory.getLogger(DiagnosticQueryService.class);

    private final DiagnosticRepository diagnosticRepository;

    private final DiagnosticMapper diagnosticMapper;

    public DiagnosticQueryService(DiagnosticRepository diagnosticRepository, DiagnosticMapper diagnosticMapper) {
        this.diagnosticRepository = diagnosticRepository;
        this.diagnosticMapper = diagnosticMapper;
    }

    /**
     * Return a {@link List} of {@link DiagnosticDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DiagnosticDTO> findByCriteria(DiagnosticCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Diagnostic> specification = createSpecification(criteria);
        return diagnosticMapper.toDto(diagnosticRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link DiagnosticDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DiagnosticDTO> findByCriteria(DiagnosticCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Diagnostic> specification = createSpecification(criteria);
        return diagnosticRepository.findAll(specification, page).map(diagnosticMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DiagnosticCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Diagnostic> specification = createSpecification(criteria);
        return diagnosticRepository.count(specification);
    }

    /**
     * Function to convert {@link DiagnosticCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Diagnostic> createSpecification(DiagnosticCriteria criteria) {
        Specification<Diagnostic> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Diagnostic_.id));
            }
            if (criteria.getStatutONT() != null) {
                specification = specification.and(buildSpecification(criteria.getStatutONT(), Diagnostic_.statutONT));
            }
            if (criteria.getDateDiagnostic() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateDiagnostic(), Diagnostic_.dateDiagnostic));
            }
            if (criteria.getTypeDiagnostic() != null) {
                specification = specification.and(buildSpecification(criteria.getTypeDiagnostic(), Diagnostic_.typeDiagnostic));
            }
            if (criteria.getDebitUp() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDebitUp(), Diagnostic_.debitUp));
            }
            if (criteria.getDebitDown() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDebitDown(), Diagnostic_.debitDown));
            }
            if (criteria.getPowerONT() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPowerONT(), Diagnostic_.powerONT));
            }
            if (criteria.getPowerOLT() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPowerOLT(), Diagnostic_.powerOLT));
            }
            if (criteria.getSignalId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getSignalId(), root -> root.join(Diagnostic_.signal, JoinType.LEFT).get(Signal_.id))
                    );
            }
            if (criteria.getOntId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getOntId(), root -> root.join(Diagnostic_.ont, JoinType.LEFT).get(ONT_.id))
                    );
            }
            if (criteria.getAnomalieId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAnomalieId(),
                            root -> root.join(Diagnostic_.anomalies, JoinType.LEFT).get(Anomalie_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
