package ghammaz.coopcycle.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class LivraisonSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("id_livraison", table, columnPrefix + "_id_livraison"));
        columns.add(Column.aliased("prix_livraison", table, columnPrefix + "_prix_livraison"));
        columns.add(Column.aliased("duree", table, columnPrefix + "_duree"));
        columns.add(Column.aliased("adresse_livraison", table, columnPrefix + "_adresse_livraison"));

        columns.add(Column.aliased("client_id", table, columnPrefix + "_client_id"));
        columns.add(Column.aliased("livreur_id", table, columnPrefix + "_livreur_id"));
        return columns;
    }
}
