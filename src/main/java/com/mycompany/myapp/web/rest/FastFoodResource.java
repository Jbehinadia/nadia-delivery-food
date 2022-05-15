package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.FastFoodRepository;
import com.mycompany.myapp.service.FastFoodService;
import com.mycompany.myapp.service.dto.FastFoodDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.FastFood}.
 */
@RestController
@RequestMapping("/api")
public class FastFoodResource {

    private final Logger log = LoggerFactory.getLogger(FastFoodResource.class);

    private static final String ENTITY_NAME = "fastFood";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FastFoodService fastFoodService;

    private final FastFoodRepository fastFoodRepository;

    public FastFoodResource(FastFoodService fastFoodService, FastFoodRepository fastFoodRepository) {
        this.fastFoodService = fastFoodService;
        this.fastFoodRepository = fastFoodRepository;
    }

    /**
     * {@code POST  /fast-foods} : Create a new fastFood.
     *
     * @param fastFoodDTO the fastFoodDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fastFoodDTO, or with status {@code 400 (Bad Request)} if the fastFood has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/fast-foods")
    public Mono<ResponseEntity<FastFoodDTO>> createFastFood(@RequestBody FastFoodDTO fastFoodDTO) throws URISyntaxException {
        log.debug("REST request to save FastFood : {}", fastFoodDTO);
        if (fastFoodDTO.getId() != null) {
            throw new BadRequestAlertException("A new fastFood cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return fastFoodService
            .save(fastFoodDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/fast-foods/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /fast-foods/:id} : Updates an existing fastFood.
     *
     * @param id the id of the fastFoodDTO to save.
     * @param fastFoodDTO the fastFoodDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fastFoodDTO,
     * or with status {@code 400 (Bad Request)} if the fastFoodDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fastFoodDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/fast-foods/{id}")
    public Mono<ResponseEntity<FastFoodDTO>> updateFastFood(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FastFoodDTO fastFoodDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FastFood : {}, {}", id, fastFoodDTO);
        if (fastFoodDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fastFoodDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return fastFoodRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return fastFoodService
                    .update(fastFoodDTO)
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
     * {@code PATCH  /fast-foods/:id} : Partial updates given fields of an existing fastFood, field will ignore if it is null
     *
     * @param id the id of the fastFoodDTO to save.
     * @param fastFoodDTO the fastFoodDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fastFoodDTO,
     * or with status {@code 400 (Bad Request)} if the fastFoodDTO is not valid,
     * or with status {@code 404 (Not Found)} if the fastFoodDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the fastFoodDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/fast-foods/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<FastFoodDTO>> partialUpdateFastFood(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FastFoodDTO fastFoodDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FastFood partially : {}, {}", id, fastFoodDTO);
        if (fastFoodDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fastFoodDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return fastFoodRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<FastFoodDTO> result = fastFoodService.partialUpdate(fastFoodDTO);

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
     * {@code GET  /fast-foods} : get all the fastFoods.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fastFoods in body.
     */
    @GetMapping("/fast-foods")
    public Mono<ResponseEntity<List<FastFoodDTO>>> getAllFastFoods(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of FastFoods");
        return fastFoodService
            .countAll()
            .zipWith(fastFoodService.findAll(pageable).collectList())
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
     * {@code GET  /fast-foods/:id} : get the "id" fastFood.
     *
     * @param id the id of the fastFoodDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fastFoodDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/fast-foods/{id}")
    public Mono<ResponseEntity<FastFoodDTO>> getFastFood(@PathVariable Long id) {
        log.debug("REST request to get FastFood : {}", id);
        Mono<FastFoodDTO> fastFoodDTO = fastFoodService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fastFoodDTO);
    }

    /**
     * {@code DELETE  /fast-foods/:id} : delete the "id" fastFood.
     *
     * @param id the id of the fastFoodDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/fast-foods/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteFastFood(@PathVariable Long id) {
        log.debug("REST request to delete FastFood : {}", id);
        return fastFoodService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
