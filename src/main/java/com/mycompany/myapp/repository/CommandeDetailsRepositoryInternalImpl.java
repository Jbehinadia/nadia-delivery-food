package com.mycompany.myapp.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import com.mycompany.myapp.domain.CommandeDetails;
import com.mycompany.myapp.repository.rowmapper.BoissonsRowMapper;
import com.mycompany.myapp.repository.rowmapper.CommandeDetailsRowMapper;
import com.mycompany.myapp.repository.rowmapper.CommandeRowMapper;
import com.mycompany.myapp.repository.rowmapper.DessertRowMapper;
import com.mycompany.myapp.repository.rowmapper.FastFoodRowMapper;
import com.mycompany.myapp.repository.rowmapper.PlatRowMapper;
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
 * Spring Data SQL reactive custom repository implementation for the CommandeDetails entity.
 */
@SuppressWarnings("unused")
class CommandeDetailsRepositoryInternalImpl
    extends SimpleR2dbcRepository<CommandeDetails, Long>
    implements CommandeDetailsRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final CommandeRowMapper commandeMapper;
    private final FastFoodRowMapper fastfoodMapper;
    private final PlatRowMapper platMapper;
    private final BoissonsRowMapper boissonsMapper;
    private final DessertRowMapper dessertMapper;
    private final CommandeDetailsRowMapper commandedetailsMapper;

    private static final Table entityTable = Table.aliased("commande_details", EntityManager.ENTITY_ALIAS);
    private static final Table commandeTable = Table.aliased("commande", "commande");
    private static final Table fastFoodTable = Table.aliased("fast_food", "fastFood");
    private static final Table platTable = Table.aliased("plat", "plat");
    private static final Table boissonsTable = Table.aliased("boissons", "boissons");
    private static final Table dessertTable = Table.aliased("dessert", "dessert");

    public CommandeDetailsRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        CommandeRowMapper commandeMapper,
        FastFoodRowMapper fastfoodMapper,
        PlatRowMapper platMapper,
        BoissonsRowMapper boissonsMapper,
        DessertRowMapper dessertMapper,
        CommandeDetailsRowMapper commandedetailsMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(CommandeDetails.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.commandeMapper = commandeMapper;
        this.fastfoodMapper = fastfoodMapper;
        this.platMapper = platMapper;
        this.boissonsMapper = boissonsMapper;
        this.dessertMapper = dessertMapper;
        this.commandedetailsMapper = commandedetailsMapper;
    }

    @Override
    public Flux<CommandeDetails> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<CommandeDetails> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = CommandeDetailsSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(CommandeSqlHelper.getColumns(commandeTable, "commande"));
        columns.addAll(FastFoodSqlHelper.getColumns(fastFoodTable, "fastFood"));
        columns.addAll(PlatSqlHelper.getColumns(platTable, "plat"));
        columns.addAll(BoissonsSqlHelper.getColumns(boissonsTable, "boissons"));
        columns.addAll(DessertSqlHelper.getColumns(dessertTable, "dessert"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(commandeTable)
            .on(Column.create("commande_id", entityTable))
            .equals(Column.create("id", commandeTable))
            .leftOuterJoin(fastFoodTable)
            .on(Column.create("fast_food_id", entityTable))
            .equals(Column.create("id", fastFoodTable))
            .leftOuterJoin(platTable)
            .on(Column.create("plat_id", entityTable))
            .equals(Column.create("id", platTable))
            .leftOuterJoin(boissonsTable)
            .on(Column.create("boissons_id", entityTable))
            .equals(Column.create("id", boissonsTable))
            .leftOuterJoin(dessertTable)
            .on(Column.create("dessert_id", entityTable))
            .equals(Column.create("id", dessertTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, CommandeDetails.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<CommandeDetails> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<CommandeDetails> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private CommandeDetails process(Row row, RowMetadata metadata) {
        CommandeDetails entity = commandedetailsMapper.apply(row, "e");
        entity.setCommande(commandeMapper.apply(row, "commande"));
        entity.setFastFood(fastfoodMapper.apply(row, "fastFood"));
        entity.setPlat(platMapper.apply(row, "plat"));
        entity.setBoissons(boissonsMapper.apply(row, "boissons"));
        entity.setDessert(dessertMapper.apply(row, "dessert"));
        return entity;
    }

    @Override
    public <S extends CommandeDetails> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
