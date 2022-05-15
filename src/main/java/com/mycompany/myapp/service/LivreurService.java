package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.LivreurDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Livreur}.
 */
public interface LivreurService {
    /**
     * Save a livreur.
     *
     * @param livreurDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<LivreurDTO> save(LivreurDTO livreurDTO);

    /**
     * Updates a livreur.
     *
     * @param livreurDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<LivreurDTO> update(LivreurDTO livreurDTO);

    /**
     * Partially updates a livreur.
     *
     * @param livreurDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<LivreurDTO> partialUpdate(LivreurDTO livreurDTO);

    /**
     * Get all the livreurs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<LivreurDTO> findAll(Pageable pageable);

    /**
     * Returns the number of livreurs available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" livreur.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<LivreurDTO> findOne(Long id);

    /**
     * Delete the "id" livreur.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
