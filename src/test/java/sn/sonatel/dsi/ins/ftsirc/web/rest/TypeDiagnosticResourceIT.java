package sn.sonatel.dsi.ins.ftsirc.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.sonatel.dsi.ins.ftsirc.domain.TypeDiagnosticAsserts.*;
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
import sn.sonatel.dsi.ins.ftsirc.domain.TypeDiagnostic;
import sn.sonatel.dsi.ins.ftsirc.repository.TypeDiagnosticRepository;
import sn.sonatel.dsi.ins.ftsirc.service.dto.TypeDiagnosticDTO;
import sn.sonatel.dsi.ins.ftsirc.service.mapper.TypeDiagnosticMapper;

/**
 * Integration tests for the {@link TypeDiagnosticResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TypeDiagnosticResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/type-diagnostics";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TypeDiagnosticRepository typeDiagnosticRepository;

    @Autowired
    private TypeDiagnosticMapper typeDiagnosticMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTypeDiagnosticMockMvc;

    private TypeDiagnostic typeDiagnostic;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeDiagnostic createEntity(EntityManager em) {
        TypeDiagnostic typeDiagnostic = new TypeDiagnostic().libelle(DEFAULT_LIBELLE);
        return typeDiagnostic;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeDiagnostic createUpdatedEntity(EntityManager em) {
        TypeDiagnostic typeDiagnostic = new TypeDiagnostic().libelle(UPDATED_LIBELLE);
        return typeDiagnostic;
    }

    @BeforeEach
    public void initTest() {
        typeDiagnostic = createEntity(em);
    }

    @Test
    @Transactional
    void createTypeDiagnostic() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TypeDiagnostic
        TypeDiagnosticDTO typeDiagnosticDTO = typeDiagnosticMapper.toDto(typeDiagnostic);
        var returnedTypeDiagnosticDTO = om.readValue(
            restTypeDiagnosticMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(typeDiagnosticDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TypeDiagnosticDTO.class
        );

        // Validate the TypeDiagnostic in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTypeDiagnostic = typeDiagnosticMapper.toEntity(returnedTypeDiagnosticDTO);
        assertTypeDiagnosticUpdatableFieldsEquals(returnedTypeDiagnostic, getPersistedTypeDiagnostic(returnedTypeDiagnostic));
    }

    @Test
    @Transactional
    void createTypeDiagnosticWithExistingId() throws Exception {
        // Create the TypeDiagnostic with an existing ID
        typeDiagnostic.setId(1L);
        TypeDiagnosticDTO typeDiagnosticDTO = typeDiagnosticMapper.toDto(typeDiagnostic);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTypeDiagnosticMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(typeDiagnosticDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeDiagnostic in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLibelleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        typeDiagnostic.setLibelle(null);

        // Create the TypeDiagnostic, which fails.
        TypeDiagnosticDTO typeDiagnosticDTO = typeDiagnosticMapper.toDto(typeDiagnostic);

        restTypeDiagnosticMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(typeDiagnosticDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTypeDiagnostics() throws Exception {
        // Initialize the database
        typeDiagnosticRepository.saveAndFlush(typeDiagnostic);

        // Get all the typeDiagnosticList
        restTypeDiagnosticMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeDiagnostic.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)));
    }

    @Test
    @Transactional
    void getTypeDiagnostic() throws Exception {
        // Initialize the database
        typeDiagnosticRepository.saveAndFlush(typeDiagnostic);

        // Get the typeDiagnostic
        restTypeDiagnosticMockMvc
            .perform(get(ENTITY_API_URL_ID, typeDiagnostic.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(typeDiagnostic.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE));
    }

    @Test
    @Transactional
    void getTypeDiagnosticsByIdFiltering() throws Exception {
        // Initialize the database
        typeDiagnosticRepository.saveAndFlush(typeDiagnostic);

        Long id = typeDiagnostic.getId();

        defaultTypeDiagnosticFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultTypeDiagnosticFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultTypeDiagnosticFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTypeDiagnosticsByLibelleIsEqualToSomething() throws Exception {
        // Initialize the database
        typeDiagnosticRepository.saveAndFlush(typeDiagnostic);

        // Get all the typeDiagnosticList where libelle equals to
        defaultTypeDiagnosticFiltering("libelle.equals=" + DEFAULT_LIBELLE, "libelle.equals=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllTypeDiagnosticsByLibelleIsInShouldWork() throws Exception {
        // Initialize the database
        typeDiagnosticRepository.saveAndFlush(typeDiagnostic);

        // Get all the typeDiagnosticList where libelle in
        defaultTypeDiagnosticFiltering("libelle.in=" + DEFAULT_LIBELLE + "," + UPDATED_LIBELLE, "libelle.in=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllTypeDiagnosticsByLibelleIsNullOrNotNull() throws Exception {
        // Initialize the database
        typeDiagnosticRepository.saveAndFlush(typeDiagnostic);

        // Get all the typeDiagnosticList where libelle is not null
        defaultTypeDiagnosticFiltering("libelle.specified=true", "libelle.specified=false");
    }

    @Test
    @Transactional
    void getAllTypeDiagnosticsByLibelleContainsSomething() throws Exception {
        // Initialize the database
        typeDiagnosticRepository.saveAndFlush(typeDiagnostic);

        // Get all the typeDiagnosticList where libelle contains
        defaultTypeDiagnosticFiltering("libelle.contains=" + DEFAULT_LIBELLE, "libelle.contains=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllTypeDiagnosticsByLibelleNotContainsSomething() throws Exception {
        // Initialize the database
        typeDiagnosticRepository.saveAndFlush(typeDiagnostic);

        // Get all the typeDiagnosticList where libelle does not contain
        defaultTypeDiagnosticFiltering("libelle.doesNotContain=" + UPDATED_LIBELLE, "libelle.doesNotContain=" + DEFAULT_LIBELLE);
    }

    private void defaultTypeDiagnosticFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultTypeDiagnosticShouldBeFound(shouldBeFound);
        defaultTypeDiagnosticShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTypeDiagnosticShouldBeFound(String filter) throws Exception {
        restTypeDiagnosticMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeDiagnostic.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)));

        // Check, that the count call also returns 1
        restTypeDiagnosticMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTypeDiagnosticShouldNotBeFound(String filter) throws Exception {
        restTypeDiagnosticMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTypeDiagnosticMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTypeDiagnostic() throws Exception {
        // Get the typeDiagnostic
        restTypeDiagnosticMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTypeDiagnostic() throws Exception {
        // Initialize the database
        typeDiagnosticRepository.saveAndFlush(typeDiagnostic);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the typeDiagnostic
        TypeDiagnostic updatedTypeDiagnostic = typeDiagnosticRepository.findById(typeDiagnostic.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTypeDiagnostic are not directly saved in db
        em.detach(updatedTypeDiagnostic);
        updatedTypeDiagnostic.libelle(UPDATED_LIBELLE);
        TypeDiagnosticDTO typeDiagnosticDTO = typeDiagnosticMapper.toDto(updatedTypeDiagnostic);

        restTypeDiagnosticMockMvc
            .perform(
                put(ENTITY_API_URL_ID, typeDiagnosticDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(typeDiagnosticDTO))
            )
            .andExpect(status().isOk());

        // Validate the TypeDiagnostic in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTypeDiagnosticToMatchAllProperties(updatedTypeDiagnostic);
    }

    @Test
    @Transactional
    void putNonExistingTypeDiagnostic() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        typeDiagnostic.setId(longCount.incrementAndGet());

        // Create the TypeDiagnostic
        TypeDiagnosticDTO typeDiagnosticDTO = typeDiagnosticMapper.toDto(typeDiagnostic);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeDiagnosticMockMvc
            .perform(
                put(ENTITY_API_URL_ID, typeDiagnosticDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(typeDiagnosticDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeDiagnostic in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTypeDiagnostic() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        typeDiagnostic.setId(longCount.incrementAndGet());

        // Create the TypeDiagnostic
        TypeDiagnosticDTO typeDiagnosticDTO = typeDiagnosticMapper.toDto(typeDiagnostic);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeDiagnosticMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(typeDiagnosticDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeDiagnostic in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTypeDiagnostic() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        typeDiagnostic.setId(longCount.incrementAndGet());

        // Create the TypeDiagnostic
        TypeDiagnosticDTO typeDiagnosticDTO = typeDiagnosticMapper.toDto(typeDiagnostic);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeDiagnosticMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(typeDiagnosticDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypeDiagnostic in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTypeDiagnosticWithPatch() throws Exception {
        // Initialize the database
        typeDiagnosticRepository.saveAndFlush(typeDiagnostic);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the typeDiagnostic using partial update
        TypeDiagnostic partialUpdatedTypeDiagnostic = new TypeDiagnostic();
        partialUpdatedTypeDiagnostic.setId(typeDiagnostic.getId());

        restTypeDiagnosticMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypeDiagnostic.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTypeDiagnostic))
            )
            .andExpect(status().isOk());

        // Validate the TypeDiagnostic in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTypeDiagnosticUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTypeDiagnostic, typeDiagnostic),
            getPersistedTypeDiagnostic(typeDiagnostic)
        );
    }

    @Test
    @Transactional
    void fullUpdateTypeDiagnosticWithPatch() throws Exception {
        // Initialize the database
        typeDiagnosticRepository.saveAndFlush(typeDiagnostic);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the typeDiagnostic using partial update
        TypeDiagnostic partialUpdatedTypeDiagnostic = new TypeDiagnostic();
        partialUpdatedTypeDiagnostic.setId(typeDiagnostic.getId());

        partialUpdatedTypeDiagnostic.libelle(UPDATED_LIBELLE);

        restTypeDiagnosticMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypeDiagnostic.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTypeDiagnostic))
            )
            .andExpect(status().isOk());

        // Validate the TypeDiagnostic in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTypeDiagnosticUpdatableFieldsEquals(partialUpdatedTypeDiagnostic, getPersistedTypeDiagnostic(partialUpdatedTypeDiagnostic));
    }

    @Test
    @Transactional
    void patchNonExistingTypeDiagnostic() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        typeDiagnostic.setId(longCount.incrementAndGet());

        // Create the TypeDiagnostic
        TypeDiagnosticDTO typeDiagnosticDTO = typeDiagnosticMapper.toDto(typeDiagnostic);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeDiagnosticMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, typeDiagnosticDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(typeDiagnosticDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeDiagnostic in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTypeDiagnostic() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        typeDiagnostic.setId(longCount.incrementAndGet());

        // Create the TypeDiagnostic
        TypeDiagnosticDTO typeDiagnosticDTO = typeDiagnosticMapper.toDto(typeDiagnostic);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeDiagnosticMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(typeDiagnosticDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeDiagnostic in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTypeDiagnostic() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        typeDiagnostic.setId(longCount.incrementAndGet());

        // Create the TypeDiagnostic
        TypeDiagnosticDTO typeDiagnosticDTO = typeDiagnosticMapper.toDto(typeDiagnostic);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeDiagnosticMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(typeDiagnosticDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypeDiagnostic in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTypeDiagnostic() throws Exception {
        // Initialize the database
        typeDiagnosticRepository.saveAndFlush(typeDiagnostic);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the typeDiagnostic
        restTypeDiagnosticMockMvc
            .perform(delete(ENTITY_API_URL_ID, typeDiagnostic.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return typeDiagnosticRepository.count();
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

    protected TypeDiagnostic getPersistedTypeDiagnostic(TypeDiagnostic typeDiagnostic) {
        return typeDiagnosticRepository.findById(typeDiagnostic.getId()).orElseThrow();
    }

    protected void assertPersistedTypeDiagnosticToMatchAllProperties(TypeDiagnostic expectedTypeDiagnostic) {
        assertTypeDiagnosticAllPropertiesEquals(expectedTypeDiagnostic, getPersistedTypeDiagnostic(expectedTypeDiagnostic));
    }

    protected void assertPersistedTypeDiagnosticToMatchUpdatableProperties(TypeDiagnostic expectedTypeDiagnostic) {
        assertTypeDiagnosticAllUpdatablePropertiesEquals(expectedTypeDiagnostic, getPersistedTypeDiagnostic(expectedTypeDiagnostic));
    }
}
