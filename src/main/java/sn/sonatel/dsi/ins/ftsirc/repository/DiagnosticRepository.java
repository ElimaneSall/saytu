package sn.sonatel.dsi.ins.ftsirc.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.sonatel.dsi.ins.ftsirc.domain.Diagnostic;

/**
 * Spring Data JPA repository for the Diagnostic entity.
 *
 * When extending this class, extend DiagnosticRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface DiagnosticRepository
    extends DiagnosticRepositoryWithBagRelationships, JpaRepository<Diagnostic, Long>, JpaSpecificationExecutor<Diagnostic> {
    default Optional<Diagnostic> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<Diagnostic> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<Diagnostic> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
