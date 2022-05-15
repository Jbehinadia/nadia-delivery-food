package com.mycompany.myapp.repository.rowmapper;

import com.mycompany.myapp.domain.ResponsableRestaurant;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link ResponsableRestaurant}, with proper type conversions.
 */
@Service
public class ResponsableRestaurantRowMapper implements BiFunction<Row, String, ResponsableRestaurant> {

    private final ColumnConverter converter;

    public ResponsableRestaurantRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link ResponsableRestaurant} stored in the database.
     */
    @Override
    public ResponsableRestaurant apply(Row row, String prefix) {
        ResponsableRestaurant entity = new ResponsableRestaurant();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNomResponsable(converter.fromRow(row, prefix + "_nom_responsable", String.class));
        entity.setPrenomResponsable(converter.fromRow(row, prefix + "_prenom_responsable", String.class));
        entity.setAdresseResponsable(converter.fromRow(row, prefix + "_adresse_responsable", String.class));
        entity.setNumResponsable(converter.fromRow(row, prefix + "_num_responsable", String.class));
        entity.setRestaurantId(converter.fromRow(row, prefix + "_restaurant_id", Long.class));
        return entity;
    }
}
