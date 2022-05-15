package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Dessert;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Dessert entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DessertRepository extends ReactiveCrudRepository<Dessert, Long>, DessertRepositoryInternal {
    Flux<Dessert> findAllBy(Pageable pageable);

    @Query("SELECT * FROM dessert entity WHERE entity.menu_id = :id")
    Flux<Dessert> findByMenu(Long id);

    @Query("SELECT * FROM dessert entity WHERE entity.menu_id IS NULL")
    Flux<Dessert> findAllWhereMenuIsNull();

    @Override
    <S extends Dessert> Mono<S> save(S entity);

    @Override
    Flux<Dessert> findAll();

    @Override
    Mono<Dessert> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface DessertRepositoryInternal {
    <S extends Dessert> Mono<S> save(S entity);

    Flux<Dessert> findAllBy(Pageable pageable);

    Flux<Dessert> findAll();

    Mono<Dessert> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Dessert> findAllBy(Pageable pageable, Criteria criteria);

}
