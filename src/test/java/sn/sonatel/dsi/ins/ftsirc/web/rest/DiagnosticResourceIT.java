package sn.sonatel.dsi.ins.ftsirc.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.sonatel.dsi.ins.ftsirc.domain.DiagnosticAsserts.*;
import static sn.sonatel.dsi.ins.ftsirc.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import sn.sonatel.dsi.ins.ftsirc.IntegrationTest;
import sn.sonatel.dsi.ins.ftsirc.domain.Anomalie;
import sn.sonatel.dsi.ins.ftsirc.domain.Diagnostic;
import sn.sonatel.dsi.ins.ftsirc.domain.ONT;
import sn.sonatel.dsi.ins.ftsirc.domain.Signal;
import sn.sonatel.dsi.ins.ftsirc.domain.enumeration.StatutONT;
import sn.sonatel.dsi.ins.ftsirc.domain.enumeration.TypeDiagnostic;
import sn.sonatel.dsi.ins.ftsirc.repository.DiagnosticRepository;
import sn.sonatel.dsi.ins.ftsirc.service.DiagnosticService;
import sn.sonatel.dsi.ins.ftsirc.service.dto.DiagnosticDTO;
import sn.sonatel.dsi.ins.ftsirc.service.mapper.DiagnosticMapper;

