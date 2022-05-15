package com.mycompany.myapp.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import com.mycompany.myapp.domain.FastFood;
import com.mycompany.myapp.repository.rowmapper.FastFoodRowMapper;
import com.mycompany.myapp.repository.rowmapper.MenuRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.BiFunction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Comparison;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Conditions;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive custom repository implementation for the FastFood entity.
 */
@SuppressWarnings("unused")
class FastFoodRepositoryInternalImpl extends SimpleR2dbcRepository<FastFood, Long> implements FastFoodRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final MenuRowMapper menuMapper;
    private final FastFoodRowMapper fastfoodMapper;

    private static final Table entityTable = Table.aliased("fast_food", EntityManager.ENTITY_ALIAS);
    private static final Table menuTable = Table.aliased("menu", "menu");

    public FastFoodRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        MenuRowMapper menuMapper,
        FastFoodRowMapper fastfoodMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(FastFood.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.menuMapper = menuMapper;
        this.fastfoodMapper = fastfoodMapper;
    }

    @Override
    public Flux<FastFood> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<FastFood> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = FastFoodSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(MenuSqlHelper.getColumns(menuTable, "menu"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(menuTable)
            .on(Column.create("menu_id", entityTable))
            .equals(Column.create("id", menuTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, FastFood.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<FastFood> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<FastFood> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private FastFood process(Row row, RowMetadata metadata) {
        FastFood entity = fastfoodMapper.apply(row, "e");
        entity.setMenu(menuMapper.apply(row, "menu"));
        return entity;
    }

    @Override
    public <S extends FastFood> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
