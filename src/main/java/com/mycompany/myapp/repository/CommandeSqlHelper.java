package com.mycompany.myapp.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class CommandeSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("adresse_commande", table, columnPrefix + "_adresse_commande"));
        columns.add(Column.aliased("etat", table, columnPrefix + "_etat"));
        columns.add(Column.aliased("date_commande", table, columnPrefix + "_date_commande"));
        columns.add(Column.aliased("prix_total", table, columnPrefix + "_prix_total"));
        columns.add(Column.aliased("remise_perc", table, columnPrefix + "_remise_perc"));
        columns.add(Column.aliased("remice_val", table, columnPrefix + "_remice_val"));
        columns.add(Column.aliased("prix_livreson", table, columnPrefix + "_prix_livreson"));
        columns.add(Column.aliased("date_sortie", table, columnPrefix + "_date_sortie"));

        columns.add(Column.aliased("livreur_id", table, columnPrefix + "_livreur_id"));
        columns.add(Column.aliased("client_id", table, columnPrefix + "_client_id"));
        return columns;
    }
}
