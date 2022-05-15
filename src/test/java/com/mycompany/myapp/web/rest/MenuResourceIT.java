package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Menu;
import com.mycompany.myapp.repository.EntityManager;
import com.mycompany.myapp.repository.MenuRepository;
import com.mycompany.myapp.service.dto.MenuDTO;
import com.mycompany.myapp.service.mapper.MenuMapper;
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
 * Integration tests for the {@link MenuResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class MenuResourceIT {

    private static final String DEFAULT_ID_MENU = "AAAAAAAAAA";
    private static final String UPDATED_ID_MENU = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_MENU = "AAAAAAAAAA";
    private static final String UPDATED_NOM_MENU = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/menus";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Menu menu;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Menu createEntity(EntityManager em) {
        Menu menu = new Menu().idMenu(DEFAULT_ID_MENU).nomMenu(DEFAULT_NOM_MENU);
        return menu;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Menu createUpdatedEntity(EntityManager em) {
        Menu menu = new Menu().idMenu(UPDATED_ID_MENU).nomMenu(UPDATED_NOM_MENU);
        return menu;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Menu.class).block();
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
        menu = createEntity(em);
    }

    @Test
    void createMenu() throws Exception {
        int databaseSizeBeforeCreate = menuRepository.findAll().collectList().block().size();
        // Create the Menu
        MenuDTO menuDTO = menuMapper.toDto(menu);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(menuDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Menu in the database
        List<Menu> menuList = menuRepository.findAll().collectList().block();
        assertThat(menuList).hasSize(databaseSizeBeforeCreate + 1);
        Menu testMenu = menuList.get(menuList.size() - 1);
        assertThat(testMenu.getIdMenu()).isEqualTo(DEFAULT_ID_MENU);
        assertThat(testMenu.getNomMenu()).isEqualTo(DEFAULT_NOM_MENU);
    }

    @Test
    void createMenuWithExistingId() throws Exception {
        // Create the Menu with an existing ID
        menu.setId(1L);
        MenuDTO menuDTO = menuMapper.toDto(menu);

        int databaseSizeBeforeCreate = menuRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(menuDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Menu in the database
        List<Menu> menuList = menuRepository.findAll().collectList().block();
        assertThat(menuList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllMenus() {
        // Initialize the database
        menuRepository.save(menu).block();

        // Get all the menuList
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
            .value(hasItem(menu.getId().intValue()))
            .jsonPath("$.[*].idMenu")
            .value(hasItem(DEFAULT_ID_MENU))
            .jsonPath("$.[*].nomMenu")
            .value(hasItem(DEFAULT_NOM_MENU));
    }

    @Test
    void getMenu() {
        // Initialize the database
        menuRepository.save(menu).block();

        // Get the menu
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, menu.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(menu.getId().intValue()))
            .jsonPath("$.idMenu")
            .value(is(DEFAULT_ID_MENU))
            .jsonPath("$.nomMenu")
            .value(is(DEFAULT_NOM_MENU));
    }

    @Test
    void getNonExistingMenu() {
        // Get the menu
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewMenu() throws Exception {
        // Initialize the database
        menuRepository.save(menu).block();

        int databaseSizeBeforeUpdate = menuRepository.findAll().collectList().block().size();

        // Update the menu
        Menu updatedMenu = menuRepository.findById(menu.getId()).block();
        updatedMenu.idMenu(UPDATED_ID_MENU).nomMenu(UPDATED_NOM_MENU);
        MenuDTO menuDTO = menuMapper.toDto(updatedMenu);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, menuDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(menuDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Menu in the database
        List<Menu> menuList = menuRepository.findAll().collectList().block();
        assertThat(menuList).hasSize(databaseSizeBeforeUpdate);
        Menu testMenu = menuList.get(menuList.size() - 1);
        assertThat(testMenu.getIdMenu()).isEqualTo(UPDATED_ID_MENU);
        assertThat(testMenu.getNomMenu()).isEqualTo(UPDATED_NOM_MENU);
    }

    @Test
    void putNonExistingMenu() throws Exception {
        int databaseSizeBeforeUpdate = menuRepository.findAll().collectList().block().size();
        menu.setId(count.incrementAndGet());

        // Create the Menu
        MenuDTO menuDTO = menuMapper.toDto(menu);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, menuDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(menuDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Menu in the database
        List<Menu> menuList = menuRepository.findAll().collectList().block();
        assertThat(menuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchMenu() throws Exception {
        int databaseSizeBeforeUpdate = menuRepository.findAll().collectList().block().size();
        menu.setId(count.incrementAndGet());

        // Create the Menu
        MenuDTO menuDTO = menuMapper.toDto(menu);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(menuDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Menu in the database
        List<Menu> menuList = menuRepository.findAll().collectList().block();
        assertThat(menuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamMenu() throws Exception {
        int databaseSizeBeforeUpdate = menuRepository.findAll().collectList().block().size();
        menu.setId(count.incrementAndGet());

        // Create the Menu
        MenuDTO menuDTO = menuMapper.toDto(menu);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(menuDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Menu in the database
        List<Menu> menuList = menuRepository.findAll().collectList().block();
        assertThat(menuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateMenuWithPatch() throws Exception {
        // Initialize the database
        menuRepository.save(menu).block();

        int databaseSizeBeforeUpdate = menuRepository.findAll().collectList().block().size();

        // Update the menu using partial update
        Menu partialUpdatedMenu = new Menu();
        partialUpdatedMenu.setId(menu.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedMenu.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedMenu))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Menu in the database
        List<Menu> menuList = menuRepository.findAll().collectList().block();
        assertThat(menuList).hasSize(databaseSizeBeforeUpdate);
        Menu testMenu = menuList.get(menuList.size() - 1);
        assertThat(testMenu.getIdMenu()).isEqualTo(DEFAULT_ID_MENU);
        assertThat(testMenu.getNomMenu()).isEqualTo(DEFAULT_NOM_MENU);
    }

    @Test
    void fullUpdateMenuWithPatch() throws Exception {
        // Initialize the database
        menuRepository.save(menu).block();

        int databaseSizeBeforeUpdate = menuRepository.findAll().collectList().block().size();

        // Update the menu using partial update
        Menu partialUpdatedMenu = new Menu();
        partialUpdatedMenu.setId(menu.getId());

        partialUpdatedMenu.idMenu(UPDATED_ID_MENU).nomMenu(UPDATED_NOM_MENU);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedMenu.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedMenu))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Menu in the database
        List<Menu> menuList = menuRepository.findAll().collectList().block();
        assertThat(menuList).hasSize(databaseSizeBeforeUpdate);
        Menu testMenu = menuList.get(menuList.size() - 1);
        assertThat(testMenu.getIdMenu()).isEqualTo(UPDATED_ID_MENU);
        assertThat(testMenu.getNomMenu()).isEqualTo(UPDATED_NOM_MENU);
    }

    @Test
    void patchNonExistingMenu() throws Exception {
        int databaseSizeBeforeUpdate = menuRepository.findAll().collectList().block().size();
        menu.setId(count.incrementAndGet());

        // Create the Menu
        MenuDTO menuDTO = menuMapper.toDto(menu);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, menuDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(menuDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Menu in the database
        List<Menu> menuList = menuRepository.findAll().collectList().block();
        assertThat(menuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchMenu() throws Exception {
        int databaseSizeBeforeUpdate = menuRepository.findAll().collectList().block().size();
        menu.setId(count.incrementAndGet());

        // Create the Menu
        MenuDTO menuDTO = menuMapper.toDto(menu);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(menuDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Menu in the database
        List<Menu> menuList = menuRepository.findAll().collectList().block();
        assertThat(menuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamMenu() throws Exception {
        int databaseSizeBeforeUpdate = menuRepository.findAll().collectList().block().size();
        menu.setId(count.incrementAndGet());

        // Create the Menu
        MenuDTO menuDTO = menuMapper.toDto(menu);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(menuDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Menu in the database
        List<Menu> menuList = menuRepository.findAll().collectList().block();
        assertThat(menuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteMenu() {
        // Initialize the database
        menuRepository.save(menu).block();

        int databaseSizeBeforeDelete = menuRepository.findAll().collectList().block().size();

        // Delete the menu
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, menu.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Menu> menuList = menuRepository.findAll().collectList().block();
        assertThat(menuList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
