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
import sn.sonatel.dsi.ins.ftsirc.domain.OLT;
import sn.sonatel.dsi.ins.ftsirc.domain.ONT;
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
        int databaseSizeBeforeCreate = oLTRepository.findAll().size();
        // Create the OLT
        OLTDTO oLTDTO = oLTMapper.toDto(oLT);
        restOLTMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(oLTDTO))
            )
            .andExpect(status().isCreated());

        // Validate the OLT in the database
        List<OLT> oLTList = oLTRepository.findAll();
        assertThat(oLTList).hasSize(databaseSizeBeforeCreate + 1);
        OLT testOLT = oLTList.get(oLTList.size() - 1);
        assertThat(testOLT.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testOLT.getIp()).isEqualTo(DEFAULT_IP);
        assertThat(testOLT.getVendeur()).isEqualTo(DEFAULT_VENDEUR);
        assertThat(testOLT.getTypeEquipment()).isEqualTo(DEFAULT_TYPE_EQUIPMENT);
        assertThat(testOLT.getCodeEquipment()).isEqualTo(DEFAULT_CODE_EQUIPMENT);
        assertThat(testOLT.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testOLT.getEmplacement()).isEqualTo(DEFAULT_EMPLACEMENT);
        assertThat(testOLT.getTypeCarte()).isEqualTo(DEFAULT_TYPE_CARTE);
        assertThat(testOLT.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testOLT.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
        assertThat(testOLT.getCapacite()).isEqualTo(DEFAULT_CAPACITE);
        assertThat(testOLT.getEtat()).isEqualTo(DEFAULT_ETAT);
        assertThat(testOLT.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testOLT.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createOLTWithExistingId() throws Exception {
        // Create the OLT with an existing ID
        oLT.setId(1L);
        OLTDTO oLTDTO = oLTMapper.toDto(oLT);

        int databaseSizeBeforeCreate = oLTRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOLTMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(oLTDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OLT in the database
        List<OLT> oLTList = oLTRepository.findAll();
        assertThat(oLTList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = oLTRepository.findAll().size();
        // set the field null
        oLT.setLibelle(null);

        // Create the OLT, which fails.
        OLTDTO oLTDTO = oLTMapper.toDto(oLT);

        restOLTMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(oLTDTO))
            )
            .andExpect(status().isBadRequest());

        List<OLT> oLTList = oLTRepository.findAll();
        assertThat(oLTList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIpIsRequired() throws Exception {
        int databaseSizeBeforeTest = oLTRepository.findAll().size();
        // set the field null
        oLT.setIp(null);

        // Create the OLT, which fails.
        OLTDTO oLTDTO = oLTMapper.toDto(oLT);

        restOLTMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(oLTDTO))
            )
            .andExpect(status().isBadRequest());

        List<OLT> oLTList = oLTRepository.findAll();
        assertThat(oLTList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVendeurIsRequired() throws Exception {
        int databaseSizeBeforeTest = oLTRepository.findAll().size();
        // set the field null
        oLT.setVendeur(null);

        // Create the OLT, which fails.
        OLTDTO oLTDTO = oLTMapper.toDto(oLT);

        restOLTMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(oLTDTO))
            )
            .andExpect(status().isBadRequest());

        List<OLT> oLTList = oLTRepository.findAll();
        assertThat(oLTList).hasSize(databaseSizeBeforeTest);
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

        defaultOLTShouldBeFound("id.equals=" + id);
        defaultOLTShouldNotBeFound("id.notEquals=" + id);

        defaultOLTShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultOLTShouldNotBeFound("id.greaterThan=" + id);

        defaultOLTShouldBeFound("id.lessThanOrEqual=" + id);
        defaultOLTShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOLTSByLibelleIsEqualToSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where libelle equals to DEFAULT_LIBELLE
        defaultOLTShouldBeFound("libelle.equals=" + DEFAULT_LIBELLE);

        // Get all the oLTList where libelle equals to UPDATED_LIBELLE
        defaultOLTShouldNotBeFound("libelle.equals=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllOLTSByLibelleIsInShouldWork() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where libelle in DEFAULT_LIBELLE or UPDATED_LIBELLE
        defaultOLTShouldBeFound("libelle.in=" + DEFAULT_LIBELLE + "," + UPDATED_LIBELLE);

        // Get all the oLTList where libelle equals to UPDATED_LIBELLE
        defaultOLTShouldNotBeFound("libelle.in=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllOLTSByLibelleIsNullOrNotNull() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where libelle is not null
        defaultOLTShouldBeFound("libelle.specified=true");

        // Get all the oLTList where libelle is null
        defaultOLTShouldNotBeFound("libelle.specified=false");
    }

    @Test
    @Transactional
    void getAllOLTSByLibelleContainsSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where libelle contains DEFAULT_LIBELLE
        defaultOLTShouldBeFound("libelle.contains=" + DEFAULT_LIBELLE);

        // Get all the oLTList where libelle contains UPDATED_LIBELLE
        defaultOLTShouldNotBeFound("libelle.contains=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllOLTSByLibelleNotContainsSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where libelle does not contain DEFAULT_LIBELLE
        defaultOLTShouldNotBeFound("libelle.doesNotContain=" + DEFAULT_LIBELLE);

        // Get all the oLTList where libelle does not contain UPDATED_LIBELLE
        defaultOLTShouldBeFound("libelle.doesNotContain=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllOLTSByIpIsEqualToSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where ip equals to DEFAULT_IP
        defaultOLTShouldBeFound("ip.equals=" + DEFAULT_IP);

        // Get all the oLTList where ip equals to UPDATED_IP
        defaultOLTShouldNotBeFound("ip.equals=" + UPDATED_IP);
    }

    @Test
    @Transactional
    void getAllOLTSByIpIsInShouldWork() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where ip in DEFAULT_IP or UPDATED_IP
        defaultOLTShouldBeFound("ip.in=" + DEFAULT_IP + "," + UPDATED_IP);

        // Get all the oLTList where ip equals to UPDATED_IP
        defaultOLTShouldNotBeFound("ip.in=" + UPDATED_IP);
    }

    @Test
    @Transactional
    void getAllOLTSByIpIsNullOrNotNull() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where ip is not null
        defaultOLTShouldBeFound("ip.specified=true");

        // Get all the oLTList where ip is null
        defaultOLTShouldNotBeFound("ip.specified=false");
    }

    @Test
    @Transactional
    void getAllOLTSByIpContainsSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where ip contains DEFAULT_IP
        defaultOLTShouldBeFound("ip.contains=" + DEFAULT_IP);

        // Get all the oLTList where ip contains UPDATED_IP
        defaultOLTShouldNotBeFound("ip.contains=" + UPDATED_IP);
    }

    @Test
    @Transactional
    void getAllOLTSByIpNotContainsSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where ip does not contain DEFAULT_IP
        defaultOLTShouldNotBeFound("ip.doesNotContain=" + DEFAULT_IP);

        // Get all the oLTList where ip does not contain UPDATED_IP
        defaultOLTShouldBeFound("ip.doesNotContain=" + UPDATED_IP);
    }

    @Test
    @Transactional
    void getAllOLTSByVendeurIsEqualToSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where vendeur equals to DEFAULT_VENDEUR
        defaultOLTShouldBeFound("vendeur.equals=" + DEFAULT_VENDEUR);

        // Get all the oLTList where vendeur equals to UPDATED_VENDEUR
        defaultOLTShouldNotBeFound("vendeur.equals=" + UPDATED_VENDEUR);
    }

    @Test
    @Transactional
    void getAllOLTSByVendeurIsInShouldWork() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where vendeur in DEFAULT_VENDEUR or UPDATED_VENDEUR
        defaultOLTShouldBeFound("vendeur.in=" + DEFAULT_VENDEUR + "," + UPDATED_VENDEUR);

        // Get all the oLTList where vendeur equals to UPDATED_VENDEUR
        defaultOLTShouldNotBeFound("vendeur.in=" + UPDATED_VENDEUR);
    }

    @Test
    @Transactional
    void getAllOLTSByVendeurIsNullOrNotNull() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where vendeur is not null
        defaultOLTShouldBeFound("vendeur.specified=true");

        // Get all the oLTList where vendeur is null
        defaultOLTShouldNotBeFound("vendeur.specified=false");
    }

    @Test
    @Transactional
    void getAllOLTSByVendeurContainsSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where vendeur contains DEFAULT_VENDEUR
        defaultOLTShouldBeFound("vendeur.contains=" + DEFAULT_VENDEUR);

        // Get all the oLTList where vendeur contains UPDATED_VENDEUR
        defaultOLTShouldNotBeFound("vendeur.contains=" + UPDATED_VENDEUR);
    }

    @Test
    @Transactional
    void getAllOLTSByVendeurNotContainsSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where vendeur does not contain DEFAULT_VENDEUR
        defaultOLTShouldNotBeFound("vendeur.doesNotContain=" + DEFAULT_VENDEUR);

        // Get all the oLTList where vendeur does not contain UPDATED_VENDEUR
        defaultOLTShouldBeFound("vendeur.doesNotContain=" + UPDATED_VENDEUR);
    }

    @Test
    @Transactional
    void getAllOLTSByTypeEquipmentIsEqualToSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where typeEquipment equals to DEFAULT_TYPE_EQUIPMENT
        defaultOLTShouldBeFound("typeEquipment.equals=" + DEFAULT_TYPE_EQUIPMENT);

        // Get all the oLTList where typeEquipment equals to UPDATED_TYPE_EQUIPMENT
        defaultOLTShouldNotBeFound("typeEquipment.equals=" + UPDATED_TYPE_EQUIPMENT);
    }

    @Test
    @Transactional
    void getAllOLTSByTypeEquipmentIsInShouldWork() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where typeEquipment in DEFAULT_TYPE_EQUIPMENT or UPDATED_TYPE_EQUIPMENT
        defaultOLTShouldBeFound("typeEquipment.in=" + DEFAULT_TYPE_EQUIPMENT + "," + UPDATED_TYPE_EQUIPMENT);

        // Get all the oLTList where typeEquipment equals to UPDATED_TYPE_EQUIPMENT
        defaultOLTShouldNotBeFound("typeEquipment.in=" + UPDATED_TYPE_EQUIPMENT);
    }

    @Test
    @Transactional
    void getAllOLTSByTypeEquipmentIsNullOrNotNull() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where typeEquipment is not null
        defaultOLTShouldBeFound("typeEquipment.specified=true");

        // Get all the oLTList where typeEquipment is null
        defaultOLTShouldNotBeFound("typeEquipment.specified=false");
    }

    @Test
    @Transactional
    void getAllOLTSByTypeEquipmentContainsSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where typeEquipment contains DEFAULT_TYPE_EQUIPMENT
        defaultOLTShouldBeFound("typeEquipment.contains=" + DEFAULT_TYPE_EQUIPMENT);

        // Get all the oLTList where typeEquipment contains UPDATED_TYPE_EQUIPMENT
        defaultOLTShouldNotBeFound("typeEquipment.contains=" + UPDATED_TYPE_EQUIPMENT);
    }

    @Test
    @Transactional
    void getAllOLTSByTypeEquipmentNotContainsSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where typeEquipment does not contain DEFAULT_TYPE_EQUIPMENT
        defaultOLTShouldNotBeFound("typeEquipment.doesNotContain=" + DEFAULT_TYPE_EQUIPMENT);

        // Get all the oLTList where typeEquipment does not contain UPDATED_TYPE_EQUIPMENT
        defaultOLTShouldBeFound("typeEquipment.doesNotContain=" + UPDATED_TYPE_EQUIPMENT);
    }

    @Test
    @Transactional
    void getAllOLTSByCodeEquipmentIsEqualToSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where codeEquipment equals to DEFAULT_CODE_EQUIPMENT
        defaultOLTShouldBeFound("codeEquipment.equals=" + DEFAULT_CODE_EQUIPMENT);

        // Get all the oLTList where codeEquipment equals to UPDATED_CODE_EQUIPMENT
        defaultOLTShouldNotBeFound("codeEquipment.equals=" + UPDATED_CODE_EQUIPMENT);
    }

    @Test
    @Transactional
    void getAllOLTSByCodeEquipmentIsInShouldWork() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where codeEquipment in DEFAULT_CODE_EQUIPMENT or UPDATED_CODE_EQUIPMENT
        defaultOLTShouldBeFound("codeEquipment.in=" + DEFAULT_CODE_EQUIPMENT + "," + UPDATED_CODE_EQUIPMENT);

        // Get all the oLTList where codeEquipment equals to UPDATED_CODE_EQUIPMENT
        defaultOLTShouldNotBeFound("codeEquipment.in=" + UPDATED_CODE_EQUIPMENT);
    }

    @Test
    @Transactional
    void getAllOLTSByCodeEquipmentIsNullOrNotNull() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where codeEquipment is not null
        defaultOLTShouldBeFound("codeEquipment.specified=true");

        // Get all the oLTList where codeEquipment is null
        defaultOLTShouldNotBeFound("codeEquipment.specified=false");
    }

    @Test
    @Transactional
    void getAllOLTSByCodeEquipmentContainsSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where codeEquipment contains DEFAULT_CODE_EQUIPMENT
        defaultOLTShouldBeFound("codeEquipment.contains=" + DEFAULT_CODE_EQUIPMENT);

        // Get all the oLTList where codeEquipment contains UPDATED_CODE_EQUIPMENT
        defaultOLTShouldNotBeFound("codeEquipment.contains=" + UPDATED_CODE_EQUIPMENT);
    }

    @Test
    @Transactional
    void getAllOLTSByCodeEquipmentNotContainsSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where codeEquipment does not contain DEFAULT_CODE_EQUIPMENT
        defaultOLTShouldNotBeFound("codeEquipment.doesNotContain=" + DEFAULT_CODE_EQUIPMENT);

        // Get all the oLTList where codeEquipment does not contain UPDATED_CODE_EQUIPMENT
        defaultOLTShouldBeFound("codeEquipment.doesNotContain=" + UPDATED_CODE_EQUIPMENT);
    }

    @Test
    @Transactional
    void getAllOLTSByAdresseIsEqualToSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where adresse equals to DEFAULT_ADRESSE
        defaultOLTShouldBeFound("adresse.equals=" + DEFAULT_ADRESSE);

        // Get all the oLTList where adresse equals to UPDATED_ADRESSE
        defaultOLTShouldNotBeFound("adresse.equals=" + UPDATED_ADRESSE);
    }

    @Test
    @Transactional
    void getAllOLTSByAdresseIsInShouldWork() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where adresse in DEFAULT_ADRESSE or UPDATED_ADRESSE
        defaultOLTShouldBeFound("adresse.in=" + DEFAULT_ADRESSE + "," + UPDATED_ADRESSE);

        // Get all the oLTList where adresse equals to UPDATED_ADRESSE
        defaultOLTShouldNotBeFound("adresse.in=" + UPDATED_ADRESSE);
    }

    @Test
    @Transactional
    void getAllOLTSByAdresseIsNullOrNotNull() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where adresse is not null
        defaultOLTShouldBeFound("adresse.specified=true");

        // Get all the oLTList where adresse is null
        defaultOLTShouldNotBeFound("adresse.specified=false");
    }

    @Test
    @Transactional
    void getAllOLTSByAdresseContainsSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where adresse contains DEFAULT_ADRESSE
        defaultOLTShouldBeFound("adresse.contains=" + DEFAULT_ADRESSE);

        // Get all the oLTList where adresse contains UPDATED_ADRESSE
        defaultOLTShouldNotBeFound("adresse.contains=" + UPDATED_ADRESSE);
    }

    @Test
    @Transactional
    void getAllOLTSByAdresseNotContainsSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where adresse does not contain DEFAULT_ADRESSE
        defaultOLTShouldNotBeFound("adresse.doesNotContain=" + DEFAULT_ADRESSE);

        // Get all the oLTList where adresse does not contain UPDATED_ADRESSE
        defaultOLTShouldBeFound("adresse.doesNotContain=" + UPDATED_ADRESSE);
    }

    @Test
    @Transactional
    void getAllOLTSByEmplacementIsEqualToSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where emplacement equals to DEFAULT_EMPLACEMENT
        defaultOLTShouldBeFound("emplacement.equals=" + DEFAULT_EMPLACEMENT);

        // Get all the oLTList where emplacement equals to UPDATED_EMPLACEMENT
        defaultOLTShouldNotBeFound("emplacement.equals=" + UPDATED_EMPLACEMENT);
    }

    @Test
    @Transactional
    void getAllOLTSByEmplacementIsInShouldWork() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where emplacement in DEFAULT_EMPLACEMENT or UPDATED_EMPLACEMENT
        defaultOLTShouldBeFound("emplacement.in=" + DEFAULT_EMPLACEMENT + "," + UPDATED_EMPLACEMENT);

        // Get all the oLTList where emplacement equals to UPDATED_EMPLACEMENT
        defaultOLTShouldNotBeFound("emplacement.in=" + UPDATED_EMPLACEMENT);
    }

    @Test
    @Transactional
    void getAllOLTSByEmplacementIsNullOrNotNull() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where emplacement is not null
        defaultOLTShouldBeFound("emplacement.specified=true");

        // Get all the oLTList where emplacement is null
        defaultOLTShouldNotBeFound("emplacement.specified=false");
    }

    @Test
    @Transactional
    void getAllOLTSByEmplacementContainsSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where emplacement contains DEFAULT_EMPLACEMENT
        defaultOLTShouldBeFound("emplacement.contains=" + DEFAULT_EMPLACEMENT);

        // Get all the oLTList where emplacement contains UPDATED_EMPLACEMENT
        defaultOLTShouldNotBeFound("emplacement.contains=" + UPDATED_EMPLACEMENT);
    }

    @Test
    @Transactional
    void getAllOLTSByEmplacementNotContainsSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where emplacement does not contain DEFAULT_EMPLACEMENT
        defaultOLTShouldNotBeFound("emplacement.doesNotContain=" + DEFAULT_EMPLACEMENT);

        // Get all the oLTList where emplacement does not contain UPDATED_EMPLACEMENT
        defaultOLTShouldBeFound("emplacement.doesNotContain=" + UPDATED_EMPLACEMENT);
    }

    @Test
    @Transactional
    void getAllOLTSByTypeCarteIsEqualToSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where typeCarte equals to DEFAULT_TYPE_CARTE
        defaultOLTShouldBeFound("typeCarte.equals=" + DEFAULT_TYPE_CARTE);

        // Get all the oLTList where typeCarte equals to UPDATED_TYPE_CARTE
        defaultOLTShouldNotBeFound("typeCarte.equals=" + UPDATED_TYPE_CARTE);
    }

    @Test
    @Transactional
    void getAllOLTSByTypeCarteIsInShouldWork() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where typeCarte in DEFAULT_TYPE_CARTE or UPDATED_TYPE_CARTE
        defaultOLTShouldBeFound("typeCarte.in=" + DEFAULT_TYPE_CARTE + "," + UPDATED_TYPE_CARTE);

        // Get all the oLTList where typeCarte equals to UPDATED_TYPE_CARTE
        defaultOLTShouldNotBeFound("typeCarte.in=" + UPDATED_TYPE_CARTE);
    }

    @Test
    @Transactional
    void getAllOLTSByTypeCarteIsNullOrNotNull() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where typeCarte is not null
        defaultOLTShouldBeFound("typeCarte.specified=true");

        // Get all the oLTList where typeCarte is null
        defaultOLTShouldNotBeFound("typeCarte.specified=false");
    }

    @Test
    @Transactional
    void getAllOLTSByTypeCarteContainsSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where typeCarte contains DEFAULT_TYPE_CARTE
        defaultOLTShouldBeFound("typeCarte.contains=" + DEFAULT_TYPE_CARTE);

        // Get all the oLTList where typeCarte contains UPDATED_TYPE_CARTE
        defaultOLTShouldNotBeFound("typeCarte.contains=" + UPDATED_TYPE_CARTE);
    }

    @Test
    @Transactional
    void getAllOLTSByTypeCarteNotContainsSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where typeCarte does not contain DEFAULT_TYPE_CARTE
        defaultOLTShouldNotBeFound("typeCarte.doesNotContain=" + DEFAULT_TYPE_CARTE);

        // Get all the oLTList where typeCarte does not contain UPDATED_TYPE_CARTE
        defaultOLTShouldBeFound("typeCarte.doesNotContain=" + UPDATED_TYPE_CARTE);
    }

    @Test
    @Transactional
    void getAllOLTSByLatitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where latitude equals to DEFAULT_LATITUDE
        defaultOLTShouldBeFound("latitude.equals=" + DEFAULT_LATITUDE);

        // Get all the oLTList where latitude equals to UPDATED_LATITUDE
        defaultOLTShouldNotBeFound("latitude.equals=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllOLTSByLatitudeIsInShouldWork() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where latitude in DEFAULT_LATITUDE or UPDATED_LATITUDE
        defaultOLTShouldBeFound("latitude.in=" + DEFAULT_LATITUDE + "," + UPDATED_LATITUDE);

        // Get all the oLTList where latitude equals to UPDATED_LATITUDE
        defaultOLTShouldNotBeFound("latitude.in=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllOLTSByLatitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where latitude is not null
        defaultOLTShouldBeFound("latitude.specified=true");

        // Get all the oLTList where latitude is null
        defaultOLTShouldNotBeFound("latitude.specified=false");
    }

    @Test
    @Transactional
    void getAllOLTSByLatitudeContainsSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where latitude contains DEFAULT_LATITUDE
        defaultOLTShouldBeFound("latitude.contains=" + DEFAULT_LATITUDE);

        // Get all the oLTList where latitude contains UPDATED_LATITUDE
        defaultOLTShouldNotBeFound("latitude.contains=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllOLTSByLatitudeNotContainsSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where latitude does not contain DEFAULT_LATITUDE
        defaultOLTShouldNotBeFound("latitude.doesNotContain=" + DEFAULT_LATITUDE);

        // Get all the oLTList where latitude does not contain UPDATED_LATITUDE
        defaultOLTShouldBeFound("latitude.doesNotContain=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllOLTSByLongitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where longitude equals to DEFAULT_LONGITUDE
        defaultOLTShouldBeFound("longitude.equals=" + DEFAULT_LONGITUDE);

        // Get all the oLTList where longitude equals to UPDATED_LONGITUDE
        defaultOLTShouldNotBeFound("longitude.equals=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllOLTSByLongitudeIsInShouldWork() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where longitude in DEFAULT_LONGITUDE or UPDATED_LONGITUDE
        defaultOLTShouldBeFound("longitude.in=" + DEFAULT_LONGITUDE + "," + UPDATED_LONGITUDE);

        // Get all the oLTList where longitude equals to UPDATED_LONGITUDE
        defaultOLTShouldNotBeFound("longitude.in=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllOLTSByLongitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where longitude is not null
        defaultOLTShouldBeFound("longitude.specified=true");

        // Get all the oLTList where longitude is null
        defaultOLTShouldNotBeFound("longitude.specified=false");
    }

    @Test
    @Transactional
    void getAllOLTSByLongitudeContainsSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where longitude contains DEFAULT_LONGITUDE
        defaultOLTShouldBeFound("longitude.contains=" + DEFAULT_LONGITUDE);

        // Get all the oLTList where longitude contains UPDATED_LONGITUDE
        defaultOLTShouldNotBeFound("longitude.contains=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllOLTSByLongitudeNotContainsSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where longitude does not contain DEFAULT_LONGITUDE
        defaultOLTShouldNotBeFound("longitude.doesNotContain=" + DEFAULT_LONGITUDE);

        // Get all the oLTList where longitude does not contain UPDATED_LONGITUDE
        defaultOLTShouldBeFound("longitude.doesNotContain=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllOLTSByCapaciteIsEqualToSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where capacite equals to DEFAULT_CAPACITE
        defaultOLTShouldBeFound("capacite.equals=" + DEFAULT_CAPACITE);

        // Get all the oLTList where capacite equals to UPDATED_CAPACITE
        defaultOLTShouldNotBeFound("capacite.equals=" + UPDATED_CAPACITE);
    }

    @Test
    @Transactional
    void getAllOLTSByCapaciteIsInShouldWork() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where capacite in DEFAULT_CAPACITE or UPDATED_CAPACITE
        defaultOLTShouldBeFound("capacite.in=" + DEFAULT_CAPACITE + "," + UPDATED_CAPACITE);

        // Get all the oLTList where capacite equals to UPDATED_CAPACITE
        defaultOLTShouldNotBeFound("capacite.in=" + UPDATED_CAPACITE);
    }

    @Test
    @Transactional
    void getAllOLTSByCapaciteIsNullOrNotNull() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where capacite is not null
        defaultOLTShouldBeFound("capacite.specified=true");

        // Get all the oLTList where capacite is null
        defaultOLTShouldNotBeFound("capacite.specified=false");
    }

    @Test
    @Transactional
    void getAllOLTSByCapaciteContainsSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where capacite contains DEFAULT_CAPACITE
        defaultOLTShouldBeFound("capacite.contains=" + DEFAULT_CAPACITE);

        // Get all the oLTList where capacite contains UPDATED_CAPACITE
        defaultOLTShouldNotBeFound("capacite.contains=" + UPDATED_CAPACITE);
    }

    @Test
    @Transactional
    void getAllOLTSByCapaciteNotContainsSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where capacite does not contain DEFAULT_CAPACITE
        defaultOLTShouldNotBeFound("capacite.doesNotContain=" + DEFAULT_CAPACITE);

        // Get all the oLTList where capacite does not contain UPDATED_CAPACITE
        defaultOLTShouldBeFound("capacite.doesNotContain=" + UPDATED_CAPACITE);
    }

    @Test
    @Transactional
    void getAllOLTSByEtatIsEqualToSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where etat equals to DEFAULT_ETAT
        defaultOLTShouldBeFound("etat.equals=" + DEFAULT_ETAT);

        // Get all the oLTList where etat equals to UPDATED_ETAT
        defaultOLTShouldNotBeFound("etat.equals=" + UPDATED_ETAT);
    }

    @Test
    @Transactional
    void getAllOLTSByEtatIsInShouldWork() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where etat in DEFAULT_ETAT or UPDATED_ETAT
        defaultOLTShouldBeFound("etat.in=" + DEFAULT_ETAT + "," + UPDATED_ETAT);

        // Get all the oLTList where etat equals to UPDATED_ETAT
        defaultOLTShouldNotBeFound("etat.in=" + UPDATED_ETAT);
    }

    @Test
    @Transactional
    void getAllOLTSByEtatIsNullOrNotNull() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where etat is not null
        defaultOLTShouldBeFound("etat.specified=true");

        // Get all the oLTList where etat is null
        defaultOLTShouldNotBeFound("etat.specified=false");
    }

    @Test
    @Transactional
    void getAllOLTSByEtatContainsSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where etat contains DEFAULT_ETAT
        defaultOLTShouldBeFound("etat.contains=" + DEFAULT_ETAT);

        // Get all the oLTList where etat contains UPDATED_ETAT
        defaultOLTShouldNotBeFound("etat.contains=" + UPDATED_ETAT);
    }

    @Test
    @Transactional
    void getAllOLTSByEtatNotContainsSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where etat does not contain DEFAULT_ETAT
        defaultOLTShouldNotBeFound("etat.doesNotContain=" + DEFAULT_ETAT);

        // Get all the oLTList where etat does not contain UPDATED_ETAT
        defaultOLTShouldBeFound("etat.doesNotContain=" + UPDATED_ETAT);
    }

    @Test
    @Transactional
    void getAllOLTSByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where createdAt equals to DEFAULT_CREATED_AT
        defaultOLTShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the oLTList where createdAt equals to UPDATED_CREATED_AT
        defaultOLTShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllOLTSByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultOLTShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the oLTList where createdAt equals to UPDATED_CREATED_AT
        defaultOLTShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllOLTSByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where createdAt is not null
        defaultOLTShouldBeFound("createdAt.specified=true");

        // Get all the oLTList where createdAt is null
        defaultOLTShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllOLTSByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where createdAt is greater than or equal to DEFAULT_CREATED_AT
        defaultOLTShouldBeFound("createdAt.greaterThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the oLTList where createdAt is greater than or equal to UPDATED_CREATED_AT
        defaultOLTShouldNotBeFound("createdAt.greaterThanOrEqual=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllOLTSByCreatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where createdAt is less than or equal to DEFAULT_CREATED_AT
        defaultOLTShouldBeFound("createdAt.lessThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the oLTList where createdAt is less than or equal to SMALLER_CREATED_AT
        defaultOLTShouldNotBeFound("createdAt.lessThanOrEqual=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllOLTSByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where createdAt is less than DEFAULT_CREATED_AT
        defaultOLTShouldNotBeFound("createdAt.lessThan=" + DEFAULT_CREATED_AT);

        // Get all the oLTList where createdAt is less than UPDATED_CREATED_AT
        defaultOLTShouldBeFound("createdAt.lessThan=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllOLTSByCreatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where createdAt is greater than DEFAULT_CREATED_AT
        defaultOLTShouldNotBeFound("createdAt.greaterThan=" + DEFAULT_CREATED_AT);

        // Get all the oLTList where createdAt is greater than SMALLER_CREATED_AT
        defaultOLTShouldBeFound("createdAt.greaterThan=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllOLTSByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultOLTShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the oLTList where updatedAt equals to UPDATED_UPDATED_AT
        defaultOLTShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllOLTSByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultOLTShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the oLTList where updatedAt equals to UPDATED_UPDATED_AT
        defaultOLTShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllOLTSByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where updatedAt is not null
        defaultOLTShouldBeFound("updatedAt.specified=true");

        // Get all the oLTList where updatedAt is null
        defaultOLTShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllOLTSByUpdatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where updatedAt is greater than or equal to DEFAULT_UPDATED_AT
        defaultOLTShouldBeFound("updatedAt.greaterThanOrEqual=" + DEFAULT_UPDATED_AT);

        // Get all the oLTList where updatedAt is greater than or equal to UPDATED_UPDATED_AT
        defaultOLTShouldNotBeFound("updatedAt.greaterThanOrEqual=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllOLTSByUpdatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where updatedAt is less than or equal to DEFAULT_UPDATED_AT
        defaultOLTShouldBeFound("updatedAt.lessThanOrEqual=" + DEFAULT_UPDATED_AT);

        // Get all the oLTList where updatedAt is less than or equal to SMALLER_UPDATED_AT
        defaultOLTShouldNotBeFound("updatedAt.lessThanOrEqual=" + SMALLER_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllOLTSByUpdatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where updatedAt is less than DEFAULT_UPDATED_AT
        defaultOLTShouldNotBeFound("updatedAt.lessThan=" + DEFAULT_UPDATED_AT);

        // Get all the oLTList where updatedAt is less than UPDATED_UPDATED_AT
        defaultOLTShouldBeFound("updatedAt.lessThan=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllOLTSByUpdatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        // Get all the oLTList where updatedAt is greater than DEFAULT_UPDATED_AT
        defaultOLTShouldNotBeFound("updatedAt.greaterThan=" + DEFAULT_UPDATED_AT);

        // Get all the oLTList where updatedAt is greater than SMALLER_UPDATED_AT
        defaultOLTShouldBeFound("updatedAt.greaterThan=" + SMALLER_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllOLTSByOntIsEqualToSomething() throws Exception {
        ONT ont;
        if (TestUtil.findAll(em, ONT.class).isEmpty()) {
            oLTRepository.saveAndFlush(oLT);
            ont = ONTResourceIT.createEntity(em);
        } else {
            ont = TestUtil.findAll(em, ONT.class).get(0);
        }
        em.persist(ont);
        em.flush();
        oLT.addOnt(ont);
        oLTRepository.saveAndFlush(oLT);
        Long ontId = ont.getId();
        // Get all the oLTList where ont equals to ontId
        defaultOLTShouldBeFound("ontId.equals=" + ontId);

        // Get all the oLTList where ont equals to (ontId + 1)
        defaultOLTShouldNotBeFound("ontId.equals=" + (ontId + 1));
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

        int databaseSizeBeforeUpdate = oLTRepository.findAll().size();

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
                    .content(TestUtil.convertObjectToJsonBytes(oLTDTO))
            )
            .andExpect(status().isOk());

        // Validate the OLT in the database
        List<OLT> oLTList = oLTRepository.findAll();
        assertThat(oLTList).hasSize(databaseSizeBeforeUpdate);
        OLT testOLT = oLTList.get(oLTList.size() - 1);
        assertThat(testOLT.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testOLT.getIp()).isEqualTo(UPDATED_IP);
        assertThat(testOLT.getVendeur()).isEqualTo(UPDATED_VENDEUR);
        assertThat(testOLT.getTypeEquipment()).isEqualTo(UPDATED_TYPE_EQUIPMENT);
        assertThat(testOLT.getCodeEquipment()).isEqualTo(UPDATED_CODE_EQUIPMENT);
        assertThat(testOLT.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testOLT.getEmplacement()).isEqualTo(UPDATED_EMPLACEMENT);
        assertThat(testOLT.getTypeCarte()).isEqualTo(UPDATED_TYPE_CARTE);
        assertThat(testOLT.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testOLT.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testOLT.getCapacite()).isEqualTo(UPDATED_CAPACITE);
        assertThat(testOLT.getEtat()).isEqualTo(UPDATED_ETAT);
        assertThat(testOLT.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testOLT.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingOLT() throws Exception {
        int databaseSizeBeforeUpdate = oLTRepository.findAll().size();
        oLT.setId(longCount.incrementAndGet());

        // Create the OLT
        OLTDTO oLTDTO = oLTMapper.toDto(oLT);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOLTMockMvc
            .perform(
                put(ENTITY_API_URL_ID, oLTDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(oLTDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OLT in the database
        List<OLT> oLTList = oLTRepository.findAll();
        assertThat(oLTList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOLT() throws Exception {
        int databaseSizeBeforeUpdate = oLTRepository.findAll().size();
        oLT.setId(longCount.incrementAndGet());

        // Create the OLT
        OLTDTO oLTDTO = oLTMapper.toDto(oLT);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOLTMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(oLTDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OLT in the database
        List<OLT> oLTList = oLTRepository.findAll();
        assertThat(oLTList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOLT() throws Exception {
        int databaseSizeBeforeUpdate = oLTRepository.findAll().size();
        oLT.setId(longCount.incrementAndGet());

        // Create the OLT
        OLTDTO oLTDTO = oLTMapper.toDto(oLT);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOLTMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(oLTDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OLT in the database
        List<OLT> oLTList = oLTRepository.findAll();
        assertThat(oLTList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOLTWithPatch() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        int databaseSizeBeforeUpdate = oLTRepository.findAll().size();

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
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOLT))
            )
            .andExpect(status().isOk());

        // Validate the OLT in the database
        List<OLT> oLTList = oLTRepository.findAll();
        assertThat(oLTList).hasSize(databaseSizeBeforeUpdate);
        OLT testOLT = oLTList.get(oLTList.size() - 1);
        assertThat(testOLT.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testOLT.getIp()).isEqualTo(DEFAULT_IP);
        assertThat(testOLT.getVendeur()).isEqualTo(UPDATED_VENDEUR);
        assertThat(testOLT.getTypeEquipment()).isEqualTo(UPDATED_TYPE_EQUIPMENT);
        assertThat(testOLT.getCodeEquipment()).isEqualTo(DEFAULT_CODE_EQUIPMENT);
        assertThat(testOLT.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testOLT.getEmplacement()).isEqualTo(UPDATED_EMPLACEMENT);
        assertThat(testOLT.getTypeCarte()).isEqualTo(UPDATED_TYPE_CARTE);
        assertThat(testOLT.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testOLT.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testOLT.getCapacite()).isEqualTo(UPDATED_CAPACITE);
        assertThat(testOLT.getEtat()).isEqualTo(UPDATED_ETAT);
        assertThat(testOLT.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testOLT.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateOLTWithPatch() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        int databaseSizeBeforeUpdate = oLTRepository.findAll().size();

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
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOLT))
            )
            .andExpect(status().isOk());

        // Validate the OLT in the database
        List<OLT> oLTList = oLTRepository.findAll();
        assertThat(oLTList).hasSize(databaseSizeBeforeUpdate);
        OLT testOLT = oLTList.get(oLTList.size() - 1);
        assertThat(testOLT.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testOLT.getIp()).isEqualTo(UPDATED_IP);
        assertThat(testOLT.getVendeur()).isEqualTo(UPDATED_VENDEUR);
        assertThat(testOLT.getTypeEquipment()).isEqualTo(UPDATED_TYPE_EQUIPMENT);
        assertThat(testOLT.getCodeEquipment()).isEqualTo(UPDATED_CODE_EQUIPMENT);
        assertThat(testOLT.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testOLT.getEmplacement()).isEqualTo(UPDATED_EMPLACEMENT);
        assertThat(testOLT.getTypeCarte()).isEqualTo(UPDATED_TYPE_CARTE);
        assertThat(testOLT.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testOLT.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testOLT.getCapacite()).isEqualTo(UPDATED_CAPACITE);
        assertThat(testOLT.getEtat()).isEqualTo(UPDATED_ETAT);
        assertThat(testOLT.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testOLT.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingOLT() throws Exception {
        int databaseSizeBeforeUpdate = oLTRepository.findAll().size();
        oLT.setId(longCount.incrementAndGet());

        // Create the OLT
        OLTDTO oLTDTO = oLTMapper.toDto(oLT);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOLTMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, oLTDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(oLTDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OLT in the database
        List<OLT> oLTList = oLTRepository.findAll();
        assertThat(oLTList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOLT() throws Exception {
        int databaseSizeBeforeUpdate = oLTRepository.findAll().size();
        oLT.setId(longCount.incrementAndGet());

        // Create the OLT
        OLTDTO oLTDTO = oLTMapper.toDto(oLT);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOLTMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(oLTDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OLT in the database
        List<OLT> oLTList = oLTRepository.findAll();
        assertThat(oLTList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOLT() throws Exception {
        int databaseSizeBeforeUpdate = oLTRepository.findAll().size();
        oLT.setId(longCount.incrementAndGet());

        // Create the OLT
        OLTDTO oLTDTO = oLTMapper.toDto(oLT);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOLTMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(oLTDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OLT in the database
        List<OLT> oLTList = oLTRepository.findAll();
        assertThat(oLTList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOLT() throws Exception {
        // Initialize the database
        oLTRepository.saveAndFlush(oLT);

        int databaseSizeBeforeDelete = oLTRepository.findAll().size();

        // Delete the oLT
        restOLTMockMvc
            .perform(delete(ENTITY_API_URL_ID, oLT.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OLT> oLTList = oLTRepository.findAll();
        assertThat(oLTList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
