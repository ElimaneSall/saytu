package sn.sonatel.dsi.ins.ftsirc.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.sonatel.dsi.ins.ftsirc.domain.OLTAsserts.*;
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
import sn.sonatel.dsi.ins.ftsirc.domain.Adresse;
import sn.sonatel.dsi.ins.ftsirc.domain.OLT;
import sn.sonatel.dsi.ins.ftsirc.repository.OLTRepository;
import sn.sonatel.dsi.ins.ftsirc.service.OLTService;
import sn.sonatel.dsi.ins.ftsirc.service.dto.OLTDTO;
import sn.sonatel.dsi.ins.ftsirc.service.mapper.OLTMapper;

/**
 * Integration tests for the {@link OLTResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class OLTResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_IP = "AAAAAAAAAA";
    private static final String UPDATED_IP = "BBBBBBBBBB";

    private static final String DEFAULT_VENDEUR = "AAAAAAAAAA";
    private static final String UPDATED_VENDEUR = "BBBBBBBBBB";

    private static final String DEFAULT_ETAT = "AAAAAAAAAA";
    private static final String UPDATED_ETAT = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_AT = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATED_AT = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_UPDATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATED_AT = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_UPDATED_AT = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/olts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private OLTRepository oLTRepository;

    @Mock
    private OLTRepository oLTRepositoryMock;

    @Autowired
    private OLTMapper oLTMapper;

    @Mock
    private OLTService oLTServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOLTMockMvc;

    private OLT oLT;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OLT createEntity(EntityManager em) {
        OLT oLT = new OLT()
            .nom(DEFAULT_NOM)
            .ip(DEFAULT_IP)
            .vendeur(DEFAULT_VENDEUR)
            .etat(DEFAULT_ETAT)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return oLT;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OLT createUpdatedEntity(EntityManager em) {
        OLT oLT = new OLT()
            .nom(UPDATED_NOM)
            .ip(UPDATED_IP)
            .vendeur(UPDATED_VENDEUR)
            .etat(UPDATED_ETAT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return oLT;
    }

    @BeforeEach
    public void initTest() {
        oLT = createEntity(em);
    }

    @Test
    @Transactional
    void createOLT() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the OLT
        OLTDTO oLTDTO = oLTMapper.toDto(oLT);
        var returnedOLTDTO = om.readValue(
            restOLTMockMvc
                .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(oLTDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            OLTDTO.class
        );

        // Validate the OLT in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedOLT = oLTMapper.toEntity(returnedOLTDTO);
        assertOLTUpdatableFieldsEquals(returnedOLT, getPersistedOLT(returnedOLT));
    }

    @Test
    @Transactional
    void createOLTWithExistingId() throws Exception {
        // Create the OLT with an existing ID
        oLT.setId(1L);
        OLTDTO oLTDTO = oLTMapper.toDto(oLT);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOLTMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(oLTDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OLT in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        oLT.setNom(null);

        // Create the OLT, which fails.
        OLTDTO oLTDTO = oLTMapper.toDto(oLT);

        restOLTMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(oLTDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIpIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        oLT.setIp(null);

        // Create the OLT, which fails.
        OLTDTO oLTDTO = oLTMapper.toDto(oLT);

        restOLTMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(oLTDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVendeurIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        oLT.setVendeur(null);

        // Create the OLT, which fails.
        OLTDTO oLTDTO = oLTMapper.toDto(oLT);

        restOLTMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(oLTDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEtatIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        oLT.setEtat(null);

        // Create the OLT, which fails.
        OLTDTO oLTDTO = oLTMapper.toDto(oLT);

        restOLTMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(oLTDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOLTS() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList
        restOLTMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(oLT.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].ip").value(hasItem(DEFAULT_IP)))
            .andExpect(jsonPath("$.[*].vendeur").value(hasItem(DEFAULT_VENDEUR)))
            .andExpect(jsonPath("$.[*].etat").value(hasItem(DEFAULT_ETAT)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOLTSWithEagerRelationshipsIsEnabled() throws Exception {
        when(oLTServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOLTMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(oLTServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOLTSWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(oLTServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOLTMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(oLTRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getOLT() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get the oLT
        restOLTMockMvc
            .perform(get(ENTITY_API_URL_ID, oLT.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(oLT.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.ip").value(DEFAULT_IP))
            .andExpect(jsonPath("$.vendeur").value(DEFAULT_VENDEUR))
            .andExpect(jsonPath("$.etat").value(DEFAULT_ETAT))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getOLTSByIdFiltering() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        Long id = oLT.getId();

        defaultOLTFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultOLTFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultOLTFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOLTSByNomIsEqualToSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where nom equals to
        defaultOLTFiltering("nom.equals=" + DEFAULT_NOM, "nom.equals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllOLTSByNomIsInShouldWork() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where nom in
        defaultOLTFiltering("nom.in=" + DEFAULT_NOM + "," + UPDATED_NOM, "nom.in=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllOLTSByNomIsNullOrNotNull() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where nom is not null
        defaultOLTFiltering("nom.specified=true", "nom.specified=false");
    }

    @Test
    @Transactional
    void getAllOLTSByNomContainsSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where nom contains
        defaultOLTFiltering("nom.contains=" + DEFAULT_NOM, "nom.contains=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllOLTSByNomNotContainsSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where nom does not contain
        defaultOLTFiltering("nom.doesNotContain=" + UPDATED_NOM, "nom.doesNotContain=" + DEFAULT_NOM);
    }

    @Test
    @Transactional
    void getAllOLTSByIpIsEqualToSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where ip equals to
        defaultOLTFiltering("ip.equals=" + DEFAULT_IP, "ip.equals=" + UPDATED_IP);
    }

    @Test
    @Transactional
    void getAllOLTSByIpIsInShouldWork() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where ip in
        defaultOLTFiltering("ip.in=" + DEFAULT_IP + "," + UPDATED_IP, "ip.in=" + UPDATED_IP);
    }

    @Test
    @Transactional
    void getAllOLTSByIpIsNullOrNotNull() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where ip is not null
        defaultOLTFiltering("ip.specified=true", "ip.specified=false");
    }

    @Test
    @Transactional
    void getAllOLTSByIpContainsSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where ip contains
        defaultOLTFiltering("ip.contains=" + DEFAULT_IP, "ip.contains=" + UPDATED_IP);
    }

    @Test
    @Transactional
    void getAllOLTSByIpNotContainsSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where ip does not contain
        defaultOLTFiltering("ip.doesNotContain=" + UPDATED_IP, "ip.doesNotContain=" + DEFAULT_IP);
    }

    @Test
    @Transactional
    void getAllOLTSByVendeurIsEqualToSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where vendeur equals to
        defaultOLTFiltering("vendeur.equals=" + DEFAULT_VENDEUR, "vendeur.equals=" + UPDATED_VENDEUR);
    }

    @Test
    @Transactional
    void getAllOLTSByVendeurIsInShouldWork() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where vendeur in
        defaultOLTFiltering("vendeur.in=" + DEFAULT_VENDEUR + "," + UPDATED_VENDEUR, "vendeur.in=" + UPDATED_VENDEUR);
    }

    @Test
    @Transactional
    void getAllOLTSByVendeurIsNullOrNotNull() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where vendeur is not null
        defaultOLTFiltering("vendeur.specified=true", "vendeur.specified=false");
    }

    @Test
    @Transactional
    void getAllOLTSByVendeurContainsSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where vendeur contains
        defaultOLTFiltering("vendeur.contains=" + DEFAULT_VENDEUR, "vendeur.contains=" + UPDATED_VENDEUR);
    }

    @Test
    @Transactional
    void getAllOLTSByVendeurNotContainsSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where vendeur does not contain
        defaultOLTFiltering("vendeur.doesNotContain=" + UPDATED_VENDEUR, "vendeur.doesNotContain=" + DEFAULT_VENDEUR);
    }

    @Test
    @Transactional
    void getAllOLTSByEtatIsEqualToSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where etat equals to
        defaultOLTFiltering("etat.equals=" + DEFAULT_ETAT, "etat.equals=" + UPDATED_ETAT);
    }

    @Test
    @Transactional
    void getAllOLTSByEtatIsInShouldWork() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where etat in
        defaultOLTFiltering("etat.in=" + DEFAULT_ETAT + "," + UPDATED_ETAT, "etat.in=" + UPDATED_ETAT);
    }

    @Test
    @Transactional
    void getAllOLTSByEtatIsNullOrNotNull() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where etat is not null
        defaultOLTFiltering("etat.specified=true", "etat.specified=false");
    }

    @Test
    @Transactional
    void getAllOLTSByEtatContainsSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where etat contains
        defaultOLTFiltering("etat.contains=" + DEFAULT_ETAT, "etat.contains=" + UPDATED_ETAT);
    }

    @Test
    @Transactional
    void getAllOLTSByEtatNotContainsSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where etat does not contain
        defaultOLTFiltering("etat.doesNotContain=" + UPDATED_ETAT, "etat.doesNotContain=" + DEFAULT_ETAT);
    }

    @Test
    @Transactional
    void getAllOLTSByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where createdAt equals to
        defaultOLTFiltering("createdAt.equals=" + DEFAULT_CREATED_AT, "createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllOLTSByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where createdAt in
        defaultOLTFiltering("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT, "createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllOLTSByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where createdAt is not null
        defaultOLTFiltering("createdAt.specified=true", "createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllOLTSByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where createdAt is greater than or equal to
        defaultOLTFiltering("createdAt.greaterThanOrEqual=" + DEFAULT_CREATED_AT, "createdAt.greaterThanOrEqual=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllOLTSByCreatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where createdAt is less than or equal to
        defaultOLTFiltering("createdAt.lessThanOrEqual=" + DEFAULT_CREATED_AT, "createdAt.lessThanOrEqual=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllOLTSByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where createdAt is less than
        defaultOLTFiltering("createdAt.lessThan=" + UPDATED_CREATED_AT, "createdAt.lessThan=" + DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllOLTSByCreatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where createdAt is greater than
        defaultOLTFiltering("createdAt.greaterThan=" + SMALLER_CREATED_AT, "createdAt.greaterThan=" + DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllOLTSByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where updatedAt equals to
        defaultOLTFiltering("updatedAt.equals=" + DEFAULT_UPDATED_AT, "updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllOLTSByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where updatedAt in
        defaultOLTFiltering("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT, "updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllOLTSByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where updatedAt is not null
        defaultOLTFiltering("updatedAt.specified=true", "updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllOLTSByUpdatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where updatedAt is greater than or equal to
        defaultOLTFiltering("updatedAt.greaterThanOrEqual=" + DEFAULT_UPDATED_AT, "updatedAt.greaterThanOrEqual=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllOLTSByUpdatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where updatedAt is less than or equal to
        defaultOLTFiltering("updatedAt.lessThanOrEqual=" + DEFAULT_UPDATED_AT, "updatedAt.lessThanOrEqual=" + SMALLER_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllOLTSByUpdatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where updatedAt is less than
        defaultOLTFiltering("updatedAt.lessThan=" + UPDATED_UPDATED_AT, "updatedAt.lessThan=" + DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllOLTSByUpdatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where updatedAt is greater than
        defaultOLTFiltering("updatedAt.greaterThan=" + SMALLER_UPDATED_AT, "updatedAt.greaterThan=" + DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllOLTSByAdresseIsEqualToSomething() throws Exception {
        Adresse adresse;
        if (TestUtil.findAll(em, Adresse.class).isEmpty()) {
            oLTRepository.saveAndFlush(oLT);
            adresse = AdresseResourceIT.createEntity(em);
        } else {
            adresse = TestUtil.findAll(em, Adresse.class).get(0);
        }
        em.persist(adresse);
        em.flush();
        oLT.setAdresse(adresse);
        oLTRepository.saveAndFlush(oLT);
        Long adresseId = adresse.getId();
        // Get all the oLTList where adresse equals to adresseId
        defaultOLTShouldBeFound("adresseId.equals=" + adresseId);

        // Get all the oLTList where adresse equals to (adresseId + 1)
        defaultOLTShouldNotBeFound("adresseId.equals=" + (adresseId + 1));
    }

    private void defaultOLTFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultOLTShouldBeFound(shouldBeFound);
        defaultOLTShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOLTShouldBeFound(String filter) throws Exception {
        restOLTMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(oLT.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].ip").value(hasItem(DEFAULT_IP)))
            .andExpect(jsonPath("$.[*].vendeur").value(hasItem(DEFAULT_VENDEUR)))
            .andExpect(jsonPath("$.[*].etat").value(hasItem(DEFAULT_ETAT)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));

        // Check, that the count call also returns 1
        restOLTMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOLTShouldNotBeFound(String filter) throws Exception {
        restOLTMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOLTMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOLT() throws Exception {
        // Get the oLT
        restOLTMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOLT() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the oLT
        OLT updatedOLT = oLTRepository.findById(oLT.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOLT are not directly saved in db
        em.detach(updatedOLT);
        updatedOLT
            .nom(UPDATED_NOM)
            .ip(UPDATED_IP)
            .vendeur(UPDATED_VENDEUR)
            .etat(UPDATED_ETAT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        OLTDTO oLTDTO = oLTMapper.toDto(updatedOLT);

        restOLTMockMvc
            .perform(
                put(ENTITY_API_URL_ID, oLTDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(oLTDTO))
            )
            .andExpect(status().isOk());

        // Validate the OLT in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedOLTToMatchAllProperties(updatedOLT);
    }

    @Test
    @Transactional
    void putNonExistingOLT() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        oLT.setId(longCount.incrementAndGet());

        // Create the OLT
        OLTDTO oLTDTO = oLTMapper.toDto(oLT);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOLTMockMvc
            .perform(
                put(ENTITY_API_URL_ID, oLTDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(oLTDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OLT in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOLT() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        oLT.setId(longCount.incrementAndGet());

        // Create the OLT
        OLTDTO oLTDTO = oLTMapper.toDto(oLT);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOLTMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(oLTDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OLT in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOLT() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        oLT.setId(longCount.incrementAndGet());

        // Create the OLT
        OLTDTO oLTDTO = oLTMapper.toDto(oLT);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOLTMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(oLTDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OLT in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOLTWithPatch() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the oLT using partial update
        OLT partialUpdatedOLT = new OLT();
        partialUpdatedOLT.setId(oLT.getId());

        partialUpdatedOLT.vendeur(UPDATED_VENDEUR).etat(UPDATED_ETAT).updatedAt(UPDATED_UPDATED_AT);

        restOLTMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOLT.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOLT))
            )
            .andExpect(status().isOk());

        // Validate the OLT in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOLTUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedOLT, oLT), getPersistedOLT(oLT));
    }

    @Test
    @Transactional
    void fullUpdateOLTWithPatch() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the oLT using partial update
        OLT partialUpdatedOLT = new OLT();
        partialUpdatedOLT.setId(oLT.getId());

        partialUpdatedOLT
            .nom(UPDATED_NOM)
            .ip(UPDATED_IP)
            .vendeur(UPDATED_VENDEUR)
            .etat(UPDATED_ETAT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restOLTMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOLT.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOLT))
            )
            .andExpect(status().isOk());

        // Validate the OLT in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOLTUpdatableFieldsEquals(partialUpdatedOLT, getPersistedOLT(partialUpdatedOLT));
    }

    @Test
    @Transactional
    void patchNonExistingOLT() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        oLT.setId(longCount.incrementAndGet());

        // Create the OLT
        OLTDTO oLTDTO = oLTMapper.toDto(oLT);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOLTMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, oLTDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(oLTDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OLT in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOLT() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        oLT.setId(longCount.incrementAndGet());

        // Create the OLT
        OLTDTO oLTDTO = oLTMapper.toDto(oLT);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOLTMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(oLTDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OLT in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOLT() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        oLT.setId(longCount.incrementAndGet());

        // Create the OLT
        OLTDTO oLTDTO = oLTMapper.toDto(oLT);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOLTMockMvc
            .perform(patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(oLTDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OLT in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOLT() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the oLT
        restOLTMockMvc
            .perform(delete(ENTITY_API_URL_ID, oLT.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return oLTRepository.count();
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

    protected OLT getPersistedOLT(OLT oLT) {
        return oLTRepository.findById(oLT.getId()).orElseThrow();
    }

    protected void assertPersistedOLTToMatchAllProperties(OLT expectedOLT) {
        assertOLTAllPropertiesEquals(expectedOLT, getPersistedOLT(expectedOLT));
    }

    protected void assertPersistedOLTToMatchUpdatableProperties(OLT expectedOLT) {
        assertOLTAllUpdatablePropertiesEquals(expectedOLT, getPersistedOLT(expectedOLT));
    }
}
