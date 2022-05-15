package com.mycompany.myapp.repository.rowmapper;

import com.mycompany.myapp.domain.CommandeDetails;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link CommandeDetails}, with proper type conversions.
 */
@Service
public class CommandeDetailsRowMapper implements BiFunction<Row, String, CommandeDetails> {

    private final ColumnConverter converter;

    public CommandeDetailsRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link CommandeDetails} stored in the database.
     */
    @Override
    public CommandeDetails apply(Row row, String prefix) {
        CommandeDetails entity = new CommandeDetails();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setPrix(converter.fromRow(row, prefix + "_prix", Double.class));
        entity.setEtat(converter.fromRow(row, prefix + "_etat", String.class));
        entity.setCommandeId(converter.fromRow(row, prefix + "_commande_id", Long.class));
        entity.setFastFoodId(converter.fromRow(row, prefix + "_fast_food_id", Long.class));
        entity.setPlatId(converter.fromRow(row, prefix + "_plat_id", Long.class));
        entity.setBoissonsId(converter.fromRow(row, prefix + "_boissons_id", Long.class));
        entity.setDessertId(converter.fromRow(row, prefix + "_dessert_id", Long.class));
        return entity;
    }
}
