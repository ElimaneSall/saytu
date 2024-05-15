package sn.sonatel.dsi.ins.ftsirc.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
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
import sn.sonatel.dsi.ins.ftsirc.domain.Metrique;
import sn.sonatel.dsi.ins.ftsirc.domain.ONT;
import sn.sonatel.dsi.ins.ftsirc.repository.MetriqueRepository;
import sn.sonatel.dsi.ins.ftsirc.service.dto.MetriqueDTO;
import sn.sonatel.dsi.ins.ftsirc.service.mapper.MetriqueMapper;

/**
 * Integration tests for the {@link MetriqueResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MetriqueResourceIT {

    private static final String DEFAULT_OLT_POWER = "AAAAAAAAAA";
    private static final String UPDATED_OLT_POWER = "BBBBBBBBBB";

    private static final String DEFAULT_ONT_POWER = "AAAAAAAAAA";
    private static final String UPDATED_ONT_POWER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_AT = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATED_AT = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/metriques";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MetriqueRepository metriqueRepository;

    @Autowired
    private MetriqueMapper metriqueMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMetriqueMockMvc;

    private Metrique metrique;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Metrique createEntity(EntityManager em) {
        Metrique metrique = new Metrique().oltPower(DEFAULT_OLT_POWER).ontPower(DEFAULT_ONT_POWER).createdAt(DEFAULT_CREATED_AT);
        return metrique;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Metrique createUpdatedEntity(EntityManager em) {
        Metrique metrique = new Metrique().oltPower(UPDATED_OLT_POWER).ontPower(UPDATED_ONT_POWER).createdAt(UPDATED_CREATED_AT);
        return metrique;
    }

    @BeforeEach
    public void initTest() {
        metrique = createEntity(em);
    }

    @Test
    @Transactional
    void createMetrique() throws Exception {
        int databaseSizeBeforeCreate = metriqueRepository.findAll().size();
        // Create the Metrique
        MetriqueDTO metriqueDTO = metriqueMapper.toDto(metrique);
        restMetriqueMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(metriqueDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Metrique in the database
        List<Metrique> metriqueList = metriqueRepository.findAll();
        assertThat(metriqueList).hasSize(databaseSizeBeforeCreate + 1);
        Metrique testMetrique = metriqueList.get(metriqueList.size() - 1);
        assertThat(testMetrique.getOltPower()).isEqualTo(DEFAULT_OLT_POWER);
        assertThat(testMetrique.getOntPower()).isEqualTo(DEFAULT_ONT_POWER);
        assertThat(testMetrique.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    void createMetriqueWithExistingId() throws Exception {
        // Create the Metrique with an existing ID
        metrique.setId(1L);
        MetriqueDTO metriqueDTO = metriqueMapper.toDto(metrique);

        int databaseSizeBeforeCreate = metriqueRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMetriqueMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(metriqueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Metrique in the database
        List<Metrique> metriqueList = metriqueRepository.findAll();
        assertThat(metriqueList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkOltPowerIsRequired() throws Exception {
        int databaseSizeBeforeTest = metriqueRepository.findAll().size();
        // set the field null
        metrique.setOltPower(null);

        // Create the Metrique, which fails.
        MetriqueDTO metriqueDTO = metriqueMapper.toDto(metrique);

        restMetriqueMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(metriqueDTO))
            )
            .andExpect(status().isBadRequest());

        List<Metrique> metriqueList = metriqueRepository.findAll();
        assertThat(metriqueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOntPowerIsRequired() throws Exception {
        int databaseSizeBeforeTest = metriqueRepository.findAll().size();
        // set the field null
        metrique.setOntPower(null);

        // Create the Metrique, which fails.
        MetriqueDTO metriqueDTO = metriqueMapper.toDto(metrique);

        restMetriqueMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(metriqueDTO))
            )
            .andExpect(status().isBadRequest());

        List<Metrique> metriqueList = metriqueRepository.findAll();
        assertThat(metriqueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMetriques() throws Exception {
        // Initialize the database
        metriqueRepository.saveAndFlush(metrique);

        // Get all the metriqueList
        restMetriqueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(metrique.getId().intValue())))
            .andExpect(jsonPath("$.[*].oltPower").value(hasItem(DEFAULT_OLT_POWER)))
            .andExpect(jsonPath("$.[*].ontPower").value(hasItem(DEFAULT_ONT_POWER)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())));
    }

    @Test
    @Transactional
    void getMetrique() throws Exception {
        // Initialize the database
        metriqueRepository.saveAndFlush(metrique);

        // Get the metrique
        restMetriqueMockMvc
            .perform(get(ENTITY_API_URL_ID, metrique.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(metrique.getId().intValue()))
            .andExpect(jsonPath("$.oltPower").value(DEFAULT_OLT_POWER))
            .andExpect(jsonPath("$.ontPower").value(DEFAULT_ONT_POWER))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()));
    }

    @Test
    @Transactional
    void getMetriquesByIdFiltering() throws Exception {
        // Initialize the database
        metriqueRepository.saveAndFlush(metrique);

        Long id = metrique.getId();

        defaultMetriqueShouldBeFound("id.equals=" + id);
        defaultMetriqueShouldNotBeFound("id.notEquals=" + id);

        defaultMetriqueShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMetriqueShouldNotBeFound("id.greaterThan=" + id);

        defaultMetriqueShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMetriqueShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMetriquesByOltPowerIsEqualToSomething() throws Exception {
        // Initialize the database
        metriqueRepository.saveAndFlush(metrique);

        // Get all the metriqueList where oltPower equals to DEFAULT_OLT_POWER
        defaultMetriqueShouldBeFound("oltPower.equals=" + DEFAULT_OLT_POWER);

        // Get all the metriqueList where oltPower equals to UPDATED_OLT_POWER
        defaultMetriqueShouldNotBeFound("oltPower.equals=" + UPDATED_OLT_POWER);
    }

    @Test
    @Transactional
    void getAllMetriquesByOltPowerIsInShouldWork() throws Exception {
        // Initialize the database
        metriqueRepository.saveAndFlush(metrique);

        // Get all the metriqueList where oltPower in DEFAULT_OLT_POWER or UPDATED_OLT_POWER
        defaultMetriqueShouldBeFound("oltPower.in=" + DEFAULT_OLT_POWER + "," + UPDATED_OLT_POWER);

        // Get all the metriqueList where oltPower equals to UPDATED_OLT_POWER
        defaultMetriqueShouldNotBeFound("oltPower.in=" + UPDATED_OLT_POWER);
    }

    @Test
    @Transactional
    void getAllMetriquesByOltPowerIsNullOrNotNull() throws Exception {
        // Initialize the database
        metriqueRepository.saveAndFlush(metrique);

        // Get all the metriqueList where oltPower is not null
        defaultMetriqueShouldBeFound("oltPower.specified=true");

        // Get all the metriqueList where oltPower is null
        defaultMetriqueShouldNotBeFound("oltPower.specified=false");
    }

    @Test
    @Transactional
    void getAllMetriquesByOltPowerContainsSomething() throws Exception {
        // Initialize the database
        metriqueRepository.saveAndFlush(metrique);

        // Get all the metriqueList where oltPower contains DEFAULT_OLT_POWER
        defaultMetriqueShouldBeFound("oltPower.contains=" + DEFAULT_OLT_POWER);

        // Get all the metriqueList where oltPower contains UPDATED_OLT_POWER
        defaultMetriqueShouldNotBeFound("oltPower.contains=" + UPDATED_OLT_POWER);
    }

    @Test
    @Transactional
    void getAllMetriquesByOltPowerNotContainsSomething() throws Exception {
        // Initialize the database
        metriqueRepository.saveAndFlush(metrique);

        // Get all the metriqueList where oltPower does not contain DEFAULT_OLT_POWER
        defaultMetriqueShouldNotBeFound("oltPower.doesNotContain=" + DEFAULT_OLT_POWER);

        // Get all the metriqueList where oltPower does not contain UPDATED_OLT_POWER
        defaultMetriqueShouldBeFound("oltPower.doesNotContain=" + UPDATED_OLT_POWER);
    }

    @Test
    @Transactional
    void getAllMetriquesByOntPowerIsEqualToSomething() throws Exception {
        // Initialize the database
        metriqueRepository.saveAndFlush(metrique);

        // Get all the metriqueList where ontPower equals to DEFAULT_ONT_POWER
        defaultMetriqueShouldBeFound("ontPower.equals=" + DEFAULT_ONT_POWER);

        // Get all the metriqueList where ontPower equals to UPDATED_ONT_POWER
        defaultMetriqueShouldNotBeFound("ontPower.equals=" + UPDATED_ONT_POWER);
    }

    @Test
    @Transactional
    void getAllMetriquesByOntPowerIsInShouldWork() throws Exception {
        // Initialize the database
        metriqueRepository.saveAndFlush(metrique);

        // Get all the metriqueList where ontPower in DEFAULT_ONT_POWER or UPDATED_ONT_POWER
        defaultMetriqueShouldBeFound("ontPower.in=" + DEFAULT_ONT_POWER + "," + UPDATED_ONT_POWER);

        // Get all the metriqueList where ontPower equals to UPDATED_ONT_POWER
        defaultMetriqueShouldNotBeFound("ontPower.in=" + UPDATED_ONT_POWER);
    }

    @Test
    @Transactional
    void getAllMetriquesByOntPowerIsNullOrNotNull() throws Exception {
        // Initialize the database
        metriqueRepository.saveAndFlush(metrique);

        // Get all the metriqueList where ontPower is not null
        defaultMetriqueShouldBeFound("ontPower.specified=true");

        // Get all the metriqueList where ontPower is null
        defaultMetriqueShouldNotBeFound("ontPower.specified=false");
    }

    @Test
    @Transactional
    void getAllMetriquesByOntPowerContainsSomething() throws Exception {
        // Initialize the database
        metriqueRepository.saveAndFlush(metrique);

        // Get all the metriqueList where ontPower contains DEFAULT_ONT_POWER
        defaultMetriqueShouldBeFound("ontPower.contains=" + DEFAULT_ONT_POWER);

        // Get all the metriqueList where ontPower contains UPDATED_ONT_POWER
        defaultMetriqueShouldNotBeFound("ontPower.contains=" + UPDATED_ONT_POWER);
    }

    @Test
    @Transactional
    void getAllMetriquesByOntPowerNotContainsSomething() throws Exception {
        // Initialize the database
        metriqueRepository.saveAndFlush(metrique);

        // Get all the metriqueList where ontPower does not contain DEFAULT_ONT_POWER
        defaultMetriqueShouldNotBeFound("ontPower.doesNotContain=" + DEFAULT_ONT_POWER);

        // Get all the metriqueList where ontPower does not contain UPDATED_ONT_POWER
        defaultMetriqueShouldBeFound("ontPower.doesNotContain=" + UPDATED_ONT_POWER);
    }

    @Test
    @Transactional
    void getAllMetriquesByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        metriqueRepository.saveAndFlush(metrique);

        // Get all the metriqueList where createdAt equals to DEFAULT_CREATED_AT
        defaultMetriqueShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the metriqueList where createdAt equals to UPDATED_CREATED_AT
        defaultMetriqueShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllMetriquesByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        metriqueRepository.saveAndFlush(metrique);

        // Get all the metriqueList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultMetriqueShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the metriqueList where createdAt equals to UPDATED_CREATED_AT
        defaultMetriqueShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllMetriquesByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        metriqueRepository.saveAndFlush(metrique);

        // Get all the metriqueList where createdAt is not null
        defaultMetriqueShouldBeFound("createdAt.specified=true");

        // Get all the metriqueList where createdAt is null
        defaultMetriqueShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllMetriquesByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        metriqueRepository.saveAndFlush(metrique);

        // Get all the metriqueList where createdAt is greater than or equal to DEFAULT_CREATED_AT
        defaultMetriqueShouldBeFound("createdAt.greaterThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the metriqueList where createdAt is greater than or equal to UPDATED_CREATED_AT
        defaultMetriqueShouldNotBeFound("createdAt.greaterThanOrEqual=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllMetriquesByCreatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        metriqueRepository.saveAndFlush(metrique);

        // Get all the metriqueList where createdAt is less than or equal to DEFAULT_CREATED_AT
        defaultMetriqueShouldBeFound("createdAt.lessThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the metriqueList where createdAt is less than or equal to SMALLER_CREATED_AT
        defaultMetriqueShouldNotBeFound("createdAt.lessThanOrEqual=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllMetriquesByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        metriqueRepository.saveAndFlush(metrique);

        // Get all the metriqueList where createdAt is less than DEFAULT_CREATED_AT
        defaultMetriqueShouldNotBeFound("createdAt.lessThan=" + DEFAULT_CREATED_AT);

        // Get all the metriqueList where createdAt is less than UPDATED_CREATED_AT
        defaultMetriqueShouldBeFound("createdAt.lessThan=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllMetriquesByCreatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        metriqueRepository.saveAndFlush(metrique);

        // Get all the metriqueList where createdAt is greater than DEFAULT_CREATED_AT
        defaultMetriqueShouldNotBeFound("createdAt.greaterThan=" + DEFAULT_CREATED_AT);

        // Get all the metriqueList where createdAt is greater than SMALLER_CREATED_AT
        defaultMetriqueShouldBeFound("createdAt.greaterThan=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllMetriquesByOntIsEqualToSomething() throws Exception {
        ONT ont;
        if (TestUtil.findAll(em, ONT.class).isEmpty()) {
            metriqueRepository.saveAndFlush(metrique);
            ont = ONTResourceIT.createEntity(em);
        } else {
            ont = TestUtil.findAll(em, ONT.class).get(0);
        }
        em.persist(ont);
        em.flush();
        metrique.setOnt(ont);
        metriqueRepository.saveAndFlush(metrique);
        Long ontId = ont.getId();
        // Get all the metriqueList where ont equals to ontId
        defaultMetriqueShouldBeFound("ontId.equals=" + ontId);

        // Get all the metriqueList where ont equals to (ontId + 1)
        defaultMetriqueShouldNotBeFound("ontId.equals=" + (ontId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMetriqueShouldBeFound(String filter) throws Exception {
        restMetriqueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(metrique.getId().intValue())))
            .andExpect(jsonPath("$.[*].oltPower").value(hasItem(DEFAULT_OLT_POWER)))
            .andExpect(jsonPath("$.[*].ontPower").value(hasItem(DEFAULT_ONT_POWER)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())));

        // Check, that the count call also returns 1
        restMetriqueMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMetriqueShouldNotBeFound(String filter) throws Exception {
        restMetriqueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMetriqueMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMetrique() throws Exception {
        // Get the metrique
        restMetriqueMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMetrique() throws Exception {
        // Initialize the database
        metriqueRepository.saveAndFlush(metrique);

        int databaseSizeBeforeUpdate = metriqueRepository.findAll().size();

        // Update the metrique
        Metrique updatedMetrique = metriqueRepository.findById(metrique.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMetrique are not directly saved in db
        em.detach(updatedMetrique);
        updatedMetrique.oltPower(UPDATED_OLT_POWER).ontPower(UPDATED_ONT_POWER).createdAt(UPDATED_CREATED_AT);
        MetriqueDTO metriqueDTO = metriqueMapper.toDto(updatedMetrique);

        restMetriqueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, metriqueDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(metriqueDTO))
            )
            .andExpect(status().isOk());

        // Validate the Metrique in the database
        List<Metrique> metriqueList = metriqueRepository.findAll();
        assertThat(metriqueList).hasSize(databaseSizeBeforeUpdate);
        Metrique testMetrique = metriqueList.get(metriqueList.size() - 1);
        assertThat(testMetrique.getOltPower()).isEqualTo(UPDATED_OLT_POWER);
        assertThat(testMetrique.getOntPower()).isEqualTo(UPDATED_ONT_POWER);
        assertThat(testMetrique.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingMetrique() throws Exception {
        int databaseSizeBeforeUpdate = metriqueRepository.findAll().size();
        metrique.setId(longCount.incrementAndGet());

        // Create the Metrique
        MetriqueDTO metriqueDTO = metriqueMapper.toDto(metrique);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMetriqueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, metriqueDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(metriqueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Metrique in the database
        List<Metrique> metriqueList = metriqueRepository.findAll();
        assertThat(metriqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMetrique() throws Exception {
        int databaseSizeBeforeUpdate = metriqueRepository.findAll().size();
        metrique.setId(longCount.incrementAndGet());

        // Create the Metrique
        MetriqueDTO metriqueDTO = metriqueMapper.toDto(metrique);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMetriqueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(metriqueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Metrique in the database
        List<Metrique> metriqueList = metriqueRepository.findAll();
        assertThat(metriqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMetrique() throws Exception {
        int databaseSizeBeforeUpdate = metriqueRepository.findAll().size();
        metrique.setId(longCount.incrementAndGet());

        // Create the Metrique
        MetriqueDTO metriqueDTO = metriqueMapper.toDto(metrique);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMetriqueMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(metriqueDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Metrique in the database
        List<Metrique> metriqueList = metriqueRepository.findAll();
        assertThat(metriqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMetriqueWithPatch() throws Exception {
        // Initialize the database
        metriqueRepository.saveAndFlush(metrique);

        int databaseSizeBeforeUpdate = metriqueRepository.findAll().size();

        // Update the metrique using partial update
        Metrique partialUpdatedMetrique = new Metrique();
        partialUpdatedMetrique.setId(metrique.getId());

        partialUpdatedMetrique.oltPower(UPDATED_OLT_POWER);

        restMetriqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMetrique.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMetrique))
            )
            .andExpect(status().isOk());

        // Validate the Metrique in the database
        List<Metrique> metriqueList = metriqueRepository.findAll();
        assertThat(metriqueList).hasSize(databaseSizeBeforeUpdate);
        Metrique testMetrique = metriqueList.get(metriqueList.size() - 1);
        assertThat(testMetrique.getOltPower()).isEqualTo(UPDATED_OLT_POWER);
        assertThat(testMetrique.getOntPower()).isEqualTo(DEFAULT_ONT_POWER);
        assertThat(testMetrique.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateMetriqueWithPatch() throws Exception {
        // Initialize the database
        metriqueRepository.saveAndFlush(metrique);

        int databaseSizeBeforeUpdate = metriqueRepository.findAll().size();

        // Update the metrique using partial update
        Metrique partialUpdatedMetrique = new Metrique();
        partialUpdatedMetrique.setId(metrique.getId());

        partialUpdatedMetrique.oltPower(UPDATED_OLT_POWER).ontPower(UPDATED_ONT_POWER).createdAt(UPDATED_CREATED_AT);

        restMetriqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMetrique.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMetrique))
            )
            .andExpect(status().isOk());

        // Validate the Metrique in the database
        List<Metrique> metriqueList = metriqueRepository.findAll();
        assertThat(metriqueList).hasSize(databaseSizeBeforeUpdate);
        Metrique testMetrique = metriqueList.get(metriqueList.size() - 1);
        assertThat(testMetrique.getOltPower()).isEqualTo(UPDATED_OLT_POWER);
        assertThat(testMetrique.getOntPower()).isEqualTo(UPDATED_ONT_POWER);
        assertThat(testMetrique.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingMetrique() throws Exception {
        int databaseSizeBeforeUpdate = metriqueRepository.findAll().size();
        metrique.setId(longCount.incrementAndGet());

        // Create the Metrique
        MetriqueDTO metriqueDTO = metriqueMapper.toDto(metrique);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMetriqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, metriqueDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(metriqueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Metrique in the database
        List<Metrique> metriqueList = metriqueRepository.findAll();
        assertThat(metriqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMetrique() throws Exception {
        int databaseSizeBeforeUpdate = metriqueRepository.findAll().size();
        metrique.setId(longCount.incrementAndGet());

        // Create the Metrique
        MetriqueDTO metriqueDTO = metriqueMapper.toDto(metrique);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMetriqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(metriqueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Metrique in the database
        List<Metrique> metriqueList = metriqueRepository.findAll();
        assertThat(metriqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMetrique() throws Exception {
        int databaseSizeBeforeUpdate = metriqueRepository.findAll().size();
        metrique.setId(longCount.incrementAndGet());

        // Create the Metrique
        MetriqueDTO metriqueDTO = metriqueMapper.toDto(metrique);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMetriqueMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(metriqueDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Metrique in the database
        List<Metrique> metriqueList = metriqueRepository.findAll();
        assertThat(metriqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMetrique() throws Exception {
        // Initialize the database
        metriqueRepository.saveAndFlush(metrique);

        int databaseSizeBeforeDelete = metriqueRepository.findAll().size();

        // Delete the metrique
        restMetriqueMockMvc
            .perform(delete(ENTITY_API_URL_ID, metrique.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Metrique> metriqueList = metriqueRepository.findAll();
        assertThat(metriqueList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