/**
 * Integration tests for the {@link DiagnosticResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DiagnosticResourceIT {

    private static final String DEFAULT_INDEX = "AAAAAAAAAA";
    private static final String UPDATED_INDEX = "BBBBBBBBBB";

    private static final StatutONT DEFAULT_STATUT_ONT = StatutONT.ACTIF;
    private static final StatutONT UPDATED_STATUT_ONT = StatutONT.INACTIF;

    private static final String DEFAULT_DEBIT_UP = "AAAAAAAAAA";
    private static final String UPDATED_DEBIT_UP = "BBBBBBBBBB";

    private static final String DEFAULT_DEBIT_DOWN = "AAAAAAAAAA";
    private static final String UPDATED_DEBIT_DOWN = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_DIAGNOSTIC = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DIAGNOSTIC = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_DIAGNOSTIC = LocalDate.ofEpochDay(-1L);

    private static final TypeDiagnostic DEFAULT_TYPE_DIAGNOSTIC = TypeDiagnostic.AUTOMATIQUE;
    private static final TypeDiagnostic UPDATED_TYPE_DIAGNOSTIC = TypeDiagnostic.MANUEL;

    private static final String ENTITY_API_URL = "/api/diagnostics";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DiagnosticRepository diagnosticRepository;

    @Mock
    private DiagnosticRepository diagnosticRepositoryMock;

    @Autowired
    private DiagnosticMapper diagnosticMapper;

    @Mock
    private DiagnosticService diagnosticServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDiagnosticMockMvc;

    private Diagnostic diagnostic;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Diagnostic createEntity(EntityManager em) {
        Diagnostic diagnostic = new Diagnostic()
            .index(DEFAULT_INDEX)
            .statutONT(DEFAULT_STATUT_ONT)
            .debitUp(DEFAULT_DEBIT_UP)
            .debitDown(DEFAULT_DEBIT_DOWN)
            .dateDiagnostic(DEFAULT_DATE_DIAGNOSTIC)
            .typeDiagnostic(DEFAULT_TYPE_DIAGNOSTIC);
        return diagnostic;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Diagnostic createUpdatedEntity(EntityManager em) {
        Diagnostic diagnostic = new Diagnostic()
            .index(UPDATED_INDEX)
            .statutONT(UPDATED_STATUT_ONT)
            .debitUp(UPDATED_DEBIT_UP)
            .debitDown(UPDATED_DEBIT_DOWN)
            .dateDiagnostic(UPDATED_DATE_DIAGNOSTIC)
            .typeDiagnostic(UPDATED_TYPE_DIAGNOSTIC);
        return diagnostic;
    }

    @BeforeEach
    public void initTest() {
        diagnostic = createEntity(em);
    }

    @Test
    @Transactional
    void createDiagnostic() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Diagnostic
        DiagnosticDTO diagnosticDTO = diagnosticMapper.toDto(diagnostic);
        var returnedDiagnosticDTO = om.readValue(
            restDiagnosticMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(diagnosticDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DiagnosticDTO.class
        );

        // Validate the Diagnostic in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDiagnostic = diagnosticMapper.toEntity(returnedDiagnosticDTO);
        assertDiagnosticUpdatableFieldsEquals(returnedDiagnostic, getPersistedDiagnostic(returnedDiagnostic));
    }

    @Test
    @Transactional
    void createDiagnosticWithExistingId() throws Exception {
        // Create the Diagnostic with an existing ID
        diagnostic.setId(1L);
        DiagnosticDTO diagnosticDTO = diagnosticMapper.toDto(diagnostic);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDiagnosticMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(diagnosticDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Diagnostic in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIndexIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        diagnostic.setIndex(null);

        // Create the Diagnostic, which fails.
        DiagnosticDTO diagnosticDTO = diagnosticMapper.toDto(diagnostic);

        restDiagnosticMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(diagnosticDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDiagnostics() throws Exception {
        // Initialize the database
        diagnosticRepository.saveAndFlush(diagnostic);

        // Get all the diagnosticList
        restDiagnosticMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(diagnostic.getId().intValue())))
            .andExpect(jsonPath("$.[*].index").value(hasItem(DEFAULT_INDEX)))
            .andExpect(jsonPath("$.[*].statutONT").value(hasItem(DEFAULT_STATUT_ONT.toString())))
            .andExpect(jsonPath("$.[*].debitUp").value(hasItem(DEFAULT_DEBIT_UP)))
            .andExpect(jsonPath("$.[*].debitDown").value(hasItem(DEFAULT_DEBIT_DOWN)))
            .andExpect(jsonPath("$.[*].dateDiagnostic").value(hasItem(DEFAULT_DATE_DIAGNOSTIC.toString())))
            .andExpect(jsonPath("$.[*].typeDiagnostic").value(hasItem(DEFAULT_TYPE_DIAGNOSTIC.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDiagnosticsWithEagerRelationshipsIsEnabled() throws Exception {
        when(diagnosticServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDiagnosticMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(diagnosticServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDiagnosticsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(diagnosticServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDiagnosticMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(diagnosticRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getDiagnostic() throws Exception {
        // Initialize the database
        diagnosticRepository.saveAndFlush(diagnostic);

        // Get the diagnostic
        restDiagnosticMockMvc
            .perform(get(ENTITY_API_URL_ID, diagnostic.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(diagnostic.getId().intValue()))
            .andExpect(jsonPath("$.index").value(DEFAULT_INDEX))
            .andExpect(jsonPath("$.statutONT").value(DEFAULT_STATUT_ONT.toString()))
            .andExpect(jsonPath("$.debitUp").value(DEFAULT_DEBIT_UP))
            .andExpect(jsonPath("$.debitDown").value(DEFAULT_DEBIT_DOWN))
            .andExpect(jsonPath("$.dateDiagnostic").value(DEFAULT_DATE_DIAGNOSTIC.toString()))
            .andExpect(jsonPath("$.typeDiagnostic").value(DEFAULT_TYPE_DIAGNOSTIC.toString()));
    }

    @Test
    @Transactional
    void getDiagnosticsByIdFiltering() throws Exception {
        // Initialize the database
        diagnosticRepository.saveAndFlush(diagnostic);

        Long id = diagnostic.getId();

        defaultDiagnosticFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultDiagnosticFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultDiagnosticFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDiagnosticsByIndexIsEqualToSomething() throws Exception {
        // Initialize the database
        diagnosticRepository.saveAndFlush(diagnostic);

        // Get all the diagnosticList where index equals to
        defaultDiagnosticFiltering("index.equals=" + DEFAULT_INDEX, "index.equals=" + UPDATED_INDEX);
    }

    @Test
    @Transactional
    void getAllDiagnosticsByIndexIsInShouldWork() throws Exception {
        // Initialize the database
        diagnosticRepository.saveAndFlush(diagnostic);

        // Get all the diagnosticList where index in
        defaultDiagnosticFiltering("index.in=" + DEFAULT_INDEX + "," + UPDATED_INDEX, "index.in=" + UPDATED_INDEX);
    }

    @Test
    @Transactional
    void getAllDiagnosticsByIndexIsNullOrNotNull() throws Exception {
        // Initialize the database
        diagnosticRepository.saveAndFlush(diagnostic);

        // Get all the diagnosticList where index is not null
        defaultDiagnosticFiltering("index.specified=true", "index.specified=false");
    }

    @Test
    @Transactional
    void getAllDiagnosticsByIndexContainsSomething() throws Exception {
        // Initialize the database
        diagnosticRepository.saveAndFlush(diagnostic);

        // Get all the diagnosticList where index contains
        defaultDiagnosticFiltering("index.contains=" + DEFAULT_INDEX, "index.contains=" + UPDATED_INDEX);
    }

    @Test
    @Transactional
    void getAllDiagnosticsByIndexNotContainsSomething() throws Exception {
        // Initialize the database
        diagnosticRepository.saveAndFlush(diagnostic);

        // Get all the diagnosticList where index does not contain
        defaultDiagnosticFiltering("index.doesNotContain=" + UPDATED_INDEX, "index.doesNotContain=" + DEFAULT_INDEX);
    }

    @Test
    @Transactional
    void getAllDiagnosticsByStatutONTIsEqualToSomething() throws Exception {
        // Initialize the database
        diagnosticRepository.saveAndFlush(diagnostic);

        // Get all the diagnosticList where statutONT equals to
        defaultDiagnosticFiltering("statutONT.equals=" + DEFAULT_STATUT_ONT, "statutONT.equals=" + UPDATED_STATUT_ONT);
    }

    @Test
    @Transactional
    void getAllDiagnosticsByStatutONTIsInShouldWork() throws Exception {
        // Initialize the database
        diagnosticRepository.saveAndFlush(diagnostic);

        // Get all the diagnosticList where statutONT in
        defaultDiagnosticFiltering("statutONT.in=" + DEFAULT_STATUT_ONT + "," + UPDATED_STATUT_ONT, "statutONT.in=" + UPDATED_STATUT_ONT);
    }

    @Test
    @Transactional
    void getAllDiagnosticsByStatutONTIsNullOrNotNull() throws Exception {
        // Initialize the database
        diagnosticRepository.saveAndFlush(diagnostic);

        // Get all the diagnosticList where statutONT is not null
        defaultDiagnosticFiltering("statutONT.specified=true", "statutONT.specified=false");
    }

    @Test
    @Transactional
    void getAllDiagnosticsByDebitUpIsEqualToSomething() throws Exception {
        // Initialize the database
        diagnosticRepository.saveAndFlush(diagnostic);

        // Get all the diagnosticList where debitUp equals to
        defaultDiagnosticFiltering("debitUp.equals=" + DEFAULT_DEBIT_UP, "debitUp.equals=" + UPDATED_DEBIT_UP);
    }

    @Test
    @Transactional
    void getAllDiagnosticsByDebitUpIsInShouldWork() throws Exception {
        // Initialize the database
        diagnosticRepository.saveAndFlush(diagnostic);

        // Get all the diagnosticList where debitUp in
        defaultDiagnosticFiltering("debitUp.in=" + DEFAULT_DEBIT_UP + "," + UPDATED_DEBIT_UP, "debitUp.in=" + UPDATED_DEBIT_UP);
    }

    @Test
    @Transactional
    void getAllDiagnosticsByDebitUpIsNullOrNotNull() throws Exception {
        // Initialize the database
        diagnosticRepository.saveAndFlush(diagnostic);

        // Get all the diagnosticList where debitUp is not null
        defaultDiagnosticFiltering("debitUp.specified=true", "debitUp.specified=false");
    }

    @Test
    @Transactional
    void getAllDiagnosticsByDebitUpContainsSomething() throws Exception {
        // Initialize the database
        diagnosticRepository.saveAndFlush(diagnostic);

        // Get all the diagnosticList where debitUp contains
        defaultDiagnosticFiltering("debitUp.contains=" + DEFAULT_DEBIT_UP, "debitUp.contains=" + UPDATED_DEBIT_UP);
    }

    @Test
    @Transactional
    void getAllDiagnosticsByDebitUpNotContainsSomething() throws Exception {
        // Initialize the database
        diagnosticRepository.saveAndFlush(diagnostic);

        // Get all the diagnosticList where debitUp does not contain
        defaultDiagnosticFiltering("debitUp.doesNotContain=" + UPDATED_DEBIT_UP, "debitUp.doesNotContain=" + DEFAULT_DEBIT_UP);
    }

    @Test
    @Transactional
    void getAllDiagnosticsByDebitDownIsEqualToSomething() throws Exception {
        // Initialize the database
        diagnosticRepository.saveAndFlush(diagnostic);

        // Get all the diagnosticList where debitDown equals to
        defaultDiagnosticFiltering("debitDown.equals=" + DEFAULT_DEBIT_DOWN, "debitDown.equals=" + UPDATED_DEBIT_DOWN);
    }

    @Test
    @Transactional
    void getAllDiagnosticsByDebitDownIsInShouldWork() throws Exception {
        // Initialize the database
        diagnosticRepository.saveAndFlush(diagnostic);

        // Get all the diagnosticList where debitDown in
        defaultDiagnosticFiltering("debitDown.in=" + DEFAULT_DEBIT_DOWN + "," + UPDATED_DEBIT_DOWN, "debitDown.in=" + UPDATED_DEBIT_DOWN);
    }

    @Test
    @Transactional
    void getAllDiagnosticsByDebitDownIsNullOrNotNull() throws Exception {
        // Initialize the database
        diagnosticRepository.saveAndFlush(diagnostic);

        // Get all the diagnosticList where debitDown is not null
        defaultDiagnosticFiltering("debitDown.specified=true", "debitDown.specified=false");
    }

    @Test
    @Transactional
    void getAllDiagnosticsByDebitDownContainsSomething() throws Exception {
        // Initialize the database
        diagnosticRepository.saveAndFlush(diagnostic);

        // Get all the diagnosticList where debitDown contains
        defaultDiagnosticFiltering("debitDown.contains=" + DEFAULT_DEBIT_DOWN, "debitDown.contains=" + UPDATED_DEBIT_DOWN);
    }

    @Test
    @Transactional
    void getAllDiagnosticsByDebitDownNotContainsSomething() throws Exception {
        // Initialize the database
        diagnosticRepository.saveAndFlush(diagnostic);

        // Get all the diagnosticList where debitDown does not contain
        defaultDiagnosticFiltering("debitDown.doesNotContain=" + UPDATED_DEBIT_DOWN, "debitDown.doesNotContain=" + DEFAULT_DEBIT_DOWN);
    }

    @Test
    @Transactional
    void getAllDiagnosticsByDateDiagnosticIsEqualToSomething() throws Exception {
        // Initialize the database
        diagnosticRepository.saveAndFlush(diagnostic);

        // Get all the diagnosticList where dateDiagnostic equals to
        defaultDiagnosticFiltering("dateDiagnostic.equals=" + DEFAULT_DATE_DIAGNOSTIC, "dateDiagnostic.equals=" + UPDATED_DATE_DIAGNOSTIC);
    }

    @Test
    @Transactional
    void getAllDiagnosticsByDateDiagnosticIsInShouldWork() throws Exception {
        // Initialize the database
        diagnosticRepository.saveAndFlush(diagnostic);

        // Get all the diagnosticList where dateDiagnostic in
        defaultDiagnosticFiltering(
            "dateDiagnostic.in=" + DEFAULT_DATE_DIAGNOSTIC + "," + UPDATED_DATE_DIAGNOSTIC,
            "dateDiagnostic.in=" + UPDATED_DATE_DIAGNOSTIC
        );
    }

    @Test
    @Transactional
    void getAllDiagnosticsByDateDiagnosticIsNullOrNotNull() throws Exception {
        // Initialize the database
        diagnosticRepository.saveAndFlush(diagnostic);

        // Get all the diagnosticList where dateDiagnostic is not null
        defaultDiagnosticFiltering("dateDiagnostic.specified=true", "dateDiagnostic.specified=false");
    }

    @Test
    @Transactional
    void getAllDiagnosticsByDateDiagnosticIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        diagnosticRepository.saveAndFlush(diagnostic);

        // Get all the diagnosticList where dateDiagnostic is greater than or equal to
        defaultDiagnosticFiltering(
            "dateDiagnostic.greaterThanOrEqual=" + DEFAULT_DATE_DIAGNOSTIC,
            "dateDiagnostic.greaterThanOrEqual=" + UPDATED_DATE_DIAGNOSTIC
        );
    }

    @Test
    @Transactional
    void getAllDiagnosticsByDateDiagnosticIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        diagnosticRepository.saveAndFlush(diagnostic);

        // Get all the diagnosticList where dateDiagnostic is less than or equal to
        defaultDiagnosticFiltering(
            "dateDiagnostic.lessThanOrEqual=" + DEFAULT_DATE_DIAGNOSTIC,
            "dateDiagnostic.lessThanOrEqual=" + SMALLER_DATE_DIAGNOSTIC
        );
    }

    @Test
    @Transactional
    void getAllDiagnosticsByDateDiagnosticIsLessThanSomething() throws Exception {
        // Initialize the database
        diagnosticRepository.saveAndFlush(diagnostic);

        // Get all the diagnosticList where dateDiagnostic is less than
        defaultDiagnosticFiltering(
            "dateDiagnostic.lessThan=" + UPDATED_DATE_DIAGNOSTIC,
            "dateDiagnostic.lessThan=" + DEFAULT_DATE_DIAGNOSTIC
        );
    }

    @Test
    @Transactional
    void getAllDiagnosticsByDateDiagnosticIsGreaterThanSomething() throws Exception {
        // Initialize the database
        diagnosticRepository.saveAndFlush(diagnostic);

        // Get all the diagnosticList where dateDiagnostic is greater than
        defaultDiagnosticFiltering(
            "dateDiagnostic.greaterThan=" + SMALLER_DATE_DIAGNOSTIC,
            "dateDiagnostic.greaterThan=" + DEFAULT_DATE_DIAGNOSTIC
        );
    }

    @Test
    @Transactional
    void getAllDiagnosticsByTypeDiagnosticIsEqualToSomething() throws Exception {
        // Initialize the database
        diagnosticRepository.saveAndFlush(diagnostic);

        // Get all the diagnosticList where typeDiagnostic equals to
        defaultDiagnosticFiltering("typeDiagnostic.equals=" + DEFAULT_TYPE_DIAGNOSTIC, "typeDiagnostic.equals=" + UPDATED_TYPE_DIAGNOSTIC);
    }

    @Test
    @Transactional
    void getAllDiagnosticsByTypeDiagnosticIsInShouldWork() throws Exception {
        // Initialize the database
        diagnosticRepository.saveAndFlush(diagnostic);

        // Get all the diagnosticList where typeDiagnostic in
        defaultDiagnosticFiltering(
            "typeDiagnostic.in=" + DEFAULT_TYPE_DIAGNOSTIC + "," + UPDATED_TYPE_DIAGNOSTIC,
            "typeDiagnostic.in=" + UPDATED_TYPE_DIAGNOSTIC
        );
    }

    @Test
    @Transactional
    void getAllDiagnosticsByTypeDiagnosticIsNullOrNotNull() throws Exception {
        // Initialize the database
        diagnosticRepository.saveAndFlush(diagnostic);

        // Get all the diagnosticList where typeDiagnostic is not null
        defaultDiagnosticFiltering("typeDiagnostic.specified=true", "typeDiagnostic.specified=false");
    }

    @Test
    @Transactional
    void getAllDiagnosticsBySignalIsEqualToSomething() throws Exception {
        Signal signal;
        if (TestUtil.findAll(em, Signal.class).isEmpty()) {
            diagnosticRepository.saveAndFlush(diagnostic);
            signal = SignalResourceIT.createEntity(em);
        } else {
            signal = TestUtil.findAll(em, Signal.class).get(0);
        }
        em.persist(signal);
        em.flush();
        diagnostic.setSignal(signal);
        diagnosticRepository.saveAndFlush(diagnostic);
        Long signalId = signal.getId();
        // Get all the diagnosticList where signal equals to signalId
        defaultDiagnosticShouldBeFound("signalId.equals=" + signalId);

        // Get all the diagnosticList where signal equals to (signalId + 1)
        defaultDiagnosticShouldNotBeFound("signalId.equals=" + (signalId + 1));
    }

    @Test
    @Transactional
    void getAllDiagnosticsByOntIsEqualToSomething() throws Exception {
        ONT ont;
        if (TestUtil.findAll(em, ONT.class).isEmpty()) {
            diagnosticRepository.saveAndFlush(diagnostic);
            ont = ONTResourceIT.createEntity(em);
        } else {
            ont = TestUtil.findAll(em, ONT.class).get(0);
        }
        em.persist(ont);
        em.flush();
        diagnostic.setOnt(ont);
        diagnosticRepository.saveAndFlush(diagnostic);
        Long ontId = ont.getId();
        // Get all the diagnosticList where ont equals to ontId
        defaultDiagnosticShouldBeFound("ontId.equals=" + ontId);

        // Get all the diagnosticList where ont equals to (ontId + 1)
        defaultDiagnosticShouldNotBeFound("ontId.equals=" + (ontId + 1));
    }

    @Test
    @Transactional
    void getAllDiagnosticsByAnomalieIsEqualToSomething() throws Exception {
        Anomalie anomalie;
        if (TestUtil.findAll(em, Anomalie.class).isEmpty()) {
            diagnosticRepository.saveAndFlush(diagnostic);
            anomalie = AnomalieResourceIT.createEntity(em);
        } else {
            anomalie = TestUtil.findAll(em, Anomalie.class).get(0);
        }
        em.persist(anomalie);
        em.flush();
        diagnostic.addAnomalie(anomalie);
        diagnosticRepository.saveAndFlush(diagnostic);
        Long anomalieId = anomalie.getId();
        // Get all the diagnosticList where anomalie equals to anomalieId
        defaultDiagnosticShouldBeFound("anomalieId.equals=" + anomalieId);

        // Get all the diagnosticList where anomalie equals to (anomalieId + 1)
        defaultDiagnosticShouldNotBeFound("anomalieId.equals=" + (anomalieId + 1));
    }

    private void defaultDiagnosticFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultDiagnosticShouldBeFound(shouldBeFound);
        defaultDiagnosticShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDiagnosticShouldBeFound(String filter) throws Exception {
        restDiagnosticMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(diagnostic.getId().intValue())))
            .andExpect(jsonPath("$.[*].index").value(hasItem(DEFAULT_INDEX)))
            .andExpect(jsonPath("$.[*].statutONT").value(hasItem(DEFAULT_STATUT_ONT.toString())))
            .andExpect(jsonPath("$.[*].debitUp").value(hasItem(DEFAULT_DEBIT_UP)))
            .andExpect(jsonPath("$.[*].debitDown").value(hasItem(DEFAULT_DEBIT_DOWN)))
            .andExpect(jsonPath("$.[*].dateDiagnostic").value(hasItem(DEFAULT_DATE_DIAGNOSTIC.toString())))
            .andExpect(jsonPath("$.[*].typeDiagnostic").value(hasItem(DEFAULT_TYPE_DIAGNOSTIC.toString())));

        // Check, that the count call also returns 1
        restDiagnosticMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDiagnosticShouldNotBeFound(String filter) throws Exception {
        restDiagnosticMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDiagnosticMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDiagnostic() throws Exception {
        // Get the diagnostic
        restDiagnosticMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDiagnostic() throws Exception {
        // Initialize the database
        diagnosticRepository.saveAndFlush(diagnostic);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the diagnostic
        Diagnostic updatedDiagnostic = diagnosticRepository.findById(diagnostic.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDiagnostic are not directly saved in db
        em.detach(updatedDiagnostic);
        updatedDiagnostic
            .index(UPDATED_INDEX)
            .statutONT(UPDATED_STATUT_ONT)
            .debitUp(UPDATED_DEBIT_UP)
            .debitDown(UPDATED_DEBIT_DOWN)
            .dateDiagnostic(UPDATED_DATE_DIAGNOSTIC)
            .typeDiagnostic(UPDATED_TYPE_DIAGNOSTIC);
        DiagnosticDTO diagnosticDTO = diagnosticMapper.toDto(updatedDiagnostic);

        restDiagnosticMockMvc
            .perform(
                put(ENTITY_API_URL_ID, diagnosticDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(diagnosticDTO))
            )
            .andExpect(status().isOk());

        // Validate the Diagnostic in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDiagnosticToMatchAllProperties(updatedDiagnostic);
    }

    @Test
    @Transactional
    void putNonExistingDiagnostic() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        diagnostic.setId(longCount.incrementAndGet());

        // Create the Diagnostic
        DiagnosticDTO diagnosticDTO = diagnosticMapper.toDto(diagnostic);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDiagnosticMockMvc
            .perform(
                put(ENTITY_API_URL_ID, diagnosticDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(diagnosticDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Diagnostic in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDiagnostic() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        diagnostic.setId(longCount.incrementAndGet());

        // Create the Diagnostic
        DiagnosticDTO diagnosticDTO = diagnosticMapper.toDto(diagnostic);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDiagnosticMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(diagnosticDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Diagnostic in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDiagnostic() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        diagnostic.setId(longCount.incrementAndGet());

        // Create the Diagnostic
        DiagnosticDTO diagnosticDTO = diagnosticMapper.toDto(diagnostic);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDiagnosticMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(diagnosticDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Diagnostic in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDiagnosticWithPatch() throws Exception {
        // Initialize the database
        diagnosticRepository.saveAndFlush(diagnostic);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the diagnostic using partial update
        Diagnostic partialUpdatedDiagnostic = new Diagnostic();
        partialUpdatedDiagnostic.setId(diagnostic.getId());

        partialUpdatedDiagnostic.index(UPDATED_INDEX).statutONT(UPDATED_STATUT_ONT).dateDiagnostic(UPDATED_DATE_DIAGNOSTIC);

        restDiagnosticMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDiagnostic.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDiagnostic))
            )
            .andExpect(status().isOk());

        // Validate the Diagnostic in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDiagnosticUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDiagnostic, diagnostic),
            getPersistedDiagnostic(diagnostic)
        );
    }

    @Test
    @Transactional
    void fullUpdateDiagnosticWithPatch() throws Exception {
        // Initialize the database
        diagnosticRepository.saveAndFlush(diagnostic);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the diagnostic using partial update
        Diagnostic partialUpdatedDiagnostic = new Diagnostic();
        partialUpdatedDiagnostic.setId(diagnostic.getId());

        partialUpdatedDiagnostic
            .index(UPDATED_INDEX)
            .statutONT(UPDATED_STATUT_ONT)
            .debitUp(UPDATED_DEBIT_UP)
            .debitDown(UPDATED_DEBIT_DOWN)
            .dateDiagnostic(UPDATED_DATE_DIAGNOSTIC)
            .typeDiagnostic(UPDATED_TYPE_DIAGNOSTIC);

        restDiagnosticMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDiagnostic.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDiagnostic))
            )
            .andExpect(status().isOk());

        // Validate the Diagnostic in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDiagnosticUpdatableFieldsEquals(partialUpdatedDiagnostic, getPersistedDiagnostic(partialUpdatedDiagnostic));
    }

    @Test
    @Transactional
    void patchNonExistingDiagnostic() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        diagnostic.setId(longCount.incrementAndGet());

        // Create the Diagnostic
        DiagnosticDTO diagnosticDTO = diagnosticMapper.toDto(diagnostic);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDiagnosticMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, diagnosticDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(diagnosticDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Diagnostic in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDiagnostic() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        diagnostic.setId(longCount.incrementAndGet());

        // Create the Diagnostic
        DiagnosticDTO diagnosticDTO = diagnosticMapper.toDto(diagnostic);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDiagnosticMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(diagnosticDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Diagnostic in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDiagnostic() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        diagnostic.setId(longCount.incrementAndGet());

        // Create the Diagnostic
        DiagnosticDTO diagnosticDTO = diagnosticMapper.toDto(diagnostic);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDiagnosticMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(diagnosticDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Diagnostic in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDiagnostic() throws Exception {
        // Initialize the database
        diagnosticRepository.saveAndFlush(diagnostic);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the diagnostic
        restDiagnosticMockMvc
            .perform(delete(ENTITY_API_URL_ID, diagnostic.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return diagnosticRepository.count();
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

    protected Diagnostic getPersistedDiagnostic(Diagnostic diagnostic) {
        return diagnosticRepository.findById(diagnostic.getId()).orElseThrow();
    }

    protected void assertPersistedDiagnosticToMatchAllProperties(Diagnostic expectedDiagnostic) {
        assertDiagnosticAllPropertiesEquals(expectedDiagnostic, getPersistedDiagnostic(expectedDiagnostic));
    }

    protected void assertPersistedDiagnosticToMatchUpdatableProperties(Diagnostic expectedDiagnostic) {
        assertDiagnosticAllUpdatablePropertiesEquals(expectedDiagnostic, getPersistedDiagnostic(expectedDiagnostic));
    }
}
