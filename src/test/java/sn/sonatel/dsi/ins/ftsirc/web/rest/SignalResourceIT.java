package sn.sonatel.dsi.ins.ftsirc.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.sonatel.dsi.ins.ftsirc.domain.SignalAsserts.*;
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
import sn.sonatel.dsi.ins.ftsirc.domain.Signal;
import sn.sonatel.dsi.ins.ftsirc.repository.SignalRepository;
import sn.sonatel.dsi.ins.ftsirc.service.dto.SignalDTO;
import sn.sonatel.dsi.ins.ftsirc.service.mapper.SignalMapper;

/**
 * Integration tests for the {@link SignalResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SignalResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final Double DEFAULT_SEUIL_MIN = 1D;
    private static final Double UPDATED_SEUIL_MIN = 2D;
    private static final Double SMALLER_SEUIL_MIN = 1D - 1D;

    private static final Double DEFAULT_SEUIL_MAX = 1D;
    private static final Double UPDATED_SEUIL_MAX = 2D;
    private static final Double SMALLER_SEUIL_MAX = 1D - 1D;

    private static final String ENTITY_API_URL = "/api/signals";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SignalRepository signalRepository;

    @Autowired
    private SignalMapper signalMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSignalMockMvc;

    private Signal signal;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Signal createEntity(EntityManager em) {
        Signal signal = new Signal().libelle(DEFAULT_LIBELLE).seuilMin(DEFAULT_SEUIL_MIN).seuilMax(DEFAULT_SEUIL_MAX);
        return signal;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Signal createUpdatedEntity(EntityManager em) {
        Signal signal = new Signal().libelle(UPDATED_LIBELLE).seuilMin(UPDATED_SEUIL_MIN).seuilMax(UPDATED_SEUIL_MAX);
        return signal;
    }

    @BeforeEach
    public void initTest() {
        signal = createEntity(em);
    }

    @Test
    @Transactional
    void createSignal() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Signal
        SignalDTO signalDTO = signalMapper.toDto(signal);
        var returnedSignalDTO = om.readValue(
            restSignalMockMvc
                .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(signalDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SignalDTO.class
        );

        // Validate the Signal in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSignal = signalMapper.toEntity(returnedSignalDTO);
        assertSignalUpdatableFieldsEquals(returnedSignal, getPersistedSignal(returnedSignal));
    }

    @Test
    @Transactional
    void createSignalWithExistingId() throws Exception {
        // Create the Signal with an existing ID
        signal.setId(1L);
        SignalDTO signalDTO = signalMapper.toDto(signal);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSignalMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(signalDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Signal in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLibelleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        signal.setLibelle(null);

        // Create the Signal, which fails.
        SignalDTO signalDTO = signalMapper.toDto(signal);

        restSignalMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(signalDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSeuilMinIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        signal.setSeuilMin(null);

        // Create the Signal, which fails.
        SignalDTO signalDTO = signalMapper.toDto(signal);

        restSignalMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(signalDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSeuilMaxIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        signal.setSeuilMax(null);

        // Create the Signal, which fails.
        SignalDTO signalDTO = signalMapper.toDto(signal);

        restSignalMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(signalDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSignals() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList
        restSignalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(signal.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].seuilMin").value(hasItem(DEFAULT_SEUIL_MIN.doubleValue())))
            .andExpect(jsonPath("$.[*].seuilMax").value(hasItem(DEFAULT_SEUIL_MAX.doubleValue())));
    }

    @Test
    @Transactional
    void getSignal() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get the signal
        restSignalMockMvc
            .perform(get(ENTITY_API_URL_ID, signal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(signal.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.seuilMin").value(DEFAULT_SEUIL_MIN.doubleValue()))
            .andExpect(jsonPath("$.seuilMax").value(DEFAULT_SEUIL_MAX.doubleValue()));
    }

    @Test
    @Transactional
    void getSignalsByIdFiltering() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        Long id = signal.getId();

        defaultSignalFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultSignalFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultSignalFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSignalsByLibelleIsEqualToSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where libelle equals to
        defaultSignalFiltering("libelle.equals=" + DEFAULT_LIBELLE, "libelle.equals=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllSignalsByLibelleIsInShouldWork() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where libelle in
        defaultSignalFiltering("libelle.in=" + DEFAULT_LIBELLE + "," + UPDATED_LIBELLE, "libelle.in=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllSignalsByLibelleIsNullOrNotNull() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where libelle is not null
        defaultSignalFiltering("libelle.specified=true", "libelle.specified=false");
    }

    @Test
    @Transactional
    void getAllSignalsByLibelleContainsSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where libelle contains
        defaultSignalFiltering("libelle.contains=" + DEFAULT_LIBELLE, "libelle.contains=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllSignalsByLibelleNotContainsSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where libelle does not contain
        defaultSignalFiltering("libelle.doesNotContain=" + UPDATED_LIBELLE, "libelle.doesNotContain=" + DEFAULT_LIBELLE);
    }

    @Test
    @Transactional
    void getAllSignalsBySeuilMinIsEqualToSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where seuilMin equals to
        defaultSignalFiltering("seuilMin.equals=" + DEFAULT_SEUIL_MIN, "seuilMin.equals=" + UPDATED_SEUIL_MIN);
    }

    @Test
    @Transactional
    void getAllSignalsBySeuilMinIsInShouldWork() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where seuilMin in
        defaultSignalFiltering("seuilMin.in=" + DEFAULT_SEUIL_MIN + "," + UPDATED_SEUIL_MIN, "seuilMin.in=" + UPDATED_SEUIL_MIN);
    }

    @Test
    @Transactional
    void getAllSignalsBySeuilMinIsNullOrNotNull() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where seuilMin is not null
        defaultSignalFiltering("seuilMin.specified=true", "seuilMin.specified=false");
    }

    @Test
    @Transactional
    void getAllSignalsBySeuilMinIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where seuilMin is greater than or equal to
        defaultSignalFiltering("seuilMin.greaterThanOrEqual=" + DEFAULT_SEUIL_MIN, "seuilMin.greaterThanOrEqual=" + UPDATED_SEUIL_MIN);
    }

    @Test
    @Transactional
    void getAllSignalsBySeuilMinIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where seuilMin is less than or equal to
        defaultSignalFiltering("seuilMin.lessThanOrEqual=" + DEFAULT_SEUIL_MIN, "seuilMin.lessThanOrEqual=" + SMALLER_SEUIL_MIN);
    }

    @Test
    @Transactional
    void getAllSignalsBySeuilMinIsLessThanSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where seuilMin is less than
        defaultSignalFiltering("seuilMin.lessThan=" + UPDATED_SEUIL_MIN, "seuilMin.lessThan=" + DEFAULT_SEUIL_MIN);
    }

    @Test
    @Transactional
    void getAllSignalsBySeuilMinIsGreaterThanSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where seuilMin is greater than
        defaultSignalFiltering("seuilMin.greaterThan=" + SMALLER_SEUIL_MIN, "seuilMin.greaterThan=" + DEFAULT_SEUIL_MIN);
    }

    @Test
    @Transactional
    void getAllSignalsBySeuilMaxIsEqualToSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where seuilMax equals to
        defaultSignalFiltering("seuilMax.equals=" + DEFAULT_SEUIL_MAX, "seuilMax.equals=" + UPDATED_SEUIL_MAX);
    }

    @Test
    @Transactional
    void getAllSignalsBySeuilMaxIsInShouldWork() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where seuilMax in
        defaultSignalFiltering("seuilMax.in=" + DEFAULT_SEUIL_MAX + "," + UPDATED_SEUIL_MAX, "seuilMax.in=" + UPDATED_SEUIL_MAX);
    }

    @Test
    @Transactional
    void getAllSignalsBySeuilMaxIsNullOrNotNull() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where seuilMax is not null
        defaultSignalFiltering("seuilMax.specified=true", "seuilMax.specified=false");
    }

    @Test
    @Transactional
    void getAllSignalsBySeuilMaxIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where seuilMax is greater than or equal to
        defaultSignalFiltering("seuilMax.greaterThanOrEqual=" + DEFAULT_SEUIL_MAX, "seuilMax.greaterThanOrEqual=" + UPDATED_SEUIL_MAX);
    }

    @Test
    @Transactional
    void getAllSignalsBySeuilMaxIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where seuilMax is less than or equal to
        defaultSignalFiltering("seuilMax.lessThanOrEqual=" + DEFAULT_SEUIL_MAX, "seuilMax.lessThanOrEqual=" + SMALLER_SEUIL_MAX);
    }

    @Test
    @Transactional
    void getAllSignalsBySeuilMaxIsLessThanSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where seuilMax is less than
        defaultSignalFiltering("seuilMax.lessThan=" + UPDATED_SEUIL_MAX, "seuilMax.lessThan=" + DEFAULT_SEUIL_MAX);
    }

    @Test
    @Transactional
    void getAllSignalsBySeuilMaxIsGreaterThanSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where seuilMax is greater than
        defaultSignalFiltering("seuilMax.greaterThan=" + SMALLER_SEUIL_MAX, "seuilMax.greaterThan=" + DEFAULT_SEUIL_MAX);
    }

    private void defaultSignalFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultSignalShouldBeFound(shouldBeFound);
        defaultSignalShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSignalShouldBeFound(String filter) throws Exception {
        restSignalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(signal.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].seuilMin").value(hasItem(DEFAULT_SEUIL_MIN.doubleValue())))
            .andExpect(jsonPath("$.[*].seuilMax").value(hasItem(DEFAULT_SEUIL_MAX.doubleValue())));

        // Check, that the count call also returns 1
        restSignalMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSignalShouldNotBeFound(String filter) throws Exception {
        restSignalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSignalMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSignal() throws Exception {
        // Get the signal
        restSignalMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSignal() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the signal
        Signal updatedSignal = signalRepository.findById(signal.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSignal are not directly saved in db
        em.detach(updatedSignal);
        updatedSignal.libelle(UPDATED_LIBELLE).seuilMin(UPDATED_SEUIL_MIN).seuilMax(UPDATED_SEUIL_MAX);
        SignalDTO signalDTO = signalMapper.toDto(updatedSignal);

        restSignalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, signalDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(signalDTO))
            )
            .andExpect(status().isOk());

        // Validate the Signal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSignalToMatchAllProperties(updatedSignal);
    }

    @Test
    @Transactional
    void putNonExistingSignal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        signal.setId(longCount.incrementAndGet());

        // Create the Signal
        SignalDTO signalDTO = signalMapper.toDto(signal);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSignalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, signalDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(signalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Signal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSignal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        signal.setId(longCount.incrementAndGet());

        // Create the Signal
        SignalDTO signalDTO = signalMapper.toDto(signal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSignalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(signalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Signal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSignal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        signal.setId(longCount.incrementAndGet());

        // Create the Signal
        SignalDTO signalDTO = signalMapper.toDto(signal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSignalMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(signalDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Signal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSignalWithPatch() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the signal using partial update
        Signal partialUpdatedSignal = new Signal();
        partialUpdatedSignal.setId(signal.getId());

        partialUpdatedSignal.libelle(UPDATED_LIBELLE);

        restSignalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSignal.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSignal))
            )
            .andExpect(status().isOk());

        // Validate the Signal in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSignalUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedSignal, signal), getPersistedSignal(signal));
    }

    @Test
    @Transactional
    void fullUpdateSignalWithPatch() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the signal using partial update
        Signal partialUpdatedSignal = new Signal();
        partialUpdatedSignal.setId(signal.getId());

        partialUpdatedSignal.libelle(UPDATED_LIBELLE).seuilMin(UPDATED_SEUIL_MIN).seuilMax(UPDATED_SEUIL_MAX);

        restSignalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSignal.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSignal))
            )
            .andExpect(status().isOk());

        // Validate the Signal in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSignalUpdatableFieldsEquals(partialUpdatedSignal, getPersistedSignal(partialUpdatedSignal));
    }

    @Test
    @Transactional
    void patchNonExistingSignal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        signal.setId(longCount.incrementAndGet());

        // Create the Signal
        SignalDTO signalDTO = signalMapper.toDto(signal);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSignalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, signalDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(signalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Signal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSignal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        signal.setId(longCount.incrementAndGet());

        // Create the Signal
        SignalDTO signalDTO = signalMapper.toDto(signal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSignalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(signalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Signal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSignal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        signal.setId(longCount.incrementAndGet());

        // Create the Signal
        SignalDTO signalDTO = signalMapper.toDto(signal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSignalMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(signalDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Signal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSignal() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the signal
        restSignalMockMvc
            .perform(delete(ENTITY_API_URL_ID, signal.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return signalRepository.count();
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

    protected Signal getPersistedSignal(Signal signal) {
        return signalRepository.findById(signal.getId()).orElseThrow();
    }

    protected void assertPersistedSignalToMatchAllProperties(Signal expectedSignal) {
        assertSignalAllPropertiesEquals(expectedSignal, getPersistedSignal(expectedSignal));
    }

    protected void assertPersistedSignalToMatchUpdatableProperties(Signal expectedSignal) {
        assertSignalAllUpdatablePropertiesEquals(expectedSignal, getPersistedSignal(expectedSignal));
    }
}
