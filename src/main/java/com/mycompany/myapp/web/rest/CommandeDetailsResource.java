package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.CommandeDetailsRepository;
import com.mycompany.myapp.service.CommandeDetailsService;
import com.mycompany.myapp.service.dto.CommandeDetailsDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.CommandeDetails}.
 */
@RestController
@RequestMapping("/api")
public class CommandeDetailsResource {

    private final Logger log = LoggerFactory.getLogger(CommandeDetailsResource.class);

    private static final String ENTITY_NAME = "commandeDetails";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CommandeDetailsService commandeDetailsService;

    private final CommandeDetailsRepository commandeDetailsRepository;

    public CommandeDetailsResource(CommandeDetailsService commandeDetailsService, CommandeDetailsRepository commandeDetailsRepository) {
        this.commandeDetailsService = commandeDetailsService;
        this.commandeDetailsRepository = commandeDetailsRepository;
    }

    /**
     * {@code POST  /commande-details} : Create a new commandeDetails.
     *
     * @param commandeDetailsDTO the commandeDetailsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new commandeDetailsDTO, or with status {@code 400 (Bad Request)} if the commandeDetails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/commande-details")
    public Mono<ResponseEntity<CommandeDetailsDTO>> createCommandeDetails(@RequestBody CommandeDetailsDTO commandeDetailsDTO)
        throws URISyntaxException {
        log.debug("REST request to save CommandeDetails : {}", commandeDetailsDTO);
        if (commandeDetailsDTO.getId() != null) {
            throw new BadRequestAlertException("A new commandeDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return commandeDetailsService
            .save(commandeDetailsDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/commande-details/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /commande-details/:id} : Updates an existing commandeDetails.
     *
     * @param id the id of the commandeDetailsDTO to save.
     * @param commandeDetailsDTO the commandeDetailsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commandeDetailsDTO,
     * or with status {@code 400 (Bad Request)} if the commandeDetailsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the commandeDetailsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/commande-details/{id}")
    public Mono<ResponseEntity<CommandeDetailsDTO>> updateCommandeDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CommandeDetailsDTO commandeDetailsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CommandeDetails : {}, {}", id, commandeDetailsDTO);
        if (commandeDetailsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, commandeDetailsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return commandeDetailsRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return commandeDetailsService
                    .update(commandeDetailsDTO)
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
     * {@code PATCH  /commande-details/:id} : Partial updates given fields of an existing commandeDetails, field will ignore if it is null
     *
     * @param id the id of the commandeDetailsDTO to save.
     * @param commandeDetailsDTO the commandeDetailsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commandeDetailsDTO,
     * or with status {@code 400 (Bad Request)} if the commandeDetailsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the commandeDetailsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the commandeDetailsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/commande-details/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<CommandeDetailsDTO>> partialUpdateCommandeDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CommandeDetailsDTO commandeDetailsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CommandeDetails partially : {}, {}", id, commandeDetailsDTO);
        if (commandeDetailsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, commandeDetailsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return commandeDetailsRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<CommandeDetailsDTO> result = commandeDetailsService.partialUpdate(commandeDetailsDTO);

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
     * {@code GET  /commande-details} : get all the commandeDetails.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of commandeDetails in body.
     */
    @GetMapping("/commande-details")
    public Mono<ResponseEntity<List<CommandeDetailsDTO>>> getAllCommandeDetails(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of CommandeDetails");
        return commandeDetailsService
            .countAll()
            .zipWith(commandeDetailsService.findAll(pageable).collectList())
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
     * {@code GET  /commande-details/:id} : get the "id" commandeDetails.
     *
     * @param id the id of the commandeDetailsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the commandeDetailsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/commande-details/{id}")
    public Mono<ResponseEntity<CommandeDetailsDTO>> getCommandeDetails(@PathVariable Long id) {
        log.debug("REST request to get CommandeDetails : {}", id);
        Mono<CommandeDetailsDTO> commandeDetailsDTO = commandeDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(commandeDetailsDTO);
    }

    /**
     * {@code DELETE  /commande-details/:id} : delete the "id" commandeDetails.
     *
     * @param id the id of the commandeDetailsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/commande-details/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteCommandeDetails(@PathVariable Long id) {
        log.debug("REST request to delete CommandeDetails : {}", id);
        return commandeDetailsService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
