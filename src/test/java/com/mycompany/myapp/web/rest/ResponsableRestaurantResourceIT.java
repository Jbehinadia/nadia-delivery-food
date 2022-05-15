package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.ResponsableRestaurant;
import com.mycompany.myapp.repository.EntityManager;
import com.mycompany.myapp.repository.ResponsableRestaurantRepository;
import com.mycompany.myapp.service.dto.ResponsableRestaurantDTO;
import com.mycompany.myapp.service.mapper.ResponsableRestaurantMapper;
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
 * Integration tests for the {@link ResponsableRestaurantResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ResponsableRestaurantResourceIT {

    private static final String DEFAULT_ID_RESPONSABLE = "AAAAAAAAAA";
    private static final String UPDATED_ID_RESPONSABLE = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_RESPONSABLE = "AAAAAAAAAA";
    private static final String UPDATED_NOM_RESPONSABLE = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM_RESPONSABLE = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM_RESPONSABLE = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESSE_RESPONSABLE = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE_RESPONSABLE = "BBBBBBBBBB";

    private static final String DEFAULT_NUM_RESPONSABLE = "AAAAAAAAAA";
    private static final String UPDATED_NUM_RESPONSABLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/responsable-restaurants";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ResponsableRestaurantRepository responsableRestaurantRepository;

    @Autowired
    private ResponsableRestaurantMapper responsableRestaurantMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private ResponsableRestaurant responsableRestaurant;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ResponsableRestaurant createEntity(EntityManager em) {
        ResponsableRestaurant responsableRestaurant = new ResponsableRestaurant()
            .idResponsable(DEFAULT_ID_RESPONSABLE)
            .nomResponsable(DEFAULT_NOM_RESPONSABLE)
            .prenomResponsable(DEFAULT_PRENOM_RESPONSABLE)
            .adresseResponsable(DEFAULT_ADRESSE_RESPONSABLE)
            .numResponsable(DEFAULT_NUM_RESPONSABLE);
        return responsableRestaurant;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ResponsableRestaurant createUpdatedEntity(EntityManager em) {
        ResponsableRestaurant responsableRestaurant = new ResponsableRestaurant()
            .idResponsable(UPDATED_ID_RESPONSABLE)
            .nomResponsable(UPDATED_NOM_RESPONSABLE)
            .prenomResponsable(UPDATED_PRENOM_RESPONSABLE)
            .adresseResponsable(UPDATED_ADRESSE_RESPONSABLE)
            .numResponsable(UPDATED_NUM_RESPONSABLE);
        return responsableRestaurant;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(ResponsableRestaurant.class).block();
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
        responsableRestaurant = createEntity(em);
    }

    @Test
    void createResponsableRestaurant() throws Exception {
        int databaseSizeBeforeCreate = responsableRestaurantRepository.findAll().collectList().block().size();
        // Create the ResponsableRestaurant
        ResponsableRestaurantDTO responsableRestaurantDTO = responsableRestaurantMapper.toDto(responsableRestaurant);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(responsableRestaurantDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the ResponsableRestaurant in the database
        List<ResponsableRestaurant> responsableRestaurantList = responsableRestaurantRepository.findAll().collectList().block();
        assertThat(responsableRestaurantList).hasSize(databaseSizeBeforeCreate + 1);
        ResponsableRestaurant testResponsableRestaurant = responsableRestaurantList.get(responsableRestaurantList.size() - 1);
        assertThat(testResponsableRestaurant.getIdResponsable()).isEqualTo(DEFAULT_ID_RESPONSABLE);
        assertThat(testResponsableRestaurant.getNomResponsable()).isEqualTo(DEFAULT_NOM_RESPONSABLE);
        assertThat(testResponsableRestaurant.getPrenomResponsable()).isEqualTo(DEFAULT_PRENOM_RESPONSABLE);
        assertThat(testResponsableRestaurant.getAdresseResponsable()).isEqualTo(DEFAULT_ADRESSE_RESPONSABLE);
        assertThat(testResponsableRestaurant.getNumResponsable()).isEqualTo(DEFAULT_NUM_RESPONSABLE);
    }

    @Test
    void createResponsableRestaurantWithExistingId() throws Exception {
        // Create the ResponsableRestaurant with an existing ID
        responsableRestaurant.setId(1L);
        ResponsableRestaurantDTO responsableRestaurantDTO = responsableRestaurantMapper.toDto(responsableRestaurant);

        int databaseSizeBeforeCreate = responsableRestaurantRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(responsableRestaurantDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ResponsableRestaurant in the database
        List<ResponsableRestaurant> responsableRestaurantList = responsableRestaurantRepository.findAll().collectList().block();
        assertThat(responsableRestaurantList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllResponsableRestaurants() {
        // Initialize the database
        responsableRestaurantRepository.save(responsableRestaurant).block();

        // Get all the responsableRestaurantList
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
            .value(hasItem(responsableRestaurant.getId().intValue()))
            .jsonPath("$.[*].idResponsable")
            .value(hasItem(DEFAULT_ID_RESPONSABLE))
            .jsonPath("$.[*].nomResponsable")
            .value(hasItem(DEFAULT_NOM_RESPONSABLE))
            .jsonPath("$.[*].prenomResponsable")
            .value(hasItem(DEFAULT_PRENOM_RESPONSABLE))
            .jsonPath("$.[*].adresseResponsable")
            .value(hasItem(DEFAULT_ADRESSE_RESPONSABLE))
            .jsonPath("$.[*].numResponsable")
            .value(hasItem(DEFAULT_NUM_RESPONSABLE));
    }

    @Test
    void getResponsableRestaurant() {
        // Initialize the database
        responsableRestaurantRepository.save(responsableRestaurant).block();

        // Get the responsableRestaurant
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, responsableRestaurant.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(responsableRestaurant.getId().intValue()))
            .jsonPath("$.idResponsable")
            .value(is(DEFAULT_ID_RESPONSABLE))
            .jsonPath("$.nomResponsable")
            .value(is(DEFAULT_NOM_RESPONSABLE))
            .jsonPath("$.prenomResponsable")
            .value(is(DEFAULT_PRENOM_RESPONSABLE))
            .jsonPath("$.adresseResponsable")
            .value(is(DEFAULT_ADRESSE_RESPONSABLE))
            .jsonPath("$.numResponsable")
            .value(is(DEFAULT_NUM_RESPONSABLE));
    }

    @Test
    void getNonExistingResponsableRestaurant() {
        // Get the responsableRestaurant
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewResponsableRestaurant() throws Exception {
        // Initialize the database
        responsableRestaurantRepository.save(responsableRestaurant).block();

        int databaseSizeBeforeUpdate = responsableRestaurantRepository.findAll().collectList().block().size();

        // Update the responsableRestaurant
        ResponsableRestaurant updatedResponsableRestaurant = responsableRestaurantRepository
            .findById(responsableRestaurant.getId())
            .block();
        updatedResponsableRestaurant
            .idResponsable(UPDATED_ID_RESPONSABLE)
            .nomResponsable(UPDATED_NOM_RESPONSABLE)
            .prenomResponsable(UPDATED_PRENOM_RESPONSABLE)
            .adresseResponsable(UPDATED_ADRESSE_RESPONSABLE)
            .numResponsable(UPDATED_NUM_RESPONSABLE);
        ResponsableRestaurantDTO responsableRestaurantDTO = responsableRestaurantMapper.toDto(updatedResponsableRestaurant);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, responsableRestaurantDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(responsableRestaurantDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ResponsableRestaurant in the database
        List<ResponsableRestaurant> responsableRestaurantList = responsableRestaurantRepository.findAll().collectList().block();
        assertThat(responsableRestaurantList).hasSize(databaseSizeBeforeUpdate);
        ResponsableRestaurant testResponsableRestaurant = responsableRestaurantList.get(responsableRestaurantList.size() - 1);
        assertThat(testResponsableRestaurant.getIdResponsable()).isEqualTo(UPDATED_ID_RESPONSABLE);
        assertThat(testResponsableRestaurant.getNomResponsable()).isEqualTo(UPDATED_NOM_RESPONSABLE);
        assertThat(testResponsableRestaurant.getPrenomResponsable()).isEqualTo(UPDATED_PRENOM_RESPONSABLE);
        assertThat(testResponsableRestaurant.getAdresseResponsable()).isEqualTo(UPDATED_ADRESSE_RESPONSABLE);
        assertThat(testResponsableRestaurant.getNumResponsable()).isEqualTo(UPDATED_NUM_RESPONSABLE);
    }

    @Test
    void putNonExistingResponsableRestaurant() throws Exception {
        int databaseSizeBeforeUpdate = responsableRestaurantRepository.findAll().collectList().block().size();
        responsableRestaurant.setId(count.incrementAndGet());

        // Create the ResponsableRestaurant
        ResponsableRestaurantDTO responsableRestaurantDTO = responsableRestaurantMapper.toDto(responsableRestaurant);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, responsableRestaurantDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(responsableRestaurantDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ResponsableRestaurant in the database
        List<ResponsableRestaurant> responsableRestaurantList = responsableRestaurantRepository.findAll().collectList().block();
        assertThat(responsableRestaurantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchResponsableRestaurant() throws Exception {
        int databaseSizeBeforeUpdate = responsableRestaurantRepository.findAll().collectList().block().size();
        responsableRestaurant.setId(count.incrementAndGet());

        // Create the ResponsableRestaurant
        ResponsableRestaurantDTO responsableRestaurantDTO = responsableRestaurantMapper.toDto(responsableRestaurant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(responsableRestaurantDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ResponsableRestaurant in the database
        List<ResponsableRestaurant> responsableRestaurantList = responsableRestaurantRepository.findAll().collectList().block();
        assertThat(responsableRestaurantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamResponsableRestaurant() throws Exception {
        int databaseSizeBeforeUpdate = responsableRestaurantRepository.findAll().collectList().block().size();
        responsableRestaurant.setId(count.incrementAndGet());

        // Create the ResponsableRestaurant
        ResponsableRestaurantDTO responsableRestaurantDTO = responsableRestaurantMapper.toDto(responsableRestaurant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(responsableRestaurantDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ResponsableRestaurant in the database
        List<ResponsableRestaurant> responsableRestaurantList = responsableRestaurantRepository.findAll().collectList().block();
        assertThat(responsableRestaurantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateResponsableRestaurantWithPatch() throws Exception {
        // Initialize the database
        responsableRestaurantRepository.save(responsableRestaurant).block();

        int databaseSizeBeforeUpdate = responsableRestaurantRepository.findAll().collectList().block().size();

        // Update the responsableRestaurant using partial update
        ResponsableRestaurant partialUpdatedResponsableRestaurant = new ResponsableRestaurant();
        partialUpdatedResponsableRestaurant.setId(responsableRestaurant.getId());

        partialUpdatedResponsableRestaurant
            .idResponsable(UPDATED_ID_RESPONSABLE)
            .prenomResponsable(UPDATED_PRENOM_RESPONSABLE)
            .adresseResponsable(UPDATED_ADRESSE_RESPONSABLE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedResponsableRestaurant.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedResponsableRestaurant))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ResponsableRestaurant in the database
        List<ResponsableRestaurant> responsableRestaurantList = responsableRestaurantRepository.findAll().collectList().block();
        assertThat(responsableRestaurantList).hasSize(databaseSizeBeforeUpdate);
        ResponsableRestaurant testResponsableRestaurant = responsableRestaurantList.get(responsableRestaurantList.size() - 1);
        assertThat(testResponsableRestaurant.getIdResponsable()).isEqualTo(UPDATED_ID_RESPONSABLE);
        assertThat(testResponsableRestaurant.getNomResponsable()).isEqualTo(DEFAULT_NOM_RESPONSABLE);
        assertThat(testResponsableRestaurant.getPrenomResponsable()).isEqualTo(UPDATED_PRENOM_RESPONSABLE);
        assertThat(testResponsableRestaurant.getAdresseResponsable()).isEqualTo(UPDATED_ADRESSE_RESPONSABLE);
        assertThat(testResponsableRestaurant.getNumResponsable()).isEqualTo(DEFAULT_NUM_RESPONSABLE);
    }

    @Test
    void fullUpdateResponsableRestaurantWithPatch() throws Exception {
        // Initialize the database
        responsableRestaurantRepository.save(responsableRestaurant).block();

        int databaseSizeBeforeUpdate = responsableRestaurantRepository.findAll().collectList().block().size();

        // Update the responsableRestaurant using partial update
        ResponsableRestaurant partialUpdatedResponsableRestaurant = new ResponsableRestaurant();
        partialUpdatedResponsableRestaurant.setId(responsableRestaurant.getId());

        partialUpdatedResponsableRestaurant
            .idResponsable(UPDATED_ID_RESPONSABLE)
            .nomResponsable(UPDATED_NOM_RESPONSABLE)
            .prenomResponsable(UPDATED_PRENOM_RESPONSABLE)
            .adresseResponsable(UPDATED_ADRESSE_RESPONSABLE)
            .numResponsable(UPDATED_NUM_RESPONSABLE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedResponsableRestaurant.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedResponsableRestaurant))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ResponsableRestaurant in the database
        List<ResponsableRestaurant> responsableRestaurantList = responsableRestaurantRepository.findAll().collectList().block();
        assertThat(responsableRestaurantList).hasSize(databaseSizeBeforeUpdate);
        ResponsableRestaurant testResponsableRestaurant = responsableRestaurantList.get(responsableRestaurantList.size() - 1);
        assertThat(testResponsableRestaurant.getIdResponsable()).isEqualTo(UPDATED_ID_RESPONSABLE);
        assertThat(testResponsableRestaurant.getNomResponsable()).isEqualTo(UPDATED_NOM_RESPONSABLE);
        assertThat(testResponsableRestaurant.getPrenomResponsable()).isEqualTo(UPDATED_PRENOM_RESPONSABLE);
        assertThat(testResponsableRestaurant.getAdresseResponsable()).isEqualTo(UPDATED_ADRESSE_RESPONSABLE);
        assertThat(testResponsableRestaurant.getNumResponsable()).isEqualTo(UPDATED_NUM_RESPONSABLE);
    }

    @Test
    void patchNonExistingResponsableRestaurant() throws Exception {
        int databaseSizeBeforeUpdate = responsableRestaurantRepository.findAll().collectList().block().size();
        responsableRestaurant.setId(count.incrementAndGet());

        // Create the ResponsableRestaurant
        ResponsableRestaurantDTO responsableRestaurantDTO = responsableRestaurantMapper.toDto(responsableRestaurant);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, responsableRestaurantDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(responsableRestaurantDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ResponsableRestaurant in the database
        List<ResponsableRestaurant> responsableRestaurantList = responsableRestaurantRepository.findAll().collectList().block();
        assertThat(responsableRestaurantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchResponsableRestaurant() throws Exception {
        int databaseSizeBeforeUpdate = responsableRestaurantRepository.findAll().collectList().block().size();
        responsableRestaurant.setId(count.incrementAndGet());

        // Create the ResponsableRestaurant
        ResponsableRestaurantDTO responsableRestaurantDTO = responsableRestaurantMapper.toDto(responsableRestaurant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(responsableRestaurantDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ResponsableRestaurant in the database
        List<ResponsableRestaurant> responsableRestaurantList = responsableRestaurantRepository.findAll().collectList().block();
        assertThat(responsableRestaurantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamResponsableRestaurant() throws Exception {
        int databaseSizeBeforeUpdate = responsableRestaurantRepository.findAll().collectList().block().size();
        responsableRestaurant.setId(count.incrementAndGet());

        // Create the ResponsableRestaurant
        ResponsableRestaurantDTO responsableRestaurantDTO = responsableRestaurantMapper.toDto(responsableRestaurant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(responsableRestaurantDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ResponsableRestaurant in the database
        List<ResponsableRestaurant> responsableRestaurantList = responsableRestaurantRepository.findAll().collectList().block();
        assertThat(responsableRestaurantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteResponsableRestaurant() {
        // Initialize the database
        responsableRestaurantRepository.save(responsableRestaurant).block();

        int databaseSizeBeforeDelete = responsableRestaurantRepository.findAll().collectList().block().size();

        // Delete the responsableRestaurant
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, responsableRestaurant.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<ResponsableRestaurant> responsableRestaurantList = responsableRestaurantRepository.findAll().collectList().block();
        assertThat(responsableRestaurantList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
