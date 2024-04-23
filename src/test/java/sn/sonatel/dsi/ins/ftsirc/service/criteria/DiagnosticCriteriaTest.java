package sn.sonatel.dsi.ins.ftsirc.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class DiagnosticCriteriaTest {

    @Test
    void newDiagnosticCriteriaHasAllFiltersNullTest() {
        var diagnosticCriteria = new DiagnosticCriteria();
        assertThat(diagnosticCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void diagnosticCriteriaFluentMethodsCreatesFiltersTest() {
        var diagnosticCriteria = new DiagnosticCriteria();

        setAllFilters(diagnosticCriteria);

        assertThat(diagnosticCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void diagnosticCriteriaCopyCreatesNullFilterTest() {
        var diagnosticCriteria = new DiagnosticCriteria();
        var copy = diagnosticCriteria.copy();

        assertThat(diagnosticCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(diagnosticCriteria)
        );
    }

    @Test
    void diagnosticCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var diagnosticCriteria = new DiagnosticCriteria();
        setAllFilters(diagnosticCriteria);

        var copy = diagnosticCriteria.copy();

        assertThat(diagnosticCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(diagnosticCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var diagnosticCriteria = new DiagnosticCriteria();

        assertThat(diagnosticCriteria).hasToString("DiagnosticCriteria{}");
    }

    private static void setAllFilters(DiagnosticCriteria diagnosticCriteria) {
        diagnosticCriteria.id();
        diagnosticCriteria.index();
        diagnosticCriteria.statutONT();
        diagnosticCriteria.debitUp();
        diagnosticCriteria.debitDown();
        diagnosticCriteria.dateDiagnostic();
        diagnosticCriteria.typeDiagnostic();
        diagnosticCriteria.signalId();
        diagnosticCriteria.ontId();
        diagnosticCriteria.anomalieId();
        diagnosticCriteria.distinct();
    }

    private static Condition<DiagnosticCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getIndex()) &&
                condition.apply(criteria.getStatutONT()) &&
                condition.apply(criteria.getDebitUp()) &&
                condition.apply(criteria.getDebitDown()) &&
                condition.apply(criteria.getDateDiagnostic()) &&
                condition.apply(criteria.getTypeDiagnostic()) &&
                condition.apply(criteria.getSignalId()) &&
                condition.apply(criteria.getOntId()) &&
                condition.apply(criteria.getAnomalieId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<DiagnosticCriteria> copyFiltersAre(DiagnosticCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getIndex(), copy.getIndex()) &&
                condition.apply(criteria.getStatutONT(), copy.getStatutONT()) &&
                condition.apply(criteria.getDebitUp(), copy.getDebitUp()) &&
                condition.apply(criteria.getDebitDown(), copy.getDebitDown()) &&
                condition.apply(criteria.getDateDiagnostic(), copy.getDateDiagnostic()) &&
                condition.apply(criteria.getTypeDiagnostic(), copy.getTypeDiagnostic()) &&
                condition.apply(criteria.getSignalId(), copy.getSignalId()) &&
                condition.apply(criteria.getOntId(), copy.getOntId()) &&
                condition.apply(criteria.getAnomalieId(), copy.getAnomalieId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
