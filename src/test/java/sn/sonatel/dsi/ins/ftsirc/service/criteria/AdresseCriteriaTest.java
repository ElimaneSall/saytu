package sn.sonatel.dsi.ins.ftsirc.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AdresseCriteriaTest {

    @Test
    void newAdresseCriteriaHasAllFiltersNullTest() {
        var adresseCriteria = new AdresseCriteria();
        assertThat(adresseCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void adresseCriteriaFluentMethodsCreatesFiltersTest() {
        var adresseCriteria = new AdresseCriteria();

        setAllFilters(adresseCriteria);

        assertThat(adresseCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void adresseCriteriaCopyCreatesNullFilterTest() {
        var adresseCriteria = new AdresseCriteria();
        var copy = adresseCriteria.copy();

        assertThat(adresseCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(adresseCriteria)
        );
    }

    @Test
    void adresseCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var adresseCriteria = new AdresseCriteria();
        setAllFilters(adresseCriteria);

        var copy = adresseCriteria.copy();

        assertThat(adresseCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(adresseCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var adresseCriteria = new AdresseCriteria();

        assertThat(adresseCriteria).hasToString("AdresseCriteria{}");
    }

    private static void setAllFilters(AdresseCriteria adresseCriteria) {
        adresseCriteria.id();
        adresseCriteria.region();
        adresseCriteria.ville();
        adresseCriteria.commune();
        adresseCriteria.latitude();
        adresseCriteria.longitude();
        adresseCriteria.oltId();
        adresseCriteria.distinct();
    }

    private static Condition<AdresseCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getRegion()) &&
                condition.apply(criteria.getVille()) &&
                condition.apply(criteria.getCommune()) &&
                condition.apply(criteria.getLatitude()) &&
                condition.apply(criteria.getLongitude()) &&
                condition.apply(criteria.getOltId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AdresseCriteria> copyFiltersAre(AdresseCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getRegion(), copy.getRegion()) &&
                condition.apply(criteria.getVille(), copy.getVille()) &&
                condition.apply(criteria.getCommune(), copy.getCommune()) &&
                condition.apply(criteria.getLatitude(), copy.getLatitude()) &&
                condition.apply(criteria.getLongitude(), copy.getLongitude()) &&
                condition.apply(criteria.getOltId(), copy.getOltId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
