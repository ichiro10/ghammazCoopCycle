package ghammaz.coopcycle.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class ClientSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("id_client", table, columnPrefix + "_id_client"));
        columns.add(Column.aliased("prenom_client", table, columnPrefix + "_prenom_client"));
        columns.add(Column.aliased("nom_client", table, columnPrefix + "_nom_client"));
        columns.add(Column.aliased("adresse_client", table, columnPrefix + "_adresse_client"));
        columns.add(Column.aliased("email", table, columnPrefix + "_email"));
        columns.add(Column.aliased("tel_c_lient", table, columnPrefix + "_tel_c_lient"));

        return columns;
    }
}
