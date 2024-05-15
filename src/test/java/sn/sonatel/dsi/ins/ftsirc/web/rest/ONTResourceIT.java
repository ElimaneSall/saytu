package sn.sonatel.dsi.ins.ftsirc.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
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
import sn.sonatel.dsi.ins.ftsirc.domain.Client;
import sn.sonatel.dsi.ins.ftsirc.domain.Diagnostic;
import sn.sonatel.dsi.ins.ftsirc.domain.Metrique;
import sn.sonatel.dsi.ins.ftsirc.domain.OLT;
import sn.sonatel.dsi.ins.ftsirc.domain.ONT;
import sn.sonatel.dsi.ins.ftsirc.repository.ONTRepository;
import sn.sonatel.dsi.ins.ftsirc.service.ONTService;
import sn.sonatel.dsi.ins.ftsirc.service.dto.ONTDTO;
import sn.sonatel.dsi.ins.ftsirc.service.mapper.ONTMapper;

/**
 * Integration tests for the {@link ONTResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ONTResourceIT {

    private static final String DEFAULT_INDEX = "AAAAAAAAAA";
    private static final String UPDATED_INDEX = "BBBBBBBBBB";

    private static final String DEFAULT_ONT_ID = "AAAAAAAAAA";
    private static final String UPDATED_ONT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_SERVICE_ID = "AAAAAAAAAA";
    private static final String UPDATED_SERVICE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_SLOT = "AAAAAAAAAA";
    private static final String UPDATED_SLOT = "BBBBBBBBBB";

    private static final String DEFAULT_PON = "AAAAAAAAAA";
    private static final String UPDATED_PON = "BBBBBBBBBB";

    private static final String DEFAULT_PON_INDEX = "AAAAAAAAAA";
    private static final String UPDATED_PON_INDEX = "BBBBBBBBBB";

    private static final String DEFAULT_MAX_UP = "AAAAAAAAAA";
    private static final String UPDATED_MAX_UP = "BBBBBBBBBB";

    private static final String DEFAULT_MAX_DOWN = "AAAAAAAAAA";
    private static final String UPDATED_MAX_DOWN = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_AT = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATED_AT = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_UPDATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATED_AT = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_UPDATED_AT = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_ETAT_OLT = "AAAAAAAAAA";
    private static final String UPDATED_ETAT_OLT = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_STATUS_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_STATUS_AT = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_STATUS_AT = LocalDate.ofEpochDay(-1L);

    private static final Long DEFAULT_NBRE_LIGNES_COUPER = 1L;
    private static final Long UPDATED_NBRE_LIGNES_COUPER = 2L;
    private static final Long SMALLER_NBRE_LIGNES_COUPER = 1L - 1L;

    private static final String ENTITY_API_URL = "/api/onts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ONTRepository oNTRepository;

    @Mock
    private ONTRepository oNTRepositoryMock;

    @Autowired
    private ONTMapper oNTMapper;

    @Mock
    private ONTService oNTServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restONTMockMvc;

    private ONT oNT;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ONT createEntity(EntityManager em) {
        ONT oNT = new ONT()
            .index(DEFAULT_INDEX)
            .ontID(DEFAULT_ONT_ID)
            .serviceId(DEFAULT_SERVICE_ID)
            .slot(DEFAULT_SLOT)
            .pon(DEFAULT_PON)
            .ponIndex(DEFAULT_PON_INDEX)
            .maxUp(DEFAULT_MAX_UP)
            .maxDown(DEFAULT_MAX_DOWN)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT)
            .etatOlt(DEFAULT_ETAT_OLT)
            .status(DEFAULT_STATUS)
            .statusAt(DEFAULT_STATUS_AT)
            .nbreLignesCouper(DEFAULT_NBRE_LIGNES_COUPER);
        return oNT;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ONT createUpdatedEntity(EntityManager em) {
        ONT oNT = new ONT()
            .index(UPDATED_INDEX)
            .ontID(UPDATED_ONT_ID)
            .serviceId(UPDATED_SERVICE_ID)
            .slot(UPDATED_SLOT)
            .pon(UPDATED_PON)
            .ponIndex(UPDATED_PON_INDEX)
            .maxUp(UPDATED_MAX_UP)
            .maxDown(UPDATED_MAX_DOWN)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .etatOlt(UPDATED_ETAT_OLT)
            .status(UPDATED_STATUS)
            .statusAt(UPDATED_STATUS_AT)
            .nbreLignesCouper(UPDATED_NBRE_LIGNES_COUPER);
        return oNT;
    }

    @BeforeEach
    public void initTest() {
        oNT = createEntity(em);
    }

    @Test
    @Transactional
    void createONT() throws Exception {
        int databaseSizeBeforeCreate = oNTRepository.findAll().size();
        // Create the ONT
        ONTDTO oNTDTO = oNTMapper.toDto(oNT);
        restONTMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(oNTDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ONT in the database
        List<ONT> oNTList = oNTRepository.findAll();
        assertThat(oNTList).hasSize(databaseSizeBeforeCreate + 1);
        ONT testONT = oNTList.get(oNTList.size() - 1);
        assertThat(testONT.getIndex()).isEqualTo(DEFAULT_INDEX);
        assertThat(testONT.getOntID()).isEqualTo(DEFAULT_ONT_ID);
        assertThat(testONT.getServiceId()).isEqualTo(DEFAULT_SERVICE_ID);
        assertThat(testONT.getSlot()).isEqualTo(DEFAULT_SLOT);
        assertThat(testONT.getPon()).isEqualTo(DEFAULT_PON);
        assertThat(testONT.getPonIndex()).isEqualTo(DEFAULT_PON_INDEX);
        assertThat(testONT.getMaxUp()).isEqualTo(DEFAULT_MAX_UP);
        assertThat(testONT.getMaxDown()).isEqualTo(DEFAULT_MAX_DOWN);
        assertThat(testONT.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testONT.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testONT.getEtatOlt()).isEqualTo(DEFAULT_ETAT_OLT);
        assertThat(testONT.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testONT.getStatusAt()).isEqualTo(DEFAULT_STATUS_AT);
        assertThat(testONT.getNbreLignesCouper()).isEqualTo(DEFAULT_NBRE_LIGNES_COUPER);
    }

    @Test
    @Transactional
    void createONTWithExistingId() throws Exception {
        // Create the ONT with an existing ID
        oNT.setId(1L);
        ONTDTO oNTDTO = oNTMapper.toDto(oNT);

        int databaseSizeBeforeCreate = oNTRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restONTMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(oNTDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ONT in the database
        List<ONT> oNTList = oNTRepository.findAll();
        assertThat(oNTList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIndexIsRequired() throws Exception {
        int databaseSizeBeforeTest = oNTRepository.findAll().size();
        // set the field null
        oNT.setIndex(null);

        // Create the ONT, which fails.
        ONTDTO oNTDTO = oNTMapper.toDto(oNT);

        restONTMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(oNTDTO))
            )
            .andExpect(status().isBadRequest());

        List<ONT> oNTList = oNTRepository.findAll();
        assertThat(oNTList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOntIDIsRequired() throws Exception {
        int databaseSizeBeforeTest = oNTRepository.findAll().size();
        // set the field null
        oNT.setOntID(null);

        // Create the ONT, which fails.
        ONTDTO oNTDTO = oNTMapper.toDto(oNT);

        restONTMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(oNTDTO))
            )
            .andExpect(status().isBadRequest());

        List<ONT> oNTList = oNTRepository.findAll();
        assertThat(oNTList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkServiceIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = oNTRepository.findAll().size();
        // set the field null
        oNT.setServiceId(null);

        // Create the ONT, which fails.
        ONTDTO oNTDTO = oNTMapper.toDto(oNT);

        restONTMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(oNTDTO))
            )
            .andExpect(status().isBadRequest());

        List<ONT> oNTList = oNTRepository.findAll();
        assertThat(oNTList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSlotIsRequired() throws Exception {
        int databaseSizeBeforeTest = oNTRepository.findAll().size();
        // set the field null
        oNT.setSlot(null);

        // Create the ONT, which fails.
        ONTDTO oNTDTO = oNTMapper.toDto(oNT);

        restONTMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(oNTDTO))
            )
            .andExpect(status().isBadRequest());

        List<ONT> oNTList = oNTRepository.findAll();
        assertThat(oNTList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPonIsRequired() throws Exception {
        int databaseSizeBeforeTest = oNTRepository.findAll().size();
        // set the field null
        oNT.setPon(null);

        // Create the ONT, which fails.
        ONTDTO oNTDTO = oNTMapper.toDto(oNT);

        restONTMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(oNTDTO))
            )
            .andExpect(status().isBadRequest());

        List<ONT> oNTList = oNTRepository.findAll();
        assertThat(oNTList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPonIndexIsRequired() throws Exception {
        int databaseSizeBeforeTest = oNTRepository.findAll().size();
        // set the field null
        oNT.setPonIndex(null);

        // Create the ONT, which fails.
        ONTDTO oNTDTO = oNTMapper.toDto(oNT);

        restONTMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(oNTDTO))
            )
            .andExpect(status().isBadRequest());

        List<ONT> oNTList = oNTRepository.findAll();
        assertThat(oNTList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllONTS() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList
        restONTMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(oNT.getId().intValue())))
            .andExpect(jsonPath("$.[*].index").value(hasItem(DEFAULT_INDEX)))
            .andExpect(jsonPath("$.[*].ontID").value(hasItem(DEFAULT_ONT_ID)))
            .andExpect(jsonPath("$.[*].serviceId").value(hasItem(DEFAULT_SERVICE_ID)))
            .andExpect(jsonPath("$.[*].slot").value(hasItem(DEFAULT_SLOT)))
            .andExpect(jsonPath("$.[*].pon").value(hasItem(DEFAULT_PON)))
            .andExpect(jsonPath("$.[*].ponIndex").value(hasItem(DEFAULT_PON_INDEX)))
            .andExpect(jsonPath("$.[*].maxUp").value(hasItem(DEFAULT_MAX_UP)))
            .andExpect(jsonPath("$.[*].maxDown").value(hasItem(DEFAULT_MAX_DOWN)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].etatOlt").value(hasItem(DEFAULT_ETAT_OLT)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].statusAt").value(hasItem(DEFAULT_STATUS_AT.toString())))
            .andExpect(jsonPath("$.[*].nbreLignesCouper").value(hasItem(DEFAULT_NBRE_LIGNES_COUPER.intValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllONTSWithEagerRelationshipsIsEnabled() throws Exception {
        when(oNTServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restONTMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(oNTServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllONTSWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(oNTServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restONTMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(oNTRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getONT() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get the oNT
        restONTMockMvc
            .perform(get(ENTITY_API_URL_ID, oNT.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(oNT.getId().intValue()))
            .andExpect(jsonPath("$.index").value(DEFAULT_INDEX))
            .andExpect(jsonPath("$.ontID").value(DEFAULT_ONT_ID))
            .andExpect(jsonPath("$.serviceId").value(DEFAULT_SERVICE_ID))
            .andExpect(jsonPath("$.slot").value(DEFAULT_SLOT))
            .andExpect(jsonPath("$.pon").value(DEFAULT_PON))
            .andExpect(jsonPath("$.ponIndex").value(DEFAULT_PON_INDEX))
            .andExpect(jsonPath("$.maxUp").value(DEFAULT_MAX_UP))
            .andExpect(jsonPath("$.maxDown").value(DEFAULT_MAX_DOWN))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()))
            .andExpect(jsonPath("$.etatOlt").value(DEFAULT_ETAT_OLT))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.statusAt").value(DEFAULT_STATUS_AT.toString()))
            .andExpect(jsonPath("$.nbreLignesCouper").value(DEFAULT_NBRE_LIGNES_COUPER.intValue()));
    }

    @Test
    @Transactional
    void getONTSByIdFiltering() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        Long id = oNT.getId();

        defaultONTShouldBeFound("id.equals=" + id);
        defaultONTShouldNotBeFound("id.notEquals=" + id);

        defaultONTShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultONTShouldNotBeFound("id.greaterThan=" + id);

        defaultONTShouldBeFound("id.lessThanOrEqual=" + id);
        defaultONTShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllONTSByIndexIsEqualToSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where index equals to DEFAULT_INDEX
        defaultONTShouldBeFound("index.equals=" + DEFAULT_INDEX);

        // Get all the oNTList where index equals to UPDATED_INDEX
        defaultONTShouldNotBeFound("index.equals=" + UPDATED_INDEX);
    }

    @Test
    @Transactional
    void getAllONTSByIndexIsInShouldWork() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where index in DEFAULT_INDEX or UPDATED_INDEX
        defaultONTShouldBeFound("index.in=" + DEFAULT_INDEX + "," + UPDATED_INDEX);

        // Get all the oNTList where index equals to UPDATED_INDEX
        defaultONTShouldNotBeFound("index.in=" + UPDATED_INDEX);
    }

    @Test
    @Transactional
    void getAllONTSByIndexIsNullOrNotNull() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where index is not null
        defaultONTShouldBeFound("index.specified=true");

        // Get all the oNTList where index is null
        defaultONTShouldNotBeFound("index.specified=false");
    }

    @Test
    @Transactional
    void getAllONTSByIndexContainsSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where index contains DEFAULT_INDEX
        defaultONTShouldBeFound("index.contains=" + DEFAULT_INDEX);

        // Get all the oNTList where index contains UPDATED_INDEX
        defaultONTShouldNotBeFound("index.contains=" + UPDATED_INDEX);
    }

    @Test
    @Transactional
    void getAllONTSByIndexNotContainsSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where index does not contain DEFAULT_INDEX
        defaultONTShouldNotBeFound("index.doesNotContain=" + DEFAULT_INDEX);

        // Get all the oNTList where index does not contain UPDATED_INDEX
        defaultONTShouldBeFound("index.doesNotContain=" + UPDATED_INDEX);
    }

    @Test
    @Transactional
    void getAllONTSByOntIDIsEqualToSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where ontID equals to DEFAULT_ONT_ID
        defaultONTShouldBeFound("ontID.equals=" + DEFAULT_ONT_ID);

        // Get all the oNTList where ontID equals to UPDATED_ONT_ID
        defaultONTShouldNotBeFound("ontID.equals=" + UPDATED_ONT_ID);
    }

    @Test
    @Transactional
    void getAllONTSByOntIDIsInShouldWork() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where ontID in DEFAULT_ONT_ID or UPDATED_ONT_ID
        defaultONTShouldBeFound("ontID.in=" + DEFAULT_ONT_ID + "," + UPDATED_ONT_ID);

        // Get all the oNTList where ontID equals to UPDATED_ONT_ID
        defaultONTShouldNotBeFound("ontID.in=" + UPDATED_ONT_ID);
    }

    @Test
    @Transactional
    void getAllONTSByOntIDIsNullOrNotNull() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where ontID is not null
        defaultONTShouldBeFound("ontID.specified=true");

        // Get all the oNTList where ontID is null
        defaultONTShouldNotBeFound("ontID.specified=false");
    }

    @Test
    @Transactional
    void getAllONTSByOntIDContainsSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where ontID contains DEFAULT_ONT_ID
        defaultONTShouldBeFound("ontID.contains=" + DEFAULT_ONT_ID);

        // Get all the oNTList where ontID contains UPDATED_ONT_ID
        defaultONTShouldNotBeFound("ontID.contains=" + UPDATED_ONT_ID);
    }

    @Test
    @Transactional
    void getAllONTSByOntIDNotContainsSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where ontID does not contain DEFAULT_ONT_ID
        defaultONTShouldNotBeFound("ontID.doesNotContain=" + DEFAULT_ONT_ID);

        // Get all the oNTList where ontID does not contain UPDATED_ONT_ID
        defaultONTShouldBeFound("ontID.doesNotContain=" + UPDATED_ONT_ID);
    }

    @Test
    @Transactional
    void getAllONTSByServiceIdIsEqualToSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where serviceId equals to DEFAULT_SERVICE_ID
        defaultONTShouldBeFound("serviceId.equals=" + DEFAULT_SERVICE_ID);

        // Get all the oNTList where serviceId equals to UPDATED_SERVICE_ID
        defaultONTShouldNotBeFound("serviceId.equals=" + UPDATED_SERVICE_ID);
    }

    @Test
    @Transactional
    void getAllONTSByServiceIdIsInShouldWork() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where serviceId in DEFAULT_SERVICE_ID or UPDATED_SERVICE_ID
        defaultONTShouldBeFound("serviceId.in=" + DEFAULT_SERVICE_ID + "," + UPDATED_SERVICE_ID);

        // Get all the oNTList where serviceId equals to UPDATED_SERVICE_ID
        defaultONTShouldNotBeFound("serviceId.in=" + UPDATED_SERVICE_ID);
    }

    @Test
    @Transactional
    void getAllONTSByServiceIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where serviceId is not null
        defaultONTShouldBeFound("serviceId.specified=true");

        // Get all the oNTList where serviceId is null
        defaultONTShouldNotBeFound("serviceId.specified=false");
    }

    @Test
    @Transactional
    void getAllONTSByServiceIdContainsSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where serviceId contains DEFAULT_SERVICE_ID
        defaultONTShouldBeFound("serviceId.contains=" + DEFAULT_SERVICE_ID);

        // Get all the oNTList where serviceId contains UPDATED_SERVICE_ID
        defaultONTShouldNotBeFound("serviceId.contains=" + UPDATED_SERVICE_ID);
    }

    @Test
    @Transactional
    void getAllONTSByServiceIdNotContainsSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where serviceId does not contain DEFAULT_SERVICE_ID
        defaultONTShouldNotBeFound("serviceId.doesNotContain=" + DEFAULT_SERVICE_ID);

        // Get all the oNTList where serviceId does not contain UPDATED_SERVICE_ID
        defaultONTShouldBeFound("serviceId.doesNotContain=" + UPDATED_SERVICE_ID);
    }

    @Test
    @Transactional
    void getAllONTSBySlotIsEqualToSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where slot equals to DEFAULT_SLOT
        defaultONTShouldBeFound("slot.equals=" + DEFAULT_SLOT);

        // Get all the oNTList where slot equals to UPDATED_SLOT
        defaultONTShouldNotBeFound("slot.equals=" + UPDATED_SLOT);
    }

    @Test
    @Transactional
    void getAllONTSBySlotIsInShouldWork() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where slot in DEFAULT_SLOT or UPDATED_SLOT
        defaultONTShouldBeFound("slot.in=" + DEFAULT_SLOT + "," + UPDATED_SLOT);

        // Get all the oNTList where slot equals to UPDATED_SLOT
        defaultONTShouldNotBeFound("slot.in=" + UPDATED_SLOT);
    }

    @Test
    @Transactional
    void getAllONTSBySlotIsNullOrNotNull() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where slot is not null
        defaultONTShouldBeFound("slot.specified=true");

        // Get all the oNTList where slot is null
        defaultONTShouldNotBeFound("slot.specified=false");
    }

    @Test
    @Transactional
    void getAllONTSBySlotContainsSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where slot contains DEFAULT_SLOT
        defaultONTShouldBeFound("slot.contains=" + DEFAULT_SLOT);

        // Get all the oNTList where slot contains UPDATED_SLOT
        defaultONTShouldNotBeFound("slot.contains=" + UPDATED_SLOT);
    }

    @Test
    @Transactional
    void getAllONTSBySlotNotContainsSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where slot does not contain DEFAULT_SLOT
        defaultONTShouldNotBeFound("slot.doesNotContain=" + DEFAULT_SLOT);

        // Get all the oNTList where slot does not contain UPDATED_SLOT
        defaultONTShouldBeFound("slot.doesNotContain=" + UPDATED_SLOT);
    }

    @Test
    @Transactional
    void getAllONTSByPonIsEqualToSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where pon equals to DEFAULT_PON
        defaultONTShouldBeFound("pon.equals=" + DEFAULT_PON);

        // Get all the oNTList where pon equals to UPDATED_PON
        defaultONTShouldNotBeFound("pon.equals=" + UPDATED_PON);
    }

    @Test
    @Transactional
    void getAllONTSByPonIsInShouldWork() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where pon in DEFAULT_PON or UPDATED_PON
        defaultONTShouldBeFound("pon.in=" + DEFAULT_PON + "," + UPDATED_PON);

        // Get all the oNTList where pon equals to UPDATED_PON
        defaultONTShouldNotBeFound("pon.in=" + UPDATED_PON);
    }

    @Test
    @Transactional
    void getAllONTSByPonIsNullOrNotNull() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where pon is not null
        defaultONTShouldBeFound("pon.specified=true");

        // Get all the oNTList where pon is null
        defaultONTShouldNotBeFound("pon.specified=false");
    }

    @Test
    @Transactional
    void getAllONTSByPonContainsSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where pon contains DEFAULT_PON
        defaultONTShouldBeFound("pon.contains=" + DEFAULT_PON);

        // Get all the oNTList where pon contains UPDATED_PON
        defaultONTShouldNotBeFound("pon.contains=" + UPDATED_PON);
    }

    @Test
    @Transactional
    void getAllONTSByPonNotContainsSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where pon does not contain DEFAULT_PON
        defaultONTShouldNotBeFound("pon.doesNotContain=" + DEFAULT_PON);

        // Get all the oNTList where pon does not contain UPDATED_PON
        defaultONTShouldBeFound("pon.doesNotContain=" + UPDATED_PON);
    }

    @Test
    @Transactional
    void getAllONTSByPonIndexIsEqualToSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where ponIndex equals to DEFAULT_PON_INDEX
        defaultONTShouldBeFound("ponIndex.equals=" + DEFAULT_PON_INDEX);

        // Get all the oNTList where ponIndex equals to UPDATED_PON_INDEX
        defaultONTShouldNotBeFound("ponIndex.equals=" + UPDATED_PON_INDEX);
    }

    @Test
    @Transactional
    void getAllONTSByPonIndexIsInShouldWork() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where ponIndex in DEFAULT_PON_INDEX or UPDATED_PON_INDEX
        defaultONTShouldBeFound("ponIndex.in=" + DEFAULT_PON_INDEX + "," + UPDATED_PON_INDEX);

        // Get all the oNTList where ponIndex equals to UPDATED_PON_INDEX
        defaultONTShouldNotBeFound("ponIndex.in=" + UPDATED_PON_INDEX);
    }

    @Test
    @Transactional
    void getAllONTSByPonIndexIsNullOrNotNull() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where ponIndex is not null
        defaultONTShouldBeFound("ponIndex.specified=true");

        // Get all the oNTList where ponIndex is null
        defaultONTShouldNotBeFound("ponIndex.specified=false");
    }

    @Test
    @Transactional
    void getAllONTSByPonIndexContainsSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where ponIndex contains DEFAULT_PON_INDEX
        defaultONTShouldBeFound("ponIndex.contains=" + DEFAULT_PON_INDEX);

        // Get all the oNTList where ponIndex contains UPDATED_PON_INDEX
        defaultONTShouldNotBeFound("ponIndex.contains=" + UPDATED_PON_INDEX);
    }

    @Test
    @Transactional
    void getAllONTSByPonIndexNotContainsSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where ponIndex does not contain DEFAULT_PON_INDEX
        defaultONTShouldNotBeFound("ponIndex.doesNotContain=" + DEFAULT_PON_INDEX);

        // Get all the oNTList where ponIndex does not contain UPDATED_PON_INDEX
        defaultONTShouldBeFound("ponIndex.doesNotContain=" + UPDATED_PON_INDEX);
    }

    @Test
    @Transactional
    void getAllONTSByMaxUpIsEqualToSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where maxUp equals to DEFAULT_MAX_UP
        defaultONTShouldBeFound("maxUp.equals=" + DEFAULT_MAX_UP);

        // Get all the oNTList where maxUp equals to UPDATED_MAX_UP
        defaultONTShouldNotBeFound("maxUp.equals=" + UPDATED_MAX_UP);
    }

    @Test
    @Transactional
    void getAllONTSByMaxUpIsInShouldWork() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where maxUp in DEFAULT_MAX_UP or UPDATED_MAX_UP
        defaultONTShouldBeFound("maxUp.in=" + DEFAULT_MAX_UP + "," + UPDATED_MAX_UP);

        // Get all the oNTList where maxUp equals to UPDATED_MAX_UP
        defaultONTShouldNotBeFound("maxUp.in=" + UPDATED_MAX_UP);
    }

    @Test
    @Transactional
    void getAllONTSByMaxUpIsNullOrNotNull() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where maxUp is not null
        defaultONTShouldBeFound("maxUp.specified=true");

        // Get all the oNTList where maxUp is null
        defaultONTShouldNotBeFound("maxUp.specified=false");
    }

    @Test
    @Transactional
    void getAllONTSByMaxUpContainsSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where maxUp contains DEFAULT_MAX_UP
        defaultONTShouldBeFound("maxUp.contains=" + DEFAULT_MAX_UP);

        // Get all the oNTList where maxUp contains UPDATED_MAX_UP
        defaultONTShouldNotBeFound("maxUp.contains=" + UPDATED_MAX_UP);
    }

    @Test
    @Transactional
    void getAllONTSByMaxUpNotContainsSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where maxUp does not contain DEFAULT_MAX_UP
        defaultONTShouldNotBeFound("maxUp.doesNotContain=" + DEFAULT_MAX_UP);

        // Get all the oNTList where maxUp does not contain UPDATED_MAX_UP
        defaultONTShouldBeFound("maxUp.doesNotContain=" + UPDATED_MAX_UP);
    }

    @Test
    @Transactional
    void getAllONTSByMaxDownIsEqualToSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where maxDown equals to DEFAULT_MAX_DOWN
        defaultONTShouldBeFound("maxDown.equals=" + DEFAULT_MAX_DOWN);

        // Get all the oNTList where maxDown equals to UPDATED_MAX_DOWN
        defaultONTShouldNotBeFound("maxDown.equals=" + UPDATED_MAX_DOWN);
    }

    @Test
    @Transactional
    void getAllONTSByMaxDownIsInShouldWork() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where maxDown in DEFAULT_MAX_DOWN or UPDATED_MAX_DOWN
        defaultONTShouldBeFound("maxDown.in=" + DEFAULT_MAX_DOWN + "," + UPDATED_MAX_DOWN);

        // Get all the oNTList where maxDown equals to UPDATED_MAX_DOWN
        defaultONTShouldNotBeFound("maxDown.in=" + UPDATED_MAX_DOWN);
    }

    @Test
    @Transactional
    void getAllONTSByMaxDownIsNullOrNotNull() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where maxDown is not null
        defaultONTShouldBeFound("maxDown.specified=true");

        // Get all the oNTList where maxDown is null
        defaultONTShouldNotBeFound("maxDown.specified=false");
    }

    @Test
    @Transactional
    void getAllONTSByMaxDownContainsSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where maxDown contains DEFAULT_MAX_DOWN
        defaultONTShouldBeFound("maxDown.contains=" + DEFAULT_MAX_DOWN);

        // Get all the oNTList where maxDown contains UPDATED_MAX_DOWN
        defaultONTShouldNotBeFound("maxDown.contains=" + UPDATED_MAX_DOWN);
    }

    @Test
    @Transactional
    void getAllONTSByMaxDownNotContainsSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where maxDown does not contain DEFAULT_MAX_DOWN
        defaultONTShouldNotBeFound("maxDown.doesNotContain=" + DEFAULT_MAX_DOWN);

        // Get all the oNTList where maxDown does not contain UPDATED_MAX_DOWN
        defaultONTShouldBeFound("maxDown.doesNotContain=" + UPDATED_MAX_DOWN);
    }

    @Test
    @Transactional
    void getAllONTSByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where createdAt equals to DEFAULT_CREATED_AT
        defaultONTShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the oNTList where createdAt equals to UPDATED_CREATED_AT
        defaultONTShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllONTSByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultONTShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the oNTList where createdAt equals to UPDATED_CREATED_AT
        defaultONTShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllONTSByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where createdAt is not null
        defaultONTShouldBeFound("createdAt.specified=true");

        // Get all the oNTList where createdAt is null
        defaultONTShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllONTSByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where createdAt is greater than or equal to DEFAULT_CREATED_AT
        defaultONTShouldBeFound("createdAt.greaterThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the oNTList where createdAt is greater than or equal to UPDATED_CREATED_AT
        defaultONTShouldNotBeFound("createdAt.greaterThanOrEqual=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllONTSByCreatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where createdAt is less than or equal to DEFAULT_CREATED_AT
        defaultONTShouldBeFound("createdAt.lessThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the oNTList where createdAt is less than or equal to SMALLER_CREATED_AT
        defaultONTShouldNotBeFound("createdAt.lessThanOrEqual=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllONTSByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where createdAt is less than DEFAULT_CREATED_AT
        defaultONTShouldNotBeFound("createdAt.lessThan=" + DEFAULT_CREATED_AT);

        // Get all the oNTList where createdAt is less than UPDATED_CREATED_AT
        defaultONTShouldBeFound("createdAt.lessThan=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllONTSByCreatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where createdAt is greater than DEFAULT_CREATED_AT
        defaultONTShouldNotBeFound("createdAt.greaterThan=" + DEFAULT_CREATED_AT);

        // Get all the oNTList where createdAt is greater than SMALLER_CREATED_AT
        defaultONTShouldBeFound("createdAt.greaterThan=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllONTSByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultONTShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the oNTList where updatedAt equals to UPDATED_UPDATED_AT
        defaultONTShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllONTSByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultONTShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the oNTList where updatedAt equals to UPDATED_UPDATED_AT
        defaultONTShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllONTSByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where updatedAt is not null
        defaultONTShouldBeFound("updatedAt.specified=true");

        // Get all the oNTList where updatedAt is null
        defaultONTShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllONTSByUpdatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where updatedAt is greater than or equal to DEFAULT_UPDATED_AT
        defaultONTShouldBeFound("updatedAt.greaterThanOrEqual=" + DEFAULT_UPDATED_AT);

        // Get all the oNTList where updatedAt is greater than or equal to UPDATED_UPDATED_AT
        defaultONTShouldNotBeFound("updatedAt.greaterThanOrEqual=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllONTSByUpdatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where updatedAt is less than or equal to DEFAULT_UPDATED_AT
        defaultONTShouldBeFound("updatedAt.lessThanOrEqual=" + DEFAULT_UPDATED_AT);

        // Get all the oNTList where updatedAt is less than or equal to SMALLER_UPDATED_AT
        defaultONTShouldNotBeFound("updatedAt.lessThanOrEqual=" + SMALLER_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllONTSByUpdatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where updatedAt is less than DEFAULT_UPDATED_AT
        defaultONTShouldNotBeFound("updatedAt.lessThan=" + DEFAULT_UPDATED_AT);

        // Get all the oNTList where updatedAt is less than UPDATED_UPDATED_AT
        defaultONTShouldBeFound("updatedAt.lessThan=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllONTSByUpdatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where updatedAt is greater than DEFAULT_UPDATED_AT
        defaultONTShouldNotBeFound("updatedAt.greaterThan=" + DEFAULT_UPDATED_AT);

        // Get all the oNTList where updatedAt is greater than SMALLER_UPDATED_AT
        defaultONTShouldBeFound("updatedAt.greaterThan=" + SMALLER_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllONTSByEtatOltIsEqualToSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where etatOlt equals to DEFAULT_ETAT_OLT
        defaultONTShouldBeFound("etatOlt.equals=" + DEFAULT_ETAT_OLT);

        // Get all the oNTList where etatOlt equals to UPDATED_ETAT_OLT
        defaultONTShouldNotBeFound("etatOlt.equals=" + UPDATED_ETAT_OLT);
    }

    @Test
    @Transactional
    void getAllONTSByEtatOltIsInShouldWork() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where etatOlt in DEFAULT_ETAT_OLT or UPDATED_ETAT_OLT
        defaultONTShouldBeFound("etatOlt.in=" + DEFAULT_ETAT_OLT + "," + UPDATED_ETAT_OLT);

        // Get all the oNTList where etatOlt equals to UPDATED_ETAT_OLT
        defaultONTShouldNotBeFound("etatOlt.in=" + UPDATED_ETAT_OLT);
    }

    @Test
    @Transactional
    void getAllONTSByEtatOltIsNullOrNotNull() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where etatOlt is not null
        defaultONTShouldBeFound("etatOlt.specified=true");

        // Get all the oNTList where etatOlt is null
        defaultONTShouldNotBeFound("etatOlt.specified=false");
    }

    @Test
    @Transactional
    void getAllONTSByEtatOltContainsSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where etatOlt contains DEFAULT_ETAT_OLT
        defaultONTShouldBeFound("etatOlt.contains=" + DEFAULT_ETAT_OLT);

        // Get all the oNTList where etatOlt contains UPDATED_ETAT_OLT
        defaultONTShouldNotBeFound("etatOlt.contains=" + UPDATED_ETAT_OLT);
    }

    @Test
    @Transactional
    void getAllONTSByEtatOltNotContainsSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where etatOlt does not contain DEFAULT_ETAT_OLT
        defaultONTShouldNotBeFound("etatOlt.doesNotContain=" + DEFAULT_ETAT_OLT);

        // Get all the oNTList where etatOlt does not contain UPDATED_ETAT_OLT
        defaultONTShouldBeFound("etatOlt.doesNotContain=" + UPDATED_ETAT_OLT);
    }

    @Test
    @Transactional
    void getAllONTSByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where status equals to DEFAULT_STATUS
        defaultONTShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the oNTList where status equals to UPDATED_STATUS
        defaultONTShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllONTSByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultONTShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the oNTList where status equals to UPDATED_STATUS
        defaultONTShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllONTSByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where status is not null
        defaultONTShouldBeFound("status.specified=true");

        // Get all the oNTList where status is null
        defaultONTShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllONTSByStatusContainsSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where status contains DEFAULT_STATUS
        defaultONTShouldBeFound("status.contains=" + DEFAULT_STATUS);

        // Get all the oNTList where status contains UPDATED_STATUS
        defaultONTShouldNotBeFound("status.contains=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllONTSByStatusNotContainsSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where status does not contain DEFAULT_STATUS
        defaultONTShouldNotBeFound("status.doesNotContain=" + DEFAULT_STATUS);

        // Get all the oNTList where status does not contain UPDATED_STATUS
        defaultONTShouldBeFound("status.doesNotContain=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllONTSByStatusAtIsEqualToSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where statusAt equals to DEFAULT_STATUS_AT
        defaultONTShouldBeFound("statusAt.equals=" + DEFAULT_STATUS_AT);

        // Get all the oNTList where statusAt equals to UPDATED_STATUS_AT
        defaultONTShouldNotBeFound("statusAt.equals=" + UPDATED_STATUS_AT);
    }

    @Test
    @Transactional
    void getAllONTSByStatusAtIsInShouldWork() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where statusAt in DEFAULT_STATUS_AT or UPDATED_STATUS_AT
        defaultONTShouldBeFound("statusAt.in=" + DEFAULT_STATUS_AT + "," + UPDATED_STATUS_AT);

        // Get all the oNTList where statusAt equals to UPDATED_STATUS_AT
        defaultONTShouldNotBeFound("statusAt.in=" + UPDATED_STATUS_AT);
    }

    @Test
    @Transactional
    void getAllONTSByStatusAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where statusAt is not null
        defaultONTShouldBeFound("statusAt.specified=true");

        // Get all the oNTList where statusAt is null
        defaultONTShouldNotBeFound("statusAt.specified=false");
    }

    @Test
    @Transactional
    void getAllONTSByStatusAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where statusAt is greater than or equal to DEFAULT_STATUS_AT
        defaultONTShouldBeFound("statusAt.greaterThanOrEqual=" + DEFAULT_STATUS_AT);

        // Get all the oNTList where statusAt is greater than or equal to UPDATED_STATUS_AT
        defaultONTShouldNotBeFound("statusAt.greaterThanOrEqual=" + UPDATED_STATUS_AT);
    }

    @Test
    @Transactional
    void getAllONTSByStatusAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where statusAt is less than or equal to DEFAULT_STATUS_AT
        defaultONTShouldBeFound("statusAt.lessThanOrEqual=" + DEFAULT_STATUS_AT);

        // Get all the oNTList where statusAt is less than or equal to SMALLER_STATUS_AT
        defaultONTShouldNotBeFound("statusAt.lessThanOrEqual=" + SMALLER_STATUS_AT);
    }

    @Test
    @Transactional
    void getAllONTSByStatusAtIsLessThanSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where statusAt is less than DEFAULT_STATUS_AT
        defaultONTShouldNotBeFound("statusAt.lessThan=" + DEFAULT_STATUS_AT);

        // Get all the oNTList where statusAt is less than UPDATED_STATUS_AT
        defaultONTShouldBeFound("statusAt.lessThan=" + UPDATED_STATUS_AT);
    }

    @Test
    @Transactional
    void getAllONTSByStatusAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where statusAt is greater than DEFAULT_STATUS_AT
        defaultONTShouldNotBeFound("statusAt.greaterThan=" + DEFAULT_STATUS_AT);

        // Get all the oNTList where statusAt is greater than SMALLER_STATUS_AT
        defaultONTShouldBeFound("statusAt.greaterThan=" + SMALLER_STATUS_AT);
    }

    @Test
    @Transactional
    void getAllONTSByNbreLignesCouperIsEqualToSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where nbreLignesCouper equals to DEFAULT_NBRE_LIGNES_COUPER
        defaultONTShouldBeFound("nbreLignesCouper.equals=" + DEFAULT_NBRE_LIGNES_COUPER);

        // Get all the oNTList where nbreLignesCouper equals to UPDATED_NBRE_LIGNES_COUPER
        defaultONTShouldNotBeFound("nbreLignesCouper.equals=" + UPDATED_NBRE_LIGNES_COUPER);
    }

    @Test
    @Transactional
    void getAllONTSByNbreLignesCouperIsInShouldWork() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where nbreLignesCouper in DEFAULT_NBRE_LIGNES_COUPER or UPDATED_NBRE_LIGNES_COUPER
        defaultONTShouldBeFound("nbreLignesCouper.in=" + DEFAULT_NBRE_LIGNES_COUPER + "," + UPDATED_NBRE_LIGNES_COUPER);

        // Get all the oNTList where nbreLignesCouper equals to UPDATED_NBRE_LIGNES_COUPER
        defaultONTShouldNotBeFound("nbreLignesCouper.in=" + UPDATED_NBRE_LIGNES_COUPER);
    }

    @Test
    @Transactional
    void getAllONTSByNbreLignesCouperIsNullOrNotNull() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where nbreLignesCouper is not null
        defaultONTShouldBeFound("nbreLignesCouper.specified=true");

        // Get all the oNTList where nbreLignesCouper is null
        defaultONTShouldNotBeFound("nbreLignesCouper.specified=false");
    }

    @Test
    @Transactional
    void getAllONTSByNbreLignesCouperIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where nbreLignesCouper is greater than or equal to DEFAULT_NBRE_LIGNES_COUPER
        defaultONTShouldBeFound("nbreLignesCouper.greaterThanOrEqual=" + DEFAULT_NBRE_LIGNES_COUPER);

        // Get all the oNTList where nbreLignesCouper is greater than or equal to UPDATED_NBRE_LIGNES_COUPER
        defaultONTShouldNotBeFound("nbreLignesCouper.greaterThanOrEqual=" + UPDATED_NBRE_LIGNES_COUPER);
    }

    @Test
    @Transactional
    void getAllONTSByNbreLignesCouperIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where nbreLignesCouper is less than or equal to DEFAULT_NBRE_LIGNES_COUPER
        defaultONTShouldBeFound("nbreLignesCouper.lessThanOrEqual=" + DEFAULT_NBRE_LIGNES_COUPER);

        // Get all the oNTList where nbreLignesCouper is less than or equal to SMALLER_NBRE_LIGNES_COUPER
        defaultONTShouldNotBeFound("nbreLignesCouper.lessThanOrEqual=" + SMALLER_NBRE_LIGNES_COUPER);
    }

    @Test
    @Transactional
    void getAllONTSByNbreLignesCouperIsLessThanSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where nbreLignesCouper is less than DEFAULT_NBRE_LIGNES_COUPER
        defaultONTShouldNotBeFound("nbreLignesCouper.lessThan=" + DEFAULT_NBRE_LIGNES_COUPER);

        // Get all the oNTList where nbreLignesCouper is less than UPDATED_NBRE_LIGNES_COUPER
        defaultONTShouldBeFound("nbreLignesCouper.lessThan=" + UPDATED_NBRE_LIGNES_COUPER);
    }

    @Test
    @Transactional
    void getAllONTSByNbreLignesCouperIsGreaterThanSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where nbreLignesCouper is greater than DEFAULT_NBRE_LIGNES_COUPER
        defaultONTShouldNotBeFound("nbreLignesCouper.greaterThan=" + DEFAULT_NBRE_LIGNES_COUPER);

        // Get all the oNTList where nbreLignesCouper is greater than SMALLER_NBRE_LIGNES_COUPER
        defaultONTShouldBeFound("nbreLignesCouper.greaterThan=" + SMALLER_NBRE_LIGNES_COUPER);
    }

    @Test
    @Transactional
    void getAllONTSByClientIsEqualToSomething() throws Exception {
        Client client;
        if (TestUtil.findAll(em, Client.class).isEmpty()) {
            oNTRepository.saveAndFlush(oNT);
            client = ClientResourceIT.createEntity(em);
        } else {
            client = TestUtil.findAll(em, Client.class).get(0);
        }
        em.persist(client);
        em.flush();
        oNT.setClient(client);
        oNTRepository.saveAndFlush(oNT);
        Long clientId = client.getId();
        // Get all the oNTList where client equals to clientId
        defaultONTShouldBeFound("clientId.equals=" + clientId);

        // Get all the oNTList where client equals to (clientId + 1)
        defaultONTShouldNotBeFound("clientId.equals=" + (clientId + 1));
    }

    @Test
    @Transactional
    void getAllONTSByOltIsEqualToSomething() throws Exception {
        OLT olt;
        if (TestUtil.findAll(em, OLT.class).isEmpty()) {
            oNTRepository.saveAndFlush(oNT);
            olt = OLTResourceIT.createEntity(em);
        } else {
            olt = TestUtil.findAll(em, OLT.class).get(0);
        }
        em.persist(olt);
        em.flush();
        oNT.setOlt(olt);
        oNTRepository.saveAndFlush(oNT);
        Long oltId = olt.getId();
        // Get all the oNTList where olt equals to oltId
        defaultONTShouldBeFound("oltId.equals=" + oltId);

        // Get all the oNTList where olt equals to (oltId + 1)
        defaultONTShouldNotBeFound("oltId.equals=" + (oltId + 1));
    }

    @Test
    @Transactional
    void getAllONTSByDiagnosticIsEqualToSomething() throws Exception {
        Diagnostic diagnostic;
        if (TestUtil.findAll(em, Diagnostic.class).isEmpty()) {
            oNTRepository.saveAndFlush(oNT);
            diagnostic = DiagnosticResourceIT.createEntity(em);
        } else {
            diagnostic = TestUtil.findAll(em, Diagnostic.class).get(0);
        }
        em.persist(diagnostic);
        em.flush();
        oNT.addDiagnostic(diagnostic);
        oNTRepository.saveAndFlush(oNT);
        Long diagnosticId = diagnostic.getId();
        // Get all the oNTList where diagnostic equals to diagnosticId
        defaultONTShouldBeFound("diagnosticId.equals=" + diagnosticId);

        // Get all the oNTList where diagnostic equals to (diagnosticId + 1)
        defaultONTShouldNotBeFound("diagnosticId.equals=" + (diagnosticId + 1));
    }

    @Test
    @Transactional
    void getAllONTSByMetriqueIsEqualToSomething() throws Exception {
        Metrique metrique;
        if (TestUtil.findAll(em, Metrique.class).isEmpty()) {
            oNTRepository.saveAndFlush(oNT);
            metrique = MetriqueResourceIT.createEntity(em);
        } else {
            metrique = TestUtil.findAll(em, Metrique.class).get(0);
        }
        em.persist(metrique);
        em.flush();
        oNT.addMetrique(metrique);
        oNTRepository.saveAndFlush(oNT);
        Long metriqueId = metrique.getId();
        // Get all the oNTList where metrique equals to metriqueId
        defaultONTShouldBeFound("metriqueId.equals=" + metriqueId);

        // Get all the oNTList where metrique equals to (metriqueId + 1)
        defaultONTShouldNotBeFound("metriqueId.equals=" + (metriqueId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultONTShouldBeFound(String filter) throws Exception {
        restONTMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(oNT.getId().intValue())))
            .andExpect(jsonPath("$.[*].index").value(hasItem(DEFAULT_INDEX)))
            .andExpect(jsonPath("$.[*].ontID").value(hasItem(DEFAULT_ONT_ID)))
            .andExpect(jsonPath("$.[*].serviceId").value(hasItem(DEFAULT_SERVICE_ID)))
            .andExpect(jsonPath("$.[*].slot").value(hasItem(DEFAULT_SLOT)))
            .andExpect(jsonPath("$.[*].pon").value(hasItem(DEFAULT_PON)))
            .andExpect(jsonPath("$.[*].ponIndex").value(hasItem(DEFAULT_PON_INDEX)))
            .andExpect(jsonPath("$.[*].maxUp").value(hasItem(DEFAULT_MAX_UP)))
            .andExpect(jsonPath("$.[*].maxDown").value(hasItem(DEFAULT_MAX_DOWN)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].etatOlt").value(hasItem(DEFAULT_ETAT_OLT)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].statusAt").value(hasItem(DEFAULT_STATUS_AT.toString())))
            .andExpect(jsonPath("$.[*].nbreLignesCouper").value(hasItem(DEFAULT_NBRE_LIGNES_COUPER.intValue())));

        // Check, that the count call also returns 1
        restONTMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultONTShouldNotBeFound(String filter) throws Exception {
        restONTMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restONTMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingONT() throws Exception {
        // Get the oNT
        restONTMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingONT() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        int databaseSizeBeforeUpdate = oNTRepository.findAll().size();

        // Update the oNT
        ONT updatedONT = oNTRepository.findById(oNT.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedONT are not directly saved in db
        em.detach(updatedONT);
        updatedONT
            .index(UPDATED_INDEX)
            .ontID(UPDATED_ONT_ID)
            .serviceId(UPDATED_SERVICE_ID)
            .slot(UPDATED_SLOT)
            .pon(UPDATED_PON)
            .ponIndex(UPDATED_PON_INDEX)
            .maxUp(UPDATED_MAX_UP)
            .maxDown(UPDATED_MAX_DOWN)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .etatOlt(UPDATED_ETAT_OLT)
            .status(UPDATED_STATUS)
            .statusAt(UPDATED_STATUS_AT)
            .nbreLignesCouper(UPDATED_NBRE_LIGNES_COUPER);
        ONTDTO oNTDTO = oNTMapper.toDto(updatedONT);

        restONTMockMvc
            .perform(
                put(ENTITY_API_URL_ID, oNTDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(oNTDTO))
            )
            .andExpect(status().isOk());

        // Validate the ONT in the database
        List<ONT> oNTList = oNTRepository.findAll();
        assertThat(oNTList).hasSize(databaseSizeBeforeUpdate);
        ONT testONT = oNTList.get(oNTList.size() - 1);
        assertThat(testONT.getIndex()).isEqualTo(UPDATED_INDEX);
        assertThat(testONT.getOntID()).isEqualTo(UPDATED_ONT_ID);
        assertThat(testONT.getServiceId()).isEqualTo(UPDATED_SERVICE_ID);
        assertThat(testONT.getSlot()).isEqualTo(UPDATED_SLOT);
        assertThat(testONT.getPon()).isEqualTo(UPDATED_PON);
        assertThat(testONT.getPonIndex()).isEqualTo(UPDATED_PON_INDEX);
        assertThat(testONT.getMaxUp()).isEqualTo(UPDATED_MAX_UP);
        assertThat(testONT.getMaxDown()).isEqualTo(UPDATED_MAX_DOWN);
        assertThat(testONT.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testONT.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testONT.getEtatOlt()).isEqualTo(UPDATED_ETAT_OLT);
        assertThat(testONT.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testONT.getStatusAt()).isEqualTo(UPDATED_STATUS_AT);
        assertThat(testONT.getNbreLignesCouper()).isEqualTo(UPDATED_NBRE_LIGNES_COUPER);
    }

    @Test
    @Transactional
    void putNonExistingONT() throws Exception {
        int databaseSizeBeforeUpdate = oNTRepository.findAll().size();
        oNT.setId(longCount.incrementAndGet());

        // Create the ONT
        ONTDTO oNTDTO = oNTMapper.toDto(oNT);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restONTMockMvc
            .perform(
                put(ENTITY_API_URL_ID, oNTDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(oNTDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ONT in the database
        List<ONT> oNTList = oNTRepository.findAll();
        assertThat(oNTList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchONT() throws Exception {
        int databaseSizeBeforeUpdate = oNTRepository.findAll().size();
        oNT.setId(longCount.incrementAndGet());

        // Create the ONT
        ONTDTO oNTDTO = oNTMapper.toDto(oNT);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restONTMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(oNTDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ONT in the database
        List<ONT> oNTList = oNTRepository.findAll();
        assertThat(oNTList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamONT() throws Exception {
        int databaseSizeBeforeUpdate = oNTRepository.findAll().size();
        oNT.setId(longCount.incrementAndGet());

        // Create the ONT
        ONTDTO oNTDTO = oNTMapper.toDto(oNT);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restONTMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(oNTDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ONT in the database
        List<ONT> oNTList = oNTRepository.findAll();
        assertThat(oNTList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateONTWithPatch() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        int databaseSizeBeforeUpdate = oNTRepository.findAll().size();

        // Update the oNT using partial update
        ONT partialUpdatedONT = new ONT();
        partialUpdatedONT.setId(oNT.getId());

        partialUpdatedONT
            .ponIndex(UPDATED_PON_INDEX)
            .updatedAt(UPDATED_UPDATED_AT)
            .etatOlt(UPDATED_ETAT_OLT)
            .statusAt(UPDATED_STATUS_AT)
            .nbreLignesCouper(UPDATED_NBRE_LIGNES_COUPER);

        restONTMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedONT.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedONT))
            )
            .andExpect(status().isOk());

        // Validate the ONT in the database
        List<ONT> oNTList = oNTRepository.findAll();
        assertThat(oNTList).hasSize(databaseSizeBeforeUpdate);
        ONT testONT = oNTList.get(oNTList.size() - 1);
        assertThat(testONT.getIndex()).isEqualTo(DEFAULT_INDEX);
        assertThat(testONT.getOntID()).isEqualTo(DEFAULT_ONT_ID);
        assertThat(testONT.getServiceId()).isEqualTo(DEFAULT_SERVICE_ID);
        assertThat(testONT.getSlot()).isEqualTo(DEFAULT_SLOT);
        assertThat(testONT.getPon()).isEqualTo(DEFAULT_PON);
        assertThat(testONT.getPonIndex()).isEqualTo(UPDATED_PON_INDEX);
        assertThat(testONT.getMaxUp()).isEqualTo(DEFAULT_MAX_UP);
        assertThat(testONT.getMaxDown()).isEqualTo(DEFAULT_MAX_DOWN);
        assertThat(testONT.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testONT.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testONT.getEtatOlt()).isEqualTo(UPDATED_ETAT_OLT);
        assertThat(testONT.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testONT.getStatusAt()).isEqualTo(UPDATED_STATUS_AT);
        assertThat(testONT.getNbreLignesCouper()).isEqualTo(UPDATED_NBRE_LIGNES_COUPER);
    }

    @Test
    @Transactional
    void fullUpdateONTWithPatch() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        int databaseSizeBeforeUpdate = oNTRepository.findAll().size();

        // Update the oNT using partial update
        ONT partialUpdatedONT = new ONT();
        partialUpdatedONT.setId(oNT.getId());

        partialUpdatedONT
            .index(UPDATED_INDEX)
            .ontID(UPDATED_ONT_ID)
            .serviceId(UPDATED_SERVICE_ID)
            .slot(UPDATED_SLOT)
            .pon(UPDATED_PON)
            .ponIndex(UPDATED_PON_INDEX)
            .maxUp(UPDATED_MAX_UP)
            .maxDown(UPDATED_MAX_DOWN)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .etatOlt(UPDATED_ETAT_OLT)
            .status(UPDATED_STATUS)
            .statusAt(UPDATED_STATUS_AT)
            .nbreLignesCouper(UPDATED_NBRE_LIGNES_COUPER);

        restONTMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedONT.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedONT))
            )
            .andExpect(status().isOk());

        // Validate the ONT in the database
        List<ONT> oNTList = oNTRepository.findAll();
        assertThat(oNTList).hasSize(databaseSizeBeforeUpdate);
        ONT testONT = oNTList.get(oNTList.size() - 1);
        assertThat(testONT.getIndex()).isEqualTo(UPDATED_INDEX);
        assertThat(testONT.getOntID()).isEqualTo(UPDATED_ONT_ID);
        assertThat(testONT.getServiceId()).isEqualTo(UPDATED_SERVICE_ID);
        assertThat(testONT.getSlot()).isEqualTo(UPDATED_SLOT);
        assertThat(testONT.getPon()).isEqualTo(UPDATED_PON);
        assertThat(testONT.getPonIndex()).isEqualTo(UPDATED_PON_INDEX);
        assertThat(testONT.getMaxUp()).isEqualTo(UPDATED_MAX_UP);
        assertThat(testONT.getMaxDown()).isEqualTo(UPDATED_MAX_DOWN);
        assertThat(testONT.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testONT.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testONT.getEtatOlt()).isEqualTo(UPDATED_ETAT_OLT);
        assertThat(testONT.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testONT.getStatusAt()).isEqualTo(UPDATED_STATUS_AT);
        assertThat(testONT.getNbreLignesCouper()).isEqualTo(UPDATED_NBRE_LIGNES_COUPER);
    }

    @Test
    @Transactional
    void patchNonExistingONT() throws Exception {
        int databaseSizeBeforeUpdate = oNTRepository.findAll().size();
        oNT.setId(longCount.incrementAndGet());

        // Create the ONT
        ONTDTO oNTDTO = oNTMapper.toDto(oNT);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restONTMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, oNTDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(oNTDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ONT in the database
        List<ONT> oNTList = oNTRepository.findAll();
        assertThat(oNTList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchONT() throws Exception {
        int databaseSizeBeforeUpdate = oNTRepository.findAll().size();
        oNT.setId(longCount.incrementAndGet());

        // Create the ONT
        ONTDTO oNTDTO = oNTMapper.toDto(oNT);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restONTMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(oNTDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ONT in the database
        List<ONT> oNTList = oNTRepository.findAll();
        assertThat(oNTList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamONT() throws Exception {
        int databaseSizeBeforeUpdate = oNTRepository.findAll().size();
        oNT.setId(longCount.incrementAndGet());

        // Create the ONT
        ONTDTO oNTDTO = oNTMapper.toDto(oNT);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restONTMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(oNTDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ONT in the database
        List<ONT> oNTList = oNTRepository.findAll();
        assertThat(oNTList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteONT() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        int databaseSizeBeforeDelete = oNTRepository.findAll().size();

        // Delete the oNT
        restONTMockMvc
            .perform(delete(ENTITY_API_URL_ID, oNT.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ONT> oNTList = oNTRepository.findAll();
        assertThat(oNTList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
