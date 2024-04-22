package sn.sonatel.dsi.ins.ftsirc.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.sonatel.dsi.ins.ftsirc.domain.ONTAsserts.*;
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
import sn.sonatel.dsi.ins.ftsirc.domain.Client;
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

    private static final String DEFAULT_ONT_IP = "AAAAAAAAAA";
    private static final String UPDATED_ONT_IP = "BBBBBBBBBB";

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

    private static final String ENTITY_API_URL = "/api/onts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

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
            .ontIP(DEFAULT_ONT_IP)
            .serviceId(DEFAULT_SERVICE_ID)
            .slot(DEFAULT_SLOT)
            .pon(DEFAULT_PON)
            .ponIndex(DEFAULT_PON_INDEX)
            .maxUp(DEFAULT_MAX_UP)
            .maxDown(DEFAULT_MAX_DOWN)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
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
            .ontIP(UPDATED_ONT_IP)
            .serviceId(UPDATED_SERVICE_ID)
            .slot(UPDATED_SLOT)
            .pon(UPDATED_PON)
            .ponIndex(UPDATED_PON_INDEX)
            .maxUp(UPDATED_MAX_UP)
            .maxDown(UPDATED_MAX_DOWN)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return oNT;
    }

    @BeforeEach
    public void initTest() {
        oNT = createEntity(em);
    }

    @Test
    @Transactional
    void createONT() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ONT
        ONTDTO oNTDTO = oNTMapper.toDto(oNT);
        var returnedONTDTO = om.readValue(
            restONTMockMvc
                .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(oNTDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ONTDTO.class
        );

        // Validate the ONT in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedONT = oNTMapper.toEntity(returnedONTDTO);
        assertONTUpdatableFieldsEquals(returnedONT, getPersistedONT(returnedONT));
    }

    @Test
    @Transactional
    void createONTWithExistingId() throws Exception {
        // Create the ONT with an existing ID
        oNT.setId(1L);
        ONTDTO oNTDTO = oNTMapper.toDto(oNT);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restONTMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(oNTDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ONT in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIndexIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        oNT.setIndex(null);

        // Create the ONT, which fails.
        ONTDTO oNTDTO = oNTMapper.toDto(oNT);

        restONTMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(oNTDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOntIPIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        oNT.setOntIP(null);

        // Create the ONT, which fails.
        ONTDTO oNTDTO = oNTMapper.toDto(oNT);

        restONTMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(oNTDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkServiceIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        oNT.setServiceId(null);

        // Create the ONT, which fails.
        ONTDTO oNTDTO = oNTMapper.toDto(oNT);

        restONTMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(oNTDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSlotIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        oNT.setSlot(null);

        // Create the ONT, which fails.
        ONTDTO oNTDTO = oNTMapper.toDto(oNT);

        restONTMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(oNTDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPonIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        oNT.setPon(null);

        // Create the ONT, which fails.
        ONTDTO oNTDTO = oNTMapper.toDto(oNT);

        restONTMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(oNTDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPonIndexIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        oNT.setPonIndex(null);

        // Create the ONT, which fails.
        ONTDTO oNTDTO = oNTMapper.toDto(oNT);

        restONTMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(oNTDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMaxUpIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        oNT.setMaxUp(null);

        // Create the ONT, which fails.
        ONTDTO oNTDTO = oNTMapper.toDto(oNT);

        restONTMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(oNTDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMaxDownIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        oNT.setMaxDown(null);

        // Create the ONT, which fails.
        ONTDTO oNTDTO = oNTMapper.toDto(oNT);

        restONTMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(oNTDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
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
            .andExpect(jsonPath("$.[*].ontIP").value(hasItem(DEFAULT_ONT_IP)))
            .andExpect(jsonPath("$.[*].serviceId").value(hasItem(DEFAULT_SERVICE_ID)))
            .andExpect(jsonPath("$.[*].slot").value(hasItem(DEFAULT_SLOT)))
            .andExpect(jsonPath("$.[*].pon").value(hasItem(DEFAULT_PON)))
            .andExpect(jsonPath("$.[*].ponIndex").value(hasItem(DEFAULT_PON_INDEX)))
            .andExpect(jsonPath("$.[*].maxUp").value(hasItem(DEFAULT_MAX_UP)))
            .andExpect(jsonPath("$.[*].maxDown").value(hasItem(DEFAULT_MAX_DOWN)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
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
            .andExpect(jsonPath("$.ontIP").value(DEFAULT_ONT_IP))
            .andExpect(jsonPath("$.serviceId").value(DEFAULT_SERVICE_ID))
            .andExpect(jsonPath("$.slot").value(DEFAULT_SLOT))
            .andExpect(jsonPath("$.pon").value(DEFAULT_PON))
            .andExpect(jsonPath("$.ponIndex").value(DEFAULT_PON_INDEX))
            .andExpect(jsonPath("$.maxUp").value(DEFAULT_MAX_UP))
            .andExpect(jsonPath("$.maxDown").value(DEFAULT_MAX_DOWN))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getONTSByIdFiltering() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        Long id = oNT.getId();

        defaultONTFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultONTFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultONTFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllONTSByIndexIsEqualToSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where index equals to
        defaultONTFiltering("index.equals=" + DEFAULT_INDEX, "index.equals=" + UPDATED_INDEX);
    }

    @Test
    @Transactional
    void getAllONTSByIndexIsInShouldWork() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where index in
        defaultONTFiltering("index.in=" + DEFAULT_INDEX + "," + UPDATED_INDEX, "index.in=" + UPDATED_INDEX);
    }

    @Test
    @Transactional
    void getAllONTSByIndexIsNullOrNotNull() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where index is not null
        defaultONTFiltering("index.specified=true", "index.specified=false");
    }

    @Test
    @Transactional
    void getAllONTSByIndexContainsSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where index contains
        defaultONTFiltering("index.contains=" + DEFAULT_INDEX, "index.contains=" + UPDATED_INDEX);
    }

    @Test
    @Transactional
    void getAllONTSByIndexNotContainsSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where index does not contain
        defaultONTFiltering("index.doesNotContain=" + UPDATED_INDEX, "index.doesNotContain=" + DEFAULT_INDEX);
    }

    @Test
    @Transactional
    void getAllONTSByOntIPIsEqualToSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where ontIP equals to
        defaultONTFiltering("ontIP.equals=" + DEFAULT_ONT_IP, "ontIP.equals=" + UPDATED_ONT_IP);
    }

    @Test
    @Transactional
    void getAllONTSByOntIPIsInShouldWork() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where ontIP in
        defaultONTFiltering("ontIP.in=" + DEFAULT_ONT_IP + "," + UPDATED_ONT_IP, "ontIP.in=" + UPDATED_ONT_IP);
    }

    @Test
    @Transactional
    void getAllONTSByOntIPIsNullOrNotNull() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where ontIP is not null
        defaultONTFiltering("ontIP.specified=true", "ontIP.specified=false");
    }

    @Test
    @Transactional
    void getAllONTSByOntIPContainsSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where ontIP contains
        defaultONTFiltering("ontIP.contains=" + DEFAULT_ONT_IP, "ontIP.contains=" + UPDATED_ONT_IP);
    }

    @Test
    @Transactional
    void getAllONTSByOntIPNotContainsSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where ontIP does not contain
        defaultONTFiltering("ontIP.doesNotContain=" + UPDATED_ONT_IP, "ontIP.doesNotContain=" + DEFAULT_ONT_IP);
    }

    @Test
    @Transactional
    void getAllONTSByServiceIdIsEqualToSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where serviceId equals to
        defaultONTFiltering("serviceId.equals=" + DEFAULT_SERVICE_ID, "serviceId.equals=" + UPDATED_SERVICE_ID);
    }

    @Test
    @Transactional
    void getAllONTSByServiceIdIsInShouldWork() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where serviceId in
        defaultONTFiltering("serviceId.in=" + DEFAULT_SERVICE_ID + "," + UPDATED_SERVICE_ID, "serviceId.in=" + UPDATED_SERVICE_ID);
    }

    @Test
    @Transactional
    void getAllONTSByServiceIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where serviceId is not null
        defaultONTFiltering("serviceId.specified=true", "serviceId.specified=false");
    }

    @Test
    @Transactional
    void getAllONTSByServiceIdContainsSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where serviceId contains
        defaultONTFiltering("serviceId.contains=" + DEFAULT_SERVICE_ID, "serviceId.contains=" + UPDATED_SERVICE_ID);
    }

    @Test
    @Transactional
    void getAllONTSByServiceIdNotContainsSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where serviceId does not contain
        defaultONTFiltering("serviceId.doesNotContain=" + UPDATED_SERVICE_ID, "serviceId.doesNotContain=" + DEFAULT_SERVICE_ID);
    }

    @Test
    @Transactional
    void getAllONTSBySlotIsEqualToSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where slot equals to
        defaultONTFiltering("slot.equals=" + DEFAULT_SLOT, "slot.equals=" + UPDATED_SLOT);
    }

    @Test
    @Transactional
    void getAllONTSBySlotIsInShouldWork() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where slot in
        defaultONTFiltering("slot.in=" + DEFAULT_SLOT + "," + UPDATED_SLOT, "slot.in=" + UPDATED_SLOT);
    }

    @Test
    @Transactional
    void getAllONTSBySlotIsNullOrNotNull() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where slot is not null
        defaultONTFiltering("slot.specified=true", "slot.specified=false");
    }

    @Test
    @Transactional
    void getAllONTSBySlotContainsSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where slot contains
        defaultONTFiltering("slot.contains=" + DEFAULT_SLOT, "slot.contains=" + UPDATED_SLOT);
    }

    @Test
    @Transactional
    void getAllONTSBySlotNotContainsSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where slot does not contain
        defaultONTFiltering("slot.doesNotContain=" + UPDATED_SLOT, "slot.doesNotContain=" + DEFAULT_SLOT);
    }

    @Test
    @Transactional
    void getAllONTSByPonIsEqualToSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where pon equals to
        defaultONTFiltering("pon.equals=" + DEFAULT_PON, "pon.equals=" + UPDATED_PON);
    }

    @Test
    @Transactional
    void getAllONTSByPonIsInShouldWork() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where pon in
        defaultONTFiltering("pon.in=" + DEFAULT_PON + "," + UPDATED_PON, "pon.in=" + UPDATED_PON);
    }

    @Test
    @Transactional
    void getAllONTSByPonIsNullOrNotNull() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where pon is not null
        defaultONTFiltering("pon.specified=true", "pon.specified=false");
    }

    @Test
    @Transactional
    void getAllONTSByPonContainsSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where pon contains
        defaultONTFiltering("pon.contains=" + DEFAULT_PON, "pon.contains=" + UPDATED_PON);
    }

    @Test
    @Transactional
    void getAllONTSByPonNotContainsSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where pon does not contain
        defaultONTFiltering("pon.doesNotContain=" + UPDATED_PON, "pon.doesNotContain=" + DEFAULT_PON);
    }

    @Test
    @Transactional
    void getAllONTSByPonIndexIsEqualToSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where ponIndex equals to
        defaultONTFiltering("ponIndex.equals=" + DEFAULT_PON_INDEX, "ponIndex.equals=" + UPDATED_PON_INDEX);
    }

    @Test
    @Transactional
    void getAllONTSByPonIndexIsInShouldWork() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where ponIndex in
        defaultONTFiltering("ponIndex.in=" + DEFAULT_PON_INDEX + "," + UPDATED_PON_INDEX, "ponIndex.in=" + UPDATED_PON_INDEX);
    }

    @Test
    @Transactional
    void getAllONTSByPonIndexIsNullOrNotNull() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where ponIndex is not null
        defaultONTFiltering("ponIndex.specified=true", "ponIndex.specified=false");
    }

    @Test
    @Transactional
    void getAllONTSByPonIndexContainsSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where ponIndex contains
        defaultONTFiltering("ponIndex.contains=" + DEFAULT_PON_INDEX, "ponIndex.contains=" + UPDATED_PON_INDEX);
    }

    @Test
    @Transactional
    void getAllONTSByPonIndexNotContainsSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where ponIndex does not contain
        defaultONTFiltering("ponIndex.doesNotContain=" + UPDATED_PON_INDEX, "ponIndex.doesNotContain=" + DEFAULT_PON_INDEX);
    }

    @Test
    @Transactional
    void getAllONTSByMaxUpIsEqualToSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where maxUp equals to
        defaultONTFiltering("maxUp.equals=" + DEFAULT_MAX_UP, "maxUp.equals=" + UPDATED_MAX_UP);
    }

    @Test
    @Transactional
    void getAllONTSByMaxUpIsInShouldWork() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where maxUp in
        defaultONTFiltering("maxUp.in=" + DEFAULT_MAX_UP + "," + UPDATED_MAX_UP, "maxUp.in=" + UPDATED_MAX_UP);
    }

    @Test
    @Transactional
    void getAllONTSByMaxUpIsNullOrNotNull() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where maxUp is not null
        defaultONTFiltering("maxUp.specified=true", "maxUp.specified=false");
    }

    @Test
    @Transactional
    void getAllONTSByMaxUpContainsSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where maxUp contains
        defaultONTFiltering("maxUp.contains=" + DEFAULT_MAX_UP, "maxUp.contains=" + UPDATED_MAX_UP);
    }

    @Test
    @Transactional
    void getAllONTSByMaxUpNotContainsSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where maxUp does not contain
        defaultONTFiltering("maxUp.doesNotContain=" + UPDATED_MAX_UP, "maxUp.doesNotContain=" + DEFAULT_MAX_UP);
    }

    @Test
    @Transactional
    void getAllONTSByMaxDownIsEqualToSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where maxDown equals to
        defaultONTFiltering("maxDown.equals=" + DEFAULT_MAX_DOWN, "maxDown.equals=" + UPDATED_MAX_DOWN);
    }

    @Test
    @Transactional
    void getAllONTSByMaxDownIsInShouldWork() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where maxDown in
        defaultONTFiltering("maxDown.in=" + DEFAULT_MAX_DOWN + "," + UPDATED_MAX_DOWN, "maxDown.in=" + UPDATED_MAX_DOWN);
    }

    @Test
    @Transactional
    void getAllONTSByMaxDownIsNullOrNotNull() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where maxDown is not null
        defaultONTFiltering("maxDown.specified=true", "maxDown.specified=false");
    }

    @Test
    @Transactional
    void getAllONTSByMaxDownContainsSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where maxDown contains
        defaultONTFiltering("maxDown.contains=" + DEFAULT_MAX_DOWN, "maxDown.contains=" + UPDATED_MAX_DOWN);
    }

    @Test
    @Transactional
    void getAllONTSByMaxDownNotContainsSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where maxDown does not contain
        defaultONTFiltering("maxDown.doesNotContain=" + UPDATED_MAX_DOWN, "maxDown.doesNotContain=" + DEFAULT_MAX_DOWN);
    }

    @Test
    @Transactional
    void getAllONTSByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where createdAt equals to
        defaultONTFiltering("createdAt.equals=" + DEFAULT_CREATED_AT, "createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllONTSByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where createdAt in
        defaultONTFiltering("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT, "createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllONTSByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where createdAt is not null
        defaultONTFiltering("createdAt.specified=true", "createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllONTSByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where createdAt is greater than or equal to
        defaultONTFiltering("createdAt.greaterThanOrEqual=" + DEFAULT_CREATED_AT, "createdAt.greaterThanOrEqual=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllONTSByCreatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where createdAt is less than or equal to
        defaultONTFiltering("createdAt.lessThanOrEqual=" + DEFAULT_CREATED_AT, "createdAt.lessThanOrEqual=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllONTSByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where createdAt is less than
        defaultONTFiltering("createdAt.lessThan=" + UPDATED_CREATED_AT, "createdAt.lessThan=" + DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllONTSByCreatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where createdAt is greater than
        defaultONTFiltering("createdAt.greaterThan=" + SMALLER_CREATED_AT, "createdAt.greaterThan=" + DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllONTSByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where updatedAt equals to
        defaultONTFiltering("updatedAt.equals=" + DEFAULT_UPDATED_AT, "updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllONTSByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where updatedAt in
        defaultONTFiltering("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT, "updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllONTSByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where updatedAt is not null
        defaultONTFiltering("updatedAt.specified=true", "updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllONTSByUpdatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where updatedAt is greater than or equal to
        defaultONTFiltering("updatedAt.greaterThanOrEqual=" + DEFAULT_UPDATED_AT, "updatedAt.greaterThanOrEqual=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllONTSByUpdatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where updatedAt is less than or equal to
        defaultONTFiltering("updatedAt.lessThanOrEqual=" + DEFAULT_UPDATED_AT, "updatedAt.lessThanOrEqual=" + SMALLER_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllONTSByUpdatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where updatedAt is less than
        defaultONTFiltering("updatedAt.lessThan=" + UPDATED_UPDATED_AT, "updatedAt.lessThan=" + DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllONTSByUpdatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        // Get all the oNTList where updatedAt is greater than
        defaultONTFiltering("updatedAt.greaterThan=" + SMALLER_UPDATED_AT, "updatedAt.greaterThan=" + DEFAULT_UPDATED_AT);
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

    private void defaultONTFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultONTShouldBeFound(shouldBeFound);
        defaultONTShouldNotBeFound(shouldNotBeFound);
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
            .andExpect(jsonPath("$.[*].ontIP").value(hasItem(DEFAULT_ONT_IP)))
            .andExpect(jsonPath("$.[*].serviceId").value(hasItem(DEFAULT_SERVICE_ID)))
            .andExpect(jsonPath("$.[*].slot").value(hasItem(DEFAULT_SLOT)))
            .andExpect(jsonPath("$.[*].pon").value(hasItem(DEFAULT_PON)))
            .andExpect(jsonPath("$.[*].ponIndex").value(hasItem(DEFAULT_PON_INDEX)))
            .andExpect(jsonPath("$.[*].maxUp").value(hasItem(DEFAULT_MAX_UP)))
            .andExpect(jsonPath("$.[*].maxDown").value(hasItem(DEFAULT_MAX_DOWN)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));

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

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the oNT
        ONT updatedONT = oNTRepository.findById(oNT.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedONT are not directly saved in db
        em.detach(updatedONT);
        updatedONT
            .index(UPDATED_INDEX)
            .ontIP(UPDATED_ONT_IP)
            .serviceId(UPDATED_SERVICE_ID)
            .slot(UPDATED_SLOT)
            .pon(UPDATED_PON)
            .ponIndex(UPDATED_PON_INDEX)
            .maxUp(UPDATED_MAX_UP)
            .maxDown(UPDATED_MAX_DOWN)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        ONTDTO oNTDTO = oNTMapper.toDto(updatedONT);

        restONTMockMvc
            .perform(
                put(ENTITY_API_URL_ID, oNTDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(oNTDTO))
            )
            .andExpect(status().isOk());

        // Validate the ONT in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedONTToMatchAllProperties(updatedONT);
    }

    @Test
    @Transactional
    void putNonExistingONT() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        oNT.setId(longCount.incrementAndGet());

        // Create the ONT
        ONTDTO oNTDTO = oNTMapper.toDto(oNT);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restONTMockMvc
            .perform(
                put(ENTITY_API_URL_ID, oNTDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(oNTDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ONT in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchONT() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        oNT.setId(longCount.incrementAndGet());

        // Create the ONT
        ONTDTO oNTDTO = oNTMapper.toDto(oNT);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restONTMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(oNTDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ONT in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamONT() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        oNT.setId(longCount.incrementAndGet());

        // Create the ONT
        ONTDTO oNTDTO = oNTMapper.toDto(oNT);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restONTMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(oNTDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ONT in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateONTWithPatch() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the oNT using partial update
        ONT partialUpdatedONT = new ONT();
        partialUpdatedONT.setId(oNT.getId());

        partialUpdatedONT.ponIndex(UPDATED_PON_INDEX).updatedAt(UPDATED_UPDATED_AT);

        restONTMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedONT.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedONT))
            )
            .andExpect(status().isOk());

        // Validate the ONT in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertONTUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedONT, oNT), getPersistedONT(oNT));
    }

    @Test
    @Transactional
    void fullUpdateONTWithPatch() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the oNT using partial update
        ONT partialUpdatedONT = new ONT();
        partialUpdatedONT.setId(oNT.getId());

        partialUpdatedONT
            .index(UPDATED_INDEX)
            .ontIP(UPDATED_ONT_IP)
            .serviceId(UPDATED_SERVICE_ID)
            .slot(UPDATED_SLOT)
            .pon(UPDATED_PON)
            .ponIndex(UPDATED_PON_INDEX)
            .maxUp(UPDATED_MAX_UP)
            .maxDown(UPDATED_MAX_DOWN)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restONTMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedONT.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedONT))
            )
            .andExpect(status().isOk());

        // Validate the ONT in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertONTUpdatableFieldsEquals(partialUpdatedONT, getPersistedONT(partialUpdatedONT));
    }

    @Test
    @Transactional
    void patchNonExistingONT() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        oNT.setId(longCount.incrementAndGet());

        // Create the ONT
        ONTDTO oNTDTO = oNTMapper.toDto(oNT);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restONTMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, oNTDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(oNTDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ONT in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchONT() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        oNT.setId(longCount.incrementAndGet());

        // Create the ONT
        ONTDTO oNTDTO = oNTMapper.toDto(oNT);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restONTMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(oNTDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ONT in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamONT() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        oNT.setId(longCount.incrementAndGet());

        // Create the ONT
        ONTDTO oNTDTO = oNTMapper.toDto(oNT);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restONTMockMvc
            .perform(patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(oNTDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ONT in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteONT() throws Exception {
        // Initialize the database
        oNTRepository.saveAndFlush(oNT);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the oNT
        restONTMockMvc
            .perform(delete(ENTITY_API_URL_ID, oNT.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return oNTRepository.count();
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

    protected ONT getPersistedONT(ONT oNT) {
        return oNTRepository.findById(oNT.getId()).orElseThrow();
    }

    protected void assertPersistedONTToMatchAllProperties(ONT expectedONT) {
        assertONTAllPropertiesEquals(expectedONT, getPersistedONT(expectedONT));
    }

    protected void assertPersistedONTToMatchUpdatableProperties(ONT expectedONT) {
        assertONTAllUpdatablePropertiesEquals(expectedONT, getPersistedONT(expectedONT));
    }
}
