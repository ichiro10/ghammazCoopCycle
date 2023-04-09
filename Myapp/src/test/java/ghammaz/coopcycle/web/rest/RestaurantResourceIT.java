package ghammaz.coopcycle.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import ghammaz.coopcycle.IntegrationTest;
import ghammaz.coopcycle.domain.Restaurant;
import ghammaz.coopcycle.repository.EntityManager;
import ghammaz.coopcycle.repository.RestaurantRepository;
import ghammaz.coopcycle.service.dto.RestaurantDTO;
import ghammaz.coopcycle.service.mapper.RestaurantMapper;
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
 * Integration tests for the {@link RestaurantResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class RestaurantResourceIT {

    private static final Integer DEFAULT_ID_REST = 1;
    private static final Integer UPDATED_ID_REST = 2;

    private static final String DEFAULT_NOM_REST = "AAAAAAAAAA";
    private static final String UPDATED_NOM_REST = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESSE_REST = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE_REST = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/restaurants";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private RestaurantMapper restaurantMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Restaurant restaurant;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Restaurant createEntity(EntityManager em) {
        Restaurant restaurant = new Restaurant().idRest(DEFAULT_ID_REST).nomRest(DEFAULT_NOM_REST).adresseRest(DEFAULT_ADRESSE_REST);
        return restaurant;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Restaurant createUpdatedEntity(EntityManager em) {
        Restaurant restaurant = new Restaurant().idRest(UPDATED_ID_REST).nomRest(UPDATED_NOM_REST).adresseRest(UPDATED_ADRESSE_REST);
        return restaurant;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Restaurant.class).block();
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
        restaurant = createEntity(em);
    }

    @Test
    void createRestaurant() throws Exception {
        int databaseSizeBeforeCreate = restaurantRepository.findAll().collectList().block().size();
        // Create the Restaurant
        RestaurantDTO restaurantDTO = restaurantMapper.toDto(restaurant);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(restaurantDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Restaurant in the database
        List<Restaurant> restaurantList = restaurantRepository.findAll().collectList().block();
        assertThat(restaurantList).hasSize(databaseSizeBeforeCreate + 1);
        Restaurant testRestaurant = restaurantList.get(restaurantList.size() - 1);
        assertThat(testRestaurant.getIdRest()).isEqualTo(DEFAULT_ID_REST);
        assertThat(testRestaurant.getNomRest()).isEqualTo(DEFAULT_NOM_REST);
        assertThat(testRestaurant.getAdresseRest()).isEqualTo(DEFAULT_ADRESSE_REST);
    }

    @Test
    void createRestaurantWithExistingId() throws Exception {
        // Create the Restaurant with an existing ID
        restaurant.setId(1L);
        RestaurantDTO restaurantDTO = restaurantMapper.toDto(restaurant);

        int databaseSizeBeforeCreate = restaurantRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(restaurantDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Restaurant in the database
        List<Restaurant> restaurantList = restaurantRepository.findAll().collectList().block();
        assertThat(restaurantList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkIdRestIsRequired() throws Exception {
        int databaseSizeBeforeTest = restaurantRepository.findAll().collectList().block().size();
        // set the field null
        restaurant.setIdRest(null);

        // Create the Restaurant, which fails.
        RestaurantDTO restaurantDTO = restaurantMapper.toDto(restaurant);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(restaurantDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Restaurant> restaurantList = restaurantRepository.findAll().collectList().block();
        assertThat(restaurantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkNomRestIsRequired() throws Exception {
        int databaseSizeBeforeTest = restaurantRepository.findAll().collectList().block().size();
        // set the field null
        restaurant.setNomRest(null);

        // Create the Restaurant, which fails.
        RestaurantDTO restaurantDTO = restaurantMapper.toDto(restaurant);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(restaurantDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Restaurant> restaurantList = restaurantRepository.findAll().collectList().block();
        assertThat(restaurantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkAdresseRestIsRequired() throws Exception {
        int databaseSizeBeforeTest = restaurantRepository.findAll().collectList().block().size();
        // set the field null
        restaurant.setAdresseRest(null);

        // Create the Restaurant, which fails.
        RestaurantDTO restaurantDTO = restaurantMapper.toDto(restaurant);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(restaurantDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Restaurant> restaurantList = restaurantRepository.findAll().collectList().block();
        assertThat(restaurantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllRestaurantsAsStream() {
        // Initialize the database
        restaurantRepository.save(restaurant).block();

        List<Restaurant> restaurantList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(RestaurantDTO.class)
            .getResponseBody()
            .map(restaurantMapper::toEntity)
            .filter(restaurant::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(restaurantList).isNotNull();
        assertThat(restaurantList).hasSize(1);
        Restaurant testRestaurant = restaurantList.get(0);
        assertThat(testRestaurant.getIdRest()).isEqualTo(DEFAULT_ID_REST);
        assertThat(testRestaurant.getNomRest()).isEqualTo(DEFAULT_NOM_REST);
        assertThat(testRestaurant.getAdresseRest()).isEqualTo(DEFAULT_ADRESSE_REST);
    }

    @Test
    void getAllRestaurants() {
        // Initialize the database
        restaurantRepository.save(restaurant).block();

        // Get all the restaurantList
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
            .value(hasItem(restaurant.getId().intValue()))
            .jsonPath("$.[*].idRest")
            .value(hasItem(DEFAULT_ID_REST))
            .jsonPath("$.[*].nomRest")
            .value(hasItem(DEFAULT_NOM_REST))
            .jsonPath("$.[*].adresseRest")
            .value(hasItem(DEFAULT_ADRESSE_REST));
    }

    @Test
    void getRestaurant() {
        // Initialize the database
        restaurantRepository.save(restaurant).block();

        // Get the restaurant
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, restaurant.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(restaurant.getId().intValue()))
            .jsonPath("$.idRest")
            .value(is(DEFAULT_ID_REST))
            .jsonPath("$.nomRest")
            .value(is(DEFAULT_NOM_REST))
            .jsonPath("$.adresseRest")
            .value(is(DEFAULT_ADRESSE_REST));
    }

    @Test
    void getNonExistingRestaurant() {
        // Get the restaurant
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingRestaurant() throws Exception {
        // Initialize the database
        restaurantRepository.save(restaurant).block();

        int databaseSizeBeforeUpdate = restaurantRepository.findAll().collectList().block().size();

        // Update the restaurant
        Restaurant updatedRestaurant = restaurantRepository.findById(restaurant.getId()).block();
        updatedRestaurant.idRest(UPDATED_ID_REST).nomRest(UPDATED_NOM_REST).adresseRest(UPDATED_ADRESSE_REST);
        RestaurantDTO restaurantDTO = restaurantMapper.toDto(updatedRestaurant);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, restaurantDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(restaurantDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Restaurant in the database
        List<Restaurant> restaurantList = restaurantRepository.findAll().collectList().block();
        assertThat(restaurantList).hasSize(databaseSizeBeforeUpdate);
        Restaurant testRestaurant = restaurantList.get(restaurantList.size() - 1);
        assertThat(testRestaurant.getIdRest()).isEqualTo(UPDATED_ID_REST);
        assertThat(testRestaurant.getNomRest()).isEqualTo(UPDATED_NOM_REST);
        assertThat(testRestaurant.getAdresseRest()).isEqualTo(UPDATED_ADRESSE_REST);
    }

    @Test
    void putNonExistingRestaurant() throws Exception {
        int databaseSizeBeforeUpdate = restaurantRepository.findAll().collectList().block().size();
        restaurant.setId(count.incrementAndGet());

        // Create the Restaurant
        RestaurantDTO restaurantDTO = restaurantMapper.toDto(restaurant);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, restaurantDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(restaurantDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Restaurant in the database
        List<Restaurant> restaurantList = restaurantRepository.findAll().collectList().block();
        assertThat(restaurantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchRestaurant() throws Exception {
        int databaseSizeBeforeUpdate = restaurantRepository.findAll().collectList().block().size();
        restaurant.setId(count.incrementAndGet());

        // Create the Restaurant
        RestaurantDTO restaurantDTO = restaurantMapper.toDto(restaurant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(restaurantDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Restaurant in the database
        List<Restaurant> restaurantList = restaurantRepository.findAll().collectList().block();
        assertThat(restaurantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamRestaurant() throws Exception {
        int databaseSizeBeforeUpdate = restaurantRepository.findAll().collectList().block().size();
        restaurant.setId(count.incrementAndGet());

        // Create the Restaurant
        RestaurantDTO restaurantDTO = restaurantMapper.toDto(restaurant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(restaurantDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Restaurant in the database
        List<Restaurant> restaurantList = restaurantRepository.findAll().collectList().block();
        assertThat(restaurantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateRestaurantWithPatch() throws Exception {
        // Initialize the database
        restaurantRepository.save(restaurant).block();

        int databaseSizeBeforeUpdate = restaurantRepository.findAll().collectList().block().size();

        // Update the restaurant using partial update
        Restaurant partialUpdatedRestaurant = new Restaurant();
        partialUpdatedRestaurant.setId(restaurant.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedRestaurant.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedRestaurant))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Restaurant in the database
        List<Restaurant> restaurantList = restaurantRepository.findAll().collectList().block();
        assertThat(restaurantList).hasSize(databaseSizeBeforeUpdate);
        Restaurant testRestaurant = restaurantList.get(restaurantList.size() - 1);
        assertThat(testRestaurant.getIdRest()).isEqualTo(DEFAULT_ID_REST);
        assertThat(testRestaurant.getNomRest()).isEqualTo(DEFAULT_NOM_REST);
        assertThat(testRestaurant.getAdresseRest()).isEqualTo(DEFAULT_ADRESSE_REST);
    }

    @Test
    void fullUpdateRestaurantWithPatch() throws Exception {
        // Initialize the database
        restaurantRepository.save(restaurant).block();

        int databaseSizeBeforeUpdate = restaurantRepository.findAll().collectList().block().size();

        // Update the restaurant using partial update
        Restaurant partialUpdatedRestaurant = new Restaurant();
        partialUpdatedRestaurant.setId(restaurant.getId());

        partialUpdatedRestaurant.idRest(UPDATED_ID_REST).nomRest(UPDATED_NOM_REST).adresseRest(UPDATED_ADRESSE_REST);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedRestaurant.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedRestaurant))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Restaurant in the database
        List<Restaurant> restaurantList = restaurantRepository.findAll().collectList().block();
        assertThat(restaurantList).hasSize(databaseSizeBeforeUpdate);
        Restaurant testRestaurant = restaurantList.get(restaurantList.size() - 1);
        assertThat(testRestaurant.getIdRest()).isEqualTo(UPDATED_ID_REST);
        assertThat(testRestaurant.getNomRest()).isEqualTo(UPDATED_NOM_REST);
        assertThat(testRestaurant.getAdresseRest()).isEqualTo(UPDATED_ADRESSE_REST);
    }

    @Test
    void patchNonExistingRestaurant() throws Exception {
        int databaseSizeBeforeUpdate = restaurantRepository.findAll().collectList().block().size();
        restaurant.setId(count.incrementAndGet());

        // Create the Restaurant
        RestaurantDTO restaurantDTO = restaurantMapper.toDto(restaurant);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, restaurantDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(restaurantDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Restaurant in the database
        List<Restaurant> restaurantList = restaurantRepository.findAll().collectList().block();
        assertThat(restaurantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchRestaurant() throws Exception {
        int databaseSizeBeforeUpdate = restaurantRepository.findAll().collectList().block().size();
        restaurant.setId(count.incrementAndGet());

        // Create the Restaurant
        RestaurantDTO restaurantDTO = restaurantMapper.toDto(restaurant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(restaurantDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Restaurant in the database
        List<Restaurant> restaurantList = restaurantRepository.findAll().collectList().block();
        assertThat(restaurantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamRestaurant() throws Exception {
        int databaseSizeBeforeUpdate = restaurantRepository.findAll().collectList().block().size();
        restaurant.setId(count.incrementAndGet());

        // Create the Restaurant
        RestaurantDTO restaurantDTO = restaurantMapper.toDto(restaurant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(restaurantDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Restaurant in the database
        List<Restaurant> restaurantList = restaurantRepository.findAll().collectList().block();
        assertThat(restaurantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteRestaurant() {
        // Initialize the database
        restaurantRepository.save(restaurant).block();

        int databaseSizeBeforeDelete = restaurantRepository.findAll().collectList().block().size();

        // Delete the restaurant
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, restaurant.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Restaurant> restaurantList = restaurantRepository.findAll().collectList().block();
        assertThat(restaurantList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
