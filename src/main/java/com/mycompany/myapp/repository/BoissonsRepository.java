package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Boissons;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Boissons entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BoissonsRepository extends ReactiveCrudRepository<Boissons, Long>, BoissonsRepositoryInternal {
    Flux<Boissons> findAllBy(Pageable pageable);

    @Query("SELECT * FROM boissons entity WHERE entity.menu_id = :id")
    Flux<Boissons> findByMenu(Long id);

    @Query("SELECT * FROM boissons entity WHERE entity.menu_id IS NULL")
    Flux<Boissons> findAllWhereMenuIsNull();

    @Override
    <S extends Boissons> Mono<S> save(S entity);

    @Override
    Flux<Boissons> findAll();

    @Override
    Mono<Boissons> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface BoissonsRepositoryInternal {
    <S extends Boissons> Mono<S> save(S entity);

    Flux<Boissons> findAllBy(Pageable pageable);

    Flux<Boissons> findAll();

    Mono<Boissons> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Boissons> findAllBy(Pageable pageable, Criteria criteria);

}
