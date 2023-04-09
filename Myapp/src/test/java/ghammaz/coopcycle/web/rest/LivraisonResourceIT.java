package ghammaz.coopcycle.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import ghammaz.coopcycle.IntegrationTest;
import ghammaz.coopcycle.domain.Livraison;
import ghammaz.coopcycle.repository.EntityManager;
import ghammaz.coopcycle.repository.LivraisonRepository;
import ghammaz.coopcycle.service.dto.LivraisonDTO;
import ghammaz.coopcycle.service.mapper.LivraisonMapper;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link LivraisonResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class LivraisonResourceIT {

    private static final Integer DEFAULT_ID_LIVRAISON = 1;
    private static final Integer UPDATED_ID_LIVRAISON = 2;

    private static final Float DEFAULT_PRIX_LIVRAISON = 0F;
    private static final Float UPDATED_PRIX_LIVRAISON = 1F;

    private static final Instant DEFAULT_DUREE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DUREE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_ADRESSE_LIVRAISON = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE_LIVRAISON = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/livraisons";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LivraisonRepository livraisonRepository;

    @Autowired
    private LivraisonMapper livraisonMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Livraison livraison;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Livraison createEntity(EntityManager em) {
        Livraison livraison = new Livraison()
            .idLivraison(DEFAULT_ID_LIVRAISON)
            .prixLivraison(DEFAULT_PRIX_LIVRAISON)
            .duree(DEFAULT_DUREE)
            .adresseLivraison(DEFAULT_ADRESSE_LIVRAISON);
        return livraison;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Livraison createUpdatedEntity(EntityManager em) {
        Livraison livraison = new Livraison()
            .idLivraison(UPDATED_ID_LIVRAISON)
            .prixLivraison(UPDATED_PRIX_LIVRAISON)
            .duree(UPDATED_DUREE)
            .adresseLivraison(UPDATED_ADRESSE_LIVRAISON);
        return livraison;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Livraison.class).block();
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
        livraison = createEntity(em);
    }

    @Test
    void createLivraison() throws Exception {
        int databaseSizeBeforeCreate = livraisonRepository.findAll().collectList().block().size();
        // Create the Livraison
        LivraisonDTO livraisonDTO = livraisonMapper.toDto(livraison);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(livraisonDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Livraison in the database
        List<Livraison> livraisonList = livraisonRepository.findAll().collectList().block();
        assertThat(livraisonList).hasSize(databaseSizeBeforeCreate + 1);
        Livraison testLivraison = livraisonList.get(livraisonList.size() - 1);
        assertThat(testLivraison.getIdLivraison()).isEqualTo(DEFAULT_ID_LIVRAISON);
        assertThat(testLivraison.getPrixLivraison()).isEqualTo(DEFAULT_PRIX_LIVRAISON);
        assertThat(testLivraison.getDuree()).isEqualTo(DEFAULT_DUREE);
        assertThat(testLivraison.getAdresseLivraison()).isEqualTo(DEFAULT_ADRESSE_LIVRAISON);
    }

    @Test
    void createLivraisonWithExistingId() throws Exception {
        // Create the Livraison with an existing ID
        livraison.setId(1L);
        LivraisonDTO livraisonDTO = livraisonMapper.toDto(livraison);

        int databaseSizeBeforeCreate = livraisonRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(livraisonDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Livraison in the database
        List<Livraison> livraisonList = livraisonRepository.findAll().collectList().block();
        assertThat(livraisonList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkIdLivraisonIsRequired() throws Exception {
        int databaseSizeBeforeTest = livraisonRepository.findAll().collectList().block().size();
        // set the field null
        livraison.setIdLivraison(null);

        // Create the Livraison, which fails.
        LivraisonDTO livraisonDTO = livraisonMapper.toDto(livraison);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(livraisonDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Livraison> livraisonList = livraisonRepository.findAll().collectList().block();
        assertThat(livraisonList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkPrixLivraisonIsRequired() throws Exception {
        int databaseSizeBeforeTest = livraisonRepository.findAll().collectList().block().size();
        // set the field null
        livraison.setPrixLivraison(null);

        // Create the Livraison, which fails.
        LivraisonDTO livraisonDTO = livraisonMapper.toDto(livraison);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(livraisonDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Livraison> livraisonList = livraisonRepository.findAll().collectList().block();
        assertThat(livraisonList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkDureeIsRequired() throws Exception {
        int databaseSizeBeforeTest = livraisonRepository.findAll().collectList().block().size();
        // set the field null
        livraison.setDuree(null);

        // Create the Livraison, which fails.
        LivraisonDTO livraisonDTO = livraisonMapper.toDto(livraison);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(livraisonDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Livraison> livraisonList = livraisonRepository.findAll().collectList().block();
        assertThat(livraisonList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkAdresseLivraisonIsRequired() throws Exception {
        int databaseSizeBeforeTest = livraisonRepository.findAll().collectList().block().size();
        // set the field null
        livraison.setAdresseLivraison(null);

        // Create the Livraison, which fails.
        LivraisonDTO livraisonDTO = livraisonMapper.toDto(livraison);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(livraisonDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Livraison> livraisonList = livraisonRepository.findAll().collectList().block();
        assertThat(livraisonList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllLivraisonsAsStream() {
        // Initialize the database
        livraisonRepository.save(livraison).block();

        List<Livraison> livraisonList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(LivraisonDTO.class)
            .getResponseBody()
            .map(livraisonMapper::toEntity)
            .filter(livraison::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(livraisonList).isNotNull();
        assertThat(livraisonList).hasSize(1);
        Livraison testLivraison = livraisonList.get(0);
        assertThat(testLivraison.getIdLivraison()).isEqualTo(DEFAULT_ID_LIVRAISON);
        assertThat(testLivraison.getPrixLivraison()).isEqualTo(DEFAULT_PRIX_LIVRAISON);
        assertThat(testLivraison.getDuree()).isEqualTo(DEFAULT_DUREE);
        assertThat(testLivraison.getAdresseLivraison()).isEqualTo(DEFAULT_ADRESSE_LIVRAISON);
    }

    @Test
    void getAllLivraisons() {
        // Initialize the database
        livraisonRepository.save(livraison).block();

        // Get all the livraisonList
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
            .value(hasItem(livraison.getId().intValue()))
            .jsonPath("$.[*].idLivraison")
            .value(hasItem(DEFAULT_ID_LIVRAISON))
            .jsonPath("$.[*].prixLivraison")
            .value(hasItem(DEFAULT_PRIX_LIVRAISON.doubleValue()))
            .jsonPath("$.[*].duree")
            .value(hasItem(DEFAULT_DUREE.toString()))
            .jsonPath("$.[*].adresseLivraison")
            .value(hasItem(DEFAULT_ADRESSE_LIVRAISON));
    }

    @Test
    void getLivraison() {
        // Initialize the database
        livraisonRepository.save(livraison).block();

        // Get the livraison
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, livraison.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(livraison.getId().intValue()))
            .jsonPath("$.idLivraison")
            .value(is(DEFAULT_ID_LIVRAISON))
            .jsonPath("$.prixLivraison")
            .value(is(DEFAULT_PRIX_LIVRAISON.doubleValue()))
            .jsonPath("$.duree")
            .value(is(DEFAULT_DUREE.toString()))
            .jsonPath("$.adresseLivraison")
            .value(is(DEFAULT_ADRESSE_LIVRAISON));
    }

    @Test
    void getNonExistingLivraison() {
        // Get the livraison
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingLivraison() throws Exception {
        // Initialize the database
        livraisonRepository.save(livraison).block();

        int databaseSizeBeforeUpdate = livraisonRepository.findAll().collectList().block().size();

        // Update the livraison
        Livraison updatedLivraison = livraisonRepository.findById(livraison.getId()).block();
        updatedLivraison
            .idLivraison(UPDATED_ID_LIVRAISON)
            .prixLivraison(UPDATED_PRIX_LIVRAISON)
            .duree(UPDATED_DUREE)
            .adresseLivraison(UPDATED_ADRESSE_LIVRAISON);
        LivraisonDTO livraisonDTO = livraisonMapper.toDto(updatedLivraison);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, livraisonDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(livraisonDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Livraison in the database
        List<Livraison> livraisonList = livraisonRepository.findAll().collectList().block();
        assertThat(livraisonList).hasSize(databaseSizeBeforeUpdate);
        Livraison testLivraison = livraisonList.get(livraisonList.size() - 1);
        assertThat(testLivraison.getIdLivraison()).isEqualTo(UPDATED_ID_LIVRAISON);
        assertThat(testLivraison.getPrixLivraison()).isEqualTo(UPDATED_PRIX_LIVRAISON);
        assertThat(testLivraison.getDuree()).isEqualTo(UPDATED_DUREE);
        assertThat(testLivraison.getAdresseLivraison()).isEqualTo(UPDATED_ADRESSE_LIVRAISON);
    }

    @Test
    void putNonExistingLivraison() throws Exception {
        int databaseSizeBeforeUpdate = livraisonRepository.findAll().collectList().block().size();
        livraison.setId(count.incrementAndGet());

        // Create the Livraison
        LivraisonDTO livraisonDTO = livraisonMapper.toDto(livraison);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, livraisonDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(livraisonDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Livraison in the database
        List<Livraison> livraisonList = livraisonRepository.findAll().collectList().block();
        assertThat(livraisonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchLivraison() throws Exception {
        int databaseSizeBeforeUpdate = livraisonRepository.findAll().collectList().block().size();
        livraison.setId(count.incrementAndGet());

        // Create the Livraison
        LivraisonDTO livraisonDTO = livraisonMapper.toDto(livraison);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(livraisonDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Livraison in the database
        List<Livraison> livraisonList = livraisonRepository.findAll().collectList().block();
        assertThat(livraisonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamLivraison() throws Exception {
        int databaseSizeBeforeUpdate = livraisonRepository.findAll().collectList().block().size();
        livraison.setId(count.incrementAndGet());

        // Create the Livraison
        LivraisonDTO livraisonDTO = livraisonMapper.toDto(livraison);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(livraisonDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Livraison in the database
        List<Livraison> livraisonList = livraisonRepository.findAll().collectList().block();
        assertThat(livraisonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateLivraisonWithPatch() throws Exception {
        // Initialize the database
        livraisonRepository.save(livraison).block();

        int databaseSizeBeforeUpdate = livraisonRepository.findAll().collectList().block().size();

        // Update the livraison using partial update
        Livraison partialUpdatedLivraison = new Livraison();
        partialUpdatedLivraison.setId(livraison.getId());

        partialUpdatedLivraison.duree(UPDATED_DUREE).adresseLivraison(UPDATED_ADRESSE_LIVRAISON);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedLivraison.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedLivraison))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Livraison in the database
        List<Livraison> livraisonList = livraisonRepository.findAll().collectList().block();
        assertThat(livraisonList).hasSize(databaseSizeBeforeUpdate);
        Livraison testLivraison = livraisonList.get(livraisonList.size() - 1);
        assertThat(testLivraison.getIdLivraison()).isEqualTo(DEFAULT_ID_LIVRAISON);
        assertThat(testLivraison.getPrixLivraison()).isEqualTo(DEFAULT_PRIX_LIVRAISON);
        assertThat(testLivraison.getDuree()).isEqualTo(UPDATED_DUREE);
        assertThat(testLivraison.getAdresseLivraison()).isEqualTo(UPDATED_ADRESSE_LIVRAISON);
    }

    @Test
    void fullUpdateLivraisonWithPatch() throws Exception {
        // Initialize the database
        livraisonRepository.save(livraison).block();

        int databaseSizeBeforeUpdate = livraisonRepository.findAll().collectList().block().size();

        // Update the livraison using partial update
        Livraison partialUpdatedLivraison = new Livraison();
        partialUpdatedLivraison.setId(livraison.getId());

        partialUpdatedLivraison
            .idLivraison(UPDATED_ID_LIVRAISON)
            .prixLivraison(UPDATED_PRIX_LIVRAISON)
            .duree(UPDATED_DUREE)
            .adresseLivraison(UPDATED_ADRESSE_LIVRAISON);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedLivraison.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedLivraison))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Livraison in the database
        List<Livraison> livraisonList = livraisonRepository.findAll().collectList().block();
        assertThat(livraisonList).hasSize(databaseSizeBeforeUpdate);
        Livraison testLivraison = livraisonList.get(livraisonList.size() - 1);
        assertThat(testLivraison.getIdLivraison()).isEqualTo(UPDATED_ID_LIVRAISON);
        assertThat(testLivraison.getPrixLivraison()).isEqualTo(UPDATED_PRIX_LIVRAISON);
        assertThat(testLivraison.getDuree()).isEqualTo(UPDATED_DUREE);
        assertThat(testLivraison.getAdresseLivraison()).isEqualTo(UPDATED_ADRESSE_LIVRAISON);
    }

    @Test
    void patchNonExistingLivraison() throws Exception {
        int databaseSizeBeforeUpdate = livraisonRepository.findAll().collectList().block().size();
        livraison.setId(count.incrementAndGet());

        // Create the Livraison
        LivraisonDTO livraisonDTO = livraisonMapper.toDto(livraison);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, livraisonDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(livraisonDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Livraison in the database
        List<Livraison> livraisonList = livraisonRepository.findAll().collectList().block();
        assertThat(livraisonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchLivraison() throws Exception {
        int databaseSizeBeforeUpdate = livraisonRepository.findAll().collectList().block().size();
        livraison.setId(count.incrementAndGet());

        // Create the Livraison
        LivraisonDTO livraisonDTO = livraisonMapper.toDto(livraison);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(livraisonDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Livraison in the database
        List<Livraison> livraisonList = livraisonRepository.findAll().collectList().block();
        assertThat(livraisonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamLivraison() throws Exception {
        int databaseSizeBeforeUpdate = livraisonRepository.findAll().collectList().block().size();
        livraison.setId(count.incrementAndGet());

        // Create the Livraison
        LivraisonDTO livraisonDTO = livraisonMapper.toDto(livraison);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(livraisonDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Livraison in the database
        List<Livraison> livraisonList = livraisonRepository.findAll().collectList().block();
        assertThat(livraisonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteLivraison() {
        // Initialize the database
        livraisonRepository.save(livraison).block();

        int databaseSizeBeforeDelete = livraisonRepository.findAll().collectList().block().size();

        // Delete the livraison
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, livraison.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Livraison> livraisonList = livraisonRepository.findAll().collectList().block();
        assertThat(livraisonList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
