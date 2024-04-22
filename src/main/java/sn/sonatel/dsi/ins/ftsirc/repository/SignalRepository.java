package sn.sonatel.dsi.ins.ftsirc.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.sonatel.dsi.ins.ftsirc.domain.Signal;

/**
 * Spring Data JPA repository for the Signal entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SignalRepository extends JpaRepository<Signal, Long>, JpaSpecificationExecutor<Signal> {}
