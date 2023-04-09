package ghammaz.coopcycle.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class LivreurSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("id_livreur", table, columnPrefix + "_id_livreur"));
        columns.add(Column.aliased("nom_livreur", table, columnPrefix + "_nom_livreur"));
        columns.add(Column.aliased("prenom_livreur", table, columnPrefix + "_prenom_livreur"));
        columns.add(Column.aliased("tel_livreur", table, columnPrefix + "_tel_livreur"));

        columns.add(Column.aliased("association_id", table, columnPrefix + "_association_id"));
        return columns;
    }
}
