package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.FastFoodDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.FastFood}.
 */
public interface FastFoodService {
    /**
     * Save a fastFood.
     *
     * @param fastFoodDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<FastFoodDTO> save(FastFoodDTO fastFoodDTO);

    /**
     * Updates a fastFood.
     *
     * @param fastFoodDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<FastFoodDTO> update(FastFoodDTO fastFoodDTO);

    /**
     * Partially updates a fastFood.
     *
     * @param fastFoodDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<FastFoodDTO> partialUpdate(FastFoodDTO fastFoodDTO);

    /**
     * Get all the fastFoods.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<FastFoodDTO> findAll(Pageable pageable);

    /**
     * Returns the number of fastFoods available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" fastFood.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<FastFoodDTO> findOne(Long id);

    /**
     * Delete the "id" fastFood.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
