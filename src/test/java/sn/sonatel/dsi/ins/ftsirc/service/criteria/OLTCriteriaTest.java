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
        oLTCriteria.libelle();
        oLTCriteria.ip();
        oLTCriteria.vendeur();
        oLTCriteria.typeEquipment();
        oLTCriteria.codeEquipment();
        oLTCriteria.adresse();
        oLTCriteria.emplacement();
        oLTCriteria.typeCarte();
        oLTCriteria.latitude();
        oLTCriteria.longitude();
        oLTCriteria.capacite();
        oLTCriteria.etat();
        oLTCriteria.createdAt();
        oLTCriteria.updatedAt();
        oLTCriteria.ontId();
        oLTCriteria.distinct();
    }

    private static Condition<OLTCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getLibelle()) &&
                condition.apply(criteria.getIp()) &&
                condition.apply(criteria.getVendeur()) &&
                condition.apply(criteria.getTypeEquipment()) &&
                condition.apply(criteria.getCodeEquipment()) &&
                condition.apply(criteria.getAdresse()) &&
                condition.apply(criteria.getEmplacement()) &&
                condition.apply(criteria.getTypeCarte()) &&
                condition.apply(criteria.getLatitude()) &&
                condition.apply(criteria.getLongitude()) &&
                condition.apply(criteria.getCapacite()) &&
                condition.apply(criteria.getEtat()) &&
                condition.apply(criteria.getCreatedAt()) &&
                condition.apply(criteria.getUpdatedAt()) &&
                condition.apply(criteria.getOntId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<OLTCriteria> copyFiltersAre(OLTCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getLibelle(), copy.getLibelle()) &&
                condition.apply(criteria.getIp(), copy.getIp()) &&
                condition.apply(criteria.getVendeur(), copy.getVendeur()) &&
                condition.apply(criteria.getTypeEquipment(), copy.getTypeEquipment()) &&
                condition.apply(criteria.getCodeEquipment(), copy.getCodeEquipment()) &&
                condition.apply(criteria.getAdresse(), copy.getAdresse()) &&
                condition.apply(criteria.getEmplacement(), copy.getEmplacement()) &&
                condition.apply(criteria.getTypeCarte(), copy.getTypeCarte()) &&
                condition.apply(criteria.getLatitude(), copy.getLatitude()) &&
                condition.apply(criteria.getLongitude(), copy.getLongitude()) &&
                condition.apply(criteria.getCapacite(), copy.getCapacite()) &&
                condition.apply(criteria.getEtat(), copy.getEtat()) &&
                condition.apply(criteria.getCreatedAt(), copy.getCreatedAt()) &&
                condition.apply(criteria.getUpdatedAt(), copy.getUpdatedAt()) &&
                condition.apply(criteria.getOntId(), copy.getOntId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
