package ghammaz.coopcycle.repository.rowmapper;

import ghammaz.coopcycle.domain.Produit;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Produit}, with proper type conversions.
 */
@Service
public class ProduitRowMapper implements BiFunction<Row, String, Produit> {

    private final ColumnConverter converter;

    public ProduitRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Produit} stored in the database.
     */
    @Override
    public Produit apply(Row row, String prefix) {
        Produit entity = new Produit();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setIdProd(converter.fromRow(row, prefix + "_id_prod", Integer.class));
        entity.setNomProd(converter.fromRow(row, prefix + "_nom_prod", String.class));
        entity.setPrixProd(converter.fromRow(row, prefix + "_prix_prod", Float.class));
        entity.setLivraisonId(converter.fromRow(row, prefix + "_livraison_id", Long.class));
        entity.setClientId(converter.fromRow(row, prefix + "_client_id", Long.class));
        entity.setRestaurantId(converter.fromRow(row, prefix + "_restaurant_id", Long.class));
        return entity;
    }
}
