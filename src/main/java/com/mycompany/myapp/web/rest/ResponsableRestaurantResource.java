package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.ResponsableRestaurantRepository;
import com.mycompany.myapp.service.ResponsableRestaurantService;
import com.mycompany.myapp.service.dto.ResponsableRestaurantDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.ResponsableRestaurant}.
 */
@RestController
@RequestMapping("/api")
public class ResponsableRestaurantResource {

    private final Logger log = LoggerFactory.getLogger(ResponsableRestaurantResource.class);

    private static final String ENTITY_NAME = "responsableRestaurant";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ResponsableRestaurantService responsableRestaurantService;

    private final ResponsableRestaurantRepository responsableRestaurantRepository;

    public ResponsableRestaurantResource(
        ResponsableRestaurantService responsableRestaurantService,
        ResponsableRestaurantRepository responsableRestaurantRepository
    ) {
        this.responsableRestaurantService = responsableRestaurantService;
        this.responsableRestaurantRepository = responsableRestaurantRepository;
    }

    /**
     * {@code POST  /responsable-restaurants} : Create a new responsableRestaurant.
     *
     * @param responsableRestaurantDTO the responsableRestaurantDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new responsableRestaurantDTO, or with status {@code 400 (Bad Request)} if the responsableRestaurant has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/responsable-restaurants")
    public Mono<ResponseEntity<ResponsableRestaurantDTO>> createResponsableRestaurant(
        @RequestBody ResponsableRestaurantDTO responsableRestaurantDTO
    ) throws URISyntaxException {
        log.debug("REST request to save ResponsableRestaurant : {}", responsableRestaurantDTO);
        if (responsableRestaurantDTO.getId() != null) {
            throw new BadRequestAlertException("A new responsableRestaurant cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return responsableRestaurantService
            .save(responsableRestaurantDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/responsable-restaurants/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /responsable-restaurants/:id} : Updates an existing responsableRestaurant.
     *
     * @param id the id of the responsableRestaurantDTO to save.
     * @param responsableRestaurantDTO the responsableRestaurantDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated responsableRestaurantDTO,
     * or with status {@code 400 (Bad Request)} if the responsableRestaurantDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the responsableRestaurantDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/responsable-restaurants/{id}")
    public Mono<ResponseEntity<ResponsableRestaurantDTO>> updateResponsableRestaurant(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ResponsableRestaurantDTO responsableRestaurantDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ResponsableRestaurant : {}, {}", id, responsableRestaurantDTO);
        if (responsableRestaurantDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, responsableRestaurantDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return responsableRestaurantRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return responsableRestaurantService
                    .update(responsableRestaurantDTO)
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
     * {@code PATCH  /responsable-restaurants/:id} : Partial updates given fields of an existing responsableRestaurant, field will ignore if it is null
     *
     * @param id the id of the responsableRestaurantDTO to save.
     * @param responsableRestaurantDTO the responsableRestaurantDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated responsableRestaurantDTO,
     * or with status {@code 400 (Bad Request)} if the responsableRestaurantDTO is not valid,
     * or with status {@code 404 (Not Found)} if the responsableRestaurantDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the responsableRestaurantDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/responsable-restaurants/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ResponsableRestaurantDTO>> partialUpdateResponsableRestaurant(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ResponsableRestaurantDTO responsableRestaurantDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ResponsableRestaurant partially : {}, {}", id, responsableRestaurantDTO);
        if (responsableRestaurantDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, responsableRestaurantDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return responsableRestaurantRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ResponsableRestaurantDTO> result = responsableRestaurantService.partialUpdate(responsableRestaurantDTO);

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
     * {@code GET  /responsable-restaurants} : get all the responsableRestaurants.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of responsableRestaurants in body.
     */
    @GetMapping("/responsable-restaurants")
    public Mono<ResponseEntity<List<ResponsableRestaurantDTO>>> getAllResponsableRestaurants(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of ResponsableRestaurants");
        return responsableRestaurantService
            .countAll()
            .zipWith(responsableRestaurantService.findAll(pageable).collectList())
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
     * {@code GET  /responsable-restaurants/:id} : get the "id" responsableRestaurant.
     *
     * @param id the id of the responsableRestaurantDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the responsableRestaurantDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/responsable-restaurants/{id}")
    public Mono<ResponseEntity<ResponsableRestaurantDTO>> getResponsableRestaurant(@PathVariable Long id) {
        log.debug("REST request to get ResponsableRestaurant : {}", id);
        Mono<ResponsableRestaurantDTO> responsableRestaurantDTO = responsableRestaurantService.findOne(id);
        return ResponseUtil.wrapOrNotFound(responsableRestaurantDTO);
    }

    /**
     * {@code DELETE  /responsable-restaurants/:id} : delete the "id" responsableRestaurant.
     *
     * @param id the id of the responsableRestaurantDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/responsable-restaurants/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteResponsableRestaurant(@PathVariable Long id) {
        log.debug("REST request to delete ResponsableRestaurant : {}", id);
        return responsableRestaurantService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
