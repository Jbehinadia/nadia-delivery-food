package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.ResponsableRestaurantDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.ResponsableRestaurant}.
 */
public interface ResponsableRestaurantService {
    /**
     * Save a responsableRestaurant.
     *
     * @param responsableRestaurantDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<ResponsableRestaurantDTO> save(ResponsableRestaurantDTO responsableRestaurantDTO);

    /**
     * Updates a responsableRestaurant.
     *
     * @param responsableRestaurantDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<ResponsableRestaurantDTO> update(ResponsableRestaurantDTO responsableRestaurantDTO);

    /**
     * Partially updates a responsableRestaurant.
     *
     * @param responsableRestaurantDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<ResponsableRestaurantDTO> partialUpdate(ResponsableRestaurantDTO responsableRestaurantDTO);

    /**
     * Get all the responsableRestaurants.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<ResponsableRestaurantDTO> findAll(Pageable pageable);

    /**
     * Returns the number of responsableRestaurants available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" responsableRestaurant.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<ResponsableRestaurantDTO> findOne(Long id);

    /**
     * Delete the "id" responsableRestaurant.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
