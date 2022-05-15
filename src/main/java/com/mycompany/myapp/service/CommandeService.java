package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.CommandeDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Commande}.
 */
public interface CommandeService {
    /**
     * Save a commande.
     *
     * @param commandeDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<CommandeDTO> save(CommandeDTO commandeDTO);

    /**
     * Updates a commande.
     *
     * @param commandeDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<CommandeDTO> update(CommandeDTO commandeDTO);

    /**
     * Partially updates a commande.
     *
     * @param commandeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<CommandeDTO> partialUpdate(CommandeDTO commandeDTO);

    /**
     * Get all the commandes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<CommandeDTO> findAll(Pageable pageable);

    /**
     * Returns the number of commandes available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" commande.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<CommandeDTO> findOne(Long id);

    /**
     * Delete the "id" commande.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
