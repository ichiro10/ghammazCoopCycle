package ghammaz.coopcycle.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import ghammaz.coopcycle.IntegrationTest;
import ghammaz.coopcycle.domain.Client;
import ghammaz.coopcycle.repository.ClientRepository;
import ghammaz.coopcycle.repository.EntityManager;
import ghammaz.coopcycle.service.dto.ClientDTO;
import ghammaz.coopcycle.service.mapper.ClientMapper;
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
 * Integration tests for the {@link ClientResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ClientResourceIT {

    private static final Integer DEFAULT_ID_CLIENT = 1;
    private static final Integer UPDATED_ID_CLIENT = 2;

    private static final String DEFAULT_PRENOM_CLIENT = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM_CLIENT = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_CLIENT = "AAAAAAAAAA";
    private static final String UPDATED_NOM_CLIENT = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESSE_CLIENT = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE_CLIENT = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "1Gsl@HjV^svwhar";
    private static final String UPDATED_EMAIL = "uPI@zywexq";

    private static final String DEFAULT_TEL_C_LIENT = "AAAAAAAAAA";
    private static final String UPDATED_TEL_C_LIENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/clients";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ClientMapper clientMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Client client;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Client createEntity(EntityManager em) {
        Client client = new Client()
            .idClient(DEFAULT_ID_CLIENT)
            .prenomClient(DEFAULT_PRENOM_CLIENT)
            .nomClient(DEFAULT_NOM_CLIENT)
            .adresseClient(DEFAULT_ADRESSE_CLIENT)
            .email(DEFAULT_EMAIL)
            .telCLient(DEFAULT_TEL_C_LIENT);
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
            .idClient(UPDATED_ID_CLIENT)
            .prenomClient(UPDATED_PRENOM_CLIENT)
            .nomClient(UPDATED_NOM_CLIENT)
            .adresseClient(UPDATED_ADRESSE_CLIENT)
            .email(UPDATED_EMAIL)
            .telCLient(UPDATED_TEL_C_LIENT);
        return client;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Client.class).block();
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
        client = createEntity(em);
    }

    @Test
    void createClient() throws Exception {
        int databaseSizeBeforeCreate = clientRepository.findAll().collectList().block().size();
        // Create the Client
        ClientDTO clientDTO = clientMapper.toDto(client);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(clientDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll().collectList().block();
        assertThat(clientList).hasSize(databaseSizeBeforeCreate + 1);
        Client testClient = clientList.get(clientList.size() - 1);
        assertThat(testClient.getIdClient()).isEqualTo(DEFAULT_ID_CLIENT);
        assertThat(testClient.getPrenomClient()).isEqualTo(DEFAULT_PRENOM_CLIENT);
        assertThat(testClient.getNomClient()).isEqualTo(DEFAULT_NOM_CLIENT);
        assertThat(testClient.getAdresseClient()).isEqualTo(DEFAULT_ADRESSE_CLIENT);
        assertThat(testClient.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testClient.getTelCLient()).isEqualTo(DEFAULT_TEL_C_LIENT);
    }

    @Test
    void createClientWithExistingId() throws Exception {
        // Create the Client with an existing ID
        client.setId(1L);
        ClientDTO clientDTO = clientMapper.toDto(client);

        int databaseSizeBeforeCreate = clientRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(clientDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll().collectList().block();
        assertThat(clientList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkIdClientIsRequired() throws Exception {
        int databaseSizeBeforeTest = clientRepository.findAll().collectList().block().size();
        // set the field null
        client.setIdClient(null);

        // Create the Client, which fails.
        ClientDTO clientDTO = clientMapper.toDto(client);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(clientDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Client> clientList = clientRepository.findAll().collectList().block();
        assertThat(clientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkPrenomClientIsRequired() throws Exception {
        int databaseSizeBeforeTest = clientRepository.findAll().collectList().block().size();
        // set the field null
        client.setPrenomClient(null);

        // Create the Client, which fails.
        ClientDTO clientDTO = clientMapper.toDto(client);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(clientDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Client> clientList = clientRepository.findAll().collectList().block();
        assertThat(clientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkNomClientIsRequired() throws Exception {
        int databaseSizeBeforeTest = clientRepository.findAll().collectList().block().size();
        // set the field null
        client.setNomClient(null);

        // Create the Client, which fails.
        ClientDTO clientDTO = clientMapper.toDto(client);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(clientDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Client> clientList = clientRepository.findAll().collectList().block();
        assertThat(clientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkAdresseClientIsRequired() throws Exception {
        int databaseSizeBeforeTest = clientRepository.findAll().collectList().block().size();
        // set the field null
        client.setAdresseClient(null);

        // Create the Client, which fails.
        ClientDTO clientDTO = clientMapper.toDto(client);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(clientDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Client> clientList = clientRepository.findAll().collectList().block();
        assertThat(clientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = clientRepository.findAll().collectList().block().size();
        // set the field null
        client.setEmail(null);

        // Create the Client, which fails.
        ClientDTO clientDTO = clientMapper.toDto(client);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(clientDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Client> clientList = clientRepository.findAll().collectList().block();
        assertThat(clientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkTelCLientIsRequired() throws Exception {
        int databaseSizeBeforeTest = clientRepository.findAll().collectList().block().size();
        // set the field null
        client.setTelCLient(null);

        // Create the Client, which fails.
        ClientDTO clientDTO = clientMapper.toDto(client);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(clientDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Client> clientList = clientRepository.findAll().collectList().block();
        assertThat(clientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllClientsAsStream() {
        // Initialize the database
        clientRepository.save(client).block();

        List<Client> clientList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(ClientDTO.class)
            .getResponseBody()
            .map(clientMapper::toEntity)
            .filter(client::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(clientList).isNotNull();
        assertThat(clientList).hasSize(1);
        Client testClient = clientList.get(0);
        assertThat(testClient.getIdClient()).isEqualTo(DEFAULT_ID_CLIENT);
        assertThat(testClient.getPrenomClient()).isEqualTo(DEFAULT_PRENOM_CLIENT);
        assertThat(testClient.getNomClient()).isEqualTo(DEFAULT_NOM_CLIENT);
        assertThat(testClient.getAdresseClient()).isEqualTo(DEFAULT_ADRESSE_CLIENT);
        assertThat(testClient.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testClient.getTelCLient()).isEqualTo(DEFAULT_TEL_C_LIENT);
    }

    @Test
    void getAllClients() {
        // Initialize the database
        clientRepository.save(client).block();

        // Get all the clientList
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
            .value(hasItem(client.getId().intValue()))
            .jsonPath("$.[*].idClient")
            .value(hasItem(DEFAULT_ID_CLIENT))
            .jsonPath("$.[*].prenomClient")
            .value(hasItem(DEFAULT_PRENOM_CLIENT))
            .jsonPath("$.[*].nomClient")
            .value(hasItem(DEFAULT_NOM_CLIENT))
            .jsonPath("$.[*].adresseClient")
            .value(hasItem(DEFAULT_ADRESSE_CLIENT))
            .jsonPath("$.[*].email")
            .value(hasItem(DEFAULT_EMAIL))
            .jsonPath("$.[*].telCLient")
            .value(hasItem(DEFAULT_TEL_C_LIENT));
    }

    @Test
    void getClient() {
        // Initialize the database
        clientRepository.save(client).block();

        // Get the client
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, client.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(client.getId().intValue()))
            .jsonPath("$.idClient")
            .value(is(DEFAULT_ID_CLIENT))
            .jsonPath("$.prenomClient")
            .value(is(DEFAULT_PRENOM_CLIENT))
            .jsonPath("$.nomClient")
            .value(is(DEFAULT_NOM_CLIENT))
            .jsonPath("$.adresseClient")
            .value(is(DEFAULT_ADRESSE_CLIENT))
            .jsonPath("$.email")
            .value(is(DEFAULT_EMAIL))
            .jsonPath("$.telCLient")
            .value(is(DEFAULT_TEL_C_LIENT));
    }

    @Test
    void getNonExistingClient() {
        // Get the client
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingClient() throws Exception {
        // Initialize the database
        clientRepository.save(client).block();

        int databaseSizeBeforeUpdate = clientRepository.findAll().collectList().block().size();

        // Update the client
        Client updatedClient = clientRepository.findById(client.getId()).block();
        updatedClient
            .idClient(UPDATED_ID_CLIENT)
            .prenomClient(UPDATED_PRENOM_CLIENT)
            .nomClient(UPDATED_NOM_CLIENT)
            .adresseClient(UPDATED_ADRESSE_CLIENT)
            .email(UPDATED_EMAIL)
            .telCLient(UPDATED_TEL_C_LIENT);
        ClientDTO clientDTO = clientMapper.toDto(updatedClient);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, clientDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(clientDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll().collectList().block();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
        Client testClient = clientList.get(clientList.size() - 1);
        assertThat(testClient.getIdClient()).isEqualTo(UPDATED_ID_CLIENT);
        assertThat(testClient.getPrenomClient()).isEqualTo(UPDATED_PRENOM_CLIENT);
        assertThat(testClient.getNomClient()).isEqualTo(UPDATED_NOM_CLIENT);
        assertThat(testClient.getAdresseClient()).isEqualTo(UPDATED_ADRESSE_CLIENT);
        assertThat(testClient.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testClient.getTelCLient()).isEqualTo(UPDATED_TEL_C_LIENT);
    }

    @Test
    void putNonExistingClient() throws Exception {
        int databaseSizeBeforeUpdate = clientRepository.findAll().collectList().block().size();
        client.setId(count.incrementAndGet());

        // Create the Client
        ClientDTO clientDTO = clientMapper.toDto(client);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, clientDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(clientDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll().collectList().block();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchClient() throws Exception {
        int databaseSizeBeforeUpdate = clientRepository.findAll().collectList().block().size();
        client.setId(count.incrementAndGet());

        // Create the Client
        ClientDTO clientDTO = clientMapper.toDto(client);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(clientDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll().collectList().block();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamClient() throws Exception {
        int databaseSizeBeforeUpdate = clientRepository.findAll().collectList().block().size();
        client.setId(count.incrementAndGet());

        // Create the Client
        ClientDTO clientDTO = clientMapper.toDto(client);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(clientDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll().collectList().block();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateClientWithPatch() throws Exception {
        // Initialize the database
        clientRepository.save(client).block();

        int databaseSizeBeforeUpdate = clientRepository.findAll().collectList().block().size();

        // Update the client using partial update
        Client partialUpdatedClient = new Client();
        partialUpdatedClient.setId(client.getId());

        partialUpdatedClient.prenomClient(UPDATED_PRENOM_CLIENT).nomClient(UPDATED_NOM_CLIENT).telCLient(UPDATED_TEL_C_LIENT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedClient.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedClient))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll().collectList().block();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
        Client testClient = clientList.get(clientList.size() - 1);
        assertThat(testClient.getIdClient()).isEqualTo(DEFAULT_ID_CLIENT);
        assertThat(testClient.getPrenomClient()).isEqualTo(UPDATED_PRENOM_CLIENT);
        assertThat(testClient.getNomClient()).isEqualTo(UPDATED_NOM_CLIENT);
        assertThat(testClient.getAdresseClient()).isEqualTo(DEFAULT_ADRESSE_CLIENT);
        assertThat(testClient.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testClient.getTelCLient()).isEqualTo(UPDATED_TEL_C_LIENT);
    }

    @Test
    void fullUpdateClientWithPatch() throws Exception {
        // Initialize the database
        clientRepository.save(client).block();

        int databaseSizeBeforeUpdate = clientRepository.findAll().collectList().block().size();

        // Update the client using partial update
        Client partialUpdatedClient = new Client();
        partialUpdatedClient.setId(client.getId());

        partialUpdatedClient
            .idClient(UPDATED_ID_CLIENT)
            .prenomClient(UPDATED_PRENOM_CLIENT)
            .nomClient(UPDATED_NOM_CLIENT)
            .adresseClient(UPDATED_ADRESSE_CLIENT)
            .email(UPDATED_EMAIL)
            .telCLient(UPDATED_TEL_C_LIENT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedClient.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedClient))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll().collectList().block();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
        Client testClient = clientList.get(clientList.size() - 1);
        assertThat(testClient.getIdClient()).isEqualTo(UPDATED_ID_CLIENT);
        assertThat(testClient.getPrenomClient()).isEqualTo(UPDATED_PRENOM_CLIENT);
        assertThat(testClient.getNomClient()).isEqualTo(UPDATED_NOM_CLIENT);
        assertThat(testClient.getAdresseClient()).isEqualTo(UPDATED_ADRESSE_CLIENT);
        assertThat(testClient.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testClient.getTelCLient()).isEqualTo(UPDATED_TEL_C_LIENT);
    }

    @Test
    void patchNonExistingClient() throws Exception {
        int databaseSizeBeforeUpdate = clientRepository.findAll().collectList().block().size();
        client.setId(count.incrementAndGet());

        // Create the Client
        ClientDTO clientDTO = clientMapper.toDto(client);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, clientDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(clientDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll().collectList().block();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchClient() throws Exception {
        int databaseSizeBeforeUpdate = clientRepository.findAll().collectList().block().size();
        client.setId(count.incrementAndGet());

        // Create the Client
        ClientDTO clientDTO = clientMapper.toDto(client);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(clientDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll().collectList().block();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamClient() throws Exception {
        int databaseSizeBeforeUpdate = clientRepository.findAll().collectList().block().size();
        client.setId(count.incrementAndGet());

        // Create the Client
        ClientDTO clientDTO = clientMapper.toDto(client);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(clientDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll().collectList().block();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteClient() {
        // Initialize the database
        clientRepository.save(client).block();

        int databaseSizeBeforeDelete = clientRepository.findAll().collectList().block().size();

        // Delete the client
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, client.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Client> clientList = clientRepository.findAll().collectList().block();
        assertThat(clientList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
