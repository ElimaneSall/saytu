package sn.sonatel.dsi.ins.ftsirc.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.sonatel.dsi.ins.ftsirc.domain.Metrique;

/**
 * Spring Data JPA repository for the Metrique entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MetriqueRepository extends JpaRepository<Metrique, Long>, JpaSpecificationExecutor<Metrique> {}
