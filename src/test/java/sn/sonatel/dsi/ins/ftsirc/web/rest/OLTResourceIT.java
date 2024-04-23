package sn.sonatel.dsi.ins.ftsirc.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.sonatel.dsi.ins.ftsirc.domain.OLTAsserts.*;
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
import sn.sonatel.dsi.ins.ftsirc.domain.OLT;
import sn.sonatel.dsi.ins.ftsirc.repository.OLTRepository;
import sn.sonatel.dsi.ins.ftsirc.service.dto.OLTDTO;
import sn.sonatel.dsi.ins.ftsirc.service.mapper.OLTMapper;

/**
 * Integration tests for the {@link OLTResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OLTResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String DEFAULT_IP = "AAAAAAAAAA";
    private static final String UPDATED_IP = "BBBBBBBBBB";

    private static final String DEFAULT_VENDEUR = "AAAAAAAAAA";
    private static final String UPDATED_VENDEUR = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE_EQUIPMENT = "AAAAAAAAAA";
    private static final String UPDATED_TYPE_EQUIPMENT = "BBBBBBBBBB";

    private static final String DEFAULT_CODE_EQUIPMENT = "AAAAAAAAAA";
    private static final String UPDATED_CODE_EQUIPMENT = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESSE = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE = "BBBBBBBBBB";

    private static final String DEFAULT_EMPLACEMENT = "AAAAAAAAAA";
    private static final String UPDATED_EMPLACEMENT = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE_CARTE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE_CARTE = "BBBBBBBBBB";

    private static final String DEFAULT_LATITUDE = "AAAAAAAAAA";
    private static final String UPDATED_LATITUDE = "BBBBBBBBBB";

    private static final String DEFAULT_LONGITUDE = "AAAAAAAAAA";
    private static final String UPDATED_LONGITUDE = "BBBBBBBBBB";

    private static final String DEFAULT_CAPACITE = "AAAAAAAAAA";
    private static final String UPDATED_CAPACITE = "BBBBBBBBBB";

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

    @Autowired
    private OLTMapper oLTMapper;

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
            .libelle(DEFAULT_LIBELLE)
            .ip(DEFAULT_IP)
            .vendeur(DEFAULT_VENDEUR)
            .typeEquipment(DEFAULT_TYPE_EQUIPMENT)
            .codeEquipment(DEFAULT_CODE_EQUIPMENT)
            .adresse(DEFAULT_ADRESSE)
            .emplacement(DEFAULT_EMPLACEMENT)
            .typeCarte(DEFAULT_TYPE_CARTE)
            .latitude(DEFAULT_LATITUDE)
            .longitude(DEFAULT_LONGITUDE)
            .capacite(DEFAULT_CAPACITE)
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
            .libelle(UPDATED_LIBELLE)
            .ip(UPDATED_IP)
            .vendeur(UPDATED_VENDEUR)
            .typeEquipment(UPDATED_TYPE_EQUIPMENT)
            .codeEquipment(UPDATED_CODE_EQUIPMENT)
            .adresse(UPDATED_ADRESSE)
            .emplacement(UPDATED_EMPLACEMENT)
            .typeCarte(UPDATED_TYPE_CARTE)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .capacite(UPDATED_CAPACITE)
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
    void checkLibelleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        oLT.setLibelle(null);

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
    void getAllOLTS() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList
        restOLTMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(oLT.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].ip").value(hasItem(DEFAULT_IP)))
            .andExpect(jsonPath("$.[*].vendeur").value(hasItem(DEFAULT_VENDEUR)))
            .andExpect(jsonPath("$.[*].typeEquipment").value(hasItem(DEFAULT_TYPE_EQUIPMENT)))
            .andExpect(jsonPath("$.[*].codeEquipment").value(hasItem(DEFAULT_CODE_EQUIPMENT)))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE)))
            .andExpect(jsonPath("$.[*].emplacement").value(hasItem(DEFAULT_EMPLACEMENT)))
            .andExpect(jsonPath("$.[*].typeCarte").value(hasItem(DEFAULT_TYPE_CARTE)))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE)))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE)))
            .andExpect(jsonPath("$.[*].capacite").value(hasItem(DEFAULT_CAPACITE)))
            .andExpect(jsonPath("$.[*].etat").value(hasItem(DEFAULT_ETAT)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
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
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.ip").value(DEFAULT_IP))
            .andExpect(jsonPath("$.vendeur").value(DEFAULT_VENDEUR))
            .andExpect(jsonPath("$.typeEquipment").value(DEFAULT_TYPE_EQUIPMENT))
            .andExpect(jsonPath("$.codeEquipment").value(DEFAULT_CODE_EQUIPMENT))
            .andExpect(jsonPath("$.adresse").value(DEFAULT_ADRESSE))
            .andExpect(jsonPath("$.emplacement").value(DEFAULT_EMPLACEMENT))
            .andExpect(jsonPath("$.typeCarte").value(DEFAULT_TYPE_CARTE))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE))
            .andExpect(jsonPath("$.capacite").value(DEFAULT_CAPACITE))
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
    void getAllOLTSByLibelleIsEqualToSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where libelle equals to
        defaultOLTFiltering("libelle.equals=" + DEFAULT_LIBELLE, "libelle.equals=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllOLTSByLibelleIsInShouldWork() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where libelle in
        defaultOLTFiltering("libelle.in=" + DEFAULT_LIBELLE + "," + UPDATED_LIBELLE, "libelle.in=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllOLTSByLibelleIsNullOrNotNull() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where libelle is not null
        defaultOLTFiltering("libelle.specified=true", "libelle.specified=false");
    }

    @Test
    @Transactional
    void getAllOLTSByLibelleContainsSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where libelle contains
        defaultOLTFiltering("libelle.contains=" + DEFAULT_LIBELLE, "libelle.contains=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllOLTSByLibelleNotContainsSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where libelle does not contain
        defaultOLTFiltering("libelle.doesNotContain=" + UPDATED_LIBELLE, "libelle.doesNotContain=" + DEFAULT_LIBELLE);
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
    void getAllOLTSByTypeEquipmentIsEqualToSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where typeEquipment equals to
        defaultOLTFiltering("typeEquipment.equals=" + DEFAULT_TYPE_EQUIPMENT, "typeEquipment.equals=" + UPDATED_TYPE_EQUIPMENT);
    }

    @Test
    @Transactional
    void getAllOLTSByTypeEquipmentIsInShouldWork() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where typeEquipment in
        defaultOLTFiltering(
            "typeEquipment.in=" + DEFAULT_TYPE_EQUIPMENT + "," + UPDATED_TYPE_EQUIPMENT,
            "typeEquipment.in=" + UPDATED_TYPE_EQUIPMENT
        );
    }

    @Test
    @Transactional
    void getAllOLTSByTypeEquipmentIsNullOrNotNull() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where typeEquipment is not null
        defaultOLTFiltering("typeEquipment.specified=true", "typeEquipment.specified=false");
    }

    @Test
    @Transactional
    void getAllOLTSByTypeEquipmentContainsSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where typeEquipment contains
        defaultOLTFiltering("typeEquipment.contains=" + DEFAULT_TYPE_EQUIPMENT, "typeEquipment.contains=" + UPDATED_TYPE_EQUIPMENT);
    }

    @Test
    @Transactional
    void getAllOLTSByTypeEquipmentNotContainsSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where typeEquipment does not contain
        defaultOLTFiltering(
            "typeEquipment.doesNotContain=" + UPDATED_TYPE_EQUIPMENT,
            "typeEquipment.doesNotContain=" + DEFAULT_TYPE_EQUIPMENT
        );
    }

    @Test
    @Transactional
    void getAllOLTSByCodeEquipmentIsEqualToSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where codeEquipment equals to
        defaultOLTFiltering("codeEquipment.equals=" + DEFAULT_CODE_EQUIPMENT, "codeEquipment.equals=" + UPDATED_CODE_EQUIPMENT);
    }

    @Test
    @Transactional
    void getAllOLTSByCodeEquipmentIsInShouldWork() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where codeEquipment in
        defaultOLTFiltering(
            "codeEquipment.in=" + DEFAULT_CODE_EQUIPMENT + "," + UPDATED_CODE_EQUIPMENT,
            "codeEquipment.in=" + UPDATED_CODE_EQUIPMENT
        );
    }

    @Test
    @Transactional
    void getAllOLTSByCodeEquipmentIsNullOrNotNull() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where codeEquipment is not null
        defaultOLTFiltering("codeEquipment.specified=true", "codeEquipment.specified=false");
    }

    @Test
    @Transactional
    void getAllOLTSByCodeEquipmentContainsSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where codeEquipment contains
        defaultOLTFiltering("codeEquipment.contains=" + DEFAULT_CODE_EQUIPMENT, "codeEquipment.contains=" + UPDATED_CODE_EQUIPMENT);
    }

    @Test
    @Transactional
    void getAllOLTSByCodeEquipmentNotContainsSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where codeEquipment does not contain
        defaultOLTFiltering(
            "codeEquipment.doesNotContain=" + UPDATED_CODE_EQUIPMENT,
            "codeEquipment.doesNotContain=" + DEFAULT_CODE_EQUIPMENT
        );
    }

    @Test
    @Transactional
    void getAllOLTSByAdresseIsEqualToSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where adresse equals to
        defaultOLTFiltering("adresse.equals=" + DEFAULT_ADRESSE, "adresse.equals=" + UPDATED_ADRESSE);
    }

    @Test
    @Transactional
    void getAllOLTSByAdresseIsInShouldWork() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where adresse in
        defaultOLTFiltering("adresse.in=" + DEFAULT_ADRESSE + "," + UPDATED_ADRESSE, "adresse.in=" + UPDATED_ADRESSE);
    }

    @Test
    @Transactional
    void getAllOLTSByAdresseIsNullOrNotNull() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where adresse is not null
        defaultOLTFiltering("adresse.specified=true", "adresse.specified=false");
    }

    @Test
    @Transactional
    void getAllOLTSByAdresseContainsSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where adresse contains
        defaultOLTFiltering("adresse.contains=" + DEFAULT_ADRESSE, "adresse.contains=" + UPDATED_ADRESSE);
    }

    @Test
    @Transactional
    void getAllOLTSByAdresseNotContainsSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where adresse does not contain
        defaultOLTFiltering("adresse.doesNotContain=" + UPDATED_ADRESSE, "adresse.doesNotContain=" + DEFAULT_ADRESSE);
    }

    @Test
    @Transactional
    void getAllOLTSByEmplacementIsEqualToSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where emplacement equals to
        defaultOLTFiltering("emplacement.equals=" + DEFAULT_EMPLACEMENT, "emplacement.equals=" + UPDATED_EMPLACEMENT);
    }

    @Test
    @Transactional
    void getAllOLTSByEmplacementIsInShouldWork() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where emplacement in
        defaultOLTFiltering("emplacement.in=" + DEFAULT_EMPLACEMENT + "," + UPDATED_EMPLACEMENT, "emplacement.in=" + UPDATED_EMPLACEMENT);
    }

    @Test
    @Transactional
    void getAllOLTSByEmplacementIsNullOrNotNull() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where emplacement is not null
        defaultOLTFiltering("emplacement.specified=true", "emplacement.specified=false");
    }

    @Test
    @Transactional
    void getAllOLTSByEmplacementContainsSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where emplacement contains
        defaultOLTFiltering("emplacement.contains=" + DEFAULT_EMPLACEMENT, "emplacement.contains=" + UPDATED_EMPLACEMENT);
    }

    @Test
    @Transactional
    void getAllOLTSByEmplacementNotContainsSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where emplacement does not contain
        defaultOLTFiltering("emplacement.doesNotContain=" + UPDATED_EMPLACEMENT, "emplacement.doesNotContain=" + DEFAULT_EMPLACEMENT);
    }

    @Test
    @Transactional
    void getAllOLTSByTypeCarteIsEqualToSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where typeCarte equals to
        defaultOLTFiltering("typeCarte.equals=" + DEFAULT_TYPE_CARTE, "typeCarte.equals=" + UPDATED_TYPE_CARTE);
    }

    @Test
    @Transactional
    void getAllOLTSByTypeCarteIsInShouldWork() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where typeCarte in
        defaultOLTFiltering("typeCarte.in=" + DEFAULT_TYPE_CARTE + "," + UPDATED_TYPE_CARTE, "typeCarte.in=" + UPDATED_TYPE_CARTE);
    }

    @Test
    @Transactional
    void getAllOLTSByTypeCarteIsNullOrNotNull() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where typeCarte is not null
        defaultOLTFiltering("typeCarte.specified=true", "typeCarte.specified=false");
    }

    @Test
    @Transactional
    void getAllOLTSByTypeCarteContainsSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where typeCarte contains
        defaultOLTFiltering("typeCarte.contains=" + DEFAULT_TYPE_CARTE, "typeCarte.contains=" + UPDATED_TYPE_CARTE);
    }

    @Test
    @Transactional
    void getAllOLTSByTypeCarteNotContainsSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where typeCarte does not contain
        defaultOLTFiltering("typeCarte.doesNotContain=" + UPDATED_TYPE_CARTE, "typeCarte.doesNotContain=" + DEFAULT_TYPE_CARTE);
    }

    @Test
    @Transactional
    void getAllOLTSByLatitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where latitude equals to
        defaultOLTFiltering("latitude.equals=" + DEFAULT_LATITUDE, "latitude.equals=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllOLTSByLatitudeIsInShouldWork() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where latitude in
        defaultOLTFiltering("latitude.in=" + DEFAULT_LATITUDE + "," + UPDATED_LATITUDE, "latitude.in=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllOLTSByLatitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where latitude is not null
        defaultOLTFiltering("latitude.specified=true", "latitude.specified=false");
    }

    @Test
    @Transactional
    void getAllOLTSByLatitudeContainsSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where latitude contains
        defaultOLTFiltering("latitude.contains=" + DEFAULT_LATITUDE, "latitude.contains=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllOLTSByLatitudeNotContainsSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where latitude does not contain
        defaultOLTFiltering("latitude.doesNotContain=" + UPDATED_LATITUDE, "latitude.doesNotContain=" + DEFAULT_LATITUDE);
    }

    @Test
    @Transactional
    void getAllOLTSByLongitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where longitude equals to
        defaultOLTFiltering("longitude.equals=" + DEFAULT_LONGITUDE, "longitude.equals=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllOLTSByLongitudeIsInShouldWork() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where longitude in
        defaultOLTFiltering("longitude.in=" + DEFAULT_LONGITUDE + "," + UPDATED_LONGITUDE, "longitude.in=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllOLTSByLongitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where longitude is not null
        defaultOLTFiltering("longitude.specified=true", "longitude.specified=false");
    }

    @Test
    @Transactional
    void getAllOLTSByLongitudeContainsSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where longitude contains
        defaultOLTFiltering("longitude.contains=" + DEFAULT_LONGITUDE, "longitude.contains=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllOLTSByLongitudeNotContainsSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where longitude does not contain
        defaultOLTFiltering("longitude.doesNotContain=" + UPDATED_LONGITUDE, "longitude.doesNotContain=" + DEFAULT_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllOLTSByCapaciteIsEqualToSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where capacite equals to
        defaultOLTFiltering("capacite.equals=" + DEFAULT_CAPACITE, "capacite.equals=" + UPDATED_CAPACITE);
    }

    @Test
    @Transactional
    void getAllOLTSByCapaciteIsInShouldWork() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where capacite in
        defaultOLTFiltering("capacite.in=" + DEFAULT_CAPACITE + "," + UPDATED_CAPACITE, "capacite.in=" + UPDATED_CAPACITE);
    }

    @Test
    @Transactional
    void getAllOLTSByCapaciteIsNullOrNotNull() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where capacite is not null
        defaultOLTFiltering("capacite.specified=true", "capacite.specified=false");
    }

    @Test
    @Transactional
    void getAllOLTSByCapaciteContainsSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where capacite contains
        defaultOLTFiltering("capacite.contains=" + DEFAULT_CAPACITE, "capacite.contains=" + UPDATED_CAPACITE);
    }

    @Test
    @Transactional
    void getAllOLTSByCapaciteNotContainsSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where capacite does not contain
        defaultOLTFiltering("capacite.doesNotContain=" + UPDATED_CAPACITE, "capacite.doesNotContain=" + DEFAULT_CAPACITE);
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
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].ip").value(hasItem(DEFAULT_IP)))
            .andExpect(jsonPath("$.[*].vendeur").value(hasItem(DEFAULT_VENDEUR)))
            .andExpect(jsonPath("$.[*].typeEquipment").value(hasItem(DEFAULT_TYPE_EQUIPMENT)))
            .andExpect(jsonPath("$.[*].codeEquipment").value(hasItem(DEFAULT_CODE_EQUIPMENT)))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE)))
            .andExpect(jsonPath("$.[*].emplacement").value(hasItem(DEFAULT_EMPLACEMENT)))
            .andExpect(jsonPath("$.[*].typeCarte").value(hasItem(DEFAULT_TYPE_CARTE)))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE)))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE)))
            .andExpect(jsonPath("$.[*].capacite").value(hasItem(DEFAULT_CAPACITE)))
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
            .libelle(UPDATED_LIBELLE)
            .ip(UPDATED_IP)
            .vendeur(UPDATED_VENDEUR)
            .typeEquipment(UPDATED_TYPE_EQUIPMENT)
            .codeEquipment(UPDATED_CODE_EQUIPMENT)
            .adresse(UPDATED_ADRESSE)
            .emplacement(UPDATED_EMPLACEMENT)
            .typeCarte(UPDATED_TYPE_CARTE)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .capacite(UPDATED_CAPACITE)
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

        partialUpdatedOLT
            .vendeur(UPDATED_VENDEUR)
            .typeEquipment(UPDATED_TYPE_EQUIPMENT)
            .adresse(UPDATED_ADRESSE)
            .emplacement(UPDATED_EMPLACEMENT)
            .typeCarte(UPDATED_TYPE_CARTE)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .capacite(UPDATED_CAPACITE)
            .etat(UPDATED_ETAT)
            .createdAt(UPDATED_CREATED_AT);

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
            .libelle(UPDATED_LIBELLE)
            .ip(UPDATED_IP)
            .vendeur(UPDATED_VENDEUR)
            .typeEquipment(UPDATED_TYPE_EQUIPMENT)
            .codeEquipment(UPDATED_CODE_EQUIPMENT)
            .adresse(UPDATED_ADRESSE)
            .emplacement(UPDATED_EMPLACEMENT)
            .typeCarte(UPDATED_TYPE_CARTE)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .capacite(UPDATED_CAPACITE)
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
