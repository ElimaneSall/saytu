package sn.sonatel.dsi.ins.ftsirc.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.sonatel.dsi.ins.ftsirc.domain.MetriqueAsserts.*;
import static sn.sonatel.dsi.ins.ftsirc.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
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
    private ObjectMapper om;

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
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Metrique
        MetriqueDTO metriqueDTO = metriqueMapper.toDto(metrique);
        var returnedMetriqueDTO = om.readValue(
            restMetriqueMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(metriqueDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MetriqueDTO.class
        );

        // Validate the Metrique in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMetrique = metriqueMapper.toEntity(returnedMetriqueDTO);
        assertMetriqueUpdatableFieldsEquals(returnedMetrique, getPersistedMetrique(returnedMetrique));
    }

    @Test
    @Transactional
    void createMetriqueWithExistingId() throws Exception {
        // Create the Metrique with an existing ID
        metrique.setId(1L);
        MetriqueDTO metriqueDTO = metriqueMapper.toDto(metrique);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMetriqueMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(metriqueDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Metrique in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkOltPowerIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        metrique.setOltPower(null);

        // Create the Metrique, which fails.
        MetriqueDTO metriqueDTO = metriqueMapper.toDto(metrique);

        restMetriqueMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(metriqueDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOntPowerIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        metrique.setOntPower(null);

        // Create the Metrique, which fails.
        MetriqueDTO metriqueDTO = metriqueMapper.toDto(metrique);

        restMetriqueMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(metriqueDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
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

        defaultMetriqueFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultMetriqueFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultMetriqueFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMetriquesByOltPowerIsEqualToSomething() throws Exception {
        // Initialize the database
        metriqueRepository.saveAndFlush(metrique);

        // Get all the metriqueList where oltPower equals to
        defaultMetriqueFiltering("oltPower.equals=" + DEFAULT_OLT_POWER, "oltPower.equals=" + UPDATED_OLT_POWER);
    }

    @Test
    @Transactional
    void getAllMetriquesByOltPowerIsInShouldWork() throws Exception {
        // Initialize the database
        metriqueRepository.saveAndFlush(metrique);

        // Get all the metriqueList where oltPower in
        defaultMetriqueFiltering("oltPower.in=" + DEFAULT_OLT_POWER + "," + UPDATED_OLT_POWER, "oltPower.in=" + UPDATED_OLT_POWER);
    }

    @Test
    @Transactional
    void getAllMetriquesByOltPowerIsNullOrNotNull() throws Exception {
        // Initialize the database
        metriqueRepository.saveAndFlush(metrique);

        // Get all the metriqueList where oltPower is not null
        defaultMetriqueFiltering("oltPower.specified=true", "oltPower.specified=false");
    }

    @Test
    @Transactional
    void getAllMetriquesByOltPowerContainsSomething() throws Exception {
        // Initialize the database
        metriqueRepository.saveAndFlush(metrique);

        // Get all the metriqueList where oltPower contains
        defaultMetriqueFiltering("oltPower.contains=" + DEFAULT_OLT_POWER, "oltPower.contains=" + UPDATED_OLT_POWER);
    }

    @Test
    @Transactional
    void getAllMetriquesByOltPowerNotContainsSomething() throws Exception {
        // Initialize the database
        metriqueRepository.saveAndFlush(metrique);

        // Get all the metriqueList where oltPower does not contain
        defaultMetriqueFiltering("oltPower.doesNotContain=" + UPDATED_OLT_POWER, "oltPower.doesNotContain=" + DEFAULT_OLT_POWER);
    }

    @Test
    @Transactional
    void getAllMetriquesByOntPowerIsEqualToSomething() throws Exception {
        // Initialize the database
        metriqueRepository.saveAndFlush(metrique);

        // Get all the metriqueList where ontPower equals to
        defaultMetriqueFiltering("ontPower.equals=" + DEFAULT_ONT_POWER, "ontPower.equals=" + UPDATED_ONT_POWER);
    }

    @Test
    @Transactional
    void getAllMetriquesByOntPowerIsInShouldWork() throws Exception {
        // Initialize the database
        metriqueRepository.saveAndFlush(metrique);

        // Get all the metriqueList where ontPower in
        defaultMetriqueFiltering("ontPower.in=" + DEFAULT_ONT_POWER + "," + UPDATED_ONT_POWER, "ontPower.in=" + UPDATED_ONT_POWER);
    }

    @Test
    @Transactional
    void getAllMetriquesByOntPowerIsNullOrNotNull() throws Exception {
        // Initialize the database
        metriqueRepository.saveAndFlush(metrique);

        // Get all the metriqueList where ontPower is not null
        defaultMetriqueFiltering("ontPower.specified=true", "ontPower.specified=false");
    }

    @Test
    @Transactional
    void getAllMetriquesByOntPowerContainsSomething() throws Exception {
        // Initialize the database
        metriqueRepository.saveAndFlush(metrique);

        // Get all the metriqueList where ontPower contains
        defaultMetriqueFiltering("ontPower.contains=" + DEFAULT_ONT_POWER, "ontPower.contains=" + UPDATED_ONT_POWER);
    }

    @Test
    @Transactional
    void getAllMetriquesByOntPowerNotContainsSomething() throws Exception {
        // Initialize the database
        metriqueRepository.saveAndFlush(metrique);

        // Get all the metriqueList where ontPower does not contain
        defaultMetriqueFiltering("ontPower.doesNotContain=" + UPDATED_ONT_POWER, "ontPower.doesNotContain=" + DEFAULT_ONT_POWER);
    }

    @Test
    @Transactional
    void getAllMetriquesByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        metriqueRepository.saveAndFlush(metrique);

        // Get all the metriqueList where createdAt equals to
        defaultMetriqueFiltering("createdAt.equals=" + DEFAULT_CREATED_AT, "createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllMetriquesByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        metriqueRepository.saveAndFlush(metrique);

        // Get all the metriqueList where createdAt in
        defaultMetriqueFiltering("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT, "createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllMetriquesByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        metriqueRepository.saveAndFlush(metrique);

        // Get all the metriqueList where createdAt is not null
        defaultMetriqueFiltering("createdAt.specified=true", "createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllMetriquesByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        metriqueRepository.saveAndFlush(metrique);

        // Get all the metriqueList where createdAt is greater than or equal to
        defaultMetriqueFiltering(
            "createdAt.greaterThanOrEqual=" + DEFAULT_CREATED_AT,
            "createdAt.greaterThanOrEqual=" + UPDATED_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllMetriquesByCreatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        metriqueRepository.saveAndFlush(metrique);

        // Get all the metriqueList where createdAt is less than or equal to
        defaultMetriqueFiltering("createdAt.lessThanOrEqual=" + DEFAULT_CREATED_AT, "createdAt.lessThanOrEqual=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllMetriquesByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        metriqueRepository.saveAndFlush(metrique);

        // Get all the metriqueList where createdAt is less than
        defaultMetriqueFiltering("createdAt.lessThan=" + UPDATED_CREATED_AT, "createdAt.lessThan=" + DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllMetriquesByCreatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        metriqueRepository.saveAndFlush(metrique);

        // Get all the metriqueList where createdAt is greater than
        defaultMetriqueFiltering("createdAt.greaterThan=" + SMALLER_CREATED_AT, "createdAt.greaterThan=" + DEFAULT_CREATED_AT);
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

    private void defaultMetriqueFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultMetriqueShouldBeFound(shouldBeFound);
        defaultMetriqueShouldNotBeFound(shouldNotBeFound);
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

        long databaseSizeBeforeUpdate = getRepositoryCount();

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
                    .content(om.writeValueAsBytes(metriqueDTO))
            )
            .andExpect(status().isOk());

        // Validate the Metrique in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMetriqueToMatchAllProperties(updatedMetrique);
    }

    @Test
    @Transactional
    void putNonExistingMetrique() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        metrique.setId(longCount.incrementAndGet());

        // Create the Metrique
        MetriqueDTO metriqueDTO = metriqueMapper.toDto(metrique);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMetriqueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, metriqueDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(metriqueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Metrique in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMetrique() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        metrique.setId(longCount.incrementAndGet());

        // Create the Metrique
        MetriqueDTO metriqueDTO = metriqueMapper.toDto(metrique);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMetriqueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(metriqueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Metrique in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMetrique() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        metrique.setId(longCount.incrementAndGet());

        // Create the Metrique
        MetriqueDTO metriqueDTO = metriqueMapper.toDto(metrique);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMetriqueMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(metriqueDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Metrique in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMetriqueWithPatch() throws Exception {
        // Initialize the database
        metriqueRepository.saveAndFlush(metrique);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the metrique using partial update
        Metrique partialUpdatedMetrique = new Metrique();
        partialUpdatedMetrique.setId(metrique.getId());

        partialUpdatedMetrique.oltPower(UPDATED_OLT_POWER);

        restMetriqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMetrique.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMetrique))
            )
            .andExpect(status().isOk());

        // Validate the Metrique in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMetriqueUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedMetrique, metrique), getPersistedMetrique(metrique));
    }

    @Test
    @Transactional
    void fullUpdateMetriqueWithPatch() throws Exception {
        // Initialize the database
        metriqueRepository.saveAndFlush(metrique);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the metrique using partial update
        Metrique partialUpdatedMetrique = new Metrique();
        partialUpdatedMetrique.setId(metrique.getId());

        partialUpdatedMetrique.oltPower(UPDATED_OLT_POWER).ontPower(UPDATED_ONT_POWER).createdAt(UPDATED_CREATED_AT);

        restMetriqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMetrique.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMetrique))
            )
            .andExpect(status().isOk());

        // Validate the Metrique in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMetriqueUpdatableFieldsEquals(partialUpdatedMetrique, getPersistedMetrique(partialUpdatedMetrique));
    }

    @Test
    @Transactional
    void patchNonExistingMetrique() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        metrique.setId(longCount.incrementAndGet());

        // Create the Metrique
        MetriqueDTO metriqueDTO = metriqueMapper.toDto(metrique);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMetriqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, metriqueDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(metriqueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Metrique in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMetrique() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        metrique.setId(longCount.incrementAndGet());

        // Create the Metrique
        MetriqueDTO metriqueDTO = metriqueMapper.toDto(metrique);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMetriqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(metriqueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Metrique in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMetrique() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        metrique.setId(longCount.incrementAndGet());

        // Create the Metrique
        MetriqueDTO metriqueDTO = metriqueMapper.toDto(metrique);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMetriqueMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(metriqueDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Metrique in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMetrique() throws Exception {
        // Initialize the database
        metriqueRepository.saveAndFlush(metrique);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the metrique
        restMetriqueMockMvc
            .perform(delete(ENTITY_API_URL_ID, metrique.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return metriqueRepository.count();
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

    protected Metrique getPersistedMetrique(Metrique metrique) {
        return metriqueRepository.findById(metrique.getId()).orElseThrow();
    }

    protected void assertPersistedMetriqueToMatchAllProperties(Metrique expectedMetrique) {
        assertMetriqueAllPropertiesEquals(expectedMetrique, getPersistedMetrique(expectedMetrique));
    }

    protected void assertPersistedMetriqueToMatchUpdatableProperties(Metrique expectedMetrique) {
        assertMetriqueAllUpdatablePropertiesEquals(expectedMetrique, getPersistedMetrique(expectedMetrique));
    }
}
