package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Plat;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Plat entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlatRepository extends ReactiveCrudRepository<Plat, Long>, PlatRepositoryInternal {
    Flux<Plat> findAllBy(Pageable pageable);

    @Query("SELECT * FROM plat entity WHERE entity.menu_id = :id")
    Flux<Plat> findByMenu(Long id);

    @Query("SELECT * FROM plat entity WHERE entity.menu_id IS NULL")
    Flux<Plat> findAllWhereMenuIsNull();

    @Override
    <S extends Plat> Mono<S> save(S entity);

    @Override
    Flux<Plat> findAll();

    @Override
    Mono<Plat> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface PlatRepositoryInternal {
    <S extends Plat> Mono<S> save(S entity);

    Flux<Plat> findAllBy(Pageable pageable);

    Flux<Plat> findAll();

    Mono<Plat> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Plat> findAllBy(Pageable pageable, Criteria criteria);

}
