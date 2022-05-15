package com.mycompany.myapp.repository.rowmapper;

import com.mycompany.myapp.domain.Boissons;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Boissons}, with proper type conversions.
 */
@Service
public class BoissonsRowMapper implements BiFunction<Row, String, Boissons> {

    private final ColumnConverter converter;

    public BoissonsRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Boissons} stored in the database.
     */
    @Override
    public Boissons apply(Row row, String prefix) {
        Boissons entity = new Boissons();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setIdBoissons(converter.fromRow(row, prefix + "_id_boissons", String.class));
        entity.setNomBoissons(converter.fromRow(row, prefix + "_nom_boissons", String.class));
        entity.setImagePath(converter.fromRow(row, prefix + "_image_path", String.class));
        entity.setPrix(converter.fromRow(row, prefix + "_prix", Double.class));
        entity.setRemisePerc(converter.fromRow(row, prefix + "_remise_perc", Double.class));
        entity.setRemiceVal(converter.fromRow(row, prefix + "_remice_val", Double.class));
        entity.setMenuId(converter.fromRow(row, prefix + "_menu_id", Long.class));
        return entity;
    }
}
