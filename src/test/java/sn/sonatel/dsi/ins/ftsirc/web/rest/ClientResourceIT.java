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
import sn.sonatel.dsi.ins.ftsirc.domain.Client;
import sn.sonatel.dsi.ins.ftsirc.domain.ONT;
import sn.sonatel.dsi.ins.ftsirc.domain.Offre;
import sn.sonatel.dsi.ins.ftsirc.repository.ClientRepository;
import sn.sonatel.dsi.ins.ftsirc.service.dto.ClientDTO;
import sn.sonatel.dsi.ins.ftsirc.service.mapper.ClientMapper;

/**
 * Integration tests for the {@link ClientResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ClientResourceIT {

    private static final Long DEFAULT_NCLIENT = 1L;
    private static final Long UPDATED_NCLIENT = 2L;
    private static final Long SMALLER_NCLIENT = 1L - 1L;

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM = "BBBBBBBBBB";

    private static final String DEFAULT_ETAT = "AAAAAAAAAA";
    private static final String UPDATED_ETAT = "BBBBBBBBBB";

    private static final String DEFAULT_NUMERO_FIXE = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_FIXE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_MOBILE_CLIENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_MOBILE_CLIENT = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_DOUBLONS = false;
    private static final Boolean UPDATED_IS_DOUBLONS = true;

    private static final String ENTITY_API_URL = "/api/clients";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ClientMapper clientMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClientMockMvc;

    private Client client;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Client createEntity(EntityManager em) {
        Client client = new Client()
            .nclient(DEFAULT_NCLIENT)
            .nom(DEFAULT_NOM)
            .prenom(DEFAULT_PRENOM)
            .etat(DEFAULT_ETAT)
            .numeroFixe(DEFAULT_NUMERO_FIXE)
            .contactMobileClient(DEFAULT_CONTACT_MOBILE_CLIENT)
            .isDoublons(DEFAULT_IS_DOUBLONS);
        return client;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Client createUpdatedEntity(EntityManager em) {
        Client client = new Client()
            .nclient(UPDATED_NCLIENT)
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .etat(UPDATED_ETAT)
            .numeroFixe(UPDATED_NUMERO_FIXE)
            .contactMobileClient(UPDATED_CONTACT_MOBILE_CLIENT)
            .isDoublons(UPDATED_IS_DOUBLONS);
        return client;
    }

    @BeforeEach
    public void initTest() {
        client = createEntity(em);
    }

    @Test
    @Transactional
    void createClient() throws Exception {
        int databaseSizeBeforeCreate = clientRepository.findAll().size();
        // Create the Client
        ClientDTO clientDTO = clientMapper.toDto(client);
        restClientMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clientDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeCreate + 1);
        Client testClient = clientList.get(clientList.size() - 1);
        assertThat(testClient.getNclient()).isEqualTo(DEFAULT_NCLIENT);
        assertThat(testClient.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testClient.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testClient.getEtat()).isEqualTo(DEFAULT_ETAT);
        assertThat(testClient.getNumeroFixe()).isEqualTo(DEFAULT_NUMERO_FIXE);
        assertThat(testClient.getContactMobileClient()).isEqualTo(DEFAULT_CONTACT_MOBILE_CLIENT);
        assertThat(testClient.getIsDoublons()).isEqualTo(DEFAULT_IS_DOUBLONS);
    }

    @Test
    @Transactional
    void createClientWithExistingId() throws Exception {
        // Create the Client with an existing ID
        client.setId(1L);
        ClientDTO clientDTO = clientMapper.toDto(client);

        int databaseSizeBeforeCreate = clientRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restClientMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNclientIsRequired() throws Exception {
        int databaseSizeBeforeTest = clientRepository.findAll().size();
        // set the field null
        client.setNclient(null);

        // Create the Client, which fails.
        ClientDTO clientDTO = clientMapper.toDto(client);

        restClientMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clientDTO))
            )
            .andExpect(status().isBadRequest());

        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = clientRepository.findAll().size();
        // set the field null
        client.setNom(null);

        // Create the Client, which fails.
        ClientDTO clientDTO = clientMapper.toDto(client);

        restClientMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clientDTO))
            )
            .andExpect(status().isBadRequest());

        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrenomIsRequired() throws Exception {
        int databaseSizeBeforeTest = clientRepository.findAll().size();
        // set the field null
        client.setPrenom(null);

        // Create the Client, which fails.
        ClientDTO clientDTO = clientMapper.toDto(client);

        restClientMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clientDTO))
            )
            .andExpect(status().isBadRequest());

        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEtatIsRequired() throws Exception {
        int databaseSizeBeforeTest = clientRepository.findAll().size();
        // set the field null
        client.setEtat(null);

        // Create the Client, which fails.
        ClientDTO clientDTO = clientMapper.toDto(client);

        restClientMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clientDTO))
            )
            .andExpect(status().isBadRequest());

        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNumeroFixeIsRequired() throws Exception {
        int databaseSizeBeforeTest = clientRepository.findAll().size();
        // set the field null
        client.setNumeroFixe(null);

        // Create the Client, which fails.
        ClientDTO clientDTO = clientMapper.toDto(client);

        restClientMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clientDTO))
            )
            .andExpect(status().isBadRequest());

        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkContactMobileClientIsRequired() throws Exception {
        int databaseSizeBeforeTest = clientRepository.findAll().size();
        // set the field null
        client.setContactMobileClient(null);

        // Create the Client, which fails.
        ClientDTO clientDTO = clientMapper.toDto(client);

        restClientMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clientDTO))
            )
            .andExpect(status().isBadRequest());

        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsDoublonsIsRequired() throws Exception {
        int databaseSizeBeforeTest = clientRepository.findAll().size();
        // set the field null
        client.setIsDoublons(null);

        // Create the Client, which fails.
        ClientDTO clientDTO = clientMapper.toDto(client);

        restClientMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clientDTO))
            )
            .andExpect(status().isBadRequest());

        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllClients() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList
        restClientMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(client.getId().intValue())))
            .andExpect(jsonPath("$.[*].nclient").value(hasItem(DEFAULT_NCLIENT.intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].etat").value(hasItem(DEFAULT_ETAT)))
            .andExpect(jsonPath("$.[*].numeroFixe").value(hasItem(DEFAULT_NUMERO_FIXE)))
            .andExpect(jsonPath("$.[*].contactMobileClient").value(hasItem(DEFAULT_CONTACT_MOBILE_CLIENT)))
            .andExpect(jsonPath("$.[*].isDoublons").value(hasItem(DEFAULT_IS_DOUBLONS.booleanValue())));
    }

    @Test
    @Transactional
    void getClient() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get the client
        restClientMockMvc
            .perform(get(ENTITY_API_URL_ID, client.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(client.getId().intValue()))
            .andExpect(jsonPath("$.nclient").value(DEFAULT_NCLIENT.intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM))
            .andExpect(jsonPath("$.etat").value(DEFAULT_ETAT))
            .andExpect(jsonPath("$.numeroFixe").value(DEFAULT_NUMERO_FIXE))
            .andExpect(jsonPath("$.contactMobileClient").value(DEFAULT_CONTACT_MOBILE_CLIENT))
            .andExpect(jsonPath("$.isDoublons").value(DEFAULT_IS_DOUBLONS.booleanValue()));
    }

    @Test
    @Transactional
    void getClientsByIdFiltering() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        Long id = client.getId();

        defaultClientShouldBeFound("id.equals=" + id);
        defaultClientShouldNotBeFound("id.notEquals=" + id);

        defaultClientShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultClientShouldNotBeFound("id.greaterThan=" + id);

        defaultClientShouldBeFound("id.lessThanOrEqual=" + id);
        defaultClientShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllClientsByNclientIsEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where nclient equals to DEFAULT_NCLIENT
        defaultClientShouldBeFound("nclient.equals=" + DEFAULT_NCLIENT);

        // Get all the clientList where nclient equals to UPDATED_NCLIENT
        defaultClientShouldNotBeFound("nclient.equals=" + UPDATED_NCLIENT);
    }

    @Test
    @Transactional
    void getAllClientsByNclientIsInShouldWork() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where nclient in DEFAULT_NCLIENT or UPDATED_NCLIENT
        defaultClientShouldBeFound("nclient.in=" + DEFAULT_NCLIENT + "," + UPDATED_NCLIENT);

        // Get all the clientList where nclient equals to UPDATED_NCLIENT
        defaultClientShouldNotBeFound("nclient.in=" + UPDATED_NCLIENT);
    }

    @Test
    @Transactional
    void getAllClientsByNclientIsNullOrNotNull() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where nclient is not null
        defaultClientShouldBeFound("nclient.specified=true");

        // Get all the clientList where nclient is null
        defaultClientShouldNotBeFound("nclient.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByNclientIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where nclient is greater than or equal to DEFAULT_NCLIENT
        defaultClientShouldBeFound("nclient.greaterThanOrEqual=" + DEFAULT_NCLIENT);

        // Get all the clientList where nclient is greater than or equal to UPDATED_NCLIENT
        defaultClientShouldNotBeFound("nclient.greaterThanOrEqual=" + UPDATED_NCLIENT);
    }

    @Test
    @Transactional
    void getAllClientsByNclientIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where nclient is less than or equal to DEFAULT_NCLIENT
        defaultClientShouldBeFound("nclient.lessThanOrEqual=" + DEFAULT_NCLIENT);

        // Get all the clientList where nclient is less than or equal to SMALLER_NCLIENT
        defaultClientShouldNotBeFound("nclient.lessThanOrEqual=" + SMALLER_NCLIENT);
    }

    @Test
    @Transactional
    void getAllClientsByNclientIsLessThanSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where nclient is less than DEFAULT_NCLIENT
        defaultClientShouldNotBeFound("nclient.lessThan=" + DEFAULT_NCLIENT);

        // Get all the clientList where nclient is less than UPDATED_NCLIENT
        defaultClientShouldBeFound("nclient.lessThan=" + UPDATED_NCLIENT);
    }

    @Test
    @Transactional
    void getAllClientsByNclientIsGreaterThanSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where nclient is greater than DEFAULT_NCLIENT
        defaultClientShouldNotBeFound("nclient.greaterThan=" + DEFAULT_NCLIENT);

        // Get all the clientList where nclient is greater than SMALLER_NCLIENT
        defaultClientShouldBeFound("nclient.greaterThan=" + SMALLER_NCLIENT);
    }

    @Test
    @Transactional
    void getAllClientsByNomIsEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where nom equals to DEFAULT_NOM
        defaultClientShouldBeFound("nom.equals=" + DEFAULT_NOM);

        // Get all the clientList where nom equals to UPDATED_NOM
        defaultClientShouldNotBeFound("nom.equals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllClientsByNomIsInShouldWork() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where nom in DEFAULT_NOM or UPDATED_NOM
        defaultClientShouldBeFound("nom.in=" + DEFAULT_NOM + "," + UPDATED_NOM);

        // Get all the clientList where nom equals to UPDATED_NOM
        defaultClientShouldNotBeFound("nom.in=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllClientsByNomIsNullOrNotNull() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where nom is not null
        defaultClientShouldBeFound("nom.specified=true");

        // Get all the clientList where nom is null
        defaultClientShouldNotBeFound("nom.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByNomContainsSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where nom contains DEFAULT_NOM
        defaultClientShouldBeFound("nom.contains=" + DEFAULT_NOM);

        // Get all the clientList where nom contains UPDATED_NOM
        defaultClientShouldNotBeFound("nom.contains=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllClientsByNomNotContainsSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where nom does not contain DEFAULT_NOM
        defaultClientShouldNotBeFound("nom.doesNotContain=" + DEFAULT_NOM);

        // Get all the clientList where nom does not contain UPDATED_NOM
        defaultClientShouldBeFound("nom.doesNotContain=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllClientsByPrenomIsEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where prenom equals to DEFAULT_PRENOM
        defaultClientShouldBeFound("prenom.equals=" + DEFAULT_PRENOM);

        // Get all the clientList where prenom equals to UPDATED_PRENOM
        defaultClientShouldNotBeFound("prenom.equals=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllClientsByPrenomIsInShouldWork() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where prenom in DEFAULT_PRENOM or UPDATED_PRENOM
        defaultClientShouldBeFound("prenom.in=" + DEFAULT_PRENOM + "," + UPDATED_PRENOM);

        // Get all the clientList where prenom equals to UPDATED_PRENOM
        defaultClientShouldNotBeFound("prenom.in=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllClientsByPrenomIsNullOrNotNull() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where prenom is not null
        defaultClientShouldBeFound("prenom.specified=true");

        // Get all the clientList where prenom is null
        defaultClientShouldNotBeFound("prenom.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByPrenomContainsSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where prenom contains DEFAULT_PRENOM
        defaultClientShouldBeFound("prenom.contains=" + DEFAULT_PRENOM);

        // Get all the clientList where prenom contains UPDATED_PRENOM
        defaultClientShouldNotBeFound("prenom.contains=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllClientsByPrenomNotContainsSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where prenom does not contain DEFAULT_PRENOM
        defaultClientShouldNotBeFound("prenom.doesNotContain=" + DEFAULT_PRENOM);

        // Get all the clientList where prenom does not contain UPDATED_PRENOM
        defaultClientShouldBeFound("prenom.doesNotContain=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllClientsByEtatIsEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where etat equals to DEFAULT_ETAT
        defaultClientShouldBeFound("etat.equals=" + DEFAULT_ETAT);

        // Get all the clientList where etat equals to UPDATED_ETAT
        defaultClientShouldNotBeFound("etat.equals=" + UPDATED_ETAT);
    }

    @Test
    @Transactional
    void getAllClientsByEtatIsInShouldWork() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where etat in DEFAULT_ETAT or UPDATED_ETAT
        defaultClientShouldBeFound("etat.in=" + DEFAULT_ETAT + "," + UPDATED_ETAT);

        // Get all the clientList where etat equals to UPDATED_ETAT
        defaultClientShouldNotBeFound("etat.in=" + UPDATED_ETAT);
    }

    @Test
    @Transactional
    void getAllClientsByEtatIsNullOrNotNull() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where etat is not null
        defaultClientShouldBeFound("etat.specified=true");

        // Get all the clientList where etat is null
        defaultClientShouldNotBeFound("etat.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByEtatContainsSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where etat contains DEFAULT_ETAT
        defaultClientShouldBeFound("etat.contains=" + DEFAULT_ETAT);

        // Get all the clientList where etat contains UPDATED_ETAT
        defaultClientShouldNotBeFound("etat.contains=" + UPDATED_ETAT);
    }

    @Test
    @Transactional
    void getAllClientsByEtatNotContainsSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where etat does not contain DEFAULT_ETAT
        defaultClientShouldNotBeFound("etat.doesNotContain=" + DEFAULT_ETAT);

        // Get all the clientList where etat does not contain UPDATED_ETAT
        defaultClientShouldBeFound("etat.doesNotContain=" + UPDATED_ETAT);
    }

    @Test
    @Transactional
    void getAllClientsByNumeroFixeIsEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where numeroFixe equals to DEFAULT_NUMERO_FIXE
        defaultClientShouldBeFound("numeroFixe.equals=" + DEFAULT_NUMERO_FIXE);

        // Get all the clientList where numeroFixe equals to UPDATED_NUMERO_FIXE
        defaultClientShouldNotBeFound("numeroFixe.equals=" + UPDATED_NUMERO_FIXE);
    }

    @Test
    @Transactional
    void getAllClientsByNumeroFixeIsInShouldWork() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where numeroFixe in DEFAULT_NUMERO_FIXE or UPDATED_NUMERO_FIXE
        defaultClientShouldBeFound("numeroFixe.in=" + DEFAULT_NUMERO_FIXE + "," + UPDATED_NUMERO_FIXE);

        // Get all the clientList where numeroFixe equals to UPDATED_NUMERO_FIXE
        defaultClientShouldNotBeFound("numeroFixe.in=" + UPDATED_NUMERO_FIXE);
    }

    @Test
    @Transactional
    void getAllClientsByNumeroFixeIsNullOrNotNull() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where numeroFixe is not null
        defaultClientShouldBeFound("numeroFixe.specified=true");

        // Get all the clientList where numeroFixe is null
        defaultClientShouldNotBeFound("numeroFixe.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByNumeroFixeContainsSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where numeroFixe contains DEFAULT_NUMERO_FIXE
        defaultClientShouldBeFound("numeroFixe.contains=" + DEFAULT_NUMERO_FIXE);

        // Get all the clientList where numeroFixe contains UPDATED_NUMERO_FIXE
        defaultClientShouldNotBeFound("numeroFixe.contains=" + UPDATED_NUMERO_FIXE);
    }

    @Test
    @Transactional
    void getAllClientsByNumeroFixeNotContainsSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where numeroFixe does not contain DEFAULT_NUMERO_FIXE
        defaultClientShouldNotBeFound("numeroFixe.doesNotContain=" + DEFAULT_NUMERO_FIXE);

        // Get all the clientList where numeroFixe does not contain UPDATED_NUMERO_FIXE
        defaultClientShouldBeFound("numeroFixe.doesNotContain=" + UPDATED_NUMERO_FIXE);
    }

    @Test
    @Transactional
    void getAllClientsByContactMobileClientIsEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where contactMobileClient equals to DEFAULT_CONTACT_MOBILE_CLIENT
        defaultClientShouldBeFound("contactMobileClient.equals=" + DEFAULT_CONTACT_MOBILE_CLIENT);

        // Get all the clientList where contactMobileClient equals to UPDATED_CONTACT_MOBILE_CLIENT
        defaultClientShouldNotBeFound("contactMobileClient.equals=" + UPDATED_CONTACT_MOBILE_CLIENT);
    }

    @Test
    @Transactional
    void getAllClientsByContactMobileClientIsInShouldWork() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where contactMobileClient in DEFAULT_CONTACT_MOBILE_CLIENT or UPDATED_CONTACT_MOBILE_CLIENT
        defaultClientShouldBeFound("contactMobileClient.in=" + DEFAULT_CONTACT_MOBILE_CLIENT + "," + UPDATED_CONTACT_MOBILE_CLIENT);

        // Get all the clientList where contactMobileClient equals to UPDATED_CONTACT_MOBILE_CLIENT
        defaultClientShouldNotBeFound("contactMobileClient.in=" + UPDATED_CONTACT_MOBILE_CLIENT);
    }

    @Test
    @Transactional
    void getAllClientsByContactMobileClientIsNullOrNotNull() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where contactMobileClient is not null
        defaultClientShouldBeFound("contactMobileClient.specified=true");

        // Get all the clientList where contactMobileClient is null
        defaultClientShouldNotBeFound("contactMobileClient.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByContactMobileClientContainsSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where contactMobileClient contains DEFAULT_CONTACT_MOBILE_CLIENT
        defaultClientShouldBeFound("contactMobileClient.contains=" + DEFAULT_CONTACT_MOBILE_CLIENT);

        // Get all the clientList where contactMobileClient contains UPDATED_CONTACT_MOBILE_CLIENT
        defaultClientShouldNotBeFound("contactMobileClient.contains=" + UPDATED_CONTACT_MOBILE_CLIENT);
    }

    @Test
    @Transactional
    void getAllClientsByContactMobileClientNotContainsSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where contactMobileClient does not contain DEFAULT_CONTACT_MOBILE_CLIENT
        defaultClientShouldNotBeFound("contactMobileClient.doesNotContain=" + DEFAULT_CONTACT_MOBILE_CLIENT);

        // Get all the clientList where contactMobileClient does not contain UPDATED_CONTACT_MOBILE_CLIENT
        defaultClientShouldBeFound("contactMobileClient.doesNotContain=" + UPDATED_CONTACT_MOBILE_CLIENT);
    }

    @Test
    @Transactional
    void getAllClientsByIsDoublonsIsEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where isDoublons equals to DEFAULT_IS_DOUBLONS
        defaultClientShouldBeFound("isDoublons.equals=" + DEFAULT_IS_DOUBLONS);

        // Get all the clientList where isDoublons equals to UPDATED_IS_DOUBLONS
        defaultClientShouldNotBeFound("isDoublons.equals=" + UPDATED_IS_DOUBLONS);
    }

    @Test
    @Transactional
    void getAllClientsByIsDoublonsIsInShouldWork() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where isDoublons in DEFAULT_IS_DOUBLONS or UPDATED_IS_DOUBLONS
        defaultClientShouldBeFound("isDoublons.in=" + DEFAULT_IS_DOUBLONS + "," + UPDATED_IS_DOUBLONS);

        // Get all the clientList where isDoublons equals to UPDATED_IS_DOUBLONS
        defaultClientShouldNotBeFound("isDoublons.in=" + UPDATED_IS_DOUBLONS);
    }

    @Test
    @Transactional
    void getAllClientsByIsDoublonsIsNullOrNotNull() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where isDoublons is not null
        defaultClientShouldBeFound("isDoublons.specified=true");

        // Get all the clientList where isDoublons is null
        defaultClientShouldNotBeFound("isDoublons.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByOffreIsEqualToSomething() throws Exception {
        Offre offre;
        if (TestUtil.findAll(em, Offre.class).isEmpty()) {
            clientRepository.saveAndFlush(client);
            offre = OffreResourceIT.createEntity(em);
        } else {
            offre = TestUtil.findAll(em, Offre.class).get(0);
        }
        em.persist(offre);
        em.flush();
        client.setOffre(offre);
        clientRepository.saveAndFlush(client);
        Long offreId = offre.getId();
        // Get all the clientList where offre equals to offreId
        defaultClientShouldBeFound("offreId.equals=" + offreId);

        // Get all the clientList where offre equals to (offreId + 1)
        defaultClientShouldNotBeFound("offreId.equals=" + (offreId + 1));
    }

    @Test
    @Transactional
    void getAllClientsByOntIsEqualToSomething() throws Exception {
        ONT ont;
        if (TestUtil.findAll(em, ONT.class).isEmpty()) {
            clientRepository.saveAndFlush(client);
            ont = ONTResourceIT.createEntity(em);
        } else {
            ont = TestUtil.findAll(em, ONT.class).get(0);
        }
        em.persist(ont);
        em.flush();
        client.setOnt(ont);
        ont.setClient(client);
        clientRepository.saveAndFlush(client);
        Long ontId = ont.getId();
        // Get all the clientList where ont equals to ontId
        defaultClientShouldBeFound("ontId.equals=" + ontId);

        // Get all the clientList where ont equals to (ontId + 1)
        defaultClientShouldNotBeFound("ontId.equals=" + (ontId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultClientShouldBeFound(String filter) throws Exception {
        restClientMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(client.getId().intValue())))
            .andExpect(jsonPath("$.[*].nclient").value(hasItem(DEFAULT_NCLIENT.intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].etat").value(hasItem(DEFAULT_ETAT)))
            .andExpect(jsonPath("$.[*].numeroFixe").value(hasItem(DEFAULT_NUMERO_FIXE)))
            .andExpect(jsonPath("$.[*].contactMobileClient").value(hasItem(DEFAULT_CONTACT_MOBILE_CLIENT)))
            .andExpect(jsonPath("$.[*].isDoublons").value(hasItem(DEFAULT_IS_DOUBLONS.booleanValue())));

        // Check, that the count call also returns 1
        restClientMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultClientShouldNotBeFound(String filter) throws Exception {
        restClientMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restClientMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingClient() throws Exception {
        // Get the client
        restClientMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingClient() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        int databaseSizeBeforeUpdate = clientRepository.findAll().size();

        // Update the client
        Client updatedClient = clientRepository.findById(client.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedClient are not directly saved in db
        em.detach(updatedClient);
        updatedClient
            .nclient(UPDATED_NCLIENT)
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .etat(UPDATED_ETAT)
            .numeroFixe(UPDATED_NUMERO_FIXE)
            .contactMobileClient(UPDATED_CONTACT_MOBILE_CLIENT)
            .isDoublons(UPDATED_IS_DOUBLONS);
        ClientDTO clientDTO = clientMapper.toDto(updatedClient);

        restClientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, clientDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clientDTO))
            )
            .andExpect(status().isOk());

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
        Client testClient = clientList.get(clientList.size() - 1);
        assertThat(testClient.getNclient()).isEqualTo(UPDATED_NCLIENT);
        assertThat(testClient.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testClient.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testClient.getEtat()).isEqualTo(UPDATED_ETAT);
        assertThat(testClient.getNumeroFixe()).isEqualTo(UPDATED_NUMERO_FIXE);
        assertThat(testClient.getContactMobileClient()).isEqualTo(UPDATED_CONTACT_MOBILE_CLIENT);
        assertThat(testClient.getIsDoublons()).isEqualTo(UPDATED_IS_DOUBLONS);
    }

    @Test
    @Transactional
    void putNonExistingClient() throws Exception {
        int databaseSizeBeforeUpdate = clientRepository.findAll().size();
        client.setId(longCount.incrementAndGet());

        // Create the Client
        ClientDTO clientDTO = clientMapper.toDto(client);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, clientDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchClient() throws Exception {
        int databaseSizeBeforeUpdate = clientRepository.findAll().size();
        client.setId(longCount.incrementAndGet());

        // Create the Client
        ClientDTO clientDTO = clientMapper.toDto(client);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamClient() throws Exception {
        int databaseSizeBeforeUpdate = clientRepository.findAll().size();
        client.setId(longCount.incrementAndGet());

        // Create the Client
        ClientDTO clientDTO = clientMapper.toDto(client);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clientDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateClientWithPatch() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        int databaseSizeBeforeUpdate = clientRepository.findAll().size();

        // Update the client using partial update
        Client partialUpdatedClient = new Client();
        partialUpdatedClient.setId(client.getId());

        partialUpdatedClient.nclient(UPDATED_NCLIENT).etat(UPDATED_ETAT).isDoublons(UPDATED_IS_DOUBLONS);

        restClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClient.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClient))
            )
            .andExpect(status().isOk());

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
        Client testClient = clientList.get(clientList.size() - 1);
        assertThat(testClient.getNclient()).isEqualTo(UPDATED_NCLIENT);
        assertThat(testClient.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testClient.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testClient.getEtat()).isEqualTo(UPDATED_ETAT);
        assertThat(testClient.getNumeroFixe()).isEqualTo(DEFAULT_NUMERO_FIXE);
        assertThat(testClient.getContactMobileClient()).isEqualTo(DEFAULT_CONTACT_MOBILE_CLIENT);
        assertThat(testClient.getIsDoublons()).isEqualTo(UPDATED_IS_DOUBLONS);
    }

    @Test
    @Transactional
    void fullUpdateClientWithPatch() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        int databaseSizeBeforeUpdate = clientRepository.findAll().size();

        // Update the client using partial update
        Client partialUpdatedClient = new Client();
        partialUpdatedClient.setId(client.getId());

        partialUpdatedClient
            .nclient(UPDATED_NCLIENT)
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .etat(UPDATED_ETAT)
            .numeroFixe(UPDATED_NUMERO_FIXE)
            .contactMobileClient(UPDATED_CONTACT_MOBILE_CLIENT)
            .isDoublons(UPDATED_IS_DOUBLONS);

        restClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClient.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClient))
            )
            .andExpect(status().isOk());

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
        Client testClient = clientList.get(clientList.size() - 1);
        assertThat(testClient.getNclient()).isEqualTo(UPDATED_NCLIENT);
        assertThat(testClient.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testClient.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testClient.getEtat()).isEqualTo(UPDATED_ETAT);
        assertThat(testClient.getNumeroFixe()).isEqualTo(UPDATED_NUMERO_FIXE);
        assertThat(testClient.getContactMobileClient()).isEqualTo(UPDATED_CONTACT_MOBILE_CLIENT);
        assertThat(testClient.getIsDoublons()).isEqualTo(UPDATED_IS_DOUBLONS);
    }

    @Test
    @Transactional
    void patchNonExistingClient() throws Exception {
        int databaseSizeBeforeUpdate = clientRepository.findAll().size();
        client.setId(longCount.incrementAndGet());

        // Create the Client
        ClientDTO clientDTO = clientMapper.toDto(client);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, clientDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(clientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchClient() throws Exception {
        int databaseSizeBeforeUpdate = clientRepository.findAll().size();
        client.setId(longCount.incrementAndGet());

        // Create the Client
        ClientDTO clientDTO = clientMapper.toDto(client);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(clientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamClient() throws Exception {
        int databaseSizeBeforeUpdate = clientRepository.findAll().size();
        client.setId(longCount.incrementAndGet());

        // Create the Client
        ClientDTO clientDTO = clientMapper.toDto(client);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(clientDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteClient() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        int databaseSizeBeforeDelete = clientRepository.findAll().size();

        // Delete the client
        restClientMockMvc
            .perform(delete(ENTITY_API_URL_ID, client.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
