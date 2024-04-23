package sn.sonatel.dsi.ins.ftsirc.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class OLTAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertOLTAllPropertiesEquals(OLT expected, OLT actual) {
        assertOLTAutoGeneratedPropertiesEquals(expected, actual);
        assertOLTAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertOLTAllUpdatablePropertiesEquals(OLT expected, OLT actual) {
        assertOLTUpdatableFieldsEquals(expected, actual);
        assertOLTUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertOLTAutoGeneratedPropertiesEquals(OLT expected, OLT actual) {
        assertThat(expected)
            .as("Verify OLT auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertOLTUpdatableFieldsEquals(OLT expected, OLT actual) {
        assertThat(expected)
            .as("Verify OLT relevant properties")
            .satisfies(e -> assertThat(e.getLibelle()).as("check libelle").isEqualTo(actual.getLibelle()))
            .satisfies(e -> assertThat(e.getIp()).as("check ip").isEqualTo(actual.getIp()))
            .satisfies(e -> assertThat(e.getVendeur()).as("check vendeur").isEqualTo(actual.getVendeur()))
            .satisfies(e -> assertThat(e.getTypeEquipment()).as("check typeEquipment").isEqualTo(actual.getTypeEquipment()))
            .satisfies(e -> assertThat(e.getCodeEquipment()).as("check codeEquipment").isEqualTo(actual.getCodeEquipment()))
            .satisfies(e -> assertThat(e.getAdresse()).as("check adresse").isEqualTo(actual.getAdresse()))
            .satisfies(e -> assertThat(e.getEmplacement()).as("check emplacement").isEqualTo(actual.getEmplacement()))
            .satisfies(e -> assertThat(e.getTypeCarte()).as("check typeCarte").isEqualTo(actual.getTypeCarte()))
            .satisfies(e -> assertThat(e.getLatitude()).as("check latitude").isEqualTo(actual.getLatitude()))
            .satisfies(e -> assertThat(e.getLongitude()).as("check longitude").isEqualTo(actual.getLongitude()))
            .satisfies(e -> assertThat(e.getCapacite()).as("check capacite").isEqualTo(actual.getCapacite()))
            .satisfies(e -> assertThat(e.getEtat()).as("check etat").isEqualTo(actual.getEtat()))
            .satisfies(e -> assertThat(e.getCreatedAt()).as("check createdAt").isEqualTo(actual.getCreatedAt()))
            .satisfies(e -> assertThat(e.getUpdatedAt()).as("check updatedAt").isEqualTo(actual.getUpdatedAt()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertOLTUpdatableRelationshipsEquals(OLT expected, OLT actual) {}
}
