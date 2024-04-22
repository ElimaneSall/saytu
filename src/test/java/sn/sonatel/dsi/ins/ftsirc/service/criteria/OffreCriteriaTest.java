package sn.sonatel.dsi.ins.ftsirc.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class OffreCriteriaTest {

    @Test
    void newOffreCriteriaHasAllFiltersNullTest() {
        var offreCriteria = new OffreCriteria();
        assertThat(offreCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void offreCriteriaFluentMethodsCreatesFiltersTest() {
        var offreCriteria = new OffreCriteria();

        setAllFilters(offreCriteria);

        assertThat(offreCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void offreCriteriaCopyCreatesNullFilterTest() {
        var offreCriteria = new OffreCriteria();
        var copy = offreCriteria.copy();

        assertThat(offreCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(offreCriteria)
        );
    }

    @Test
    void offreCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var offreCriteria = new OffreCriteria();
        setAllFilters(offreCriteria);

        var copy = offreCriteria.copy();

        assertThat(offreCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(offreCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var offreCriteria = new OffreCriteria();

        assertThat(offreCriteria).hasToString("OffreCriteria{}");
    }

    private static void setAllFilters(OffreCriteria offreCriteria) {
        offreCriteria.id();
        offreCriteria.libelle();
        offreCriteria.debitMax();
        offreCriteria.clientId();
        offreCriteria.distinct();
    }

    private static Condition<OffreCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getLibelle()) &&
                condition.apply(criteria.getDebitMax()) &&
                condition.apply(criteria.getClientId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<OffreCriteria> copyFiltersAre(OffreCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getLibelle(), copy.getLibelle()) &&
                condition.apply(criteria.getDebitMax(), copy.getDebitMax()) &&
                condition.apply(criteria.getClientId(), copy.getClientId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
