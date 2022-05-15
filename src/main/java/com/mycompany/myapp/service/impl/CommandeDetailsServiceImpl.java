package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.CommandeDetails;
import com.mycompany.myapp.repository.CommandeDetailsRepository;
import com.mycompany.myapp.service.CommandeDetailsService;
import com.mycompany.myapp.service.dto.CommandeDetailsDTO;
import com.mycompany.myapp.service.mapper.CommandeDetailsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link CommandeDetails}.
 */
@Service
@Transactional
public class CommandeDetailsServiceImpl implements CommandeDetailsService {

    private final Logger log = LoggerFactory.getLogger(CommandeDetailsServiceImpl.class);

    private final CommandeDetailsRepository commandeDetailsRepository;

    private final CommandeDetailsMapper commandeDetailsMapper;

    public CommandeDetailsServiceImpl(CommandeDetailsRepository commandeDetailsRepository, CommandeDetailsMapper commandeDetailsMapper) {
        this.commandeDetailsRepository = commandeDetailsRepository;
        this.commandeDetailsMapper = commandeDetailsMapper;
    }

    @Override
    public Mono<CommandeDetailsDTO> save(CommandeDetailsDTO commandeDetailsDTO) {
        log.debug("Request to save CommandeDetails : {}", commandeDetailsDTO);
        return commandeDetailsRepository.save(commandeDetailsMapper.toEntity(commandeDetailsDTO)).map(commandeDetailsMapper::toDto);
    }

    @Override
    public Mono<CommandeDetailsDTO> update(CommandeDetailsDTO commandeDetailsDTO) {
        log.debug("Request to save CommandeDetails : {}", commandeDetailsDTO);
        return commandeDetailsRepository.save(commandeDetailsMapper.toEntity(commandeDetailsDTO)).map(commandeDetailsMapper::toDto);
    }

    @Override
    public Mono<CommandeDetailsDTO> partialUpdate(CommandeDetailsDTO commandeDetailsDTO) {
        log.debug("Request to partially update CommandeDetails : {}", commandeDetailsDTO);

        return commandeDetailsRepository
            .findById(commandeDetailsDTO.getId())
            .map(existingCommandeDetails -> {
                commandeDetailsMapper.partialUpdate(existingCommandeDetails, commandeDetailsDTO);

                return existingCommandeDetails;
            })
            .flatMap(commandeDetailsRepository::save)
            .map(commandeDetailsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<CommandeDetailsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CommandeDetails");
        return commandeDetailsRepository.findAllBy(pageable).map(commandeDetailsMapper::toDto);
    }

    public Mono<Long> countAll() {
        return commandeDetailsRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<CommandeDetailsDTO> findOne(Long id) {
        log.debug("Request to get CommandeDetails : {}", id);
        return commandeDetailsRepository.findById(id).map(commandeDetailsMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete CommandeDetails : {}", id);
        return commandeDetailsRepository.deleteById(id);
    }
}
