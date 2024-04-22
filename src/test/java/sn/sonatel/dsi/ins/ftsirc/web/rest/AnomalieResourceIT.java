package sn.sonatel.dsi.ins.ftsirc.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.sonatel.dsi.ins.ftsirc.domain.AnomalieAsserts.*;
import static sn.sonatel.dsi.ins.ftsirc.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import sn.sonatel.dsi.ins.ftsirc.IntegrationTest;
import sn.sonatel.dsi.ins.ftsirc.domain.Anomalie;
import sn.sonatel.dsi.ins.ftsirc.domain.Diagnostic;
import sn.sonatel.dsi.ins.ftsirc.repository.AnomalieRepository;
import sn.sonatel.dsi.ins.ftsirc.service.dto.AnomalieDTO;
import sn.sonatel.dsi.ins.ftsirc.service.mapper.AnomalieMapper;

/**
 * Integration tests for the {@link AnomalieResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AnomalieResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/anomalies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AnomalieRepository anomalieRepository;

    @Autowired
    private AnomalieMapper anomalieMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAnomalieMockMvc;

    private Anomalie anomalie;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Anomalie createEntity(EntityManager em) {
        Anomalie anomalie = new Anomalie().libelle(DEFAULT_LIBELLE);
        return anomalie;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Anomalie createUpdatedEntity(EntityManager em) {
        Anomalie anomalie = new Anomalie().libelle(UPDATED_LIBELLE);
        return anomalie;
    }

    @BeforeEach
    public void initTest() {
        anomalie = createEntity(em);
    }

    @Test
    @Transactional
    void createAnomalie() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Anomalie
        AnomalieDTO anomalieDTO = anomalieMapper.toDto(anomalie);
        var returnedAnomalieDTO = om.readValue(
            restAnomalieMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(anomalieDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AnomalieDTO.class
        );

        // Validate the Anomalie in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAnomalie = anomalieMapper.toEntity(returnedAnomalieDTO);
        assertAnomalieUpdatableFieldsEquals(returnedAnomalie, getPersistedAnomalie(returnedAnomalie));
    }

    @Test
    @Transactional
    void createAnomalieWithExistingId() throws Exception {
        // Create the Anomalie with an existing ID
        anomalie.setId(1L);
        AnomalieDTO anomalieDTO = anomalieMapper.toDto(anomalie);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAnomalieMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(anomalieDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Anomalie in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLibelleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        anomalie.setLibelle(null);

        // Create the Anomalie, which fails.
        AnomalieDTO anomalieDTO = anomalieMapper.toDto(anomalie);

        restAnomalieMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(anomalieDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAnomalies() throws Exception {
        // Initialize the database
        anomalieRepository.saveAndFlush(anomalie);

        // Get all the anomalieList
        restAnomalieMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(anomalie.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)));
    }

    @Test
    @Transactional
    void getAnomalie() throws Exception {
        // Initialize the database
        anomalieRepository.saveAndFlush(anomalie);

        // Get the anomalie
        restAnomalieMockMvc
            .perform(get(ENTITY_API_URL_ID, anomalie.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(anomalie.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE));
    }

    @Test
    @Transactional
    void getAnomaliesByIdFiltering() throws Exception {
        // Initialize the database
        anomalieRepository.saveAndFlush(anomalie);

        Long id = anomalie.getId();

        defaultAnomalieFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAnomalieFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAnomalieFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAnomaliesByLibelleIsEqualToSomething() throws Exception {
        // Initialize the database
        anomalieRepository.saveAndFlush(anomalie);

        // Get all the anomalieList where libelle equals to
        defaultAnomalieFiltering("libelle.equals=" + DEFAULT_LIBELLE, "libelle.equals=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllAnomaliesByLibelleIsInShouldWork() throws Exception {
        // Initialize the database
        anomalieRepository.saveAndFlush(anomalie);

        // Get all the anomalieList where libelle in
        defaultAnomalieFiltering("libelle.in=" + DEFAULT_LIBELLE + "," + UPDATED_LIBELLE, "libelle.in=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllAnomaliesByLibelleIsNullOrNotNull() throws Exception {
        // Initialize the database
        anomalieRepository.saveAndFlush(anomalie);

        // Get all the anomalieList where libelle is not null
        defaultAnomalieFiltering("libelle.specified=true", "libelle.specified=false");
    }

    @Test
    @Transactional
    void getAllAnomaliesByLibelleContainsSomething() throws Exception {
        // Initialize the database
        anomalieRepository.saveAndFlush(anomalie);

        // Get all the anomalieList where libelle contains
        defaultAnomalieFiltering("libelle.contains=" + DEFAULT_LIBELLE, "libelle.contains=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllAnomaliesByLibelleNotContainsSomething() throws Exception {
        // Initialize the database
        anomalieRepository.saveAndFlush(anomalie);

        // Get all the anomalieList where libelle does not contain
        defaultAnomalieFiltering("libelle.doesNotContain=" + UPDATED_LIBELLE, "libelle.doesNotContain=" + DEFAULT_LIBELLE);
    }

    @Test
    @Transactional
    void getAllAnomaliesByDiagnosticIsEqualToSomething() throws Exception {
        Diagnostic diagnostic;
        if (TestUtil.findAll(em, Diagnostic.class).isEmpty()) {
            anomalieRepository.saveAndFlush(anomalie);
            diagnostic = DiagnosticResourceIT.createEntity(em);
        } else {
            diagnostic = TestUtil.findAll(em, Diagnostic.class).get(0);
        }
        em.persist(diagnostic);
        em.flush();
        anomalie.addDiagnostic(diagnostic);
        anomalieRepository.saveAndFlush(anomalie);
        Long diagnosticId = diagnostic.getId();
        // Get all the anomalieList where diagnostic equals to diagnosticId
        defaultAnomalieShouldBeFound("diagnosticId.equals=" + diagnosticId);

        // Get all the anomalieList where diagnostic equals to (diagnosticId + 1)
        defaultAnomalieShouldNotBeFound("diagnosticId.equals=" + (diagnosticId + 1));
    }

    private void defaultAnomalieFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAnomalieShouldBeFound(shouldBeFound);
        defaultAnomalieShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAnomalieShouldBeFound(String filter) throws Exception {
        restAnomalieMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(anomalie.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)));

        // Check, that the count call also returns 1
        restAnomalieMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAnomalieShouldNotBeFound(String filter) throws Exception {
        restAnomalieMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAnomalieMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAnomalie() throws Exception {
        // Get the anomalie
        restAnomalieMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAnomalie() throws Exception {
        // Initialize the database
        anomalieRepository.saveAndFlush(anomalie);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the anomalie
        Anomalie updatedAnomalie = anomalieRepository.findById(anomalie.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAnomalie are not directly saved in db
        em.detach(updatedAnomalie);
        updatedAnomalie.libelle(UPDATED_LIBELLE);
        AnomalieDTO anomalieDTO = anomalieMapper.toDto(updatedAnomalie);

        restAnomalieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, anomalieDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(anomalieDTO))
            )
            .andExpect(status().isOk());

        // Validate the Anomalie in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAnomalieToMatchAllProperties(updatedAnomalie);
    }

    @Test
    @Transactional
    void putNonExistingAnomalie() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        anomalie.setId(longCount.incrementAndGet());

        // Create the Anomalie
        AnomalieDTO anomalieDTO = anomalieMapper.toDto(anomalie);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnomalieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, anomalieDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(anomalieDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Anomalie in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAnomalie() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        anomalie.setId(longCount.incrementAndGet());

        // Create the Anomalie
        AnomalieDTO anomalieDTO = anomalieMapper.toDto(anomalie);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnomalieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(anomalieDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Anomalie in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAnomalie() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        anomalie.setId(longCount.incrementAndGet());

        // Create the Anomalie
        AnomalieDTO anomalieDTO = anomalieMapper.toDto(anomalie);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnomalieMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(anomalieDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Anomalie in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAnomalieWithPatch() throws Exception {
        // Initialize the database
        anomalieRepository.saveAndFlush(anomalie);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the anomalie using partial update
        Anomalie partialUpdatedAnomalie = new Anomalie();
        partialUpdatedAnomalie.setId(anomalie.getId());

        restAnomalieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAnomalie.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAnomalie))
            )
            .andExpect(status().isOk());

        // Validate the Anomalie in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAnomalieUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedAnomalie, anomalie), getPersistedAnomalie(anomalie));
    }

    @Test
    @Transactional
    void fullUpdateAnomalieWithPatch() throws Exception {
        // Initialize the database
        anomalieRepository.saveAndFlush(anomalie);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the anomalie using partial update
        Anomalie partialUpdatedAnomalie = new Anomalie();
        partialUpdatedAnomalie.setId(anomalie.getId());

        partialUpdatedAnomalie.libelle(UPDATED_LIBELLE);

        restAnomalieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAnomalie.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAnomalie))
            )
            .andExpect(status().isOk());

        // Validate the Anomalie in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAnomalieUpdatableFieldsEquals(partialUpdatedAnomalie, getPersistedAnomalie(partialUpdatedAnomalie));
    }

    @Test
    @Transactional
    void patchNonExistingAnomalie() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        anomalie.setId(longCount.incrementAndGet());

        // Create the Anomalie
        AnomalieDTO anomalieDTO = anomalieMapper.toDto(anomalie);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnomalieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, anomalieDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(anomalieDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Anomalie in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAnomalie() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        anomalie.setId(longCount.incrementAndGet());

        // Create the Anomalie
        AnomalieDTO anomalieDTO = anomalieMapper.toDto(anomalie);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnomalieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(anomalieDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Anomalie in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAnomalie() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        anomalie.setId(longCount.incrementAndGet());

        // Create the Anomalie
        AnomalieDTO anomalieDTO = anomalieMapper.toDto(anomalie);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnomalieMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(anomalieDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Anomalie in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAnomalie() throws Exception {
        // Initialize the database
        anomalieRepository.saveAndFlush(anomalie);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the anomalie
        restAnomalieMockMvc
            .perform(delete(ENTITY_API_URL_ID, anomalie.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return anomalieRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Anomalie getPersistedAnomalie(Anomalie anomalie) {
        return anomalieRepository.findById(anomalie.getId()).orElseThrow();
    }

    protected void assertPersistedAnomalieToMatchAllProperties(Anomalie expectedAnomalie) {
        assertAnomalieAllPropertiesEquals(expectedAnomalie, getPersistedAnomalie(expectedAnomalie));
    }

    protected void assertPersistedAnomalieToMatchUpdatableProperties(Anomalie expectedAnomalie) {
        assertAnomalieAllUpdatablePropertiesEquals(expectedAnomalie, getPersistedAnomalie(expectedAnomalie));
    }
}
