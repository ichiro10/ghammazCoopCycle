package ghammaz.coopcycle.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class ProduitSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("id_prod", table, columnPrefix + "_id_prod"));
        columns.add(Column.aliased("nom_prod", table, columnPrefix + "_nom_prod"));
        columns.add(Column.aliased("prix_prod", table, columnPrefix + "_prix_prod"));

        columns.add(Column.aliased("livraison_id", table, columnPrefix + "_livraison_id"));
        columns.add(Column.aliased("client_id", table, columnPrefix + "_client_id"));
        columns.add(Column.aliased("restaurant_id", table, columnPrefix + "_restaurant_id"));
        return columns;
    }
}
