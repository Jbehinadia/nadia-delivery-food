package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Commande;
import com.mycompany.myapp.repository.CommandeRepository;
import com.mycompany.myapp.service.CommandeService;
import com.mycompany.myapp.service.dto.CommandeDTO;
import com.mycompany.myapp.service.mapper.CommandeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Commande}.
 */
@Service
@Transactional
public class CommandeServiceImpl implements CommandeService {

    private final Logger log = LoggerFactory.getLogger(CommandeServiceImpl.class);

    private final CommandeRepository commandeRepository;

    private final CommandeMapper commandeMapper;

    public CommandeServiceImpl(CommandeRepository commandeRepository, CommandeMapper commandeMapper) {
        this.commandeRepository = commandeRepository;
        this.commandeMapper = commandeMapper;
    }

    @Override
    public Mono<CommandeDTO> save(CommandeDTO commandeDTO) {
        log.debug("Request to save Commande : {}", commandeDTO);
        return commandeRepository.save(commandeMapper.toEntity(commandeDTO)).map(commandeMapper::toDto);
    }

    @Override
    public Mono<CommandeDTO> update(CommandeDTO commandeDTO) {
        log.debug("Request to save Commande : {}", commandeDTO);
        return commandeRepository.save(commandeMapper.toEntity(commandeDTO)).map(commandeMapper::toDto);
    }

    @Override
    public Mono<CommandeDTO> partialUpdate(CommandeDTO commandeDTO) {
        log.debug("Request to partially update Commande : {}", commandeDTO);

        return commandeRepository
            .findById(commandeDTO.getId())
            .map(existingCommande -> {
                commandeMapper.partialUpdate(existingCommande, commandeDTO);

                return existingCommande;
            })
            .flatMap(commandeRepository::save)
            .map(commandeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<CommandeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Commandes");
        return commandeRepository.findAllBy(pageable).map(commandeMapper::toDto);
    }

    public Mono<Long> countAll() {
        return commandeRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<CommandeDTO> findOne(Long id) {
        log.debug("Request to get Commande : {}", id);
        return commandeRepository.findById(id).map(commandeMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Commande : {}", id);
        return commandeRepository.deleteById(id);
    }
}
