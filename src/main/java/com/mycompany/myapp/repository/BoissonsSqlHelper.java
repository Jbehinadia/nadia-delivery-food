package com.mycompany.myapp.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class BoissonsSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("nom_boissons", table, columnPrefix + "_nom_boissons"));
        columns.add(Column.aliased("image_path", table, columnPrefix + "_image_path"));
        columns.add(Column.aliased("prix", table, columnPrefix + "_prix"));
        columns.add(Column.aliased("remise_perc", table, columnPrefix + "_remise_perc"));
        columns.add(Column.aliased("remice_val", table, columnPrefix + "_remice_val"));

        columns.add(Column.aliased("menu_id", table, columnPrefix + "_menu_id"));
        return columns;
    }
}
