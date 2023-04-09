package ghammaz.coopcycle.repository.rowmapper;

import ghammaz.coopcycle.domain.Restaurant;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Restaurant}, with proper type conversions.
 */
@Service
public class RestaurantRowMapper implements BiFunction<Row, String, Restaurant> {

    private final ColumnConverter converter;

    public RestaurantRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Restaurant} stored in the database.
     */
    @Override
    public Restaurant apply(Row row, String prefix) {
        Restaurant entity = new Restaurant();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setIdRest(converter.fromRow(row, prefix + "_id_rest", Integer.class));
        entity.setNomRest(converter.fromRow(row, prefix + "_nom_rest", String.class));
        entity.setAdresseRest(converter.fromRow(row, prefix + "_adresse_rest", String.class));
        return entity;
    }
}
