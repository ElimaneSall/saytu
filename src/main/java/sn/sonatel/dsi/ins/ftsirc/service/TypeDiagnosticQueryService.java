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
import sn.sonatel.dsi.ins.ftsirc.domain.TypeDiagnostic;
import sn.sonatel.dsi.ins.ftsirc.repository.TypeDiagnosticRepository;
import sn.sonatel.dsi.ins.ftsirc.service.criteria.TypeDiagnosticCriteria;
import sn.sonatel.dsi.ins.ftsirc.service.dto.TypeDiagnosticDTO;
import sn.sonatel.dsi.ins.ftsirc.service.mapper.TypeDiagnosticMapper;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link TypeDiagnostic} entities in the database.
 * The main input is a {@link TypeDiagnosticCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link TypeDiagnosticDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TypeDiagnosticQueryService extends QueryService<TypeDiagnostic> {

    private final Logger log = LoggerFactory.getLogger(TypeDiagnosticQueryService.class);

    private final TypeDiagnosticRepository typeDiagnosticRepository;

    private final TypeDiagnosticMapper typeDiagnosticMapper;

    public TypeDiagnosticQueryService(TypeDiagnosticRepository typeDiagnosticRepository, TypeDiagnosticMapper typeDiagnosticMapper) {
        this.typeDiagnosticRepository = typeDiagnosticRepository;
        this.typeDiagnosticMapper = typeDiagnosticMapper;
    }

    /**
     * Return a {@link Page} of {@link TypeDiagnosticDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TypeDiagnosticDTO> findByCriteria(TypeDiagnosticCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TypeDiagnostic> specification = createSpecification(criteria);
        return typeDiagnosticRepository.findAll(specification, page).map(typeDiagnosticMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TypeDiagnosticCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TypeDiagnostic> specification = createSpecification(criteria);
        return typeDiagnosticRepository.count(specification);
    }

    /**
     * Function to convert {@link TypeDiagnosticCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TypeDiagnostic> createSpecification(TypeDiagnosticCriteria criteria) {
        Specification<TypeDiagnostic> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TypeDiagnostic_.id));
            }
            if (criteria.getLibelle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLibelle(), TypeDiagnostic_.libelle));
            }
            if (criteria.getDiagnosticId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getDiagnosticId(),
                        root -> root.join(TypeDiagnostic_.diagnostics, JoinType.LEFT).get(Diagnostic_.id)
                    )
                );
            }
        }
        return specification;
    }
}
