package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.DessertDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Dessert}.
 */
public interface DessertService {
    /**
     * Save a dessert.
     *
     * @param dessertDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<DessertDTO> save(DessertDTO dessertDTO);

    /**
     * Updates a dessert.
     *
     * @param dessertDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<DessertDTO> update(DessertDTO dessertDTO);

    /**
     * Partially updates a dessert.
     *
     * @param dessertDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<DessertDTO> partialUpdate(DessertDTO dessertDTO);

    /**
     * Get all the desserts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<DessertDTO> findAll(Pageable pageable);

    /**
     * Returns the number of desserts available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" dessert.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<DessertDTO> findOne(Long id);

    /**
     * Delete the "id" dessert.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
