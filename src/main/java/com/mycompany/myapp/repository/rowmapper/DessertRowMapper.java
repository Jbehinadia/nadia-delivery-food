package com.mycompany.myapp.repository.rowmapper;

import com.mycompany.myapp.domain.Dessert;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Dessert}, with proper type conversions.
 */
@Service
public class DessertRowMapper implements BiFunction<Row, String, Dessert> {

    private final ColumnConverter converter;

    public DessertRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Dessert} stored in the database.
     */
    @Override
    public Dessert apply(Row row, String prefix) {
        Dessert entity = new Dessert();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNomDessert(converter.fromRow(row, prefix + "_nom_dessert", String.class));
        entity.setImagePath(converter.fromRow(row, prefix + "_image_path", String.class));
        entity.setPrix(converter.fromRow(row, prefix + "_prix", Double.class));
        entity.setRemisePerc(converter.fromRow(row, prefix + "_remise_perc", Double.class));
        entity.setRemiceVal(converter.fromRow(row, prefix + "_remice_val", Double.class));
        entity.setMenuId(converter.fromRow(row, prefix + "_menu_id", Long.class));
        return entity;
    }
}
