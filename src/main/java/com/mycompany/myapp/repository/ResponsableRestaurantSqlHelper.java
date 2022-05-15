package com.mycompany.myapp.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class ResponsableRestaurantSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("nom_responsable", table, columnPrefix + "_nom_responsable"));
        columns.add(Column.aliased("prenom_responsable", table, columnPrefix + "_prenom_responsable"));
        columns.add(Column.aliased("adresse_responsable", table, columnPrefix + "_adresse_responsable"));
        columns.add(Column.aliased("num_responsable", table, columnPrefix + "_num_responsable"));

        columns.add(Column.aliased("restaurant_id", table, columnPrefix + "_restaurant_id"));
        return columns;
    }
}
