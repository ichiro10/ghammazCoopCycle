package ghammaz.coopcycle.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import ghammaz.coopcycle.domain.Produit;
import ghammaz.coopcycle.repository.rowmapper.ClientRowMapper;
import ghammaz.coopcycle.repository.rowmapper.LivraisonRowMapper;
import ghammaz.coopcycle.repository.rowmapper.ProduitRowMapper;
import ghammaz.coopcycle.repository.rowmapper.RestaurantRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.BiFunction;
import org.apache.commons.lang3.StringUtils;
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
 * Spring Data R2DBC custom repository implementation for the Produit entity.
 */
@SuppressWarnings("unused")
class ProduitRepositoryInternalImpl extends SimpleR2dbcRepository<Produit, Long> implements ProduitRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final LivraisonRowMapper livraisonMapper;
    private final ClientRowMapper clientMapper;
    private final RestaurantRowMapper restaurantMapper;
    private final ProduitRowMapper produitMapper;

    private static final Table entityTable = Table.aliased("produit", EntityManager.ENTITY_ALIAS);
    private static final Table livraisonTable = Table.aliased("livraison", "livraison");
    private static final Table clientTable = Table.aliased("client", "client");
    private static final Table restaurantTable = Table.aliased("restaurant", "restaurant");

    public ProduitRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        LivraisonRowMapper livraisonMapper,
        ClientRowMapper clientMapper,
        RestaurantRowMapper restaurantMapper,
        ProduitRowMapper produitMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Produit.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.livraisonMapper = livraisonMapper;
        this.clientMapper = clientMapper;
        this.restaurantMapper = restaurantMapper;
        this.produitMapper = produitMapper;
    }

    @Override
    public Flux<Produit> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Produit> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = ProduitSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(LivraisonSqlHelper.getColumns(livraisonTable, "livraison"));
        columns.addAll(ClientSqlHelper.getColumns(clientTable, "client"));
        columns.addAll(RestaurantSqlHelper.getColumns(restaurantTable, "restaurant"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(livraisonTable)
            .on(Column.create("livraison_id", entityTable))
            .equals(Column.create("id", livraisonTable))
            .leftOuterJoin(clientTable)
            .on(Column.create("client_id", entityTable))
            .equals(Column.create("id", clientTable))
            .leftOuterJoin(restaurantTable)
            .on(Column.create("restaurant_id", entityTable))
            .equals(Column.create("id", restaurantTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Produit.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Produit> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Produit> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private Produit process(Row row, RowMetadata metadata) {
        Produit entity = produitMapper.apply(row, "e");
        entity.setLivraison(livraisonMapper.apply(row, "livraison"));
        entity.setClient(clientMapper.apply(row, "client"));
        entity.setRestaurant(restaurantMapper.apply(row, "restaurant"));
        return entity;
    }

    @Override
    public <S extends Produit> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
