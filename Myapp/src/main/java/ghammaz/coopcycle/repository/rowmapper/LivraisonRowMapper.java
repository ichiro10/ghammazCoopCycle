package ghammaz.coopcycle.repository.rowmapper;

import ghammaz.coopcycle.domain.Livraison;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Livraison}, with proper type conversions.
 */
@Service
public class LivraisonRowMapper implements BiFunction<Row, String, Livraison> {

    private final ColumnConverter converter;

    public LivraisonRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Livraison} stored in the database.
     */
    @Override
    public Livraison apply(Row row, String prefix) {
        Livraison entity = new Livraison();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setIdLivraison(converter.fromRow(row, prefix + "_id_livraison", Integer.class));
        entity.setPrixLivraison(converter.fromRow(row, prefix + "_prix_livraison", Float.class));
        entity.setDuree(converter.fromRow(row, prefix + "_duree", Instant.class));
        entity.setAdresseLivraison(converter.fromRow(row, prefix + "_adresse_livraison", String.class));
        entity.setClientId(converter.fromRow(row, prefix + "_client_id", Long.class));
        entity.setLivreurId(converter.fromRow(row, prefix + "_livreur_id", Long.class));
        return entity;
    }
}
