package com.mycompany.myapp.repository.rowmapper;

import com.mycompany.myapp.domain.Commande;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Commande}, with proper type conversions.
 */
@Service
public class CommandeRowMapper implements BiFunction<Row, String, Commande> {

    private final ColumnConverter converter;

    public CommandeRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Commande} stored in the database.
     */
    @Override
    public Commande apply(Row row, String prefix) {
        Commande entity = new Commande();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setAdresseCommande(converter.fromRow(row, prefix + "_adresse_commande", String.class));
        entity.setEtat(converter.fromRow(row, prefix + "_etat", String.class));
        entity.setDateCommande(converter.fromRow(row, prefix + "_date_commande", Instant.class));
        entity.setPrixTotal(converter.fromRow(row, prefix + "_prix_total", Double.class));
        entity.setRemisePerc(converter.fromRow(row, prefix + "_remise_perc", Double.class));
        entity.setRemiceVal(converter.fromRow(row, prefix + "_remice_val", Double.class));
        entity.setPrixLivreson(converter.fromRow(row, prefix + "_prix_livreson", Double.class));
        entity.setDateSortie(converter.fromRow(row, prefix + "_date_sortie", Instant.class));
        entity.setLivreurId(converter.fromRow(row, prefix + "_livreur_id", Long.class));
        entity.setClientId(converter.fromRow(row, prefix + "_client_id", Long.class));
        return entity;
    }
}
