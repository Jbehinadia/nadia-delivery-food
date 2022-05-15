package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.CommandeDetails;
import com.mycompany.myapp.repository.CommandeDetailsRepository;
import com.mycompany.myapp.repository.EntityManager;
import com.mycompany.myapp.service.dto.CommandeDetailsDTO;
import com.mycompany.myapp.service.mapper.CommandeDetailsMapper;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link CommandeDetailsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class CommandeDetailsResourceIT {

    private static final Double DEFAULT_PRIX = 1D;
    private static final Double UPDATED_PRIX = 2D;

    private static final String DEFAULT_ETAT = "AAAAAAAAAA";
    private static final String UPDATED_ETAT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/commande-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CommandeDetailsRepository commandeDetailsRepository;

    @Autowired
    private CommandeDetailsMapper commandeDetailsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private CommandeDetails commandeDetails;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CommandeDetails createEntity(EntityManager em) {
        CommandeDetails commandeDetails = new CommandeDetails().prix(DEFAULT_PRIX).etat(DEFAULT_ETAT);
        return commandeDetails;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CommandeDetails createUpdatedEntity(EntityManager em) {
        CommandeDetails commandeDetails = new CommandeDetails().prix(UPDATED_PRIX).etat(UPDATED_ETAT);
        return commandeDetails;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(CommandeDetails.class).block();
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
        commandeDetails = createEntity(em);
    }

    @Test
    void createCommandeDetails() throws Exception {
        int databaseSizeBeforeCreate = commandeDetailsRepository.findAll().collectList().block().size();
        // Create the CommandeDetails
        CommandeDetailsDTO commandeDetailsDTO = commandeDetailsMapper.toDto(commandeDetails);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(commandeDetailsDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the CommandeDetails in the database
        List<CommandeDetails> commandeDetailsList = commandeDetailsRepository.findAll().collectList().block();
        assertThat(commandeDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        CommandeDetails testCommandeDetails = commandeDetailsList.get(commandeDetailsList.size() - 1);
        assertThat(testCommandeDetails.getPrix()).isEqualTo(DEFAULT_PRIX);
        assertThat(testCommandeDetails.getEtat()).isEqualTo(DEFAULT_ETAT);
    }

    @Test
    void createCommandeDetailsWithExistingId() throws Exception {
        // Create the CommandeDetails with an existing ID
        commandeDetails.setId(1L);
        CommandeDetailsDTO commandeDetailsDTO = commandeDetailsMapper.toDto(commandeDetails);

        int databaseSizeBeforeCreate = commandeDetailsRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(commandeDetailsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CommandeDetails in the database
        List<CommandeDetails> commandeDetailsList = commandeDetailsRepository.findAll().collectList().block();
        assertThat(commandeDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllCommandeDetails() {
        // Initialize the database
        commandeDetailsRepository.save(commandeDetails).block();

        // Get all the commandeDetailsList
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
            .value(hasItem(commandeDetails.getId().intValue()))
            .jsonPath("$.[*].prix")
            .value(hasItem(DEFAULT_PRIX.doubleValue()))
            .jsonPath("$.[*].etat")
            .value(hasItem(DEFAULT_ETAT));
    }

    @Test
    void getCommandeDetails() {
        // Initialize the database
        commandeDetailsRepository.save(commandeDetails).block();

        // Get the commandeDetails
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, commandeDetails.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(commandeDetails.getId().intValue()))
            .jsonPath("$.prix")
            .value(is(DEFAULT_PRIX.doubleValue()))
            .jsonPath("$.etat")
            .value(is(DEFAULT_ETAT));
    }

    @Test
    void getNonExistingCommandeDetails() {
        // Get the commandeDetails
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewCommandeDetails() throws Exception {
        // Initialize the database
        commandeDetailsRepository.save(commandeDetails).block();

        int databaseSizeBeforeUpdate = commandeDetailsRepository.findAll().collectList().block().size();

        // Update the commandeDetails
        CommandeDetails updatedCommandeDetails = commandeDetailsRepository.findById(commandeDetails.getId()).block();
        updatedCommandeDetails.prix(UPDATED_PRIX).etat(UPDATED_ETAT);
        CommandeDetailsDTO commandeDetailsDTO = commandeDetailsMapper.toDto(updatedCommandeDetails);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, commandeDetailsDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(commandeDetailsDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CommandeDetails in the database
        List<CommandeDetails> commandeDetailsList = commandeDetailsRepository.findAll().collectList().block();
        assertThat(commandeDetailsList).hasSize(databaseSizeBeforeUpdate);
        CommandeDetails testCommandeDetails = commandeDetailsList.get(commandeDetailsList.size() - 1);
        assertThat(testCommandeDetails.getPrix()).isEqualTo(UPDATED_PRIX);
        assertThat(testCommandeDetails.getEtat()).isEqualTo(UPDATED_ETAT);
    }

    @Test
    void putNonExistingCommandeDetails() throws Exception {
        int databaseSizeBeforeUpdate = commandeDetailsRepository.findAll().collectList().block().size();
        commandeDetails.setId(count.incrementAndGet());

        // Create the CommandeDetails
        CommandeDetailsDTO commandeDetailsDTO = commandeDetailsMapper.toDto(commandeDetails);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, commandeDetailsDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(commandeDetailsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CommandeDetails in the database
        List<CommandeDetails> commandeDetailsList = commandeDetailsRepository.findAll().collectList().block();
        assertThat(commandeDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCommandeDetails() throws Exception {
        int databaseSizeBeforeUpdate = commandeDetailsRepository.findAll().collectList().block().size();
        commandeDetails.setId(count.incrementAndGet());

        // Create the CommandeDetails
        CommandeDetailsDTO commandeDetailsDTO = commandeDetailsMapper.toDto(commandeDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(commandeDetailsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CommandeDetails in the database
        List<CommandeDetails> commandeDetailsList = commandeDetailsRepository.findAll().collectList().block();
        assertThat(commandeDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCommandeDetails() throws Exception {
        int databaseSizeBeforeUpdate = commandeDetailsRepository.findAll().collectList().block().size();
        commandeDetails.setId(count.incrementAndGet());

        // Create the CommandeDetails
        CommandeDetailsDTO commandeDetailsDTO = commandeDetailsMapper.toDto(commandeDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(commandeDetailsDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the CommandeDetails in the database
        List<CommandeDetails> commandeDetailsList = commandeDetailsRepository.findAll().collectList().block();
        assertThat(commandeDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateCommandeDetailsWithPatch() throws Exception {
        // Initialize the database
        commandeDetailsRepository.save(commandeDetails).block();

        int databaseSizeBeforeUpdate = commandeDetailsRepository.findAll().collectList().block().size();

        // Update the commandeDetails using partial update
        CommandeDetails partialUpdatedCommandeDetails = new CommandeDetails();
        partialUpdatedCommandeDetails.setId(commandeDetails.getId());

        partialUpdatedCommandeDetails.prix(UPDATED_PRIX).etat(UPDATED_ETAT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCommandeDetails.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCommandeDetails))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CommandeDetails in the database
        List<CommandeDetails> commandeDetailsList = commandeDetailsRepository.findAll().collectList().block();
        assertThat(commandeDetailsList).hasSize(databaseSizeBeforeUpdate);
        CommandeDetails testCommandeDetails = commandeDetailsList.get(commandeDetailsList.size() - 1);
        assertThat(testCommandeDetails.getPrix()).isEqualTo(UPDATED_PRIX);
        assertThat(testCommandeDetails.getEtat()).isEqualTo(UPDATED_ETAT);
    }

    @Test
    void fullUpdateCommandeDetailsWithPatch() throws Exception {
        // Initialize the database
        commandeDetailsRepository.save(commandeDetails).block();

        int databaseSizeBeforeUpdate = commandeDetailsRepository.findAll().collectList().block().size();

        // Update the commandeDetails using partial update
        CommandeDetails partialUpdatedCommandeDetails = new CommandeDetails();
        partialUpdatedCommandeDetails.setId(commandeDetails.getId());

        partialUpdatedCommandeDetails.prix(UPDATED_PRIX).etat(UPDATED_ETAT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCommandeDetails.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCommandeDetails))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CommandeDetails in the database
        List<CommandeDetails> commandeDetailsList = commandeDetailsRepository.findAll().collectList().block();
        assertThat(commandeDetailsList).hasSize(databaseSizeBeforeUpdate);
        CommandeDetails testCommandeDetails = commandeDetailsList.get(commandeDetailsList.size() - 1);
        assertThat(testCommandeDetails.getPrix()).isEqualTo(UPDATED_PRIX);
        assertThat(testCommandeDetails.getEtat()).isEqualTo(UPDATED_ETAT);
    }

    @Test
    void patchNonExistingCommandeDetails() throws Exception {
        int databaseSizeBeforeUpdate = commandeDetailsRepository.findAll().collectList().block().size();
        commandeDetails.setId(count.incrementAndGet());

        // Create the CommandeDetails
        CommandeDetailsDTO commandeDetailsDTO = commandeDetailsMapper.toDto(commandeDetails);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, commandeDetailsDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(commandeDetailsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CommandeDetails in the database
        List<CommandeDetails> commandeDetailsList = commandeDetailsRepository.findAll().collectList().block();
        assertThat(commandeDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCommandeDetails() throws Exception {
        int databaseSizeBeforeUpdate = commandeDetailsRepository.findAll().collectList().block().size();
        commandeDetails.setId(count.incrementAndGet());

        // Create the CommandeDetails
        CommandeDetailsDTO commandeDetailsDTO = commandeDetailsMapper.toDto(commandeDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(commandeDetailsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CommandeDetails in the database
        List<CommandeDetails> commandeDetailsList = commandeDetailsRepository.findAll().collectList().block();
        assertThat(commandeDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCommandeDetails() throws Exception {
        int databaseSizeBeforeUpdate = commandeDetailsRepository.findAll().collectList().block().size();
        commandeDetails.setId(count.incrementAndGet());

        // Create the CommandeDetails
        CommandeDetailsDTO commandeDetailsDTO = commandeDetailsMapper.toDto(commandeDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(commandeDetailsDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the CommandeDetails in the database
        List<CommandeDetails> commandeDetailsList = commandeDetailsRepository.findAll().collectList().block();
        assertThat(commandeDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteCommandeDetails() {
        // Initialize the database
        commandeDetailsRepository.save(commandeDetails).block();

        int databaseSizeBeforeDelete = commandeDetailsRepository.findAll().collectList().block().size();

        // Delete the commandeDetails
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, commandeDetails.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<CommandeDetails> commandeDetailsList = commandeDetailsRepository.findAll().collectList().block();
        assertThat(commandeDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
