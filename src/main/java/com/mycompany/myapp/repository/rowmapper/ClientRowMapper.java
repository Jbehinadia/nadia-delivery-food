package com.mycompany.myapp.repository.rowmapper;

import com.mycompany.myapp.domain.Client;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Client}, with proper type conversions.
 */
@Service
public class ClientRowMapper implements BiFunction<Row, String, Client> {

    private final ColumnConverter converter;

    public ClientRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Client} stored in the database.
     */
    @Override
    public Client apply(Row row, String prefix) {
        Client entity = new Client();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNomClient(converter.fromRow(row, prefix + "_nom_client", String.class));
        entity.setPrenomClient(converter.fromRow(row, prefix + "_prenom_client", String.class));
        entity.setAdresseClient(converter.fromRow(row, prefix + "_adresse_client", String.class));
        entity.setNumClient(converter.fromRow(row, prefix + "_num_client", String.class));
        return entity;
    }
}
