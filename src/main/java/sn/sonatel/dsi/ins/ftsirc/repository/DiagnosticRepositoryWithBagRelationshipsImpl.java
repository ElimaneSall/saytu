package sn.sonatel.dsi.ins.ftsirc.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import sn.sonatel.dsi.ins.ftsirc.domain.Diagnostic;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class DiagnosticRepositoryWithBagRelationshipsImpl implements DiagnosticRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String DIAGNOSTICS_PARAMETER = "diagnostics";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Diagnostic> fetchBagRelationships(Optional<Diagnostic> diagnostic) {
        return diagnostic.map(this::fetchAnomalies);
    }

    @Override
    public Page<Diagnostic> fetchBagRelationships(Page<Diagnostic> diagnostics) {
        return new PageImpl<>(fetchBagRelationships(diagnostics.getContent()), diagnostics.getPageable(), diagnostics.getTotalElements());
    }

    @Override
    public List<Diagnostic> fetchBagRelationships(List<Diagnostic> diagnostics) {
        return Optional.of(diagnostics).map(this::fetchAnomalies).orElse(Collections.emptyList());
    }

    Diagnostic fetchAnomalies(Diagnostic result) {
        return entityManager
            .createQuery(
                "select diagnostic from Diagnostic diagnostic left join fetch diagnostic.anomalies where diagnostic.id = :id",
                Diagnostic.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Diagnostic> fetchAnomalies(List<Diagnostic> diagnostics) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, diagnostics.size()).forEach(index -> order.put(diagnostics.get(index).getId(), index));
        List<Diagnostic> result = entityManager
            .createQuery(
                "select diagnostic from Diagnostic diagnostic left join fetch diagnostic.anomalies where diagnostic in :diagnostics",
                Diagnostic.class
            )
            .setParameter(DIAGNOSTICS_PARAMETER, diagnostics)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
