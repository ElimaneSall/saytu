package sn.sonatel.dsi.ins.ftsirc.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.sonatel.dsi.ins.ftsirc.domain.ONT;

/**
 * Spring Data JPA repository for the ONT entity.
 */
@Repository
public interface ONTRepository extends JpaRepository<ONT, Long>, JpaSpecificationExecutor<ONT> {
    default Optional<ONT> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    @Query("select oNT from ONT oNT where oNT.serviceId =:serviceId")
    ONT findByServiceId(String serviceId);
    default List<ONT> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ONT> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(value = "select oNT from ONT oNT left join fetch oNT.client", countQuery = "select count(oNT) from ONT oNT")
    Page<ONT> findAllWithToOneRelationships(Pageable pageable);

    @Query("select oNT from ONT oNT left join fetch oNT.client")
    List<ONT> findAllWithToOneRelationships();

    @Query("select oNT from ONT oNT left join fetch oNT.client where oNT.id =:id")
    Optional<ONT> findOneWithToOneRelationships(@Param("id") Long id);
}
