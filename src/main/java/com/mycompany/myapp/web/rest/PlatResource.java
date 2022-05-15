package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.PlatRepository;
import com.mycompany.myapp.service.PlatService;
import com.mycompany.myapp.service.dto.PlatDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Plat}.
 */
@RestController
@RequestMapping("/api")
public class PlatResource {

    private final Logger log = LoggerFactory.getLogger(PlatResource.class);

    private static final String ENTITY_NAME = "plat";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PlatService platService;

    private final PlatRepository platRepository;

    public PlatResource(PlatService platService, PlatRepository platRepository) {
        this.platService = platService;
        this.platRepository = platRepository;
    }

    /**
     * {@code POST  /plats} : Create a new plat.
     *
     * @param platDTO the platDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new platDTO, or with status {@code 400 (Bad Request)} if the plat has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/plats")
    public Mono<ResponseEntity<PlatDTO>> createPlat(@RequestBody PlatDTO platDTO) throws URISyntaxException {
        log.debug("REST request to save Plat : {}", platDTO);
        if (platDTO.getId() != null) {
            throw new BadRequestAlertException("A new plat cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return platService
            .save(platDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/plats/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /plats/:id} : Updates an existing plat.
     *
     * @param id the id of the platDTO to save.
     * @param platDTO the platDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated platDTO,
     * or with status {@code 400 (Bad Request)} if the platDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the platDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/plats/{id}")
    public Mono<ResponseEntity<PlatDTO>> updatePlat(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PlatDTO platDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Plat : {}, {}", id, platDTO);
        if (platDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, platDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return platRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return platService
                    .update(platDTO)
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
     * {@code PATCH  /plats/:id} : Partial updates given fields of an existing plat, field will ignore if it is null
     *
     * @param id the id of the platDTO to save.
     * @param platDTO the platDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated platDTO,
     * or with status {@code 400 (Bad Request)} if the platDTO is not valid,
     * or with status {@code 404 (Not Found)} if the platDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the platDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/plats/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<PlatDTO>> partialUpdatePlat(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PlatDTO platDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Plat partially : {}, {}", id, platDTO);
        if (platDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, platDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return platRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<PlatDTO> result = platService.partialUpdate(platDTO);

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
     * {@code GET  /plats} : get all the plats.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of plats in body.
     */
    @GetMapping("/plats")
    public Mono<ResponseEntity<List<PlatDTO>>> getAllPlats(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of Plats");
        return platService
            .countAll()
            .zipWith(platService.findAll(pageable).collectList())
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
     * {@code GET  /plats/:id} : get the "id" plat.
     *
     * @param id the id of the platDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the platDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/plats/{id}")
    public Mono<ResponseEntity<PlatDTO>> getPlat(@PathVariable Long id) {
        log.debug("REST request to get Plat : {}", id);
        Mono<PlatDTO> platDTO = platService.findOne(id);
        return ResponseUtil.wrapOrNotFound(platDTO);
    }

    /**
     * {@code DELETE  /plats/:id} : delete the "id" plat.
     *
     * @param id the id of the platDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/plats/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deletePlat(@PathVariable Long id) {
        log.debug("REST request to delete Plat : {}", id);
        return platService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
