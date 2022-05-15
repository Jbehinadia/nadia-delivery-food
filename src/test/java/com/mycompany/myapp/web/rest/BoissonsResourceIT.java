package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Boissons;
import com.mycompany.myapp.repository.BoissonsRepository;
import com.mycompany.myapp.repository.EntityManager;
import com.mycompany.myapp.service.dto.BoissonsDTO;
import com.mycompany.myapp.service.mapper.BoissonsMapper;
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
 * Integration tests for the {@link BoissonsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class BoissonsResourceIT {

    private static final String DEFAULT_NOM_BOISSONS = "AAAAAAAAAA";
    private static final String UPDATED_NOM_BOISSONS = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE_PATH = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_PATH = "BBBBBBBBBB";

    private static final Double DEFAULT_PRIX = 1D;
    private static final Double UPDATED_PRIX = 2D;

    private static final Double DEFAULT_REMISE_PERC = 1D;
    private static final Double UPDATED_REMISE_PERC = 2D;

    private static final Double DEFAULT_REMICE_VAL = 1D;
    private static final Double UPDATED_REMICE_VAL = 2D;

    private static final String ENTITY_API_URL = "/api/boissons";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BoissonsRepository boissonsRepository;

    @Autowired
    private BoissonsMapper boissonsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Boissons boissons;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Boissons createEntity(EntityManager em) {
        Boissons boissons = new Boissons()
            .nomBoissons(DEFAULT_NOM_BOISSONS)
            .imagePath(DEFAULT_IMAGE_PATH)
            .prix(DEFAULT_PRIX)
            .remisePerc(DEFAULT_REMISE_PERC)
            .remiceVal(DEFAULT_REMICE_VAL);
        return boissons;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Boissons createUpdatedEntity(EntityManager em) {
        Boissons boissons = new Boissons()
            .nomBoissons(UPDATED_NOM_BOISSONS)
            .imagePath(UPDATED_IMAGE_PATH)
            .prix(UPDATED_PRIX)
            .remisePerc(UPDATED_REMISE_PERC)
            .remiceVal(UPDATED_REMICE_VAL);
        return boissons;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Boissons.class).block();
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
        boissons = createEntity(em);
    }

    @Test
    void createBoissons() throws Exception {
        int databaseSizeBeforeCreate = boissonsRepository.findAll().collectList().block().size();
        // Create the Boissons
        BoissonsDTO boissonsDTO = boissonsMapper.toDto(boissons);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(boissonsDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Boissons in the database
        List<Boissons> boissonsList = boissonsRepository.findAll().collectList().block();
        assertThat(boissonsList).hasSize(databaseSizeBeforeCreate + 1);
        Boissons testBoissons = boissonsList.get(boissonsList.size() - 1);
        assertThat(testBoissons.getNomBoissons()).isEqualTo(DEFAULT_NOM_BOISSONS);
        assertThat(testBoissons.getImagePath()).isEqualTo(DEFAULT_IMAGE_PATH);
        assertThat(testBoissons.getPrix()).isEqualTo(DEFAULT_PRIX);
        assertThat(testBoissons.getRemisePerc()).isEqualTo(DEFAULT_REMISE_PERC);
        assertThat(testBoissons.getRemiceVal()).isEqualTo(DEFAULT_REMICE_VAL);
    }

    @Test
    void createBoissonsWithExistingId() throws Exception {
        // Create the Boissons with an existing ID
        boissons.setId(1L);
        BoissonsDTO boissonsDTO = boissonsMapper.toDto(boissons);

        int databaseSizeBeforeCreate = boissonsRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(boissonsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Boissons in the database
        List<Boissons> boissonsList = boissonsRepository.findAll().collectList().block();
        assertThat(boissonsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllBoissons() {
        // Initialize the database
        boissonsRepository.save(boissons).block();

        // Get all the boissonsList
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
            .value(hasItem(boissons.getId().intValue()))
            .jsonPath("$.[*].nomBoissons")
            .value(hasItem(DEFAULT_NOM_BOISSONS))
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
    void getBoissons() {
        // Initialize the database
        boissonsRepository.save(boissons).block();

        // Get the boissons
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, boissons.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(boissons.getId().intValue()))
            .jsonPath("$.nomBoissons")
            .value(is(DEFAULT_NOM_BOISSONS))
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
    void getNonExistingBoissons() {
        // Get the boissons
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewBoissons() throws Exception {
        // Initialize the database
        boissonsRepository.save(boissons).block();

        int databaseSizeBeforeUpdate = boissonsRepository.findAll().collectList().block().size();

        // Update the boissons
        Boissons updatedBoissons = boissonsRepository.findById(boissons.getId()).block();
        updatedBoissons
            .nomBoissons(UPDATED_NOM_BOISSONS)
            .imagePath(UPDATED_IMAGE_PATH)
            .prix(UPDATED_PRIX)
            .remisePerc(UPDATED_REMISE_PERC)
            .remiceVal(UPDATED_REMICE_VAL);
        BoissonsDTO boissonsDTO = boissonsMapper.toDto(updatedBoissons);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, boissonsDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(boissonsDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Boissons in the database
        List<Boissons> boissonsList = boissonsRepository.findAll().collectList().block();
        assertThat(boissonsList).hasSize(databaseSizeBeforeUpdate);
        Boissons testBoissons = boissonsList.get(boissonsList.size() - 1);
        assertThat(testBoissons.getNomBoissons()).isEqualTo(UPDATED_NOM_BOISSONS);
        assertThat(testBoissons.getImagePath()).isEqualTo(UPDATED_IMAGE_PATH);
        assertThat(testBoissons.getPrix()).isEqualTo(UPDATED_PRIX);
        assertThat(testBoissons.getRemisePerc()).isEqualTo(UPDATED_REMISE_PERC);
        assertThat(testBoissons.getRemiceVal()).isEqualTo(UPDATED_REMICE_VAL);
    }

    @Test
    void putNonExistingBoissons() throws Exception {
        int databaseSizeBeforeUpdate = boissonsRepository.findAll().collectList().block().size();
        boissons.setId(count.incrementAndGet());

        // Create the Boissons
        BoissonsDTO boissonsDTO = boissonsMapper.toDto(boissons);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, boissonsDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(boissonsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Boissons in the database
        List<Boissons> boissonsList = boissonsRepository.findAll().collectList().block();
        assertThat(boissonsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchBoissons() throws Exception {
        int databaseSizeBeforeUpdate = boissonsRepository.findAll().collectList().block().size();
        boissons.setId(count.incrementAndGet());

        // Create the Boissons
        BoissonsDTO boissonsDTO = boissonsMapper.toDto(boissons);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(boissonsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Boissons in the database
        List<Boissons> boissonsList = boissonsRepository.findAll().collectList().block();
        assertThat(boissonsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamBoissons() throws Exception {
        int databaseSizeBeforeUpdate = boissonsRepository.findAll().collectList().block().size();
        boissons.setId(count.incrementAndGet());

        // Create the Boissons
        BoissonsDTO boissonsDTO = boissonsMapper.toDto(boissons);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(boissonsDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Boissons in the database
        List<Boissons> boissonsList = boissonsRepository.findAll().collectList().block();
        assertThat(boissonsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateBoissonsWithPatch() throws Exception {
        // Initialize the database
        boissonsRepository.save(boissons).block();

        int databaseSizeBeforeUpdate = boissonsRepository.findAll().collectList().block().size();

        // Update the boissons using partial update
        Boissons partialUpdatedBoissons = new Boissons();
        partialUpdatedBoissons.setId(boissons.getId());

        partialUpdatedBoissons.imagePath(UPDATED_IMAGE_PATH).remisePerc(UPDATED_REMISE_PERC);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedBoissons.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedBoissons))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Boissons in the database
        List<Boissons> boissonsList = boissonsRepository.findAll().collectList().block();
        assertThat(boissonsList).hasSize(databaseSizeBeforeUpdate);
        Boissons testBoissons = boissonsList.get(boissonsList.size() - 1);
        assertThat(testBoissons.getNomBoissons()).isEqualTo(DEFAULT_NOM_BOISSONS);
        assertThat(testBoissons.getImagePath()).isEqualTo(UPDATED_IMAGE_PATH);
        assertThat(testBoissons.getPrix()).isEqualTo(DEFAULT_PRIX);
        assertThat(testBoissons.getRemisePerc()).isEqualTo(UPDATED_REMISE_PERC);
        assertThat(testBoissons.getRemiceVal()).isEqualTo(DEFAULT_REMICE_VAL);
    }

    @Test
    void fullUpdateBoissonsWithPatch() throws Exception {
        // Initialize the database
        boissonsRepository.save(boissons).block();

        int databaseSizeBeforeUpdate = boissonsRepository.findAll().collectList().block().size();

        // Update the boissons using partial update
        Boissons partialUpdatedBoissons = new Boissons();
        partialUpdatedBoissons.setId(boissons.getId());

        partialUpdatedBoissons
            .nomBoissons(UPDATED_NOM_BOISSONS)
            .imagePath(UPDATED_IMAGE_PATH)
            .prix(UPDATED_PRIX)
            .remisePerc(UPDATED_REMISE_PERC)
            .remiceVal(UPDATED_REMICE_VAL);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedBoissons.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedBoissons))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Boissons in the database
        List<Boissons> boissonsList = boissonsRepository.findAll().collectList().block();
        assertThat(boissonsList).hasSize(databaseSizeBeforeUpdate);
        Boissons testBoissons = boissonsList.get(boissonsList.size() - 1);
        assertThat(testBoissons.getNomBoissons()).isEqualTo(UPDATED_NOM_BOISSONS);
        assertThat(testBoissons.getImagePath()).isEqualTo(UPDATED_IMAGE_PATH);
        assertThat(testBoissons.getPrix()).isEqualTo(UPDATED_PRIX);
        assertThat(testBoissons.getRemisePerc()).isEqualTo(UPDATED_REMISE_PERC);
        assertThat(testBoissons.getRemiceVal()).isEqualTo(UPDATED_REMICE_VAL);
    }

    @Test
    void patchNonExistingBoissons() throws Exception {
        int databaseSizeBeforeUpdate = boissonsRepository.findAll().collectList().block().size();
        boissons.setId(count.incrementAndGet());

        // Create the Boissons
        BoissonsDTO boissonsDTO = boissonsMapper.toDto(boissons);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, boissonsDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(boissonsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Boissons in the database
        List<Boissons> boissonsList = boissonsRepository.findAll().collectList().block();
        assertThat(boissonsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchBoissons() throws Exception {
        int databaseSizeBeforeUpdate = boissonsRepository.findAll().collectList().block().size();
        boissons.setId(count.incrementAndGet());

        // Create the Boissons
        BoissonsDTO boissonsDTO = boissonsMapper.toDto(boissons);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(boissonsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Boissons in the database
        List<Boissons> boissonsList = boissonsRepository.findAll().collectList().block();
        assertThat(boissonsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamBoissons() throws Exception {
        int databaseSizeBeforeUpdate = boissonsRepository.findAll().collectList().block().size();
        boissons.setId(count.incrementAndGet());

        // Create the Boissons
        BoissonsDTO boissonsDTO = boissonsMapper.toDto(boissons);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(boissonsDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Boissons in the database
        List<Boissons> boissonsList = boissonsRepository.findAll().collectList().block();
        assertThat(boissonsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteBoissons() {
        // Initialize the database
        boissonsRepository.save(boissons).block();

        int databaseSizeBeforeDelete = boissonsRepository.findAll().collectList().block().size();

        // Delete the boissons
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, boissons.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Boissons> boissonsList = boissonsRepository.findAll().collectList().block();
        assertThat(boissonsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
