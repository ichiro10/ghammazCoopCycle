package ghammaz.coopcycle.repository.rowmapper;

import ghammaz.coopcycle.domain.Association;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Association}, with proper type conversions.
 */
@Service
public class AssociationRowMapper implements BiFunction<Row, String, Association> {

    private final ColumnConverter converter;

    public AssociationRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Association} stored in the database.
     */
    @Override
    public Association apply(Row row, String prefix) {
        Association entity = new Association();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setIdAsso(converter.fromRow(row, prefix + "_id_asso", Integer.class));
        entity.setNomAsso(converter.fromRow(row, prefix + "_nom_asso", String.class));
        return entity;
    }
}
