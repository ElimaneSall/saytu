package sn.sonatel.dsi.ins.ftsirc.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.sonatel.dsi.ins.ftsirc.domain.Diagnostic;
import sn.sonatel.dsi.ins.ftsirc.domain.ONT;

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
    @Query("SELECT DATE(d.dateDiagnostic) as diagnosticDate, COUNT(d) as numberOfManualDiagnostics " +
        "FROM Diagnostic d " +
        "WHERE d.typeDiagnostic = 'manuel' " +
        "GROUP BY DATE(d.dateDiagnostic) " +
        "ORDER BY DATE(d.dateDiagnostic)")
    List<Object[]> countManualDiagnosticsPerDay();

    @Query("SELECT DATE(d.dateDiagnostic) AS diagnosticDate, " +
        "AVG(CAST(d.powerONT AS double)) AS averagePowerOnt, " +
        "AVG(CAST(d.powerOLT AS double)) AS averagePowerOlt " +
        "FROM Diagnostic d " +
        "WHERE d.ont = :ont " +
        "GROUP BY DATE(d.dateDiagnostic) " +
        "ORDER BY DATE(d.dateDiagnostic)")
    List<Object[]> calculateAveragePowerPerDay(ONT ont);


    default List<Diagnostic> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<Diagnostic> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
