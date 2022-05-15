package com.mycompany.myapp.repository.rowmapper;

import com.mycompany.myapp.domain.Menu;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Menu}, with proper type conversions.
 */
@Service
public class MenuRowMapper implements BiFunction<Row, String, Menu> {

    private final ColumnConverter converter;

    public MenuRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Menu} stored in the database.
     */
    @Override
    public Menu apply(Row row, String prefix) {
        Menu entity = new Menu();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNomMenu(converter.fromRow(row, prefix + "_nom_menu", String.class));
        entity.setRestaurantId(converter.fromRow(row, prefix + "_restaurant_id", Long.class));
        return entity;
    }
}
