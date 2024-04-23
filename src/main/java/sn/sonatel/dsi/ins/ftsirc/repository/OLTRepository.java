package sn.sonatel.dsi.ins.ftsirc.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.sonatel.dsi.ins.ftsirc.domain.OLT;

/**
 * Spring Data JPA repository for the OLT entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OLTRepository extends JpaRepository<OLT, Long>, JpaSpecificationExecutor<OLT> {}
