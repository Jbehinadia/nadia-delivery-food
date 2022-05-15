package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.FastFood;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the FastFood entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FastFoodRepository extends ReactiveCrudRepository<FastFood, Long>, FastFoodRepositoryInternal {
    Flux<FastFood> findAllBy(Pageable pageable);

    @Query("SELECT * FROM fast_food entity WHERE entity.menu_id = :id")
    Flux<FastFood> findByMenu(Long id);

    @Query("SELECT * FROM fast_food entity WHERE entity.menu_id IS NULL")
    Flux<FastFood> findAllWhereMenuIsNull();

    @Override
    <S extends FastFood> Mono<S> save(S entity);

    @Override
    Flux<FastFood> findAll();

    @Override
    Mono<FastFood> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface FastFoodRepositoryInternal {
    <S extends FastFood> Mono<S> save(S entity);

    Flux<FastFood> findAllBy(Pageable pageable);

    Flux<FastFood> findAll();

    Mono<FastFood> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<FastFood> findAllBy(Pageable pageable, Criteria criteria);

}
