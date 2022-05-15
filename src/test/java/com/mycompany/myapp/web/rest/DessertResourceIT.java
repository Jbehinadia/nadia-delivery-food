package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Dessert;
import com.mycompany.myapp.repository.DessertRepository;
import com.mycompany.myapp.repository.EntityManager;
import com.mycompany.myapp.service.dto.DessertDTO;
import com.mycompany.myapp.service.mapper.DessertMapper;
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
 * Integration tests for the {@link DessertResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class DessertResourceIT {

    private static final String DEFAULT_NOM_DESSERT = "AAAAAAAAAA";
    private static final String UPDATED_NOM_DESSERT = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE_PATH = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_PATH = "BBBBBBBBBB";

    private static final Double DEFAULT_PRIX = 1D;
    private static final Double UPDATED_PRIX = 2D;

    private static final Double DEFAULT_REMISE_PERC = 1D;
    private static final Double UPDATED_REMISE_PERC = 2D;

    private static final Double DEFAULT_REMICE_VAL = 1D;
    private static final Double UPDATED_REMICE_VAL = 2D;

    private static final String ENTITY_API_URL = "/api/desserts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DessertRepository dessertRepository;

    @Autowired
    private DessertMapper dessertMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Dessert dessert;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dessert createEntity(EntityManager em) {
        Dessert dessert = new Dessert()
            .nomDessert(DEFAULT_NOM_DESSERT)
            .imagePath(DEFAULT_IMAGE_PATH)
            .prix(DEFAULT_PRIX)
            .remisePerc(DEFAULT_REMISE_PERC)
            .remiceVal(DEFAULT_REMICE_VAL);
        return dessert;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dessert createUpdatedEntity(EntityManager em) {
        Dessert dessert = new Dessert()
            .nomDessert(UPDATED_NOM_DESSERT)
            .imagePath(UPDATED_IMAGE_PATH)
            .prix(UPDATED_PRIX)
            .remisePerc(UPDATED_REMISE_PERC)
            .remiceVal(UPDATED_REMICE_VAL);
        return dessert;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Dessert.class).block();
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
        dessert = createEntity(em);
    }

    @Test
    void createDessert() throws Exception {
        int databaseSizeBeforeCreate = dessertRepository.findAll().collectList().block().size();
        // Create the Dessert
        DessertDTO dessertDTO = dessertMapper.toDto(dessert);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(dessertDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Dessert in the database
        List<Dessert> dessertList = dessertRepository.findAll().collectList().block();
        assertThat(dessertList).hasSize(databaseSizeBeforeCreate + 1);
        Dessert testDessert = dessertList.get(dessertList.size() - 1);
        assertThat(testDessert.getNomDessert()).isEqualTo(DEFAULT_NOM_DESSERT);
        assertThat(testDessert.getImagePath()).isEqualTo(DEFAULT_IMAGE_PATH);
        assertThat(testDessert.getPrix()).isEqualTo(DEFAULT_PRIX);
        assertThat(testDessert.getRemisePerc()).isEqualTo(DEFAULT_REMISE_PERC);
        assertThat(testDessert.getRemiceVal()).isEqualTo(DEFAULT_REMICE_VAL);
    }

    @Test
    void createDessertWithExistingId() throws Exception {
        // Create the Dessert with an existing ID
        dessert.setId(1L);
        DessertDTO dessertDTO = dessertMapper.toDto(dessert);

        int databaseSizeBeforeCreate = dessertRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(dessertDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Dessert in the database
        List<Dessert> dessertList = dessertRepository.findAll().collectList().block();
        assertThat(dessertList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllDesserts() {
        // Initialize the database
        dessertRepository.save(dessert).block();

        // Get all the dessertList
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
            .value(hasItem(dessert.getId().intValue()))
            .jsonPath("$.[*].nomDessert")
            .value(hasItem(DEFAULT_NOM_DESSERT))
            .jsonPath("$.[*].imagePath")
            .value(hasItem(DEFAULT_IMAGE_PATH))
            .jsonPath("$.[*].prix")
            .value(hasItem(DEFAULT_PRIX.doubleValue()))
            .jsonPath("$.[*].remisePerc")
            .value(hasItem(DEFAULT_REMISE_PERC.doubleValue()))
            .jsonPath("$.[*].remiceVal")
            .value(hasItem(DEFAULT_REMICE_VAL.doubleValue()));
    }

    @Test
    void getDessert() {
        // Initialize the database
        dessertRepository.save(dessert).block();

        // Get the dessert
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, dessert.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(dessert.getId().intValue()))
            .jsonPath("$.nomDessert")
            .value(is(DEFAULT_NOM_DESSERT))
            .jsonPath("$.imagePath")
            .value(is(DEFAULT_IMAGE_PATH))
            .jsonPath("$.prix")
            .value(is(DEFAULT_PRIX.doubleValue()))
            .jsonPath("$.remisePerc")
            .value(is(DEFAULT_REMISE_PERC.doubleValue()))
            .jsonPath("$.remiceVal")
            .value(is(DEFAULT_REMICE_VAL.doubleValue()));
    }

    @Test
    void getNonExistingDessert() {
        // Get the dessert
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewDessert() throws Exception {
        // Initialize the database
        dessertRepository.save(dessert).block();

        int databaseSizeBeforeUpdate = dessertRepository.findAll().collectList().block().size();

        // Update the dessert
        Dessert updatedDessert = dessertRepository.findById(dessert.getId()).block();
        updatedDessert
            .nomDessert(UPDATED_NOM_DESSERT)
            .imagePath(UPDATED_IMAGE_PATH)
            .prix(UPDATED_PRIX)
            .remisePerc(UPDATED_REMISE_PERC)
            .remiceVal(UPDATED_REMICE_VAL);
        DessertDTO dessertDTO = dessertMapper.toDto(updatedDessert);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, dessertDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(dessertDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Dessert in the database
        List<Dessert> dessertList = dessertRepository.findAll().collectList().block();
        assertThat(dessertList).hasSize(databaseSizeBeforeUpdate);
        Dessert testDessert = dessertList.get(dessertList.size() - 1);
        assertThat(testDessert.getNomDessert()).isEqualTo(UPDATED_NOM_DESSERT);
        assertThat(testDessert.getImagePath()).isEqualTo(UPDATED_IMAGE_PATH);
        assertThat(testDessert.getPrix()).isEqualTo(UPDATED_PRIX);
        assertThat(testDessert.getRemisePerc()).isEqualTo(UPDATED_REMISE_PERC);
        assertThat(testDessert.getRemiceVal()).isEqualTo(UPDATED_REMICE_VAL);
    }

    @Test
    void putNonExistingDessert() throws Exception {
        int databaseSizeBeforeUpdate = dessertRepository.findAll().collectList().block().size();
        dessert.setId(count.incrementAndGet());

        // Create the Dessert
        DessertDTO dessertDTO = dessertMapper.toDto(dessert);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, dessertDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(dessertDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Dessert in the database
        List<Dessert> dessertList = dessertRepository.findAll().collectList().block();
        assertThat(dessertList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchDessert() throws Exception {
        int databaseSizeBeforeUpdate = dessertRepository.findAll().collectList().block().size();
        dessert.setId(count.incrementAndGet());

        // Create the Dessert
        DessertDTO dessertDTO = dessertMapper.toDto(dessert);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(dessertDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Dessert in the database
        List<Dessert> dessertList = dessertRepository.findAll().collectList().block();
        assertThat(dessertList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamDessert() throws Exception {
        int databaseSizeBeforeUpdate = dessertRepository.findAll().collectList().block().size();
        dessert.setId(count.incrementAndGet());

        // Create the Dessert
        DessertDTO dessertDTO = dessertMapper.toDto(dessert);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(dessertDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Dessert in the database
        List<Dessert> dessertList = dessertRepository.findAll().collectList().block();
        assertThat(dessertList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateDessertWithPatch() throws Exception {
        // Initialize the database
        dessertRepository.save(dessert).block();

        int databaseSizeBeforeUpdate = dessertRepository.findAll().collectList().block().size();

        // Update the dessert using partial update
        Dessert partialUpdatedDessert = new Dessert();
        partialUpdatedDessert.setId(dessert.getId());

        partialUpdatedDessert
            .nomDessert(UPDATED_NOM_DESSERT)
            .imagePath(UPDATED_IMAGE_PATH)
            .prix(UPDATED_PRIX)
            .remisePerc(UPDATED_REMISE_PERC);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedDessert.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedDessert))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Dessert in the database
        List<Dessert> dessertList = dessertRepository.findAll().collectList().block();
        assertThat(dessertList).hasSize(databaseSizeBeforeUpdate);
        Dessert testDessert = dessertList.get(dessertList.size() - 1);
        assertThat(testDessert.getNomDessert()).isEqualTo(UPDATED_NOM_DESSERT);
        assertThat(testDessert.getImagePath()).isEqualTo(UPDATED_IMAGE_PATH);
        assertThat(testDessert.getPrix()).isEqualTo(UPDATED_PRIX);
        assertThat(testDessert.getRemisePerc()).isEqualTo(UPDATED_REMISE_PERC);
        assertThat(testDessert.getRemiceVal()).isEqualTo(DEFAULT_REMICE_VAL);
    }

    @Test
    void fullUpdateDessertWithPatch() throws Exception {
        // Initialize the database
        dessertRepository.save(dessert).block();

        int databaseSizeBeforeUpdate = dessertRepository.findAll().collectList().block().size();

        // Update the dessert using partial update
        Dessert partialUpdatedDessert = new Dessert();
        partialUpdatedDessert.setId(dessert.getId());

        partialUpdatedDessert
            .nomDessert(UPDATED_NOM_DESSERT)
            .imagePath(UPDATED_IMAGE_PATH)
            .prix(UPDATED_PRIX)
            .remisePerc(UPDATED_REMISE_PERC)
            .remiceVal(UPDATED_REMICE_VAL);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedDessert.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedDessert))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Dessert in the database
        List<Dessert> dessertList = dessertRepository.findAll().collectList().block();
        assertThat(dessertList).hasSize(databaseSizeBeforeUpdate);
        Dessert testDessert = dessertList.get(dessertList.size() - 1);
        assertThat(testDessert.getNomDessert()).isEqualTo(UPDATED_NOM_DESSERT);
        assertThat(testDessert.getImagePath()).isEqualTo(UPDATED_IMAGE_PATH);
        assertThat(testDessert.getPrix()).isEqualTo(UPDATED_PRIX);
        assertThat(testDessert.getRemisePerc()).isEqualTo(UPDATED_REMISE_PERC);
        assertThat(testDessert.getRemiceVal()).isEqualTo(UPDATED_REMICE_VAL);
    }

    @Test
    void patchNonExistingDessert() throws Exception {
        int databaseSizeBeforeUpdate = dessertRepository.findAll().collectList().block().size();
        dessert.setId(count.incrementAndGet());

        // Create the Dessert
        DessertDTO dessertDTO = dessertMapper.toDto(dessert);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, dessertDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(dessertDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Dessert in the database
        List<Dessert> dessertList = dessertRepository.findAll().collectList().block();
        assertThat(dessertList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchDessert() throws Exception {
        int databaseSizeBeforeUpdate = dessertRepository.findAll().collectList().block().size();
        dessert.setId(count.incrementAndGet());

        // Create the Dessert
        DessertDTO dessertDTO = dessertMapper.toDto(dessert);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(dessertDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Dessert in the database
        List<Dessert> dessertList = dessertRepository.findAll().collectList().block();
        assertThat(dessertList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamDessert() throws Exception {
        int databaseSizeBeforeUpdate = dessertRepository.findAll().collectList().block().size();
        dessert.setId(count.incrementAndGet());

        // Create the Dessert
        DessertDTO dessertDTO = dessertMapper.toDto(dessert);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(dessertDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Dessert in the database
        List<Dessert> dessertList = dessertRepository.findAll().collectList().block();
        assertThat(dessertList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteDessert() {
        // Initialize the database
        dessertRepository.save(dessert).block();

        int databaseSizeBeforeDelete = dessertRepository.findAll().collectList().block().size();

        // Delete the dessert
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, dessert.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Dessert> dessertList = dessertRepository.findAll().collectList().block();
        assertThat(dessertList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
