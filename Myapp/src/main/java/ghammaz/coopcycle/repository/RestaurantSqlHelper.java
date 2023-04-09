package ghammaz.coopcycle.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class RestaurantSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("id_rest", table, columnPrefix + "_id_rest"));
        columns.add(Column.aliased("nom_rest", table, columnPrefix + "_nom_rest"));
        columns.add(Column.aliased("adresse_rest", table, columnPrefix + "_adresse_rest"));

        return columns;
    }
}
