package sn.sonatel.dsi.ins.ftsirc.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class TypeDiagnosticCriteriaTest {

    @Test
    void newTypeDiagnosticCriteriaHasAllFiltersNullTest() {
        var typeDiagnosticCriteria = new TypeDiagnosticCriteria();
        assertThat(typeDiagnosticCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void typeDiagnosticCriteriaFluentMethodsCreatesFiltersTest() {
        var typeDiagnosticCriteria = new TypeDiagnosticCriteria();

        setAllFilters(typeDiagnosticCriteria);

        assertThat(typeDiagnosticCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void typeDiagnosticCriteriaCopyCreatesNullFilterTest() {
        var typeDiagnosticCriteria = new TypeDiagnosticCriteria();
        var copy = typeDiagnosticCriteria.copy();

        assertThat(typeDiagnosticCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(typeDiagnosticCriteria)
        );
    }

    @Test
    void typeDiagnosticCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var typeDiagnosticCriteria = new TypeDiagnosticCriteria();
        setAllFilters(typeDiagnosticCriteria);

        var copy = typeDiagnosticCriteria.copy();

        assertThat(typeDiagnosticCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(typeDiagnosticCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var typeDiagnosticCriteria = new TypeDiagnosticCriteria();

        assertThat(typeDiagnosticCriteria).hasToString("TypeDiagnosticCriteria{}");
    }

    private static void setAllFilters(TypeDiagnosticCriteria typeDiagnosticCriteria) {
        typeDiagnosticCriteria.id();
        typeDiagnosticCriteria.libelle();
        typeDiagnosticCriteria.diagnosticId();
        typeDiagnosticCriteria.distinct();
    }

    private static Condition<TypeDiagnosticCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getLibelle()) &&
                condition.apply(criteria.getDiagnosticId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<TypeDiagnosticCriteria> copyFiltersAre(
        TypeDiagnosticCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
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
