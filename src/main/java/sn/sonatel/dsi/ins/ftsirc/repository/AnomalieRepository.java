package sn.sonatel.dsi.ins.ftsirc.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.sonatel.dsi.ins.ftsirc.domain.Anomalie;
import sn.sonatel.dsi.ins.ftsirc.domain.ONT;

/**
 * Spring Data JPA repository for the Anomalie entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AnomalieRepository extends JpaRepository<Anomalie, Long>, JpaSpecificationExecutor<Anomalie> {
    @Query("select anomalie from Anomalie anomalie  where anomalie.code =:code")
    Anomalie findByCode(String code);
}
