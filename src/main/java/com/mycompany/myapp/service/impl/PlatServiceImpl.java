package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Plat;
import com.mycompany.myapp.repository.PlatRepository;
import com.mycompany.myapp.service.PlatService;
import com.mycompany.myapp.service.dto.PlatDTO;
import com.mycompany.myapp.service.mapper.PlatMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Plat}.
 */
@Service
@Transactional
public class PlatServiceImpl implements PlatService {

    private final Logger log = LoggerFactory.getLogger(PlatServiceImpl.class);

    private final PlatRepository platRepository;

    private final PlatMapper platMapper;

    public PlatServiceImpl(PlatRepository platRepository, PlatMapper platMapper) {
        this.platRepository = platRepository;
        this.platMapper = platMapper;
    }

    @Override
    public Mono<PlatDTO> save(PlatDTO platDTO) {
        log.debug("Request to save Plat : {}", platDTO);
        return platRepository.save(platMapper.toEntity(platDTO)).map(platMapper::toDto);
    }

    @Override
    public Mono<PlatDTO> update(PlatDTO platDTO) {
        log.debug("Request to save Plat : {}", platDTO);
        return platRepository.save(platMapper.toEntity(platDTO)).map(platMapper::toDto);
    }

    @Override
    public Mono<PlatDTO> partialUpdate(PlatDTO platDTO) {
        log.debug("Request to partially update Plat : {}", platDTO);

        return platRepository
            .findById(platDTO.getId())
            .map(existingPlat -> {
                platMapper.partialUpdate(existingPlat, platDTO);

                return existingPlat;
            })
            .flatMap(platRepository::save)
            .map(platMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<PlatDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Plats");
        return platRepository.findAllBy(pageable).map(platMapper::toDto);
    }

    public Mono<Long> countAll() {
        return platRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<PlatDTO> findOne(Long id) {
        log.debug("Request to get Plat : {}", id);
        return platRepository.findById(id).map(platMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Plat : {}", id);
        return platRepository.deleteById(id);
    }
}
