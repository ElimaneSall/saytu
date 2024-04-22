package sn.sonatel.dsi.ins.ftsirc.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.sonatel.dsi.ins.ftsirc.domain.OLT;

/**
 * Spring Data JPA repository for the OLT entity.
 */
@Repository
public interface OLTRepository extends JpaRepository<OLT, Long>, JpaSpecificationExecutor<OLT> {
    default Optional<OLT> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<OLT> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<OLT> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(value = "select oLT from OLT oLT left join fetch oLT.adresse", countQuery = "select count(oLT) from OLT oLT")
    Page<OLT> findAllWithToOneRelationships(Pageable pageable);

    @Query("select oLT from OLT oLT left join fetch oLT.adresse")
    List<OLT> findAllWithToOneRelationships();

    @Query("select oLT from OLT oLT left join fetch oLT.adresse where oLT.id =:id")
    Optional<OLT> findOneWithToOneRelationships(@Param("id") Long id);
}
