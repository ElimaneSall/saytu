package sn.sonatel.dsi.ins.ftsirc.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import sn.sonatel.dsi.ins.ftsirc.domain.Diagnostic;

public interface DiagnosticRepositoryWithBagRelationships {
    Optional<Diagnostic> fetchBagRelationships(Optional<Diagnostic> diagnostic);

    List<Diagnostic> fetchBagRelationships(List<Diagnostic> diagnostics);

    Page<Diagnostic> fetchBagRelationships(Page<Diagnostic> diagnostics);
}
