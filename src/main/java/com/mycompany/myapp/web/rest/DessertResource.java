package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.DessertRepository;
import com.mycompany.myapp.service.DessertService;
import com.mycompany.myapp.service.dto.DessertDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Dessert}.
 */
@RestController
@RequestMapping("/api")
public class DessertResource {

    private final Logger log = LoggerFactory.getLogger(DessertResource.class);

    private static final String ENTITY_NAME = "dessert";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DessertService dessertService;

    private final DessertRepository dessertRepository;

    public DessertResource(DessertService dessertService, DessertRepository dessertRepository) {
        this.dessertService = dessertService;
        this.dessertRepository = dessertRepository;
    }

    /**
     * {@code POST  /desserts} : Create a new dessert.
     *
     * @param dessertDTO the dessertDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dessertDTO, or with status {@code 400 (Bad Request)} if the dessert has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/desserts")
    public Mono<ResponseEntity<DessertDTO>> createDessert(@RequestBody DessertDTO dessertDTO) throws URISyntaxException {
        log.debug("REST request to save Dessert : {}", dessertDTO);
        if (dessertDTO.getId() != null) {
            throw new BadRequestAlertException("A new dessert cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return dessertService
            .save(dessertDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/desserts/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /desserts/:id} : Updates an existing dessert.
     *
     * @param id the id of the dessertDTO to save.
     * @param dessertDTO the dessertDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dessertDTO,
     * or with status {@code 400 (Bad Request)} if the dessertDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dessertDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/desserts/{id}")
    public Mono<ResponseEntity<DessertDTO>> updateDessert(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DessertDTO dessertDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Dessert : {}, {}", id, dessertDTO);
        if (dessertDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dessertDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return dessertRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return dessertService
                    .update(dessertDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /desserts/:id} : Partial updates given fields of an existing dessert, field will ignore if it is null
     *
     * @param id the id of the dessertDTO to save.
     * @param dessertDTO the dessertDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dessertDTO,
     * or with status {@code 400 (Bad Request)} if the dessertDTO is not valid,
     * or with status {@code 404 (Not Found)} if the dessertDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the dessertDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/desserts/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<DessertDTO>> partialUpdateDessert(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DessertDTO dessertDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Dessert partially : {}, {}", id, dessertDTO);
        if (dessertDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dessertDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return dessertRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<DessertDTO> result = dessertService.partialUpdate(dessertDTO);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /desserts} : get all the desserts.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of desserts in body.
     */
    @GetMapping("/desserts")
    public Mono<ResponseEntity<List<DessertDTO>>> getAllDesserts(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of Desserts");
        return dessertService
            .countAll()
            .zipWith(dessertService.findAll(pageable).collectList())
            .map(countWithEntities ->
                ResponseEntity
                    .ok()
                    .headers(
                        PaginationUtil.generatePaginationHttpHeaders(
                            UriComponentsBuilder.fromHttpRequest(request),
                            new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
                        )
                    )
                    .body(countWithEntities.getT2())
            );
    }

    /**
     * {@code GET  /desserts/:id} : get the "id" dessert.
     *
     * @param id the id of the dessertDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dessertDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/desserts/{id}")
    public Mono<ResponseEntity<DessertDTO>> getDessert(@PathVariable Long id) {
        log.debug("REST request to get Dessert : {}", id);
        Mono<DessertDTO> dessertDTO = dessertService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dessertDTO);
    }

    /**
     * {@code DELETE  /desserts/:id} : delete the "id" dessert.
     *
     * @param id the id of the dessertDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/desserts/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteDessert(@PathVariable Long id) {
        log.debug("REST request to delete Dessert : {}", id);
        return dessertService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
