package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.FastFood;
import com.mycompany.myapp.repository.FastFoodRepository;
import com.mycompany.myapp.service.FastFoodService;
import com.mycompany.myapp.service.dto.FastFoodDTO;
import com.mycompany.myapp.service.mapper.FastFoodMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link FastFood}.
 */
@Service
@Transactional
public class FastFoodServiceImpl implements FastFoodService {

    private final Logger log = LoggerFactory.getLogger(FastFoodServiceImpl.class);

    private final FastFoodRepository fastFoodRepository;

    private final FastFoodMapper fastFoodMapper;

    public FastFoodServiceImpl(FastFoodRepository fastFoodRepository, FastFoodMapper fastFoodMapper) {
        this.fastFoodRepository = fastFoodRepository;
        this.fastFoodMapper = fastFoodMapper;
    }

    @Override
    public Mono<FastFoodDTO> save(FastFoodDTO fastFoodDTO) {
        log.debug("Request to save FastFood : {}", fastFoodDTO);
        return fastFoodRepository.save(fastFoodMapper.toEntity(fastFoodDTO)).map(fastFoodMapper::toDto);
    }

    @Override
    public Mono<FastFoodDTO> update(FastFoodDTO fastFoodDTO) {
        log.debug("Request to save FastFood : {}", fastFoodDTO);
        return fastFoodRepository.save(fastFoodMapper.toEntity(fastFoodDTO)).map(fastFoodMapper::toDto);
    }

    @Override
    public Mono<FastFoodDTO> partialUpdate(FastFoodDTO fastFoodDTO) {
        log.debug("Request to partially update FastFood : {}", fastFoodDTO);

        return fastFoodRepository
            .findById(fastFoodDTO.getId())
            .map(existingFastFood -> {
                fastFoodMapper.partialUpdate(existingFastFood, fastFoodDTO);

                return existingFastFood;
            })
            .flatMap(fastFoodRepository::save)
            .map(fastFoodMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<FastFoodDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FastFoods");
        return fastFoodRepository.findAllBy(pageable).map(fastFoodMapper::toDto);
    }

    public Mono<Long> countAll() {
        return fastFoodRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<FastFoodDTO> findOne(Long id) {
        log.debug("Request to get FastFood : {}", id);
        return fastFoodRepository.findById(id).map(fastFoodMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete FastFood : {}", id);
        return fastFoodRepository.deleteById(id);
    }
}
