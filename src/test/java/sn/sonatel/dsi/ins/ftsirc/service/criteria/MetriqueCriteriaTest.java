package sn.sonatel.dsi.ins.ftsirc.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class MetriqueCriteriaTest {

    @Test
    void newMetriqueCriteriaHasAllFiltersNullTest() {
        var metriqueCriteria = new MetriqueCriteria();
        assertThat(metriqueCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void metriqueCriteriaFluentMethodsCreatesFiltersTest() {
        var metriqueCriteria = new MetriqueCriteria();

        setAllFilters(metriqueCriteria);

        assertThat(metriqueCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void metriqueCriteriaCopyCreatesNullFilterTest() {
        var metriqueCriteria = new MetriqueCriteria();
        var copy = metriqueCriteria.copy();

        assertThat(metriqueCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(metriqueCriteria)
        );
    }

    @Test
    void metriqueCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var metriqueCriteria = new MetriqueCriteria();
        setAllFilters(metriqueCriteria);

        var copy = metriqueCriteria.copy();

        assertThat(metriqueCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(metriqueCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var metriqueCriteria = new MetriqueCriteria();

        assertThat(metriqueCriteria).hasToString("MetriqueCriteria{}");
    }

    private static void setAllFilters(MetriqueCriteria metriqueCriteria) {
        metriqueCriteria.id();
        metriqueCriteria.oltPower();
        metriqueCriteria.ontPower();
        metriqueCriteria.createdAt();
        metriqueCriteria.ontId();
        metriqueCriteria.distinct();
    }

    private static Condition<MetriqueCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getOltPower()) &&
                condition.apply(criteria.getOntPower()) &&
                condition.apply(criteria.getCreatedAt()) &&
                condition.apply(criteria.getOntId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<MetriqueCriteria> copyFiltersAre(MetriqueCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getOltPower(), copy.getOltPower()) &&
                condition.apply(criteria.getOntPower(), copy.getOntPower()) &&
                condition.apply(criteria.getCreatedAt(), copy.getCreatedAt()) &&
                condition.apply(criteria.getOntId(), copy.getOntId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
