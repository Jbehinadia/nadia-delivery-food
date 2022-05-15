package com.mycompany.myapp.repository.rowmapper;

import com.mycompany.myapp.domain.Plat;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Plat}, with proper type conversions.
 */
@Service
public class PlatRowMapper implements BiFunction<Row, String, Plat> {

    private final ColumnConverter converter;

    public PlatRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Plat} stored in the database.
     */
    @Override
    public Plat apply(Row row, String prefix) {
        Plat entity = new Plat();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setIdPlat(converter.fromRow(row, prefix + "_id_plat", String.class));
        entity.setNomPlat(converter.fromRow(row, prefix + "_nom_plat", String.class));
        entity.setImagePath(converter.fromRow(row, prefix + "_image_path", String.class));
        entity.setPrix(converter.fromRow(row, prefix + "_prix", Double.class));
        entity.setRemisePerc(converter.fromRow(row, prefix + "_remise_perc", Double.class));
        entity.setRemiceVal(converter.fromRow(row, prefix + "_remice_val", Double.class));
        entity.setMenuId(converter.fromRow(row, prefix + "_menu_id", Long.class));
        return entity;
    }
}
