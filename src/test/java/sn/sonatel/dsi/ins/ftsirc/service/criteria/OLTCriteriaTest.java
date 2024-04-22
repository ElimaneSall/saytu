package sn.sonatel.dsi.ins.ftsirc.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class OLTCriteriaTest {

    @Test
    void newOLTCriteriaHasAllFiltersNullTest() {
        var oLTCriteria = new OLTCriteria();
        assertThat(oLTCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void oLTCriteriaFluentMethodsCreatesFiltersTest() {
        var oLTCriteria = new OLTCriteria();

        setAllFilters(oLTCriteria);

        assertThat(oLTCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void oLTCriteriaCopyCreatesNullFilterTest() {
        var oLTCriteria = new OLTCriteria();
        var copy = oLTCriteria.copy();

        assertThat(oLTCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(oLTCriteria)
        );
    }

    @Test
    void oLTCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var oLTCriteria = new OLTCriteria();
        setAllFilters(oLTCriteria);

        var copy = oLTCriteria.copy();

        assertThat(oLTCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(oLTCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var oLTCriteria = new OLTCriteria();

        assertThat(oLTCriteria).hasToString("OLTCriteria{}");
    }

    private static void setAllFilters(OLTCriteria oLTCriteria) {
        oLTCriteria.id();
        oLTCriteria.nom();
        oLTCriteria.ip();
        oLTCriteria.vendeur();
        oLTCriteria.etat();
        oLTCriteria.createdAt();
        oLTCriteria.updatedAt();
        oLTCriteria.adresseId();
        oLTCriteria.ontId();
        oLTCriteria.distinct();
    }

    private static Condition<OLTCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNom()) &&
                condition.apply(criteria.getIp()) &&
                condition.apply(criteria.getVendeur()) &&
                condition.apply(criteria.getEtat()) &&
                condition.apply(criteria.getCreatedAt()) &&
                condition.apply(criteria.getUpdatedAt()) &&
                condition.apply(criteria.getAdresseId()) &&
                condition.apply(criteria.getOntId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<OLTCriteria> copyFiltersAre(OLTCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNom(), copy.getNom()) &&
                condition.apply(criteria.getIp(), copy.getIp()) &&
                condition.apply(criteria.getVendeur(), copy.getVendeur()) &&
                condition.apply(criteria.getEtat(), copy.getEtat()) &&
                condition.apply(criteria.getCreatedAt(), copy.getCreatedAt()) &&
                condition.apply(criteria.getUpdatedAt(), copy.getUpdatedAt()) &&
                condition.apply(criteria.getAdresseId(), copy.getAdresseId()) &&
                condition.apply(criteria.getOntId(), copy.getOntId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
