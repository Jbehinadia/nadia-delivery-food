package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.BoissonsDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Boissons}.
 */
public interface BoissonsService {
    /**
     * Save a boissons.
     *
     * @param boissonsDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<BoissonsDTO> save(BoissonsDTO boissonsDTO);

    /**
     * Updates a boissons.
     *
     * @param boissonsDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<BoissonsDTO> update(BoissonsDTO boissonsDTO);

    /**
     * Partially updates a boissons.
     *
     * @param boissonsDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<BoissonsDTO> partialUpdate(BoissonsDTO boissonsDTO);

    /**
     * Get all the boissons.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<BoissonsDTO> findAll(Pageable pageable);

    /**
     * Returns the number of boissons available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" boissons.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<BoissonsDTO> findOne(Long id);

    /**
     * Delete the "id" boissons.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
