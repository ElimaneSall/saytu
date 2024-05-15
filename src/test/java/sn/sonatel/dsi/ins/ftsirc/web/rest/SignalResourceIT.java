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
import sn.sonatel.dsi.ins.ftsirc.domain.Diagnostic;
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

    private static final Double DEFAULT_VALUE_SIGNAL = 1D;
    private static final Double UPDATED_VALUE_SIGNAL = 2D;
    private static final Double SMALLER_VALUE_SIGNAL = 1D - 1D;

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
        Signal signal = new Signal()
            .libelle(DEFAULT_LIBELLE)
            .valueSignal(DEFAULT_VALUE_SIGNAL)
            .seuilMin(DEFAULT_SEUIL_MIN)
            .seuilMax(DEFAULT_SEUIL_MAX);
        return signal;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Signal createUpdatedEntity(EntityManager em) {
        Signal signal = new Signal()
            .libelle(UPDATED_LIBELLE)
            .valueSignal(UPDATED_VALUE_SIGNAL)
            .seuilMin(UPDATED_SEUIL_MIN)
            .seuilMax(UPDATED_SEUIL_MAX);
        return signal;
    }

    @BeforeEach
    public void initTest() {
        signal = createEntity(em);
    }

    @Test
    @Transactional
    void createSignal() throws Exception {
        int databaseSizeBeforeCreate = signalRepository.findAll().size();
        // Create the Signal
        SignalDTO signalDTO = signalMapper.toDto(signal);
        restSignalMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(signalDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Signal in the database
        List<Signal> signalList = signalRepository.findAll();
        assertThat(signalList).hasSize(databaseSizeBeforeCreate + 1);
        Signal testSignal = signalList.get(signalList.size() - 1);
        assertThat(testSignal.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testSignal.getValueSignal()).isEqualTo(DEFAULT_VALUE_SIGNAL);
        assertThat(testSignal.getSeuilMin()).isEqualTo(DEFAULT_SEUIL_MIN);
        assertThat(testSignal.getSeuilMax()).isEqualTo(DEFAULT_SEUIL_MAX);
    }

    @Test
    @Transactional
    void createSignalWithExistingId() throws Exception {
        // Create the Signal with an existing ID
        signal.setId(1L);
        SignalDTO signalDTO = signalMapper.toDto(signal);

        int databaseSizeBeforeCreate = signalRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSignalMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(signalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Signal in the database
        List<Signal> signalList = signalRepository.findAll();
        assertThat(signalList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = signalRepository.findAll().size();
        // set the field null
        signal.setLibelle(null);

        // Create the Signal, which fails.
        SignalDTO signalDTO = signalMapper.toDto(signal);

        restSignalMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(signalDTO))
            )
            .andExpect(status().isBadRequest());

        List<Signal> signalList = signalRepository.findAll();
        assertThat(signalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSeuilMinIsRequired() throws Exception {
        int databaseSizeBeforeTest = signalRepository.findAll().size();
        // set the field null
        signal.setSeuilMin(null);

        // Create the Signal, which fails.
        SignalDTO signalDTO = signalMapper.toDto(signal);

        restSignalMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(signalDTO))
            )
            .andExpect(status().isBadRequest());

        List<Signal> signalList = signalRepository.findAll();
        assertThat(signalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSeuilMaxIsRequired() throws Exception {
        int databaseSizeBeforeTest = signalRepository.findAll().size();
        // set the field null
        signal.setSeuilMax(null);

        // Create the Signal, which fails.
        SignalDTO signalDTO = signalMapper.toDto(signal);

        restSignalMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(signalDTO))
            )
            .andExpect(status().isBadRequest());

        List<Signal> signalList = signalRepository.findAll();
        assertThat(signalList).hasSize(databaseSizeBeforeTest);
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
            .andExpect(jsonPath("$.[*].valueSignal").value(hasItem(DEFAULT_VALUE_SIGNAL.doubleValue())))
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
            .andExpect(jsonPath("$.valueSignal").value(DEFAULT_VALUE_SIGNAL.doubleValue()))
            .andExpect(jsonPath("$.seuilMin").value(DEFAULT_SEUIL_MIN.doubleValue()))
            .andExpect(jsonPath("$.seuilMax").value(DEFAULT_SEUIL_MAX.doubleValue()));
    }

    @Test
    @Transactional
    void getSignalsByIdFiltering() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        Long id = signal.getId();

        defaultSignalShouldBeFound("id.equals=" + id);
        defaultSignalShouldNotBeFound("id.notEquals=" + id);

        defaultSignalShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSignalShouldNotBeFound("id.greaterThan=" + id);

        defaultSignalShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSignalShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSignalsByLibelleIsEqualToSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where libelle equals to DEFAULT_LIBELLE
        defaultSignalShouldBeFound("libelle.equals=" + DEFAULT_LIBELLE);

        // Get all the signalList where libelle equals to UPDATED_LIBELLE
        defaultSignalShouldNotBeFound("libelle.equals=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllSignalsByLibelleIsInShouldWork() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where libelle in DEFAULT_LIBELLE or UPDATED_LIBELLE
        defaultSignalShouldBeFound("libelle.in=" + DEFAULT_LIBELLE + "," + UPDATED_LIBELLE);

        // Get all the signalList where libelle equals to UPDATED_LIBELLE
        defaultSignalShouldNotBeFound("libelle.in=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllSignalsByLibelleIsNullOrNotNull() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where libelle is not null
        defaultSignalShouldBeFound("libelle.specified=true");

        // Get all the signalList where libelle is null
        defaultSignalShouldNotBeFound("libelle.specified=false");
    }

    @Test
    @Transactional
    void getAllSignalsByLibelleContainsSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where libelle contains DEFAULT_LIBELLE
        defaultSignalShouldBeFound("libelle.contains=" + DEFAULT_LIBELLE);

        // Get all the signalList where libelle contains UPDATED_LIBELLE
        defaultSignalShouldNotBeFound("libelle.contains=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllSignalsByLibelleNotContainsSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where libelle does not contain DEFAULT_LIBELLE
        defaultSignalShouldNotBeFound("libelle.doesNotContain=" + DEFAULT_LIBELLE);

        // Get all the signalList where libelle does not contain UPDATED_LIBELLE
        defaultSignalShouldBeFound("libelle.doesNotContain=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllSignalsByValueSignalIsEqualToSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where valueSignal equals to DEFAULT_VALUE_SIGNAL
        defaultSignalShouldBeFound("valueSignal.equals=" + DEFAULT_VALUE_SIGNAL);

        // Get all the signalList where valueSignal equals to UPDATED_VALUE_SIGNAL
        defaultSignalShouldNotBeFound("valueSignal.equals=" + UPDATED_VALUE_SIGNAL);
    }

    @Test
    @Transactional
    void getAllSignalsByValueSignalIsInShouldWork() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where valueSignal in DEFAULT_VALUE_SIGNAL or UPDATED_VALUE_SIGNAL
        defaultSignalShouldBeFound("valueSignal.in=" + DEFAULT_VALUE_SIGNAL + "," + UPDATED_VALUE_SIGNAL);

        // Get all the signalList where valueSignal equals to UPDATED_VALUE_SIGNAL
        defaultSignalShouldNotBeFound("valueSignal.in=" + UPDATED_VALUE_SIGNAL);
    }

    @Test
    @Transactional
    void getAllSignalsByValueSignalIsNullOrNotNull() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where valueSignal is not null
        defaultSignalShouldBeFound("valueSignal.specified=true");

        // Get all the signalList where valueSignal is null
        defaultSignalShouldNotBeFound("valueSignal.specified=false");
    }

    @Test
    @Transactional
    void getAllSignalsByValueSignalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where valueSignal is greater than or equal to DEFAULT_VALUE_SIGNAL
        defaultSignalShouldBeFound("valueSignal.greaterThanOrEqual=" + DEFAULT_VALUE_SIGNAL);

        // Get all the signalList where valueSignal is greater than or equal to UPDATED_VALUE_SIGNAL
        defaultSignalShouldNotBeFound("valueSignal.greaterThanOrEqual=" + UPDATED_VALUE_SIGNAL);
    }

    @Test
    @Transactional
    void getAllSignalsByValueSignalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where valueSignal is less than or equal to DEFAULT_VALUE_SIGNAL
        defaultSignalShouldBeFound("valueSignal.lessThanOrEqual=" + DEFAULT_VALUE_SIGNAL);

        // Get all the signalList where valueSignal is less than or equal to SMALLER_VALUE_SIGNAL
        defaultSignalShouldNotBeFound("valueSignal.lessThanOrEqual=" + SMALLER_VALUE_SIGNAL);
    }

    @Test
    @Transactional
    void getAllSignalsByValueSignalIsLessThanSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where valueSignal is less than DEFAULT_VALUE_SIGNAL
        defaultSignalShouldNotBeFound("valueSignal.lessThan=" + DEFAULT_VALUE_SIGNAL);

        // Get all the signalList where valueSignal is less than UPDATED_VALUE_SIGNAL
        defaultSignalShouldBeFound("valueSignal.lessThan=" + UPDATED_VALUE_SIGNAL);
    }

    @Test
    @Transactional
    void getAllSignalsByValueSignalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where valueSignal is greater than DEFAULT_VALUE_SIGNAL
        defaultSignalShouldNotBeFound("valueSignal.greaterThan=" + DEFAULT_VALUE_SIGNAL);

        // Get all the signalList where valueSignal is greater than SMALLER_VALUE_SIGNAL
        defaultSignalShouldBeFound("valueSignal.greaterThan=" + SMALLER_VALUE_SIGNAL);
    }

    @Test
    @Transactional
    void getAllSignalsBySeuilMinIsEqualToSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where seuilMin equals to DEFAULT_SEUIL_MIN
        defaultSignalShouldBeFound("seuilMin.equals=" + DEFAULT_SEUIL_MIN);

        // Get all the signalList where seuilMin equals to UPDATED_SEUIL_MIN
        defaultSignalShouldNotBeFound("seuilMin.equals=" + UPDATED_SEUIL_MIN);
    }

    @Test
    @Transactional
    void getAllSignalsBySeuilMinIsInShouldWork() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where seuilMin in DEFAULT_SEUIL_MIN or UPDATED_SEUIL_MIN
        defaultSignalShouldBeFound("seuilMin.in=" + DEFAULT_SEUIL_MIN + "," + UPDATED_SEUIL_MIN);

        // Get all the signalList where seuilMin equals to UPDATED_SEUIL_MIN
        defaultSignalShouldNotBeFound("seuilMin.in=" + UPDATED_SEUIL_MIN);
    }

    @Test
    @Transactional
    void getAllSignalsBySeuilMinIsNullOrNotNull() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where seuilMin is not null
        defaultSignalShouldBeFound("seuilMin.specified=true");

        // Get all the signalList where seuilMin is null
        defaultSignalShouldNotBeFound("seuilMin.specified=false");
    }

    @Test
    @Transactional
    void getAllSignalsBySeuilMinIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where seuilMin is greater than or equal to DEFAULT_SEUIL_MIN
        defaultSignalShouldBeFound("seuilMin.greaterThanOrEqual=" + DEFAULT_SEUIL_MIN);

        // Get all the signalList where seuilMin is greater than or equal to UPDATED_SEUIL_MIN
        defaultSignalShouldNotBeFound("seuilMin.greaterThanOrEqual=" + UPDATED_SEUIL_MIN);
    }

    @Test
    @Transactional
    void getAllSignalsBySeuilMinIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where seuilMin is less than or equal to DEFAULT_SEUIL_MIN
        defaultSignalShouldBeFound("seuilMin.lessThanOrEqual=" + DEFAULT_SEUIL_MIN);

        // Get all the signalList where seuilMin is less than or equal to SMALLER_SEUIL_MIN
        defaultSignalShouldNotBeFound("seuilMin.lessThanOrEqual=" + SMALLER_SEUIL_MIN);
    }

    @Test
    @Transactional
    void getAllSignalsBySeuilMinIsLessThanSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where seuilMin is less than DEFAULT_SEUIL_MIN
        defaultSignalShouldNotBeFound("seuilMin.lessThan=" + DEFAULT_SEUIL_MIN);

        // Get all the signalList where seuilMin is less than UPDATED_SEUIL_MIN
        defaultSignalShouldBeFound("seuilMin.lessThan=" + UPDATED_SEUIL_MIN);
    }

    @Test
    @Transactional
    void getAllSignalsBySeuilMinIsGreaterThanSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where seuilMin is greater than DEFAULT_SEUIL_MIN
        defaultSignalShouldNotBeFound("seuilMin.greaterThan=" + DEFAULT_SEUIL_MIN);

        // Get all the signalList where seuilMin is greater than SMALLER_SEUIL_MIN
        defaultSignalShouldBeFound("seuilMin.greaterThan=" + SMALLER_SEUIL_MIN);
    }

    @Test
    @Transactional
    void getAllSignalsBySeuilMaxIsEqualToSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where seuilMax equals to DEFAULT_SEUIL_MAX
        defaultSignalShouldBeFound("seuilMax.equals=" + DEFAULT_SEUIL_MAX);

        // Get all the signalList where seuilMax equals to UPDATED_SEUIL_MAX
        defaultSignalShouldNotBeFound("seuilMax.equals=" + UPDATED_SEUIL_MAX);
    }

    @Test
    @Transactional
    void getAllSignalsBySeuilMaxIsInShouldWork() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where seuilMax in DEFAULT_SEUIL_MAX or UPDATED_SEUIL_MAX
        defaultSignalShouldBeFound("seuilMax.in=" + DEFAULT_SEUIL_MAX + "," + UPDATED_SEUIL_MAX);

        // Get all the signalList where seuilMax equals to UPDATED_SEUIL_MAX
        defaultSignalShouldNotBeFound("seuilMax.in=" + UPDATED_SEUIL_MAX);
    }

    @Test
    @Transactional
    void getAllSignalsBySeuilMaxIsNullOrNotNull() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where seuilMax is not null
        defaultSignalShouldBeFound("seuilMax.specified=true");

        // Get all the signalList where seuilMax is null
        defaultSignalShouldNotBeFound("seuilMax.specified=false");
    }

    @Test
    @Transactional
    void getAllSignalsBySeuilMaxIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where seuilMax is greater than or equal to DEFAULT_SEUIL_MAX
        defaultSignalShouldBeFound("seuilMax.greaterThanOrEqual=" + DEFAULT_SEUIL_MAX);

        // Get all the signalList where seuilMax is greater than or equal to UPDATED_SEUIL_MAX
        defaultSignalShouldNotBeFound("seuilMax.greaterThanOrEqual=" + UPDATED_SEUIL_MAX);
    }

    @Test
    @Transactional
    void getAllSignalsBySeuilMaxIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where seuilMax is less than or equal to DEFAULT_SEUIL_MAX
        defaultSignalShouldBeFound("seuilMax.lessThanOrEqual=" + DEFAULT_SEUIL_MAX);

        // Get all the signalList where seuilMax is less than or equal to SMALLER_SEUIL_MAX
        defaultSignalShouldNotBeFound("seuilMax.lessThanOrEqual=" + SMALLER_SEUIL_MAX);
    }

    @Test
    @Transactional
    void getAllSignalsBySeuilMaxIsLessThanSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where seuilMax is less than DEFAULT_SEUIL_MAX
        defaultSignalShouldNotBeFound("seuilMax.lessThan=" + DEFAULT_SEUIL_MAX);

        // Get all the signalList where seuilMax is less than UPDATED_SEUIL_MAX
        defaultSignalShouldBeFound("seuilMax.lessThan=" + UPDATED_SEUIL_MAX);
    }

    @Test
    @Transactional
    void getAllSignalsBySeuilMaxIsGreaterThanSomething() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        // Get all the signalList where seuilMax is greater than DEFAULT_SEUIL_MAX
        defaultSignalShouldNotBeFound("seuilMax.greaterThan=" + DEFAULT_SEUIL_MAX);

        // Get all the signalList where seuilMax is greater than SMALLER_SEUIL_MAX
        defaultSignalShouldBeFound("seuilMax.greaterThan=" + SMALLER_SEUIL_MAX);
    }

    @Test
    @Transactional
    void getAllSignalsByDiagnosticIsEqualToSomething() throws Exception {
        Diagnostic diagnostic;
        if (TestUtil.findAll(em, Diagnostic.class).isEmpty()) {
            signalRepository.saveAndFlush(signal);
            diagnostic = DiagnosticResourceIT.createEntity(em);
        } else {
            diagnostic = TestUtil.findAll(em, Diagnostic.class).get(0);
        }
        em.persist(diagnostic);
        em.flush();
        signal.addDiagnostic(diagnostic);
        signalRepository.saveAndFlush(signal);
        Long diagnosticId = diagnostic.getId();
        // Get all the signalList where diagnostic equals to diagnosticId
        defaultSignalShouldBeFound("diagnosticId.equals=" + diagnosticId);

        // Get all the signalList where diagnostic equals to (diagnosticId + 1)
        defaultSignalShouldNotBeFound("diagnosticId.equals=" + (diagnosticId + 1));
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
            .andExpect(jsonPath("$.[*].valueSignal").value(hasItem(DEFAULT_VALUE_SIGNAL.doubleValue())))
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

        int databaseSizeBeforeUpdate = signalRepository.findAll().size();

        // Update the signal
        Signal updatedSignal = signalRepository.findById(signal.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSignal are not directly saved in db
        em.detach(updatedSignal);
        updatedSignal.libelle(UPDATED_LIBELLE).valueSignal(UPDATED_VALUE_SIGNAL).seuilMin(UPDATED_SEUIL_MIN).seuilMax(UPDATED_SEUIL_MAX);
        SignalDTO signalDTO = signalMapper.toDto(updatedSignal);

        restSignalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, signalDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(signalDTO))
            )
            .andExpect(status().isOk());

        // Validate the Signal in the database
        List<Signal> signalList = signalRepository.findAll();
        assertThat(signalList).hasSize(databaseSizeBeforeUpdate);
        Signal testSignal = signalList.get(signalList.size() - 1);
        assertThat(testSignal.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testSignal.getValueSignal()).isEqualTo(UPDATED_VALUE_SIGNAL);
        assertThat(testSignal.getSeuilMin()).isEqualTo(UPDATED_SEUIL_MIN);
        assertThat(testSignal.getSeuilMax()).isEqualTo(UPDATED_SEUIL_MAX);
    }

    @Test
    @Transactional
    void putNonExistingSignal() throws Exception {
        int databaseSizeBeforeUpdate = signalRepository.findAll().size();
        signal.setId(longCount.incrementAndGet());

        // Create the Signal
        SignalDTO signalDTO = signalMapper.toDto(signal);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSignalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, signalDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(signalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Signal in the database
        List<Signal> signalList = signalRepository.findAll();
        assertThat(signalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSignal() throws Exception {
        int databaseSizeBeforeUpdate = signalRepository.findAll().size();
        signal.setId(longCount.incrementAndGet());

        // Create the Signal
        SignalDTO signalDTO = signalMapper.toDto(signal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSignalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(signalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Signal in the database
        List<Signal> signalList = signalRepository.findAll();
        assertThat(signalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSignal() throws Exception {
        int databaseSizeBeforeUpdate = signalRepository.findAll().size();
        signal.setId(longCount.incrementAndGet());

        // Create the Signal
        SignalDTO signalDTO = signalMapper.toDto(signal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSignalMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(signalDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Signal in the database
        List<Signal> signalList = signalRepository.findAll();
        assertThat(signalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSignalWithPatch() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        int databaseSizeBeforeUpdate = signalRepository.findAll().size();

        // Update the signal using partial update
        Signal partialUpdatedSignal = new Signal();
        partialUpdatedSignal.setId(signal.getId());

        partialUpdatedSignal.libelle(UPDATED_LIBELLE);

        restSignalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSignal.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSignal))
            )
            .andExpect(status().isOk());

        // Validate the Signal in the database
        List<Signal> signalList = signalRepository.findAll();
        assertThat(signalList).hasSize(databaseSizeBeforeUpdate);
        Signal testSignal = signalList.get(signalList.size() - 1);
        assertThat(testSignal.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testSignal.getValueSignal()).isEqualTo(DEFAULT_VALUE_SIGNAL);
        assertThat(testSignal.getSeuilMin()).isEqualTo(DEFAULT_SEUIL_MIN);
        assertThat(testSignal.getSeuilMax()).isEqualTo(DEFAULT_SEUIL_MAX);
    }

    @Test
    @Transactional
    void fullUpdateSignalWithPatch() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        int databaseSizeBeforeUpdate = signalRepository.findAll().size();

        // Update the signal using partial update
        Signal partialUpdatedSignal = new Signal();
        partialUpdatedSignal.setId(signal.getId());

        partialUpdatedSignal
            .libelle(UPDATED_LIBELLE)
            .valueSignal(UPDATED_VALUE_SIGNAL)
            .seuilMin(UPDATED_SEUIL_MIN)
            .seuilMax(UPDATED_SEUIL_MAX);

        restSignalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSignal.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSignal))
            )
            .andExpect(status().isOk());

        // Validate the Signal in the database
        List<Signal> signalList = signalRepository.findAll();
        assertThat(signalList).hasSize(databaseSizeBeforeUpdate);
        Signal testSignal = signalList.get(signalList.size() - 1);
        assertThat(testSignal.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testSignal.getValueSignal()).isEqualTo(UPDATED_VALUE_SIGNAL);
        assertThat(testSignal.getSeuilMin()).isEqualTo(UPDATED_SEUIL_MIN);
        assertThat(testSignal.getSeuilMax()).isEqualTo(UPDATED_SEUIL_MAX);
    }

    @Test
    @Transactional
    void patchNonExistingSignal() throws Exception {
        int databaseSizeBeforeUpdate = signalRepository.findAll().size();
        signal.setId(longCount.incrementAndGet());

        // Create the Signal
        SignalDTO signalDTO = signalMapper.toDto(signal);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSignalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, signalDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(signalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Signal in the database
        List<Signal> signalList = signalRepository.findAll();
        assertThat(signalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSignal() throws Exception {
        int databaseSizeBeforeUpdate = signalRepository.findAll().size();
        signal.setId(longCount.incrementAndGet());

        // Create the Signal
        SignalDTO signalDTO = signalMapper.toDto(signal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSignalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(signalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Signal in the database
        List<Signal> signalList = signalRepository.findAll();
        assertThat(signalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSignal() throws Exception {
        int databaseSizeBeforeUpdate = signalRepository.findAll().size();
        signal.setId(longCount.incrementAndGet());

        // Create the Signal
        SignalDTO signalDTO = signalMapper.toDto(signal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSignalMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(signalDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Signal in the database
        List<Signal> signalList = signalRepository.findAll();
        assertThat(signalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSignal() throws Exception {
        // Initialize the database
        signalRepository.saveAndFlush(signal);

        int databaseSizeBeforeDelete = signalRepository.findAll().size();

        // Delete the signal
        restSignalMockMvc
            .perform(delete(ENTITY_API_URL_ID, signal.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Signal> signalList = signalRepository.findAll();
        assertThat(signalList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
