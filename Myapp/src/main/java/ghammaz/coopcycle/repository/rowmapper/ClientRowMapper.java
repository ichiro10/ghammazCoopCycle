package ghammaz.coopcycle.repository.rowmapper;

import ghammaz.coopcycle.domain.Client;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Client}, with proper type conversions.
 */
@Service
public class ClientRowMapper implements BiFunction<Row, String, Client> {

    private final ColumnConverter converter;

    public ClientRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Client} stored in the database.
     */
    @Override
    public Client apply(Row row, String prefix) {
        Client entity = new Client();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setIdClient(converter.fromRow(row, prefix + "_id_client", Integer.class));
        entity.setPrenomClient(converter.fromRow(row, prefix + "_prenom_client", String.class));
        entity.setNomClient(converter.fromRow(row, prefix + "_nom_client", String.class));
        entity.setAdresseClient(converter.fromRow(row, prefix + "_adresse_client", String.class));
        entity.setEmail(converter.fromRow(row, prefix + "_email", String.class));
        entity.setTelCLient(converter.fromRow(row, prefix + "_tel_c_lient", String.class));
        return entity;
    }
}
