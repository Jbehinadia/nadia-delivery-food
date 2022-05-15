package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.BoissonsRepository;
import com.mycompany.myapp.service.BoissonsService;
import com.mycompany.myapp.service.dto.BoissonsDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Boissons}.
 */
@RestController
@RequestMapping("/api")
public class BoissonsResource {

    private final Logger log = LoggerFactory.getLogger(BoissonsResource.class);

    private static final String ENTITY_NAME = "boissons";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BoissonsService boissonsService;

    private final BoissonsRepository boissonsRepository;

    public BoissonsResource(BoissonsService boissonsService, BoissonsRepository boissonsRepository) {
        this.boissonsService = boissonsService;
        this.boissonsRepository = boissonsRepository;
    }

    /**
     * {@code POST  /boissons} : Create a new boissons.
     *
     * @param boissonsDTO the boissonsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new boissonsDTO, or with status {@code 400 (Bad Request)} if the boissons has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/boissons")
    public Mono<ResponseEntity<BoissonsDTO>> createBoissons(@RequestBody BoissonsDTO boissonsDTO) throws URISyntaxException {
        log.debug("REST request to save Boissons : {}", boissonsDTO);
        if (boissonsDTO.getId() != null) {
            throw new BadRequestAlertException("A new boissons cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return boissonsService
            .save(boissonsDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/boissons/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /boissons/:id} : Updates an existing boissons.
     *
     * @param id the id of the boissonsDTO to save.
     * @param boissonsDTO the boissonsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated boissonsDTO,
     * or with status {@code 400 (Bad Request)} if the boissonsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the boissonsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/boissons/{id}")
    public Mono<ResponseEntity<BoissonsDTO>> updateBoissons(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BoissonsDTO boissonsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Boissons : {}, {}", id, boissonsDTO);
        if (boissonsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, boissonsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return boissonsRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return boissonsService
                    .update(boissonsDTO)
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
     * {@code PATCH  /boissons/:id} : Partial updates given fields of an existing boissons, field will ignore if it is null
     *
     * @param id the id of the boissonsDTO to save.
     * @param boissonsDTO the boissonsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated boissonsDTO,
     * or with status {@code 400 (Bad Request)} if the boissonsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the boissonsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the boissonsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/boissons/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<BoissonsDTO>> partialUpdateBoissons(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BoissonsDTO boissonsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Boissons partially : {}, {}", id, boissonsDTO);
        if (boissonsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, boissonsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return boissonsRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<BoissonsDTO> result = boissonsService.partialUpdate(boissonsDTO);

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
     * {@code GET  /boissons} : get all the boissons.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of boissons in body.
     */
    @GetMapping("/boissons")
    public Mono<ResponseEntity<List<BoissonsDTO>>> getAllBoissons(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of Boissons");
        return boissonsService
            .countAll()
            .zipWith(boissonsService.findAll(pageable).collectList())
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
     * {@code GET  /boissons/:id} : get the "id" boissons.
     *
     * @param id the id of the boissonsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the boissonsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/boissons/{id}")
    public Mono<ResponseEntity<BoissonsDTO>> getBoissons(@PathVariable Long id) {
        log.debug("REST request to get Boissons : {}", id);
        Mono<BoissonsDTO> boissonsDTO = boissonsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(boissonsDTO);
    }

    /**
     * {@code DELETE  /boissons/:id} : delete the "id" boissons.
     *
     * @param id the id of the boissonsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/boissons/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteBoissons(@PathVariable Long id) {
        log.debug("REST request to delete Boissons : {}", id);
        return boissonsService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
