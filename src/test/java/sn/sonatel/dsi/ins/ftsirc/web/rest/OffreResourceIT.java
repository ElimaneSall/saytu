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
import sn.sonatel.dsi.ins.ftsirc.domain.Offre;
import sn.sonatel.dsi.ins.ftsirc.repository.OffreRepository;
import sn.sonatel.dsi.ins.ftsirc.service.dto.OffreDTO;
import sn.sonatel.dsi.ins.ftsirc.service.mapper.OffreMapper;

/**
 * Integration tests for the {@link OffreResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OffreResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String DEFAULT_DEBIT_MAX = "AAAAAAAAAA";
    private static final String UPDATED_DEBIT_MAX = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/offres";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OffreRepository offreRepository;

    @Autowired
    private OffreMapper offreMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOffreMockMvc;

    private Offre offre;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Offre createEntity(EntityManager em) {
        Offre offre = new Offre().libelle(DEFAULT_LIBELLE).debitMax(DEFAULT_DEBIT_MAX);
        return offre;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Offre createUpdatedEntity(EntityManager em) {
        Offre offre = new Offre().libelle(UPDATED_LIBELLE).debitMax(UPDATED_DEBIT_MAX);
        return offre;
    }

    @BeforeEach
    public void initTest() {
        offre = createEntity(em);
    }

    @Test
    @Transactional
    void createOffre() throws Exception {
        int databaseSizeBeforeCreate = offreRepository.findAll().size();
        // Create the Offre
        OffreDTO offreDTO = offreMapper.toDto(offre);
        restOffreMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(offreDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Offre in the database
        List<Offre> offreList = offreRepository.findAll();
        assertThat(offreList).hasSize(databaseSizeBeforeCreate + 1);
        Offre testOffre = offreList.get(offreList.size() - 1);
        assertThat(testOffre.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testOffre.getDebitMax()).isEqualTo(DEFAULT_DEBIT_MAX);
    }

    @Test
    @Transactional
    void createOffreWithExistingId() throws Exception {
        // Create the Offre with an existing ID
        offre.setId(1L);
        OffreDTO offreDTO = offreMapper.toDto(offre);

        int databaseSizeBeforeCreate = offreRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOffreMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(offreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Offre in the database
        List<Offre> offreList = offreRepository.findAll();
        assertThat(offreList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = offreRepository.findAll().size();
        // set the field null
        offre.setLibelle(null);

        // Create the Offre, which fails.
        OffreDTO offreDTO = offreMapper.toDto(offre);

        restOffreMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(offreDTO))
            )
            .andExpect(status().isBadRequest());

        List<Offre> offreList = offreRepository.findAll();
        assertThat(offreList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDebitMaxIsRequired() throws Exception {
        int databaseSizeBeforeTest = offreRepository.findAll().size();
        // set the field null
        offre.setDebitMax(null);

        // Create the Offre, which fails.
        OffreDTO offreDTO = offreMapper.toDto(offre);

        restOffreMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(offreDTO))
            )
            .andExpect(status().isBadRequest());

        List<Offre> offreList = offreRepository.findAll();
        assertThat(offreList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOffres() throws Exception {
        // Initialize the database
        offreRepository.saveAndFlush(offre);

        // Get all the offreList
        restOffreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(offre.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].debitMax").value(hasItem(DEFAULT_DEBIT_MAX)));
    }

    @Test
    @Transactional
    void getOffre() throws Exception {
        // Initialize the database
        offreRepository.saveAndFlush(offre);

        // Get the offre
        restOffreMockMvc
            .perform(get(ENTITY_API_URL_ID, offre.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(offre.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.debitMax").value(DEFAULT_DEBIT_MAX));
    }

    @Test
    @Transactional
    void getOffresByIdFiltering() throws Exception {
        // Initialize the database
        offreRepository.saveAndFlush(offre);

        Long id = offre.getId();

        defaultOffreShouldBeFound("id.equals=" + id);
        defaultOffreShouldNotBeFound("id.notEquals=" + id);

        defaultOffreShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultOffreShouldNotBeFound("id.greaterThan=" + id);

        defaultOffreShouldBeFound("id.lessThanOrEqual=" + id);
        defaultOffreShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOffresByLibelleIsEqualToSomething() throws Exception {
        // Initialize the database
        offreRepository.saveAndFlush(offre);

        // Get all the offreList where libelle equals to DEFAULT_LIBELLE
        defaultOffreShouldBeFound("libelle.equals=" + DEFAULT_LIBELLE);

        // Get all the offreList where libelle equals to UPDATED_LIBELLE
        defaultOffreShouldNotBeFound("libelle.equals=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllOffresByLibelleIsInShouldWork() throws Exception {
        // Initialize the database
        offreRepository.saveAndFlush(offre);

        // Get all the offreList where libelle in DEFAULT_LIBELLE or UPDATED_LIBELLE
        defaultOffreShouldBeFound("libelle.in=" + DEFAULT_LIBELLE + "," + UPDATED_LIBELLE);

        // Get all the offreList where libelle equals to UPDATED_LIBELLE
        defaultOffreShouldNotBeFound("libelle.in=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllOffresByLibelleIsNullOrNotNull() throws Exception {
        // Initialize the database
        offreRepository.saveAndFlush(offre);

        // Get all the offreList where libelle is not null
        defaultOffreShouldBeFound("libelle.specified=true");

        // Get all the offreList where libelle is null
        defaultOffreShouldNotBeFound("libelle.specified=false");
    }

    @Test
    @Transactional
    void getAllOffresByLibelleContainsSomething() throws Exception {
        // Initialize the database
        offreRepository.saveAndFlush(offre);

        // Get all the offreList where libelle contains DEFAULT_LIBELLE
        defaultOffreShouldBeFound("libelle.contains=" + DEFAULT_LIBELLE);

        // Get all the offreList where libelle contains UPDATED_LIBELLE
        defaultOffreShouldNotBeFound("libelle.contains=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllOffresByLibelleNotContainsSomething() throws Exception {
        // Initialize the database
        offreRepository.saveAndFlush(offre);

        // Get all the offreList where libelle does not contain DEFAULT_LIBELLE
        defaultOffreShouldNotBeFound("libelle.doesNotContain=" + DEFAULT_LIBELLE);

        // Get all the offreList where libelle does not contain UPDATED_LIBELLE
        defaultOffreShouldBeFound("libelle.doesNotContain=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllOffresByDebitMaxIsEqualToSomething() throws Exception {
        // Initialize the database
        offreRepository.saveAndFlush(offre);

        // Get all the offreList where debitMax equals to DEFAULT_DEBIT_MAX
        defaultOffreShouldBeFound("debitMax.equals=" + DEFAULT_DEBIT_MAX);

        // Get all the offreList where debitMax equals to UPDATED_DEBIT_MAX
        defaultOffreShouldNotBeFound("debitMax.equals=" + UPDATED_DEBIT_MAX);
    }

    @Test
    @Transactional
    void getAllOffresByDebitMaxIsInShouldWork() throws Exception {
        // Initialize the database
        offreRepository.saveAndFlush(offre);

        // Get all the offreList where debitMax in DEFAULT_DEBIT_MAX or UPDATED_DEBIT_MAX
        defaultOffreShouldBeFound("debitMax.in=" + DEFAULT_DEBIT_MAX + "," + UPDATED_DEBIT_MAX);

        // Get all the offreList where debitMax equals to UPDATED_DEBIT_MAX
        defaultOffreShouldNotBeFound("debitMax.in=" + UPDATED_DEBIT_MAX);
    }

    @Test
    @Transactional
    void getAllOffresByDebitMaxIsNullOrNotNull() throws Exception {
        // Initialize the database
        offreRepository.saveAndFlush(offre);

        // Get all the offreList where debitMax is not null
        defaultOffreShouldBeFound("debitMax.specified=true");

        // Get all the offreList where debitMax is null
        defaultOffreShouldNotBeFound("debitMax.specified=false");
    }

    @Test
    @Transactional
    void getAllOffresByDebitMaxContainsSomething() throws Exception {
        // Initialize the database
        offreRepository.saveAndFlush(offre);

        // Get all the offreList where debitMax contains DEFAULT_DEBIT_MAX
        defaultOffreShouldBeFound("debitMax.contains=" + DEFAULT_DEBIT_MAX);

        // Get all the offreList where debitMax contains UPDATED_DEBIT_MAX
        defaultOffreShouldNotBeFound("debitMax.contains=" + UPDATED_DEBIT_MAX);
    }

    @Test
    @Transactional
    void getAllOffresByDebitMaxNotContainsSomething() throws Exception {
        // Initialize the database
        offreRepository.saveAndFlush(offre);

        // Get all the offreList where debitMax does not contain DEFAULT_DEBIT_MAX
        defaultOffreShouldNotBeFound("debitMax.doesNotContain=" + DEFAULT_DEBIT_MAX);

        // Get all the offreList where debitMax does not contain UPDATED_DEBIT_MAX
        defaultOffreShouldBeFound("debitMax.doesNotContain=" + UPDATED_DEBIT_MAX);
    }

    @Test
    @Transactional
    void getAllOffresByClientIsEqualToSomething() throws Exception {
        Client client;
        if (TestUtil.findAll(em, Client.class).isEmpty()) {
            offreRepository.saveAndFlush(offre);
            client = ClientResourceIT.createEntity(em);
        } else {
            client = TestUtil.findAll(em, Client.class).get(0);
        }
        em.persist(client);
        em.flush();
        offre.addClient(client);
        offreRepository.saveAndFlush(offre);
        Long clientId = client.getId();
        // Get all the offreList where client equals to clientId
        defaultOffreShouldBeFound("clientId.equals=" + clientId);

        // Get all the offreList where client equals to (clientId + 1)
        defaultOffreShouldNotBeFound("clientId.equals=" + (clientId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOffreShouldBeFound(String filter) throws Exception {
        restOffreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(offre.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].debitMax").value(hasItem(DEFAULT_DEBIT_MAX)));

        // Check, that the count call also returns 1
        restOffreMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOffreShouldNotBeFound(String filter) throws Exception {
        restOffreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOffreMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOffre() throws Exception {
        // Get the offre
        restOffreMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOffre() throws Exception {
        // Initialize the database
        offreRepository.saveAndFlush(offre);

        int databaseSizeBeforeUpdate = offreRepository.findAll().size();

        // Update the offre
        Offre updatedOffre = offreRepository.findById(offre.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOffre are not directly saved in db
        em.detach(updatedOffre);
        updatedOffre.libelle(UPDATED_LIBELLE).debitMax(UPDATED_DEBIT_MAX);
        OffreDTO offreDTO = offreMapper.toDto(updatedOffre);

        restOffreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, offreDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(offreDTO))
            )
            .andExpect(status().isOk());

        // Validate the Offre in the database
        List<Offre> offreList = offreRepository.findAll();
        assertThat(offreList).hasSize(databaseSizeBeforeUpdate);
        Offre testOffre = offreList.get(offreList.size() - 1);
        assertThat(testOffre.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testOffre.getDebitMax()).isEqualTo(UPDATED_DEBIT_MAX);
    }

    @Test
    @Transactional
    void putNonExistingOffre() throws Exception {
        int databaseSizeBeforeUpdate = offreRepository.findAll().size();
        offre.setId(longCount.incrementAndGet());

        // Create the Offre
        OffreDTO offreDTO = offreMapper.toDto(offre);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOffreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, offreDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(offreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Offre in the database
        List<Offre> offreList = offreRepository.findAll();
        assertThat(offreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOffre() throws Exception {
        int databaseSizeBeforeUpdate = offreRepository.findAll().size();
        offre.setId(longCount.incrementAndGet());

        // Create the Offre
        OffreDTO offreDTO = offreMapper.toDto(offre);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOffreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(offreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Offre in the database
        List<Offre> offreList = offreRepository.findAll();
        assertThat(offreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOffre() throws Exception {
        int databaseSizeBeforeUpdate = offreRepository.findAll().size();
        offre.setId(longCount.incrementAndGet());

        // Create the Offre
        OffreDTO offreDTO = offreMapper.toDto(offre);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOffreMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(offreDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Offre in the database
        List<Offre> offreList = offreRepository.findAll();
        assertThat(offreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOffreWithPatch() throws Exception {
        // Initialize the database
        offreRepository.saveAndFlush(offre);

        int databaseSizeBeforeUpdate = offreRepository.findAll().size();

        // Update the offre using partial update
        Offre partialUpdatedOffre = new Offre();
        partialUpdatedOffre.setId(offre.getId());

        partialUpdatedOffre.libelle(UPDATED_LIBELLE).debitMax(UPDATED_DEBIT_MAX);

        restOffreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOffre.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOffre))
            )
            .andExpect(status().isOk());

        // Validate the Offre in the database
        List<Offre> offreList = offreRepository.findAll();
        assertThat(offreList).hasSize(databaseSizeBeforeUpdate);
        Offre testOffre = offreList.get(offreList.size() - 1);
        assertThat(testOffre.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testOffre.getDebitMax()).isEqualTo(UPDATED_DEBIT_MAX);
    }

    @Test
    @Transactional
    void fullUpdateOffreWithPatch() throws Exception {
        // Initialize the database
        offreRepository.saveAndFlush(offre);

        int databaseSizeBeforeUpdate = offreRepository.findAll().size();

        // Update the offre using partial update
        Offre partialUpdatedOffre = new Offre();
        partialUpdatedOffre.setId(offre.getId());

        partialUpdatedOffre.libelle(UPDATED_LIBELLE).debitMax(UPDATED_DEBIT_MAX);

        restOffreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOffre.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOffre))
            )
            .andExpect(status().isOk());

        // Validate the Offre in the database
        List<Offre> offreList = offreRepository.findAll();
        assertThat(offreList).hasSize(databaseSizeBeforeUpdate);
        Offre testOffre = offreList.get(offreList.size() - 1);
        assertThat(testOffre.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testOffre.getDebitMax()).isEqualTo(UPDATED_DEBIT_MAX);
    }

    @Test
    @Transactional
    void patchNonExistingOffre() throws Exception {
        int databaseSizeBeforeUpdate = offreRepository.findAll().size();
        offre.setId(longCount.incrementAndGet());

        // Create the Offre
        OffreDTO offreDTO = offreMapper.toDto(offre);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOffreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, offreDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(offreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Offre in the database
        List<Offre> offreList = offreRepository.findAll();
        assertThat(offreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOffre() throws Exception {
        int databaseSizeBeforeUpdate = offreRepository.findAll().size();
        offre.setId(longCount.incrementAndGet());

        // Create the Offre
        OffreDTO offreDTO = offreMapper.toDto(offre);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOffreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(offreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Offre in the database
        List<Offre> offreList = offreRepository.findAll();
        assertThat(offreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOffre() throws Exception {
        int databaseSizeBeforeUpdate = offreRepository.findAll().size();
        offre.setId(longCount.incrementAndGet());

        // Create the Offre
        OffreDTO offreDTO = offreMapper.toDto(offre);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOffreMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(offreDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Offre in the database
        List<Offre> offreList = offreRepository.findAll();
        assertThat(offreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOffre() throws Exception {
        // Initialize the database
        offreRepository.saveAndFlush(offre);

        int databaseSizeBeforeDelete = offreRepository.findAll().size();

        // Delete the offre
        restOffreMockMvc
            .perform(delete(ENTITY_API_URL_ID, offre.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Offre> offreList = offreRepository.findAll();
        assertThat(offreList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
