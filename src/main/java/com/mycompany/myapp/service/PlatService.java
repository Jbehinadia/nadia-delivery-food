package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.PlatDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Plat}.
 */
public interface PlatService {
    /**
     * Save a plat.
     *
     * @param platDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<PlatDTO> save(PlatDTO platDTO);

    /**
     * Updates a plat.
     *
     * @param platDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<PlatDTO> update(PlatDTO platDTO);

    /**
     * Partially updates a plat.
     *
     * @param platDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<PlatDTO> partialUpdate(PlatDTO platDTO);

    /**
     * Get all the plats.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<PlatDTO> findAll(Pageable pageable);

    /**
     * Returns the number of plats available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" plat.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<PlatDTO> findOne(Long id);

    /**
     * Delete the "id" plat.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
