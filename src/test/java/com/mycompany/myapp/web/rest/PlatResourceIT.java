package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Plat;
import com.mycompany.myapp.repository.EntityManager;
import com.mycompany.myapp.repository.PlatRepository;
import com.mycompany.myapp.service.dto.PlatDTO;
import com.mycompany.myapp.service.mapper.PlatMapper;
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
 * Integration tests for the {@link PlatResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class PlatResourceIT {

    private static final String DEFAULT_NOM_PLAT = "AAAAAAAAAA";
    private static final String UPDATED_NOM_PLAT = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE_PATH = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_PATH = "BBBBBBBBBB";

    private static final Double DEFAULT_PRIX = 1D;
    private static final Double UPDATED_PRIX = 2D;

    private static final Double DEFAULT_REMISE_PERC = 1D;
    private static final Double UPDATED_REMISE_PERC = 2D;

    private static final Double DEFAULT_REMICE_VAL = 1D;
    private static final Double UPDATED_REMICE_VAL = 2D;

    private static final String ENTITY_API_URL = "/api/plats";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PlatRepository platRepository;

    @Autowired
    private PlatMapper platMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Plat plat;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Plat createEntity(EntityManager em) {
        Plat plat = new Plat()
            .nomPlat(DEFAULT_NOM_PLAT)
            .imagePath(DEFAULT_IMAGE_PATH)
            .prix(DEFAULT_PRIX)
            .remisePerc(DEFAULT_REMISE_PERC)
            .remiceVal(DEFAULT_REMICE_VAL);
        return plat;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Plat createUpdatedEntity(EntityManager em) {
        Plat plat = new Plat()
            .nomPlat(UPDATED_NOM_PLAT)
            .imagePath(UPDATED_IMAGE_PATH)
            .prix(UPDATED_PRIX)
            .remisePerc(UPDATED_REMISE_PERC)
            .remiceVal(UPDATED_REMICE_VAL);
        return plat;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Plat.class).block();
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
        plat = createEntity(em);
    }

    @Test
    void createPlat() throws Exception {
        int databaseSizeBeforeCreate = platRepository.findAll().collectList().block().size();
        // Create the Plat
        PlatDTO platDTO = platMapper.toDto(plat);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(platDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Plat in the database
        List<Plat> platList = platRepository.findAll().collectList().block();
        assertThat(platList).hasSize(databaseSizeBeforeCreate + 1);
        Plat testPlat = platList.get(platList.size() - 1);
        assertThat(testPlat.getNomPlat()).isEqualTo(DEFAULT_NOM_PLAT);
        assertThat(testPlat.getImagePath()).isEqualTo(DEFAULT_IMAGE_PATH);
        assertThat(testPlat.getPrix()).isEqualTo(DEFAULT_PRIX);
        assertThat(testPlat.getRemisePerc()).isEqualTo(DEFAULT_REMISE_PERC);
        assertThat(testPlat.getRemiceVal()).isEqualTo(DEFAULT_REMICE_VAL);
    }

    @Test
    void createPlatWithExistingId() throws Exception {
        // Create the Plat with an existing ID
        plat.setId(1L);
        PlatDTO platDTO = platMapper.toDto(plat);

        int databaseSizeBeforeCreate = platRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(platDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Plat in the database
        List<Plat> platList = platRepository.findAll().collectList().block();
        assertThat(platList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllPlats() {
        // Initialize the database
        platRepository.save(plat).block();

        // Get all the platList
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
            .value(hasItem(plat.getId().intValue()))
            .jsonPath("$.[*].nomPlat")
            .value(hasItem(DEFAULT_NOM_PLAT))
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
    void getPlat() {
        // Initialize the database
        platRepository.save(plat).block();

        // Get the plat
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, plat.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(plat.getId().intValue()))
            .jsonPath("$.nomPlat")
            .value(is(DEFAULT_NOM_PLAT))
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
    void getNonExistingPlat() {
        // Get the plat
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewPlat() throws Exception {
        // Initialize the database
        platRepository.save(plat).block();

        int databaseSizeBeforeUpdate = platRepository.findAll().collectList().block().size();

        // Update the plat
        Plat updatedPlat = platRepository.findById(plat.getId()).block();
        updatedPlat
            .nomPlat(UPDATED_NOM_PLAT)
            .imagePath(UPDATED_IMAGE_PATH)
            .prix(UPDATED_PRIX)
            .remisePerc(UPDATED_REMISE_PERC)
            .remiceVal(UPDATED_REMICE_VAL);
        PlatDTO platDTO = platMapper.toDto(updatedPlat);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, platDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(platDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Plat in the database
        List<Plat> platList = platRepository.findAll().collectList().block();
        assertThat(platList).hasSize(databaseSizeBeforeUpdate);
        Plat testPlat = platList.get(platList.size() - 1);
        assertThat(testPlat.getNomPlat()).isEqualTo(UPDATED_NOM_PLAT);
        assertThat(testPlat.getImagePath()).isEqualTo(UPDATED_IMAGE_PATH);
        assertThat(testPlat.getPrix()).isEqualTo(UPDATED_PRIX);
        assertThat(testPlat.getRemisePerc()).isEqualTo(UPDATED_REMISE_PERC);
        assertThat(testPlat.getRemiceVal()).isEqualTo(UPDATED_REMICE_VAL);
    }

    @Test
    void putNonExistingPlat() throws Exception {
        int databaseSizeBeforeUpdate = platRepository.findAll().collectList().block().size();
        plat.setId(count.incrementAndGet());

        // Create the Plat
        PlatDTO platDTO = platMapper.toDto(plat);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, platDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(platDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Plat in the database
        List<Plat> platList = platRepository.findAll().collectList().block();
        assertThat(platList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchPlat() throws Exception {
        int databaseSizeBeforeUpdate = platRepository.findAll().collectList().block().size();
        plat.setId(count.incrementAndGet());

        // Create the Plat
        PlatDTO platDTO = platMapper.toDto(plat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(platDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Plat in the database
        List<Plat> platList = platRepository.findAll().collectList().block();
        assertThat(platList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamPlat() throws Exception {
        int databaseSizeBeforeUpdate = platRepository.findAll().collectList().block().size();
        plat.setId(count.incrementAndGet());

        // Create the Plat
        PlatDTO platDTO = platMapper.toDto(plat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(platDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Plat in the database
        List<Plat> platList = platRepository.findAll().collectList().block();
        assertThat(platList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdatePlatWithPatch() throws Exception {
        // Initialize the database
        platRepository.save(plat).block();

        int databaseSizeBeforeUpdate = platRepository.findAll().collectList().block().size();

        // Update the plat using partial update
        Plat partialUpdatedPlat = new Plat();
        partialUpdatedPlat.setId(plat.getId());

        partialUpdatedPlat
            .nomPlat(UPDATED_NOM_PLAT)
            .imagePath(UPDATED_IMAGE_PATH)
            .prix(UPDATED_PRIX)
            .remisePerc(UPDATED_REMISE_PERC)
            .remiceVal(UPDATED_REMICE_VAL);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPlat.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPlat))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Plat in the database
        List<Plat> platList = platRepository.findAll().collectList().block();
        assertThat(platList).hasSize(databaseSizeBeforeUpdate);
        Plat testPlat = platList.get(platList.size() - 1);
        assertThat(testPlat.getNomPlat()).isEqualTo(UPDATED_NOM_PLAT);
        assertThat(testPlat.getImagePath()).isEqualTo(UPDATED_IMAGE_PATH);
        assertThat(testPlat.getPrix()).isEqualTo(UPDATED_PRIX);
        assertThat(testPlat.getRemisePerc()).isEqualTo(UPDATED_REMISE_PERC);
        assertThat(testPlat.getRemiceVal()).isEqualTo(UPDATED_REMICE_VAL);
    }

    @Test
    void fullUpdatePlatWithPatch() throws Exception {
        // Initialize the database
        platRepository.save(plat).block();

        int databaseSizeBeforeUpdate = platRepository.findAll().collectList().block().size();

        // Update the plat using partial update
        Plat partialUpdatedPlat = new Plat();
        partialUpdatedPlat.setId(plat.getId());

        partialUpdatedPlat
            .nomPlat(UPDATED_NOM_PLAT)
            .imagePath(UPDATED_IMAGE_PATH)
            .prix(UPDATED_PRIX)
            .remisePerc(UPDATED_REMISE_PERC)
            .remiceVal(UPDATED_REMICE_VAL);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPlat.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPlat))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Plat in the database
        List<Plat> platList = platRepository.findAll().collectList().block();
        assertThat(platList).hasSize(databaseSizeBeforeUpdate);
        Plat testPlat = platList.get(platList.size() - 1);
        assertThat(testPlat.getNomPlat()).isEqualTo(UPDATED_NOM_PLAT);
        assertThat(testPlat.getImagePath()).isEqualTo(UPDATED_IMAGE_PATH);
        assertThat(testPlat.getPrix()).isEqualTo(UPDATED_PRIX);
        assertThat(testPlat.getRemisePerc()).isEqualTo(UPDATED_REMISE_PERC);
        assertThat(testPlat.getRemiceVal()).isEqualTo(UPDATED_REMICE_VAL);
    }

    @Test
    void patchNonExistingPlat() throws Exception {
        int databaseSizeBeforeUpdate = platRepository.findAll().collectList().block().size();
        plat.setId(count.incrementAndGet());

        // Create the Plat
        PlatDTO platDTO = platMapper.toDto(plat);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, platDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(platDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Plat in the database
        List<Plat> platList = platRepository.findAll().collectList().block();
        assertThat(platList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchPlat() throws Exception {
        int databaseSizeBeforeUpdate = platRepository.findAll().collectList().block().size();
        plat.setId(count.incrementAndGet());

        // Create the Plat
        PlatDTO platDTO = platMapper.toDto(plat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(platDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Plat in the database
        List<Plat> platList = platRepository.findAll().collectList().block();
        assertThat(platList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamPlat() throws Exception {
        int databaseSizeBeforeUpdate = platRepository.findAll().collectList().block().size();
        plat.setId(count.incrementAndGet());

        // Create the Plat
        PlatDTO platDTO = platMapper.toDto(plat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(platDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Plat in the database
        List<Plat> platList = platRepository.findAll().collectList().block();
        assertThat(platList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deletePlat() {
        // Initialize the database
        platRepository.save(plat).block();

        int databaseSizeBeforeDelete = platRepository.findAll().collectList().block().size();

        // Delete the plat
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, plat.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Plat> platList = platRepository.findAll().collectList().block();
        assertThat(platList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
