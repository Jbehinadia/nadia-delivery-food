package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.ResponsableRestaurant;
import com.mycompany.myapp.repository.ResponsableRestaurantRepository;
import com.mycompany.myapp.service.ResponsableRestaurantService;
import com.mycompany.myapp.service.dto.ResponsableRestaurantDTO;
import com.mycompany.myapp.service.mapper.ResponsableRestaurantMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link ResponsableRestaurant}.
 */
@Service
@Transactional
public class ResponsableRestaurantServiceImpl implements ResponsableRestaurantService {

    private final Logger log = LoggerFactory.getLogger(ResponsableRestaurantServiceImpl.class);

    private final ResponsableRestaurantRepository responsableRestaurantRepository;

    private final ResponsableRestaurantMapper responsableRestaurantMapper;

    public ResponsableRestaurantServiceImpl(
        ResponsableRestaurantRepository responsableRestaurantRepository,
        ResponsableRestaurantMapper responsableRestaurantMapper
    ) {
        this.responsableRestaurantRepository = responsableRestaurantRepository;
        this.responsableRestaurantMapper = responsableRestaurantMapper;
    }

    @Override
    public Mono<ResponsableRestaurantDTO> save(ResponsableRestaurantDTO responsableRestaurantDTO) {
        log.debug("Request to save ResponsableRestaurant : {}", responsableRestaurantDTO);
        return responsableRestaurantRepository
            .save(responsableRestaurantMapper.toEntity(responsableRestaurantDTO))
            .map(responsableRestaurantMapper::toDto);
    }

    @Override
    public Mono<ResponsableRestaurantDTO> update(ResponsableRestaurantDTO responsableRestaurantDTO) {
        log.debug("Request to save ResponsableRestaurant : {}", responsableRestaurantDTO);
        return responsableRestaurantRepository
            .save(responsableRestaurantMapper.toEntity(responsableRestaurantDTO))
            .map(responsableRestaurantMapper::toDto);
    }

    @Override
    public Mono<ResponsableRestaurantDTO> partialUpdate(ResponsableRestaurantDTO responsableRestaurantDTO) {
        log.debug("Request to partially update ResponsableRestaurant : {}", responsableRestaurantDTO);

        return responsableRestaurantRepository
            .findById(responsableRestaurantDTO.getId())
            .map(existingResponsableRestaurant -> {
                responsableRestaurantMapper.partialUpdate(existingResponsableRestaurant, responsableRestaurantDTO);

                return existingResponsableRestaurant;
            })
            .flatMap(responsableRestaurantRepository::save)
            .map(responsableRestaurantMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<ResponsableRestaurantDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ResponsableRestaurants");
        return responsableRestaurantRepository.findAllBy(pageable).map(responsableRestaurantMapper::toDto);
    }

    public Mono<Long> countAll() {
        return responsableRestaurantRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<ResponsableRestaurantDTO> findOne(Long id) {
        log.debug("Request to get ResponsableRestaurant : {}", id);
        return responsableRestaurantRepository.findById(id).map(responsableRestaurantMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete ResponsableRestaurant : {}", id);
        return responsableRestaurantRepository.deleteById(id);
    }
}
