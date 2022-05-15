package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Restaurant;
import com.mycompany.myapp.repository.EntityManager;
import com.mycompany.myapp.repository.RestaurantRepository;
import com.mycompany.myapp.service.dto.RestaurantDTO;
import com.mycompany.myapp.service.mapper.RestaurantMapper;
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
 * Integration tests for the {@link RestaurantResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class RestaurantResourceIT {

    private static final String DEFAULT_NOM_RESTAURANT = "AAAAAAAAAA";
    private static final String UPDATED_NOM_RESTAURANT = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESSE_RESTAURANT = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE_RESTAURANT = "BBBBBBBBBB";

    private static final String DEFAULT_NUM_RESTAURANT = "AAAAAAAAAA";
    private static final String UPDATED_NUM_RESTAURANT = "BBBBBBBBBB";

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
        Restaurant restaurant = new Restaurant()
            .nomRestaurant(DEFAULT_NOM_RESTAURANT)
            .adresseRestaurant(DEFAULT_ADRESSE_RESTAURANT)
            .numRestaurant(DEFAULT_NUM_RESTAURANT);
        return restaurant;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Restaurant createUpdatedEntity(EntityManager em) {
        Restaurant restaurant = new Restaurant()
            .nomRestaurant(UPDATED_NOM_RESTAURANT)
            .adresseRestaurant(UPDATED_ADRESSE_RESTAURANT)
            .numRestaurant(UPDATED_NUM_RESTAURANT);
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
        assertThat(testRestaurant.getNomRestaurant()).isEqualTo(DEFAULT_NOM_RESTAURANT);
        assertThat(testRestaurant.getAdresseRestaurant()).isEqualTo(DEFAULT_ADRESSE_RESTAURANT);
        assertThat(testRestaurant.getNumRestaurant()).isEqualTo(DEFAULT_NUM_RESTAURANT);
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
            .jsonPath("$.[*].nomRestaurant")
            .value(hasItem(DEFAULT_NOM_RESTAURANT))
            .jsonPath("$.[*].adresseRestaurant")
            .value(hasItem(DEFAULT_ADRESSE_RESTAURANT))
            .jsonPath("$.[*].numRestaurant")
            .value(hasItem(DEFAULT_NUM_RESTAURANT));
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
            .jsonPath("$.nomRestaurant")
            .value(is(DEFAULT_NOM_RESTAURANT))
            .jsonPath("$.adresseRestaurant")
            .value(is(DEFAULT_ADRESSE_RESTAURANT))
            .jsonPath("$.numRestaurant")
            .value(is(DEFAULT_NUM_RESTAURANT));
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
    void putNewRestaurant() throws Exception {
        // Initialize the database
        restaurantRepository.save(restaurant).block();

        int databaseSizeBeforeUpdate = restaurantRepository.findAll().collectList().block().size();

        // Update the restaurant
        Restaurant updatedRestaurant = restaurantRepository.findById(restaurant.getId()).block();
        updatedRestaurant
            .nomRestaurant(UPDATED_NOM_RESTAURANT)
            .adresseRestaurant(UPDATED_ADRESSE_RESTAURANT)
            .numRestaurant(UPDATED_NUM_RESTAURANT);
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
        assertThat(testRestaurant.getNomRestaurant()).isEqualTo(UPDATED_NOM_RESTAURANT);
        assertThat(testRestaurant.getAdresseRestaurant()).isEqualTo(UPDATED_ADRESSE_RESTAURANT);
        assertThat(testRestaurant.getNumRestaurant()).isEqualTo(UPDATED_NUM_RESTAURANT);
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
        assertThat(testRestaurant.getNomRestaurant()).isEqualTo(DEFAULT_NOM_RESTAURANT);
        assertThat(testRestaurant.getAdresseRestaurant()).isEqualTo(DEFAULT_ADRESSE_RESTAURANT);
        assertThat(testRestaurant.getNumRestaurant()).isEqualTo(DEFAULT_NUM_RESTAURANT);
    }

    @Test
    void fullUpdateRestaurantWithPatch() throws Exception {
        // Initialize the database
        restaurantRepository.save(restaurant).block();

        int databaseSizeBeforeUpdate = restaurantRepository.findAll().collectList().block().size();

        // Update the restaurant using partial update
        Restaurant partialUpdatedRestaurant = new Restaurant();
        partialUpdatedRestaurant.setId(restaurant.getId());

        partialUpdatedRestaurant
            .nomRestaurant(UPDATED_NOM_RESTAURANT)
            .adresseRestaurant(UPDATED_ADRESSE_RESTAURANT)
            .numRestaurant(UPDATED_NUM_RESTAURANT);

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
        assertThat(testRestaurant.getNomRestaurant()).isEqualTo(UPDATED_NOM_RESTAURANT);
        assertThat(testRestaurant.getAdresseRestaurant()).isEqualTo(UPDATED_ADRESSE_RESTAURANT);
        assertThat(testRestaurant.getNumRestaurant()).isEqualTo(UPDATED_NUM_RESTAURANT);
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
