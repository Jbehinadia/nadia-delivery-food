package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Commande;
import com.mycompany.myapp.repository.CommandeRepository;
import com.mycompany.myapp.repository.EntityManager;
import com.mycompany.myapp.service.dto.CommandeDTO;
import com.mycompany.myapp.service.mapper.CommandeMapper;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link CommandeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class CommandeResourceIT {

    private static final String DEFAULT_ADRESSE_COMMANDE = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE_COMMANDE = "BBBBBBBBBB";

    private static final String DEFAULT_ETAT = "AAAAAAAAAA";
    private static final String UPDATED_ETAT = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_COMMANDE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_COMMANDE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Double DEFAULT_PRIX_TOTAL = 1D;
    private static final Double UPDATED_PRIX_TOTAL = 2D;

    private static final Double DEFAULT_REMISE_PERC = 1D;
    private static final Double UPDATED_REMISE_PERC = 2D;

    private static final Double DEFAULT_REMICE_VAL = 1D;
    private static final Double UPDATED_REMICE_VAL = 2D;

    private static final Double DEFAULT_PRIX_LIVRESON = 1D;
    private static final Double UPDATED_PRIX_LIVRESON = 2D;

    private static final Instant DEFAULT_DATE_SORTIE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_SORTIE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/commandes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CommandeRepository commandeRepository;

    @Autowired
    private CommandeMapper commandeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Commande commande;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Commande createEntity(EntityManager em) {
        Commande commande = new Commande()
            .adresseCommande(DEFAULT_ADRESSE_COMMANDE)
            .etat(DEFAULT_ETAT)
            .dateCommande(DEFAULT_DATE_COMMANDE)
            .prixTotal(DEFAULT_PRIX_TOTAL)
            .remisePerc(DEFAULT_REMISE_PERC)
            .remiceVal(DEFAULT_REMICE_VAL)
            .prixLivreson(DEFAULT_PRIX_LIVRESON)
            .dateSortie(DEFAULT_DATE_SORTIE);
        return commande;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Commande createUpdatedEntity(EntityManager em) {
        Commande commande = new Commande()
            .adresseCommande(UPDATED_ADRESSE_COMMANDE)
            .etat(UPDATED_ETAT)
            .dateCommande(UPDATED_DATE_COMMANDE)
            .prixTotal(UPDATED_PRIX_TOTAL)
            .remisePerc(UPDATED_REMISE_PERC)
            .remiceVal(UPDATED_REMICE_VAL)
            .prixLivreson(UPDATED_PRIX_LIVRESON)
            .dateSortie(UPDATED_DATE_SORTIE);
        return commande;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Commande.class).block();
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
        commande = createEntity(em);
    }

    @Test
    void createCommande() throws Exception {
        int databaseSizeBeforeCreate = commandeRepository.findAll().collectList().block().size();
        // Create the Commande
        CommandeDTO commandeDTO = commandeMapper.toDto(commande);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(commandeDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Commande in the database
        List<Commande> commandeList = commandeRepository.findAll().collectList().block();
        assertThat(commandeList).hasSize(databaseSizeBeforeCreate + 1);
        Commande testCommande = commandeList.get(commandeList.size() - 1);
        assertThat(testCommande.getAdresseCommande()).isEqualTo(DEFAULT_ADRESSE_COMMANDE);
        assertThat(testCommande.getEtat()).isEqualTo(DEFAULT_ETAT);
        assertThat(testCommande.getDateCommande()).isEqualTo(DEFAULT_DATE_COMMANDE);
        assertThat(testCommande.getPrixTotal()).isEqualTo(DEFAULT_PRIX_TOTAL);
        assertThat(testCommande.getRemisePerc()).isEqualTo(DEFAULT_REMISE_PERC);
        assertThat(testCommande.getRemiceVal()).isEqualTo(DEFAULT_REMICE_VAL);
        assertThat(testCommande.getPrixLivreson()).isEqualTo(DEFAULT_PRIX_LIVRESON);
        assertThat(testCommande.getDateSortie()).isEqualTo(DEFAULT_DATE_SORTIE);
    }

    @Test
    void createCommandeWithExistingId() throws Exception {
        // Create the Commande with an existing ID
        commande.setId(1L);
        CommandeDTO commandeDTO = commandeMapper.toDto(commande);

        int databaseSizeBeforeCreate = commandeRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(commandeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Commande in the database
        List<Commande> commandeList = commandeRepository.findAll().collectList().block();
        assertThat(commandeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllCommandes() {
        // Initialize the database
        commandeRepository.save(commande).block();

        // Get all the commandeList
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
            .value(hasItem(commande.getId().intValue()))
            .jsonPath("$.[*].adresseCommande")
            .value(hasItem(DEFAULT_ADRESSE_COMMANDE))
            .jsonPath("$.[*].etat")
            .value(hasItem(DEFAULT_ETAT))
            .jsonPath("$.[*].dateCommande")
            .value(hasItem(DEFAULT_DATE_COMMANDE.toString()))
            .jsonPath("$.[*].prixTotal")
            .value(hasItem(DEFAULT_PRIX_TOTAL.doubleValue()))
            .jsonPath("$.[*].remisePerc")
            .value(hasItem(DEFAULT_REMISE_PERC.doubleValue()))
            .jsonPath("$.[*].remiceVal")
            .value(hasItem(DEFAULT_REMICE_VAL.doubleValue()))
            .jsonPath("$.[*].prixLivreson")
            .value(hasItem(DEFAULT_PRIX_LIVRESON.doubleValue()))
            .jsonPath("$.[*].dateSortie")
            .value(hasItem(DEFAULT_DATE_SORTIE.toString()));
    }

    @Test
    void getCommande() {
        // Initialize the database
        commandeRepository.save(commande).block();

        // Get the commande
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, commande.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(commande.getId().intValue()))
            .jsonPath("$.adresseCommande")
            .value(is(DEFAULT_ADRESSE_COMMANDE))
            .jsonPath("$.etat")
            .value(is(DEFAULT_ETAT))
            .jsonPath("$.dateCommande")
            .value(is(DEFAULT_DATE_COMMANDE.toString()))
            .jsonPath("$.prixTotal")
            .value(is(DEFAULT_PRIX_TOTAL.doubleValue()))
            .jsonPath("$.remisePerc")
            .value(is(DEFAULT_REMISE_PERC.doubleValue()))
            .jsonPath("$.remiceVal")
            .value(is(DEFAULT_REMICE_VAL.doubleValue()))
            .jsonPath("$.prixLivreson")
            .value(is(DEFAULT_PRIX_LIVRESON.doubleValue()))
            .jsonPath("$.dateSortie")
            .value(is(DEFAULT_DATE_SORTIE.toString()));
    }

    @Test
    void getNonExistingCommande() {
        // Get the commande
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewCommande() throws Exception {
        // Initialize the database
        commandeRepository.save(commande).block();

        int databaseSizeBeforeUpdate = commandeRepository.findAll().collectList().block().size();

        // Update the commande
        Commande updatedCommande = commandeRepository.findById(commande.getId()).block();
        updatedCommande
            .adresseCommande(UPDATED_ADRESSE_COMMANDE)
            .etat(UPDATED_ETAT)
            .dateCommande(UPDATED_DATE_COMMANDE)
            .prixTotal(UPDATED_PRIX_TOTAL)
            .remisePerc(UPDATED_REMISE_PERC)
            .remiceVal(UPDATED_REMICE_VAL)
            .prixLivreson(UPDATED_PRIX_LIVRESON)
            .dateSortie(UPDATED_DATE_SORTIE);
        CommandeDTO commandeDTO = commandeMapper.toDto(updatedCommande);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, commandeDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(commandeDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Commande in the database
        List<Commande> commandeList = commandeRepository.findAll().collectList().block();
        assertThat(commandeList).hasSize(databaseSizeBeforeUpdate);
        Commande testCommande = commandeList.get(commandeList.size() - 1);
        assertThat(testCommande.getAdresseCommande()).isEqualTo(UPDATED_ADRESSE_COMMANDE);
        assertThat(testCommande.getEtat()).isEqualTo(UPDATED_ETAT);
        assertThat(testCommande.getDateCommande()).isEqualTo(UPDATED_DATE_COMMANDE);
        assertThat(testCommande.getPrixTotal()).isEqualTo(UPDATED_PRIX_TOTAL);
        assertThat(testCommande.getRemisePerc()).isEqualTo(UPDATED_REMISE_PERC);
        assertThat(testCommande.getRemiceVal()).isEqualTo(UPDATED_REMICE_VAL);
        assertThat(testCommande.getPrixLivreson()).isEqualTo(UPDATED_PRIX_LIVRESON);
        assertThat(testCommande.getDateSortie()).isEqualTo(UPDATED_DATE_SORTIE);
    }

    @Test
    void putNonExistingCommande() throws Exception {
        int databaseSizeBeforeUpdate = commandeRepository.findAll().collectList().block().size();
        commande.setId(count.incrementAndGet());

        // Create the Commande
        CommandeDTO commandeDTO = commandeMapper.toDto(commande);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, commandeDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(commandeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Commande in the database
        List<Commande> commandeList = commandeRepository.findAll().collectList().block();
        assertThat(commandeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCommande() throws Exception {
        int databaseSizeBeforeUpdate = commandeRepository.findAll().collectList().block().size();
        commande.setId(count.incrementAndGet());

        // Create the Commande
        CommandeDTO commandeDTO = commandeMapper.toDto(commande);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(commandeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Commande in the database
        List<Commande> commandeList = commandeRepository.findAll().collectList().block();
        assertThat(commandeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCommande() throws Exception {
        int databaseSizeBeforeUpdate = commandeRepository.findAll().collectList().block().size();
        commande.setId(count.incrementAndGet());

        // Create the Commande
        CommandeDTO commandeDTO = commandeMapper.toDto(commande);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(commandeDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Commande in the database
        List<Commande> commandeList = commandeRepository.findAll().collectList().block();
        assertThat(commandeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateCommandeWithPatch() throws Exception {
        // Initialize the database
        commandeRepository.save(commande).block();

        int databaseSizeBeforeUpdate = commandeRepository.findAll().collectList().block().size();

        // Update the commande using partial update
        Commande partialUpdatedCommande = new Commande();
        partialUpdatedCommande.setId(commande.getId());

        partialUpdatedCommande
            .adresseCommande(UPDATED_ADRESSE_COMMANDE)
            .prixTotal(UPDATED_PRIX_TOTAL)
            .remisePerc(UPDATED_REMISE_PERC)
            .prixLivreson(UPDATED_PRIX_LIVRESON)
            .dateSortie(UPDATED_DATE_SORTIE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCommande.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCommande))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Commande in the database
        List<Commande> commandeList = commandeRepository.findAll().collectList().block();
        assertThat(commandeList).hasSize(databaseSizeBeforeUpdate);
        Commande testCommande = commandeList.get(commandeList.size() - 1);
        assertThat(testCommande.getAdresseCommande()).isEqualTo(UPDATED_ADRESSE_COMMANDE);
        assertThat(testCommande.getEtat()).isEqualTo(DEFAULT_ETAT);
        assertThat(testCommande.getDateCommande()).isEqualTo(DEFAULT_DATE_COMMANDE);
        assertThat(testCommande.getPrixTotal()).isEqualTo(UPDATED_PRIX_TOTAL);
        assertThat(testCommande.getRemisePerc()).isEqualTo(UPDATED_REMISE_PERC);
        assertThat(testCommande.getRemiceVal()).isEqualTo(DEFAULT_REMICE_VAL);
        assertThat(testCommande.getPrixLivreson()).isEqualTo(UPDATED_PRIX_LIVRESON);
        assertThat(testCommande.getDateSortie()).isEqualTo(UPDATED_DATE_SORTIE);
    }

    @Test
    void fullUpdateCommandeWithPatch() throws Exception {
        // Initialize the database
        commandeRepository.save(commande).block();

        int databaseSizeBeforeUpdate = commandeRepository.findAll().collectList().block().size();

        // Update the commande using partial update
        Commande partialUpdatedCommande = new Commande();
        partialUpdatedCommande.setId(commande.getId());

        partialUpdatedCommande
            .adresseCommande(UPDATED_ADRESSE_COMMANDE)
            .etat(UPDATED_ETAT)
            .dateCommande(UPDATED_DATE_COMMANDE)
            .prixTotal(UPDATED_PRIX_TOTAL)
            .remisePerc(UPDATED_REMISE_PERC)
            .remiceVal(UPDATED_REMICE_VAL)
            .prixLivreson(UPDATED_PRIX_LIVRESON)
            .dateSortie(UPDATED_DATE_SORTIE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCommande.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCommande))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Commande in the database
        List<Commande> commandeList = commandeRepository.findAll().collectList().block();
        assertThat(commandeList).hasSize(databaseSizeBeforeUpdate);
        Commande testCommande = commandeList.get(commandeList.size() - 1);
        assertThat(testCommande.getAdresseCommande()).isEqualTo(UPDATED_ADRESSE_COMMANDE);
        assertThat(testCommande.getEtat()).isEqualTo(UPDATED_ETAT);
        assertThat(testCommande.getDateCommande()).isEqualTo(UPDATED_DATE_COMMANDE);
        assertThat(testCommande.getPrixTotal()).isEqualTo(UPDATED_PRIX_TOTAL);
        assertThat(testCommande.getRemisePerc()).isEqualTo(UPDATED_REMISE_PERC);
        assertThat(testCommande.getRemiceVal()).isEqualTo(UPDATED_REMICE_VAL);
        assertThat(testCommande.getPrixLivreson()).isEqualTo(UPDATED_PRIX_LIVRESON);
        assertThat(testCommande.getDateSortie()).isEqualTo(UPDATED_DATE_SORTIE);
    }

    @Test
    void patchNonExistingCommande() throws Exception {
        int databaseSizeBeforeUpdate = commandeRepository.findAll().collectList().block().size();
        commande.setId(count.incrementAndGet());

        // Create the Commande
        CommandeDTO commandeDTO = commandeMapper.toDto(commande);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, commandeDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(commandeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Commande in the database
        List<Commande> commandeList = commandeRepository.findAll().collectList().block();
        assertThat(commandeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCommande() throws Exception {
        int databaseSizeBeforeUpdate = commandeRepository.findAll().collectList().block().size();
        commande.setId(count.incrementAndGet());

        // Create the Commande
        CommandeDTO commandeDTO = commandeMapper.toDto(commande);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(commandeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Commande in the database
        List<Commande> commandeList = commandeRepository.findAll().collectList().block();
        assertThat(commandeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCommande() throws Exception {
        int databaseSizeBeforeUpdate = commandeRepository.findAll().collectList().block().size();
        commande.setId(count.incrementAndGet());

        // Create the Commande
        CommandeDTO commandeDTO = commandeMapper.toDto(commande);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(commandeDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Commande in the database
        List<Commande> commandeList = commandeRepository.findAll().collectList().block();
        assertThat(commandeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteCommande() {
        // Initialize the database
        commandeRepository.save(commande).block();

        int databaseSizeBeforeDelete = commandeRepository.findAll().collectList().block().size();

        // Delete the commande
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, commande.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Commande> commandeList = commandeRepository.findAll().collectList().block();
        assertThat(commandeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
