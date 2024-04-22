package sn.sonatel.dsi.ins.ftsirc.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ONTCriteriaTest {

    @Test
    void newONTCriteriaHasAllFiltersNullTest() {
        var oNTCriteria = new ONTCriteria();
        assertThat(oNTCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void oNTCriteriaFluentMethodsCreatesFiltersTest() {
        var oNTCriteria = new ONTCriteria();

        setAllFilters(oNTCriteria);

        assertThat(oNTCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void oNTCriteriaCopyCreatesNullFilterTest() {
        var oNTCriteria = new ONTCriteria();
        var copy = oNTCriteria.copy();

        assertThat(oNTCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(oNTCriteria)
        );
    }

    @Test
    void oNTCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var oNTCriteria = new ONTCriteria();
        setAllFilters(oNTCriteria);

        var copy = oNTCriteria.copy();

        assertThat(oNTCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(oNTCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var oNTCriteria = new ONTCriteria();

        assertThat(oNTCriteria).hasToString("ONTCriteria{}");
    }

    private static void setAllFilters(ONTCriteria oNTCriteria) {
        oNTCriteria.id();
        oNTCriteria.index();
        oNTCriteria.ontIP();
        oNTCriteria.serviceId();
        oNTCriteria.slot();
        oNTCriteria.pon();
        oNTCriteria.ponIndex();
        oNTCriteria.maxUp();
        oNTCriteria.maxDown();
        oNTCriteria.createdAt();
        oNTCriteria.updatedAt();
        oNTCriteria.clientId();
        oNTCriteria.oltId();
        oNTCriteria.diagnosticId();
        oNTCriteria.metriqueId();
        oNTCriteria.distinct();
    }

    private static Condition<ONTCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getIndex()) &&
                condition.apply(criteria.getOntIP()) &&
                condition.apply(criteria.getServiceId()) &&
                condition.apply(criteria.getSlot()) &&
                condition.apply(criteria.getPon()) &&
                condition.apply(criteria.getPonIndex()) &&
                condition.apply(criteria.getMaxUp()) &&
                condition.apply(criteria.getMaxDown()) &&
                condition.apply(criteria.getCreatedAt()) &&
                condition.apply(criteria.getUpdatedAt()) &&
                condition.apply(criteria.getClientId()) &&
                condition.apply(criteria.getOltId()) &&
                condition.apply(criteria.getDiagnosticId()) &&
                condition.apply(criteria.getMetriqueId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ONTCriteria> copyFiltersAre(ONTCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getIndex(), copy.getIndex()) &&
                condition.apply(criteria.getOntIP(), copy.getOntIP()) &&
                condition.apply(criteria.getServiceId(), copy.getServiceId()) &&
                condition.apply(criteria.getSlot(), copy.getSlot()) &&
                condition.apply(criteria.getPon(), copy.getPon()) &&
                condition.apply(criteria.getPonIndex(), copy.getPonIndex()) &&
                condition.apply(criteria.getMaxUp(), copy.getMaxUp()) &&
                condition.apply(criteria.getMaxDown(), copy.getMaxDown()) &&
                condition.apply(criteria.getCreatedAt(), copy.getCreatedAt()) &&
                condition.apply(criteria.getUpdatedAt(), copy.getUpdatedAt()) &&
                condition.apply(criteria.getClientId(), copy.getClientId()) &&
                condition.apply(criteria.getOltId(), copy.getOltId()) &&
                condition.apply(criteria.getDiagnosticId(), copy.getDiagnosticId()) &&
                condition.apply(criteria.getMetriqueId(), copy.getMetriqueId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
