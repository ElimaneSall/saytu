package sn.sonatel.dsi.ins.ftsirc.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.sonatel.dsi.ins.ftsirc.domain.Anomalie;

/**
 * Spring Data JPA repository for the Anomalie entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AnomalieRepository extends JpaRepository<Anomalie, Long>, JpaSpecificationExecutor<Anomalie> {}
