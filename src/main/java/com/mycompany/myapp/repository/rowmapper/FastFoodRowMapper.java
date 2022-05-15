package com.mycompany.myapp.repository.rowmapper;

import com.mycompany.myapp.domain.FastFood;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link FastFood}, with proper type conversions.
 */
@Service
public class FastFoodRowMapper implements BiFunction<Row, String, FastFood> {

    private final ColumnConverter converter;

    public FastFoodRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link FastFood} stored in the database.
     */
    @Override
    public FastFood apply(Row row, String prefix) {
        FastFood entity = new FastFood();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNomFood(converter.fromRow(row, prefix + "_nom_food", String.class));
        entity.setImagePath(converter.fromRow(row, prefix + "_image_path", String.class));
        entity.setPrix(converter.fromRow(row, prefix + "_prix", Double.class));
        entity.setRemisePerc(converter.fromRow(row, prefix + "_remise_perc", Double.class));
        entity.setRemiceVal(converter.fromRow(row, prefix + "_remice_val", Double.class));
        entity.setMenuId(converter.fromRow(row, prefix + "_menu_id", Long.class));
        return entity;
    }
}
