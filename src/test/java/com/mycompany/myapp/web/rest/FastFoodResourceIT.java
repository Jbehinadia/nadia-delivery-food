package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.FastFood;
import com.mycompany.myapp.repository.EntityManager;
import com.mycompany.myapp.repository.FastFoodRepository;
import com.mycompany.myapp.service.dto.FastFoodDTO;
import com.mycompany.myapp.service.mapper.FastFoodMapper;
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
 * Integration tests for the {@link FastFoodResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class FastFoodResourceIT {

    private static final String DEFAULT_NOM_FOOD = "AAAAAAAAAA";
    private static final String UPDATED_NOM_FOOD = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE_PATH = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_PATH = "BBBBBBBBBB";

    private static final Double DEFAULT_PRIX = 1D;
    private static final Double UPDATED_PRIX = 2D;

    private static final Double DEFAULT_REMISE_PERC = 1D;
    private static final Double UPDATED_REMISE_PERC = 2D;

    private static final Double DEFAULT_REMICE_VAL = 1D;
    private static final Double UPDATED_REMICE_VAL = 2D;

    private static final String ENTITY_API_URL = "/api/fast-foods";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FastFoodRepository fastFoodRepository;

    @Autowired
    private FastFoodMapper fastFoodMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private FastFood fastFood;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FastFood createEntity(EntityManager em) {
        FastFood fastFood = new FastFood()
            .nomFood(DEFAULT_NOM_FOOD)
            .imagePath(DEFAULT_IMAGE_PATH)
            .prix(DEFAULT_PRIX)
            .remisePerc(DEFAULT_REMISE_PERC)
            .remiceVal(DEFAULT_REMICE_VAL);
        return fastFood;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FastFood createUpdatedEntity(EntityManager em) {
        FastFood fastFood = new FastFood()
            .nomFood(UPDATED_NOM_FOOD)
            .imagePath(UPDATED_IMAGE_PATH)
            .prix(UPDATED_PRIX)
            .remisePerc(UPDATED_REMISE_PERC)
            .remiceVal(UPDATED_REMICE_VAL);
        return fastFood;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(FastFood.class).block();
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
        fastFood = createEntity(em);
    }

    @Test
    void createFastFood() throws Exception {
        int databaseSizeBeforeCreate = fastFoodRepository.findAll().collectList().block().size();
        // Create the FastFood
        FastFoodDTO fastFoodDTO = fastFoodMapper.toDto(fastFood);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(fastFoodDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the FastFood in the database
        List<FastFood> fastFoodList = fastFoodRepository.findAll().collectList().block();
        assertThat(fastFoodList).hasSize(databaseSizeBeforeCreate + 1);
        FastFood testFastFood = fastFoodList.get(fastFoodList.size() - 1);
        assertThat(testFastFood.getNomFood()).isEqualTo(DEFAULT_NOM_FOOD);
        assertThat(testFastFood.getImagePath()).isEqualTo(DEFAULT_IMAGE_PATH);
        assertThat(testFastFood.getPrix()).isEqualTo(DEFAULT_PRIX);
        assertThat(testFastFood.getRemisePerc()).isEqualTo(DEFAULT_REMISE_PERC);
        assertThat(testFastFood.getRemiceVal()).isEqualTo(DEFAULT_REMICE_VAL);
    }

    @Test
    void createFastFoodWithExistingId() throws Exception {
        // Create the FastFood with an existing ID
        fastFood.setId(1L);
        FastFoodDTO fastFoodDTO = fastFoodMapper.toDto(fastFood);

        int databaseSizeBeforeCreate = fastFoodRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(fastFoodDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FastFood in the database
        List<FastFood> fastFoodList = fastFoodRepository.findAll().collectList().block();
        assertThat(fastFoodList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllFastFoods() {
        // Initialize the database
        fastFoodRepository.save(fastFood).block();

        // Get all the fastFoodList
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
            .value(hasItem(fastFood.getId().intValue()))
            .jsonPath("$.[*].nomFood")
            .value(hasItem(DEFAULT_NOM_FOOD))
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
    void getFastFood() {
        // Initialize the database
        fastFoodRepository.save(fastFood).block();

        // Get the fastFood
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, fastFood.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(fastFood.getId().intValue()))
            .jsonPath("$.nomFood")
            .value(is(DEFAULT_NOM_FOOD))
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
    void getNonExistingFastFood() {
        // Get the fastFood
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewFastFood() throws Exception {
        // Initialize the database
        fastFoodRepository.save(fastFood).block();

        int databaseSizeBeforeUpdate = fastFoodRepository.findAll().collectList().block().size();

        // Update the fastFood
        FastFood updatedFastFood = fastFoodRepository.findById(fastFood.getId()).block();
        updatedFastFood
            .nomFood(UPDATED_NOM_FOOD)
            .imagePath(UPDATED_IMAGE_PATH)
            .prix(UPDATED_PRIX)
            .remisePerc(UPDATED_REMISE_PERC)
            .remiceVal(UPDATED_REMICE_VAL);
        FastFoodDTO fastFoodDTO = fastFoodMapper.toDto(updatedFastFood);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, fastFoodDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(fastFoodDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the FastFood in the database
        List<FastFood> fastFoodList = fastFoodRepository.findAll().collectList().block();
        assertThat(fastFoodList).hasSize(databaseSizeBeforeUpdate);
        FastFood testFastFood = fastFoodList.get(fastFoodList.size() - 1);
        assertThat(testFastFood.getNomFood()).isEqualTo(UPDATED_NOM_FOOD);
        assertThat(testFastFood.getImagePath()).isEqualTo(UPDATED_IMAGE_PATH);
        assertThat(testFastFood.getPrix()).isEqualTo(UPDATED_PRIX);
        assertThat(testFastFood.getRemisePerc()).isEqualTo(UPDATED_REMISE_PERC);
        assertThat(testFastFood.getRemiceVal()).isEqualTo(UPDATED_REMICE_VAL);
    }

    @Test
    void putNonExistingFastFood() throws Exception {
        int databaseSizeBeforeUpdate = fastFoodRepository.findAll().collectList().block().size();
        fastFood.setId(count.incrementAndGet());

        // Create the FastFood
        FastFoodDTO fastFoodDTO = fastFoodMapper.toDto(fastFood);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, fastFoodDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(fastFoodDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FastFood in the database
        List<FastFood> fastFoodList = fastFoodRepository.findAll().collectList().block();
        assertThat(fastFoodList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchFastFood() throws Exception {
        int databaseSizeBeforeUpdate = fastFoodRepository.findAll().collectList().block().size();
        fastFood.setId(count.incrementAndGet());

        // Create the FastFood
        FastFoodDTO fastFoodDTO = fastFoodMapper.toDto(fastFood);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(fastFoodDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FastFood in the database
        List<FastFood> fastFoodList = fastFoodRepository.findAll().collectList().block();
        assertThat(fastFoodList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamFastFood() throws Exception {
        int databaseSizeBeforeUpdate = fastFoodRepository.findAll().collectList().block().size();
        fastFood.setId(count.incrementAndGet());

        // Create the FastFood
        FastFoodDTO fastFoodDTO = fastFoodMapper.toDto(fastFood);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(fastFoodDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the FastFood in the database
        List<FastFood> fastFoodList = fastFoodRepository.findAll().collectList().block();
        assertThat(fastFoodList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateFastFoodWithPatch() throws Exception {
        // Initialize the database
        fastFoodRepository.save(fastFood).block();

        int databaseSizeBeforeUpdate = fastFoodRepository.findAll().collectList().block().size();

        // Update the fastFood using partial update
        FastFood partialUpdatedFastFood = new FastFood();
        partialUpdatedFastFood.setId(fastFood.getId());

        partialUpdatedFastFood.imagePath(UPDATED_IMAGE_PATH).remiceVal(UPDATED_REMICE_VAL);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedFastFood.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedFastFood))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the FastFood in the database
        List<FastFood> fastFoodList = fastFoodRepository.findAll().collectList().block();
        assertThat(fastFoodList).hasSize(databaseSizeBeforeUpdate);
        FastFood testFastFood = fastFoodList.get(fastFoodList.size() - 1);
        assertThat(testFastFood.getNomFood()).isEqualTo(DEFAULT_NOM_FOOD);
        assertThat(testFastFood.getImagePath()).isEqualTo(UPDATED_IMAGE_PATH);
        assertThat(testFastFood.getPrix()).isEqualTo(DEFAULT_PRIX);
        assertThat(testFastFood.getRemisePerc()).isEqualTo(DEFAULT_REMISE_PERC);
        assertThat(testFastFood.getRemiceVal()).isEqualTo(UPDATED_REMICE_VAL);
    }

    @Test
    void fullUpdateFastFoodWithPatch() throws Exception {
        // Initialize the database
        fastFoodRepository.save(fastFood).block();

        int databaseSizeBeforeUpdate = fastFoodRepository.findAll().collectList().block().size();

        // Update the fastFood using partial update
        FastFood partialUpdatedFastFood = new FastFood();
        partialUpdatedFastFood.setId(fastFood.getId());

        partialUpdatedFastFood
            .nomFood(UPDATED_NOM_FOOD)
            .imagePath(UPDATED_IMAGE_PATH)
            .prix(UPDATED_PRIX)
            .remisePerc(UPDATED_REMISE_PERC)
            .remiceVal(UPDATED_REMICE_VAL);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedFastFood.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedFastFood))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the FastFood in the database
        List<FastFood> fastFoodList = fastFoodRepository.findAll().collectList().block();
        assertThat(fastFoodList).hasSize(databaseSizeBeforeUpdate);
        FastFood testFastFood = fastFoodList.get(fastFoodList.size() - 1);
        assertThat(testFastFood.getNomFood()).isEqualTo(UPDATED_NOM_FOOD);
        assertThat(testFastFood.getImagePath()).isEqualTo(UPDATED_IMAGE_PATH);
        assertThat(testFastFood.getPrix()).isEqualTo(UPDATED_PRIX);
        assertThat(testFastFood.getRemisePerc()).isEqualTo(UPDATED_REMISE_PERC);
        assertThat(testFastFood.getRemiceVal()).isEqualTo(UPDATED_REMICE_VAL);
    }

    @Test
    void patchNonExistingFastFood() throws Exception {
        int databaseSizeBeforeUpdate = fastFoodRepository.findAll().collectList().block().size();
        fastFood.setId(count.incrementAndGet());

        // Create the FastFood
        FastFoodDTO fastFoodDTO = fastFoodMapper.toDto(fastFood);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, fastFoodDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(fastFoodDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FastFood in the database
        List<FastFood> fastFoodList = fastFoodRepository.findAll().collectList().block();
        assertThat(fastFoodList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchFastFood() throws Exception {
        int databaseSizeBeforeUpdate = fastFoodRepository.findAll().collectList().block().size();
        fastFood.setId(count.incrementAndGet());

        // Create the FastFood
        FastFoodDTO fastFoodDTO = fastFoodMapper.toDto(fastFood);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(fastFoodDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FastFood in the database
        List<FastFood> fastFoodList = fastFoodRepository.findAll().collectList().block();
        assertThat(fastFoodList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamFastFood() throws Exception {
        int databaseSizeBeforeUpdate = fastFoodRepository.findAll().collectList().block().size();
        fastFood.setId(count.incrementAndGet());

        // Create the FastFood
        FastFoodDTO fastFoodDTO = fastFoodMapper.toDto(fastFood);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(fastFoodDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the FastFood in the database
        List<FastFood> fastFoodList = fastFoodRepository.findAll().collectList().block();
        assertThat(fastFoodList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteFastFood() {
        // Initialize the database
        fastFoodRepository.save(fastFood).block();

        int databaseSizeBeforeDelete = fastFoodRepository.findAll().collectList().block().size();

        // Delete the fastFood
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, fastFood.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<FastFood> fastFoodList = fastFoodRepository.findAll().collectList().block();
        assertThat(fastFoodList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
