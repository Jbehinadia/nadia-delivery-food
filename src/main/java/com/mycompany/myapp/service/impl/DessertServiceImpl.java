package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Dessert;
import com.mycompany.myapp.repository.DessertRepository;
import com.mycompany.myapp.service.DessertService;
import com.mycompany.myapp.service.dto.DessertDTO;
import com.mycompany.myapp.service.mapper.DessertMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Dessert}.
 */
@Service
@Transactional
public class DessertServiceImpl implements DessertService {

    private final Logger log = LoggerFactory.getLogger(DessertServiceImpl.class);

    private final DessertRepository dessertRepository;

    private final DessertMapper dessertMapper;

    public DessertServiceImpl(DessertRepository dessertRepository, DessertMapper dessertMapper) {
        this.dessertRepository = dessertRepository;
        this.dessertMapper = dessertMapper;
    }

    @Override
    public Mono<DessertDTO> save(DessertDTO dessertDTO) {
        log.debug("Request to save Dessert : {}", dessertDTO);
        return dessertRepository.save(dessertMapper.toEntity(dessertDTO)).map(dessertMapper::toDto);
    }

    @Override
    public Mono<DessertDTO> update(DessertDTO dessertDTO) {
        log.debug("Request to save Dessert : {}", dessertDTO);
        return dessertRepository.save(dessertMapper.toEntity(dessertDTO)).map(dessertMapper::toDto);
    }

    @Override
    public Mono<DessertDTO> partialUpdate(DessertDTO dessertDTO) {
        log.debug("Request to partially update Dessert : {}", dessertDTO);

        return dessertRepository
            .findById(dessertDTO.getId())
            .map(existingDessert -> {
                dessertMapper.partialUpdate(existingDessert, dessertDTO);

                return existingDessert;
            })
            .flatMap(dessertRepository::save)
            .map(dessertMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<DessertDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Desserts");
        return dessertRepository.findAllBy(pageable).map(dessertMapper::toDto);
    }

    public Mono<Long> countAll() {
        return dessertRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<DessertDTO> findOne(Long id) {
        log.debug("Request to get Dessert : {}", id);
        return dessertRepository.findById(id).map(dessertMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Dessert : {}", id);
        return dessertRepository.deleteById(id);
    }
}
