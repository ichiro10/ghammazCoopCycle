package ghammaz.coopcycle.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import ghammaz.coopcycle.IntegrationTest;
import ghammaz.coopcycle.domain.Association;
import ghammaz.coopcycle.repository.AssociationRepository;
import ghammaz.coopcycle.repository.EntityManager;
import ghammaz.coopcycle.service.dto.AssociationDTO;
import ghammaz.coopcycle.service.mapper.AssociationMapper;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link AssociationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class AssociationResourceIT {

    private static final Integer DEFAULT_ID_ASSO = 1;
    private static final Integer UPDATED_ID_ASSO = 2;

    private static final String DEFAULT_NOM_ASSO = "AAAAAAAAAA";
    private static final String UPDATED_NOM_ASSO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/associations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AssociationRepository associationRepository;

    @Autowired
    private AssociationMapper associationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Association association;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Association createEntity(EntityManager em) {
        Association association = new Association().idAsso(DEFAULT_ID_ASSO).nomAsso(DEFAULT_NOM_ASSO);
        return association;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Association createUpdatedEntity(EntityManager em) {
        Association association = new Association().idAsso(UPDATED_ID_ASSO).nomAsso(UPDATED_NOM_ASSO);
        return association;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Association.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        association = createEntity(em);
    }

    @Test
    void createAssociation() throws Exception {
        int databaseSizeBeforeCreate = associationRepository.findAll().collectList().block().size();
        // Create the Association
        AssociationDTO associationDTO = associationMapper.toDto(association);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(associationDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Association in the database
        List<Association> associationList = associationRepository.findAll().collectList().block();
        assertThat(associationList).hasSize(databaseSizeBeforeCreate + 1);
        Association testAssociation = associationList.get(associationList.size() - 1);
        assertThat(testAssociation.getIdAsso()).isEqualTo(DEFAULT_ID_ASSO);
        assertThat(testAssociation.getNomAsso()).isEqualTo(DEFAULT_NOM_ASSO);
    }

    @Test
    void createAssociationWithExistingId() throws Exception {
        // Create the Association with an existing ID
        association.setId(1L);
        AssociationDTO associationDTO = associationMapper.toDto(association);

        int databaseSizeBeforeCreate = associationRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(associationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Association in the database
        List<Association> associationList = associationRepository.findAll().collectList().block();
        assertThat(associationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkIdAssoIsRequired() throws Exception {
        int databaseSizeBeforeTest = associationRepository.findAll().collectList().block().size();
        // set the field null
        association.setIdAsso(null);

        // Create the Association, which fails.
        AssociationDTO associationDTO = associationMapper.toDto(association);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(associationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Association> associationList = associationRepository.findAll().collectList().block();
        assertThat(associationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkNomAssoIsRequired() throws Exception {
        int databaseSizeBeforeTest = associationRepository.findAll().collectList().block().size();
        // set the field null
        association.setNomAsso(null);

        // Create the Association, which fails.
        AssociationDTO associationDTO = associationMapper.toDto(association);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(associationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Association> associationList = associationRepository.findAll().collectList().block();
        assertThat(associationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllAssociationsAsStream() {
        // Initialize the database
        associationRepository.save(association).block();

        List<Association> associationList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(AssociationDTO.class)
            .getResponseBody()
            .map(associationMapper::toEntity)
            .filter(association::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(associationList).isNotNull();
        assertThat(associationList).hasSize(1);
        Association testAssociation = associationList.get(0);
        assertThat(testAssociation.getIdAsso()).isEqualTo(DEFAULT_ID_ASSO);
        assertThat(testAssociation.getNomAsso()).isEqualTo(DEFAULT_NOM_ASSO);
    }

    @Test
    void getAllAssociations() {
        // Initialize the database
        associationRepository.save(association).block();

        // Get all the associationList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(association.getId().intValue()))
            .jsonPath("$.[*].idAsso")
            .value(hasItem(DEFAULT_ID_ASSO))
            .jsonPath("$.[*].nomAsso")
            .value(hasItem(DEFAULT_NOM_ASSO));
    }

    @Test
    void getAssociation() {
        // Initialize the database
        associationRepository.save(association).block();

        // Get the association
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, association.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(association.getId().intValue()))
            .jsonPath("$.idAsso")
            .value(is(DEFAULT_ID_ASSO))
            .jsonPath("$.nomAsso")
            .value(is(DEFAULT_NOM_ASSO));
    }

    @Test
    void getNonExistingAssociation() {
        // Get the association
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingAssociation() throws Exception {
        // Initialize the database
        associationRepository.save(association).block();

        int databaseSizeBeforeUpdate = associationRepository.findAll().collectList().block().size();

        // Update the association
        Association updatedAssociation = associationRepository.findById(association.getId()).block();
        updatedAssociation.idAsso(UPDATED_ID_ASSO).nomAsso(UPDATED_NOM_ASSO);
        AssociationDTO associationDTO = associationMapper.toDto(updatedAssociation);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, associationDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(associationDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Association in the database
        List<Association> associationList = associationRepository.findAll().collectList().block();
        assertThat(associationList).hasSize(databaseSizeBeforeUpdate);
        Association testAssociation = associationList.get(associationList.size() - 1);
        assertThat(testAssociation.getIdAsso()).isEqualTo(UPDATED_ID_ASSO);
        assertThat(testAssociation.getNomAsso()).isEqualTo(UPDATED_NOM_ASSO);
    }

    @Test
    void putNonExistingAssociation() throws Exception {
        int databaseSizeBeforeUpdate = associationRepository.findAll().collectList().block().size();
        association.setId(count.incrementAndGet());

        // Create the Association
        AssociationDTO associationDTO = associationMapper.toDto(association);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, associationDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(associationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Association in the database
        List<Association> associationList = associationRepository.findAll().collectList().block();
        assertThat(associationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchAssociation() throws Exception {
        int databaseSizeBeforeUpdate = associationRepository.findAll().collectList().block().size();
        association.setId(count.incrementAndGet());

        // Create the Association
        AssociationDTO associationDTO = associationMapper.toDto(association);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(associationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Association in the database
        List<Association> associationList = associationRepository.findAll().collectList().block();
        assertThat(associationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamAssociation() throws Exception {
        int databaseSizeBeforeUpdate = associationRepository.findAll().collectList().block().size();
        association.setId(count.incrementAndGet());

        // Create the Association
        AssociationDTO associationDTO = associationMapper.toDto(association);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(associationDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Association in the database
        List<Association> associationList = associationRepository.findAll().collectList().block();
        assertThat(associationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateAssociationWithPatch() throws Exception {
        // Initialize the database
        associationRepository.save(association).block();

        int databaseSizeBeforeUpdate = associationRepository.findAll().collectList().block().size();

        // Update the association using partial update
        Association partialUpdatedAssociation = new Association();
        partialUpdatedAssociation.setId(association.getId());

        partialUpdatedAssociation.idAsso(UPDATED_ID_ASSO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAssociation.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedAssociation))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Association in the database
        List<Association> associationList = associationRepository.findAll().collectList().block();
        assertThat(associationList).hasSize(databaseSizeBeforeUpdate);
        Association testAssociation = associationList.get(associationList.size() - 1);
        assertThat(testAssociation.getIdAsso()).isEqualTo(UPDATED_ID_ASSO);
        assertThat(testAssociation.getNomAsso()).isEqualTo(DEFAULT_NOM_ASSO);
    }

    @Test
    void fullUpdateAssociationWithPatch() throws Exception {
        // Initialize the database
        associationRepository.save(association).block();

        int databaseSizeBeforeUpdate = associationRepository.findAll().collectList().block().size();

        // Update the association using partial update
        Association partialUpdatedAssociation = new Association();
        partialUpdatedAssociation.setId(association.getId());

        partialUpdatedAssociation.idAsso(UPDATED_ID_ASSO).nomAsso(UPDATED_NOM_ASSO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAssociation.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedAssociation))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Association in the database
        List<Association> associationList = associationRepository.findAll().collectList().block();
        assertThat(associationList).hasSize(databaseSizeBeforeUpdate);
        Association testAssociation = associationList.get(associationList.size() - 1);
        assertThat(testAssociation.getIdAsso()).isEqualTo(UPDATED_ID_ASSO);
        assertThat(testAssociation.getNomAsso()).isEqualTo(UPDATED_NOM_ASSO);
    }

    @Test
    void patchNonExistingAssociation() throws Exception {
        int databaseSizeBeforeUpdate = associationRepository.findAll().collectList().block().size();
        association.setId(count.incrementAndGet());

        // Create the Association
        AssociationDTO associationDTO = associationMapper.toDto(association);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, associationDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(associationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Association in the database
        List<Association> associationList = associationRepository.findAll().collectList().block();
        assertThat(associationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchAssociation() throws Exception {
        int databaseSizeBeforeUpdate = associationRepository.findAll().collectList().block().size();
        association.setId(count.incrementAndGet());

        // Create the Association
        AssociationDTO associationDTO = associationMapper.toDto(association);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(associationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Association in the database
        List<Association> associationList = associationRepository.findAll().collectList().block();
        assertThat(associationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamAssociation() throws Exception {
        int databaseSizeBeforeUpdate = associationRepository.findAll().collectList().block().size();
        association.setId(count.incrementAndGet());

        // Create the Association
        AssociationDTO associationDTO = associationMapper.toDto(association);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(associationDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Association in the database
        List<Association> associationList = associationRepository.findAll().collectList().block();
        assertThat(associationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteAssociation() {
        // Initialize the database
        associationRepository.save(association).block();

        int databaseSizeBeforeDelete = associationRepository.findAll().collectList().block().size();

        // Delete the association
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, association.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Association> associationList = associationRepository.findAll().collectList().block();
        assertThat(associationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
