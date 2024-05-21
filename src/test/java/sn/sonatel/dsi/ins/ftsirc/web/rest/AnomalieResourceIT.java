package sn.sonatel.dsi.ins.ftsirc.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jakarta.persistence.EntityManager;
import java.util.List;
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

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_ETAT = "AAAAAAAAAA";
    private static final String UPDATED_ETAT = "BBBBBBBBBB";

    private static final String DEFAULT_RECOMMANDATION = "AAAAAAAAAA";
    private static final String UPDATED_RECOMMANDATION = "BBBBBBBBBB";

    private static final Integer DEFAULT_CODE = 1;
    private static final Integer UPDATED_CODE = 2;
    private static final Integer SMALLER_CODE = 1 - 1;

    private static final String ENTITY_API_URL = "/api/anomalies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

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
        Anomalie anomalie = new Anomalie()
            .libelle(DEFAULT_LIBELLE)
            .description(DEFAULT_DESCRIPTION)
            .etat(DEFAULT_ETAT)
            .recommandation(DEFAULT_RECOMMANDATION)
            .code(DEFAULT_CODE);
        return anomalie;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Anomalie createUpdatedEntity(EntityManager em) {
        Anomalie anomalie = new Anomalie()
            .libelle(UPDATED_LIBELLE)
            .description(UPDATED_DESCRIPTION)
            .etat(UPDATED_ETAT)
            .recommandation(UPDATED_RECOMMANDATION)
            .code(UPDATED_CODE);
        return anomalie;
    }

    @BeforeEach
    public void initTest() {
        anomalie = createEntity(em);
    }

    @Test
    @Transactional
    void createAnomalie() throws Exception {
        int databaseSizeBeforeCreate = anomalieRepository.findAll().size();
        // Create the Anomalie
        AnomalieDTO anomalieDTO = anomalieMapper.toDto(anomalie);
        restAnomalieMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(anomalieDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Anomalie in the database
        List<Anomalie> anomalieList = anomalieRepository.findAll();
        assertThat(anomalieList).hasSize(databaseSizeBeforeCreate + 1);
        Anomalie testAnomalie = anomalieList.get(anomalieList.size() - 1);
        assertThat(testAnomalie.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testAnomalie.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAnomalie.getEtat()).isEqualTo(DEFAULT_ETAT);
        assertThat(testAnomalie.getRecommandation()).isEqualTo(DEFAULT_RECOMMANDATION);
        assertThat(testAnomalie.getCode()).isEqualTo(DEFAULT_CODE);
    }

    @Test
    @Transactional
    void createAnomalieWithExistingId() throws Exception {
        // Create the Anomalie with an existing ID
        anomalie.setId(1L);
        AnomalieDTO anomalieDTO = anomalieMapper.toDto(anomalie);

        int databaseSizeBeforeCreate = anomalieRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAnomalieMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(anomalieDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Anomalie in the database
        List<Anomalie> anomalieList = anomalieRepository.findAll();
        assertThat(anomalieList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = anomalieRepository.findAll().size();
        // set the field null
        anomalie.setLibelle(null);

        // Create the Anomalie, which fails.
        AnomalieDTO anomalieDTO = anomalieMapper.toDto(anomalie);

        restAnomalieMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(anomalieDTO))
            )
            .andExpect(status().isBadRequest());

        List<Anomalie> anomalieList = anomalieRepository.findAll();
        assertThat(anomalieList).hasSize(databaseSizeBeforeTest);
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
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].etat").value(hasItem(DEFAULT_ETAT)))
            .andExpect(jsonPath("$.[*].recommandation").value(hasItem(DEFAULT_RECOMMANDATION.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));
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
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.etat").value(DEFAULT_ETAT))
            .andExpect(jsonPath("$.recommandation").value(DEFAULT_RECOMMANDATION.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE));
    }

    @Test
    @Transactional
    void getAnomaliesByIdFiltering() throws Exception {
        // Initialize the database
        anomalieRepository.saveAndFlush(anomalie);

        Long id = anomalie.getId();

        defaultAnomalieShouldBeFound("id.equals=" + id);
        defaultAnomalieShouldNotBeFound("id.notEquals=" + id);

        defaultAnomalieShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAnomalieShouldNotBeFound("id.greaterThan=" + id);

        defaultAnomalieShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAnomalieShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAnomaliesByLibelleIsEqualToSomething() throws Exception {
        // Initialize the database
        anomalieRepository.saveAndFlush(anomalie);

        // Get all the anomalieList where libelle equals to DEFAULT_LIBELLE
        defaultAnomalieShouldBeFound("libelle.equals=" + DEFAULT_LIBELLE);

        // Get all the anomalieList where libelle equals to UPDATED_LIBELLE
        defaultAnomalieShouldNotBeFound("libelle.equals=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllAnomaliesByLibelleIsInShouldWork() throws Exception {
        // Initialize the database
        anomalieRepository.saveAndFlush(anomalie);

        // Get all the anomalieList where libelle in DEFAULT_LIBELLE or UPDATED_LIBELLE
        defaultAnomalieShouldBeFound("libelle.in=" + DEFAULT_LIBELLE + "," + UPDATED_LIBELLE);

        // Get all the anomalieList where libelle equals to UPDATED_LIBELLE
        defaultAnomalieShouldNotBeFound("libelle.in=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllAnomaliesByLibelleIsNullOrNotNull() throws Exception {
        // Initialize the database
        anomalieRepository.saveAndFlush(anomalie);

        // Get all the anomalieList where libelle is not null
        defaultAnomalieShouldBeFound("libelle.specified=true");

        // Get all the anomalieList where libelle is null
        defaultAnomalieShouldNotBeFound("libelle.specified=false");
    }

    @Test
    @Transactional
    void getAllAnomaliesByLibelleContainsSomething() throws Exception {
        // Initialize the database
        anomalieRepository.saveAndFlush(anomalie);

        // Get all the anomalieList where libelle contains DEFAULT_LIBELLE
        defaultAnomalieShouldBeFound("libelle.contains=" + DEFAULT_LIBELLE);

        // Get all the anomalieList where libelle contains UPDATED_LIBELLE
        defaultAnomalieShouldNotBeFound("libelle.contains=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllAnomaliesByLibelleNotContainsSomething() throws Exception {
        // Initialize the database
        anomalieRepository.saveAndFlush(anomalie);

        // Get all the anomalieList where libelle does not contain DEFAULT_LIBELLE
        defaultAnomalieShouldNotBeFound("libelle.doesNotContain=" + DEFAULT_LIBELLE);

        // Get all the anomalieList where libelle does not contain UPDATED_LIBELLE
        defaultAnomalieShouldBeFound("libelle.doesNotContain=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllAnomaliesByEtatIsEqualToSomething() throws Exception {
        // Initialize the database
        anomalieRepository.saveAndFlush(anomalie);

        // Get all the anomalieList where etat equals to DEFAULT_ETAT
        defaultAnomalieShouldBeFound("etat.equals=" + DEFAULT_ETAT);

        // Get all the anomalieList where etat equals to UPDATED_ETAT
        defaultAnomalieShouldNotBeFound("etat.equals=" + UPDATED_ETAT);
    }

    @Test
    @Transactional
    void getAllAnomaliesByEtatIsInShouldWork() throws Exception {
        // Initialize the database
        anomalieRepository.saveAndFlush(anomalie);

        // Get all the anomalieList where etat in DEFAULT_ETAT or UPDATED_ETAT
        defaultAnomalieShouldBeFound("etat.in=" + DEFAULT_ETAT + "," + UPDATED_ETAT);

        // Get all the anomalieList where etat equals to UPDATED_ETAT
        defaultAnomalieShouldNotBeFound("etat.in=" + UPDATED_ETAT);
    }

    @Test
    @Transactional
    void getAllAnomaliesByEtatIsNullOrNotNull() throws Exception {
        // Initialize the database
        anomalieRepository.saveAndFlush(anomalie);

        // Get all the anomalieList where etat is not null
        defaultAnomalieShouldBeFound("etat.specified=true");

        // Get all the anomalieList where etat is null
        defaultAnomalieShouldNotBeFound("etat.specified=false");
    }

    @Test
    @Transactional
    void getAllAnomaliesByEtatContainsSomething() throws Exception {
        // Initialize the database
        anomalieRepository.saveAndFlush(anomalie);

        // Get all the anomalieList where etat contains DEFAULT_ETAT
        defaultAnomalieShouldBeFound("etat.contains=" + DEFAULT_ETAT);

        // Get all the anomalieList where etat contains UPDATED_ETAT
        defaultAnomalieShouldNotBeFound("etat.contains=" + UPDATED_ETAT);
    }

    @Test
    @Transactional
    void getAllAnomaliesByEtatNotContainsSomething() throws Exception {
        // Initialize the database
        anomalieRepository.saveAndFlush(anomalie);

        // Get all the anomalieList where etat does not contain DEFAULT_ETAT
        defaultAnomalieShouldNotBeFound("etat.doesNotContain=" + DEFAULT_ETAT);

        // Get all the anomalieList where etat does not contain UPDATED_ETAT
        defaultAnomalieShouldBeFound("etat.doesNotContain=" + UPDATED_ETAT);
    }

    @Test
    @Transactional
    void getAllAnomaliesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        anomalieRepository.saveAndFlush(anomalie);

        // Get all the anomalieList where code equals to DEFAULT_CODE
        defaultAnomalieShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the anomalieList where code equals to UPDATED_CODE
        defaultAnomalieShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllAnomaliesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        anomalieRepository.saveAndFlush(anomalie);

        // Get all the anomalieList where code in DEFAULT_CODE or UPDATED_CODE
        defaultAnomalieShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the anomalieList where code equals to UPDATED_CODE
        defaultAnomalieShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllAnomaliesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        anomalieRepository.saveAndFlush(anomalie);

        // Get all the anomalieList where code is not null
        defaultAnomalieShouldBeFound("code.specified=true");

        // Get all the anomalieList where code is null
        defaultAnomalieShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllAnomaliesByCodeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        anomalieRepository.saveAndFlush(anomalie);

        // Get all the anomalieList where code is greater than or equal to DEFAULT_CODE
        defaultAnomalieShouldBeFound("code.greaterThanOrEqual=" + DEFAULT_CODE);

        // Get all the anomalieList where code is greater than or equal to UPDATED_CODE
        defaultAnomalieShouldNotBeFound("code.greaterThanOrEqual=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllAnomaliesByCodeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        anomalieRepository.saveAndFlush(anomalie);

        // Get all the anomalieList where code is less than or equal to DEFAULT_CODE
        defaultAnomalieShouldBeFound("code.lessThanOrEqual=" + DEFAULT_CODE);

        // Get all the anomalieList where code is less than or equal to SMALLER_CODE
        defaultAnomalieShouldNotBeFound("code.lessThanOrEqual=" + SMALLER_CODE);
    }

    @Test
    @Transactional
    void getAllAnomaliesByCodeIsLessThanSomething() throws Exception {
        // Initialize the database
        anomalieRepository.saveAndFlush(anomalie);

        // Get all the anomalieList where code is less than DEFAULT_CODE
        defaultAnomalieShouldNotBeFound("code.lessThan=" + DEFAULT_CODE);

        // Get all the anomalieList where code is less than UPDATED_CODE
        defaultAnomalieShouldBeFound("code.lessThan=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllAnomaliesByCodeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        anomalieRepository.saveAndFlush(anomalie);

        // Get all the anomalieList where code is greater than DEFAULT_CODE
        defaultAnomalieShouldNotBeFound("code.greaterThan=" + DEFAULT_CODE);

        // Get all the anomalieList where code is greater than SMALLER_CODE
        defaultAnomalieShouldBeFound("code.greaterThan=" + SMALLER_CODE);
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

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAnomalieShouldBeFound(String filter) throws Exception {
        restAnomalieMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(anomalie.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].etat").value(hasItem(DEFAULT_ETAT)))
            .andExpect(jsonPath("$.[*].recommandation").value(hasItem(DEFAULT_RECOMMANDATION.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));

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

        int databaseSizeBeforeUpdate = anomalieRepository.findAll().size();

        // Update the anomalie
        Anomalie updatedAnomalie = anomalieRepository.findById(anomalie.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAnomalie are not directly saved in db
        em.detach(updatedAnomalie);
        updatedAnomalie
            .libelle(UPDATED_LIBELLE)
            .description(UPDATED_DESCRIPTION)
            .etat(UPDATED_ETAT)
            .recommandation(UPDATED_RECOMMANDATION)
            .code(UPDATED_CODE);
        AnomalieDTO anomalieDTO = anomalieMapper.toDto(updatedAnomalie);

        restAnomalieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, anomalieDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(anomalieDTO))
            )
            .andExpect(status().isOk());

        // Validate the Anomalie in the database
        List<Anomalie> anomalieList = anomalieRepository.findAll();
        assertThat(anomalieList).hasSize(databaseSizeBeforeUpdate);
        Anomalie testAnomalie = anomalieList.get(anomalieList.size() - 1);
        assertThat(testAnomalie.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testAnomalie.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAnomalie.getEtat()).isEqualTo(UPDATED_ETAT);
        assertThat(testAnomalie.getRecommandation()).isEqualTo(UPDATED_RECOMMANDATION);
        assertThat(testAnomalie.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    void putNonExistingAnomalie() throws Exception {
        int databaseSizeBeforeUpdate = anomalieRepository.findAll().size();
        anomalie.setId(longCount.incrementAndGet());

        // Create the Anomalie
        AnomalieDTO anomalieDTO = anomalieMapper.toDto(anomalie);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnomalieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, anomalieDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(anomalieDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Anomalie in the database
        List<Anomalie> anomalieList = anomalieRepository.findAll();
        assertThat(anomalieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAnomalie() throws Exception {
        int databaseSizeBeforeUpdate = anomalieRepository.findAll().size();
        anomalie.setId(longCount.incrementAndGet());

        // Create the Anomalie
        AnomalieDTO anomalieDTO = anomalieMapper.toDto(anomalie);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnomalieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(anomalieDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Anomalie in the database
        List<Anomalie> anomalieList = anomalieRepository.findAll();
        assertThat(anomalieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAnomalie() throws Exception {
        int databaseSizeBeforeUpdate = anomalieRepository.findAll().size();
        anomalie.setId(longCount.incrementAndGet());

        // Create the Anomalie
        AnomalieDTO anomalieDTO = anomalieMapper.toDto(anomalie);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnomalieMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(anomalieDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Anomalie in the database
        List<Anomalie> anomalieList = anomalieRepository.findAll();
        assertThat(anomalieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAnomalieWithPatch() throws Exception {
        // Initialize the database
        anomalieRepository.saveAndFlush(anomalie);

        int databaseSizeBeforeUpdate = anomalieRepository.findAll().size();

        // Update the anomalie using partial update
        Anomalie partialUpdatedAnomalie = new Anomalie();
        partialUpdatedAnomalie.setId(anomalie.getId());

        partialUpdatedAnomalie.etat(UPDATED_ETAT).recommandation(UPDATED_RECOMMANDATION).code(UPDATED_CODE);

        restAnomalieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAnomalie.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAnomalie))
            )
            .andExpect(status().isOk());

        // Validate the Anomalie in the database
        List<Anomalie> anomalieList = anomalieRepository.findAll();
        assertThat(anomalieList).hasSize(databaseSizeBeforeUpdate);
        Anomalie testAnomalie = anomalieList.get(anomalieList.size() - 1);
        assertThat(testAnomalie.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testAnomalie.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAnomalie.getEtat()).isEqualTo(UPDATED_ETAT);
        assertThat(testAnomalie.getRecommandation()).isEqualTo(UPDATED_RECOMMANDATION);
        assertThat(testAnomalie.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    void fullUpdateAnomalieWithPatch() throws Exception {
        // Initialize the database
        anomalieRepository.saveAndFlush(anomalie);

        int databaseSizeBeforeUpdate = anomalieRepository.findAll().size();

        // Update the anomalie using partial update
        Anomalie partialUpdatedAnomalie = new Anomalie();
        partialUpdatedAnomalie.setId(anomalie.getId());

        partialUpdatedAnomalie
            .libelle(UPDATED_LIBELLE)
            .description(UPDATED_DESCRIPTION)
            .etat(UPDATED_ETAT)
            .recommandation(UPDATED_RECOMMANDATION)
            .code(UPDATED_CODE);

        restAnomalieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAnomalie.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAnomalie))
            )
            .andExpect(status().isOk());

        // Validate the Anomalie in the database
        List<Anomalie> anomalieList = anomalieRepository.findAll();
        assertThat(anomalieList).hasSize(databaseSizeBeforeUpdate);
        Anomalie testAnomalie = anomalieList.get(anomalieList.size() - 1);
        assertThat(testAnomalie.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testAnomalie.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAnomalie.getEtat()).isEqualTo(UPDATED_ETAT);
        assertThat(testAnomalie.getRecommandation()).isEqualTo(UPDATED_RECOMMANDATION);
        assertThat(testAnomalie.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    void patchNonExistingAnomalie() throws Exception {
        int databaseSizeBeforeUpdate = anomalieRepository.findAll().size();
        anomalie.setId(longCount.incrementAndGet());

        // Create the Anomalie
        AnomalieDTO anomalieDTO = anomalieMapper.toDto(anomalie);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnomalieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, anomalieDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(anomalieDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Anomalie in the database
        List<Anomalie> anomalieList = anomalieRepository.findAll();
        assertThat(anomalieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAnomalie() throws Exception {
        int databaseSizeBeforeUpdate = anomalieRepository.findAll().size();
        anomalie.setId(longCount.incrementAndGet());

        // Create the Anomalie
        AnomalieDTO anomalieDTO = anomalieMapper.toDto(anomalie);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnomalieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(anomalieDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Anomalie in the database
        List<Anomalie> anomalieList = anomalieRepository.findAll();
        assertThat(anomalieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAnomalie() throws Exception {
        int databaseSizeBeforeUpdate = anomalieRepository.findAll().size();
        anomalie.setId(longCount.incrementAndGet());

        // Create the Anomalie
        AnomalieDTO anomalieDTO = anomalieMapper.toDto(anomalie);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnomalieMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(anomalieDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Anomalie in the database
        List<Anomalie> anomalieList = anomalieRepository.findAll();
        assertThat(anomalieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAnomalie() throws Exception {
        // Initialize the database
        anomalieRepository.saveAndFlush(anomalie);

        int databaseSizeBeforeDelete = anomalieRepository.findAll().size();

        // Delete the anomalie
        restAnomalieMockMvc
            .perform(delete(ENTITY_API_URL_ID, anomalie.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Anomalie> anomalieList = anomalieRepository.findAll();
        assertThat(anomalieList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
