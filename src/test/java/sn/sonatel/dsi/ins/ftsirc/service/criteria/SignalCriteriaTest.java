package sn.sonatel.dsi.ins.ftsirc.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class SignalCriteriaTest {

    @Test
    void newSignalCriteriaHasAllFiltersNullTest() {
        var signalCriteria = new SignalCriteria();
        assertThat(signalCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void signalCriteriaFluentMethodsCreatesFiltersTest() {
        var signalCriteria = new SignalCriteria();

        setAllFilters(signalCriteria);

        assertThat(signalCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void signalCriteriaCopyCreatesNullFilterTest() {
        var signalCriteria = new SignalCriteria();
        var copy = signalCriteria.copy();

        assertThat(signalCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(signalCriteria)
        );
    }

    @Test
    void signalCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var signalCriteria = new SignalCriteria();
        setAllFilters(signalCriteria);

        var copy = signalCriteria.copy();

        assertThat(signalCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(signalCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var signalCriteria = new SignalCriteria();

        assertThat(signalCriteria).hasToString("SignalCriteria{}");
    }

    private static void setAllFilters(SignalCriteria signalCriteria) {
        signalCriteria.id();
        signalCriteria.libelle();
        signalCriteria.seuilMin();
        signalCriteria.seuilMax();
        signalCriteria.diagnosticId();
        signalCriteria.distinct();
    }

    private static Condition<SignalCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getLibelle()) &&
                condition.apply(criteria.getSeuilMin()) &&
                condition.apply(criteria.getSeuilMax()) &&
                condition.apply(criteria.getDiagnosticId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<SignalCriteria> copyFiltersAre(SignalCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getLibelle(), copy.getLibelle()) &&
                condition.apply(criteria.getSeuilMin(), copy.getSeuilMin()) &&
                condition.apply(criteria.getSeuilMax(), copy.getSeuilMax()) &&
                condition.apply(criteria.getDiagnosticId(), copy.getDiagnosticId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
