package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.CommandeDetailsDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.CommandeDetails}.
 */
public interface CommandeDetailsService {
    /**
     * Save a commandeDetails.
     *
     * @param commandeDetailsDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<CommandeDetailsDTO> save(CommandeDetailsDTO commandeDetailsDTO);

    /**
     * Updates a commandeDetails.
     *
     * @param commandeDetailsDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<CommandeDetailsDTO> update(CommandeDetailsDTO commandeDetailsDTO);

    /**
     * Partially updates a commandeDetails.
     *
     * @param commandeDetailsDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<CommandeDetailsDTO> partialUpdate(CommandeDetailsDTO commandeDetailsDTO);

    /**
     * Get all the commandeDetails.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<CommandeDetailsDTO> findAll(Pageable pageable);

    /**
     * Returns the number of commandeDetails available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" commandeDetails.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<CommandeDetailsDTO> findOne(Long id);

    /**
     * Delete the "id" commandeDetails.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
