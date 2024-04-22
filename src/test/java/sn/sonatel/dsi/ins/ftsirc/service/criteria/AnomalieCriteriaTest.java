package sn.sonatel.dsi.ins.ftsirc.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AnomalieCriteriaTest {

    @Test
    void newAnomalieCriteriaHasAllFiltersNullTest() {
        var anomalieCriteria = new AnomalieCriteria();
        assertThat(anomalieCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void anomalieCriteriaFluentMethodsCreatesFiltersTest() {
        var anomalieCriteria = new AnomalieCriteria();

        setAllFilters(anomalieCriteria);

        assertThat(anomalieCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void anomalieCriteriaCopyCreatesNullFilterTest() {
        var anomalieCriteria = new AnomalieCriteria();
        var copy = anomalieCriteria.copy();

        assertThat(anomalieCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(anomalieCriteria)
        );
    }

    @Test
    void anomalieCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var anomalieCriteria = new AnomalieCriteria();
        setAllFilters(anomalieCriteria);

        var copy = anomalieCriteria.copy();

        assertThat(anomalieCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(anomalieCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var anomalieCriteria = new AnomalieCriteria();

        assertThat(anomalieCriteria).hasToString("AnomalieCriteria{}");
    }

    private static void setAllFilters(AnomalieCriteria anomalieCriteria) {
        anomalieCriteria.id();
        anomalieCriteria.libelle();
        anomalieCriteria.diagnosticId();
        anomalieCriteria.distinct();
    }

    private static Condition<AnomalieCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getLibelle()) &&
                condition.apply(criteria.getDiagnosticId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AnomalieCriteria> copyFiltersAre(AnomalieCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getLibelle(), copy.getLibelle()) &&
                condition.apply(criteria.getDiagnosticId(), copy.getDiagnosticId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
