package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Boissons;
import com.mycompany.myapp.repository.BoissonsRepository;
import com.mycompany.myapp.service.BoissonsService;
import com.mycompany.myapp.service.dto.BoissonsDTO;
import com.mycompany.myapp.service.mapper.BoissonsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Boissons}.
 */
@Service
@Transactional
public class BoissonsServiceImpl implements BoissonsService {

    private final Logger log = LoggerFactory.getLogger(BoissonsServiceImpl.class);

    private final BoissonsRepository boissonsRepository;

    private final BoissonsMapper boissonsMapper;

    public BoissonsServiceImpl(BoissonsRepository boissonsRepository, BoissonsMapper boissonsMapper) {
        this.boissonsRepository = boissonsRepository;
        this.boissonsMapper = boissonsMapper;
    }

    @Override
    public Mono<BoissonsDTO> save(BoissonsDTO boissonsDTO) {
        log.debug("Request to save Boissons : {}", boissonsDTO);
        return boissonsRepository.save(boissonsMapper.toEntity(boissonsDTO)).map(boissonsMapper::toDto);
    }

    @Override
    public Mono<BoissonsDTO> update(BoissonsDTO boissonsDTO) {
        log.debug("Request to save Boissons : {}", boissonsDTO);
        return boissonsRepository.save(boissonsMapper.toEntity(boissonsDTO)).map(boissonsMapper::toDto);
    }

    @Override
    public Mono<BoissonsDTO> partialUpdate(BoissonsDTO boissonsDTO) {
        log.debug("Request to partially update Boissons : {}", boissonsDTO);

        return boissonsRepository
            .findById(boissonsDTO.getId())
            .map(existingBoissons -> {
                boissonsMapper.partialUpdate(existingBoissons, boissonsDTO);

                return existingBoissons;
            })
            .flatMap(boissonsRepository::save)
            .map(boissonsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<BoissonsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Boissons");
        return boissonsRepository.findAllBy(pageable).map(boissonsMapper::toDto);
    }

    public Mono<Long> countAll() {
        return boissonsRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<BoissonsDTO> findOne(Long id) {
        log.debug("Request to get Boissons : {}", id);
        return boissonsRepository.findById(id).map(boissonsMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Boissons : {}", id);
        return boissonsRepository.deleteById(id);
    }
}
