package sn.sonatel.dsi.ins.ftsirc.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.sonatel.dsi.ins.ftsirc.domain.TypeDiagnostic;

/**
 * Spring Data JPA repository for the TypeDiagnostic entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypeDiagnosticRepository extends JpaRepository<TypeDiagnostic, Long>, JpaSpecificationExecutor<TypeDiagnostic> {}
