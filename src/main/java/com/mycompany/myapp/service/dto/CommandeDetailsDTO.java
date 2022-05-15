package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.CommandeDetails} entity.
 */
public class CommandeDetailsDTO implements Serializable {

    private Long id;

    private Double prix;

    private String etat;

    private CommandeDTO commande;

    private FastFoodDTO fastFood;

    private PlatDTO plat;

    private BoissonsDTO boissons;

    private DessertDTO dessert;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getPrix() {
        return prix;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public CommandeDTO getCommande() {
        return commande;
    }

    public void setCommande(CommandeDTO commande) {
        this.commande = commande;
    }

    public FastFoodDTO getFastFood() {
        return fastFood;
    }

    public void setFastFood(FastFoodDTO fastFood) {
        this.fastFood = fastFood;
    }

    public PlatDTO getPlat() {
        return plat;
    }

    public void setPlat(PlatDTO plat) {
        this.plat = plat;
    }

    public BoissonsDTO getBoissons() {
        return boissons;
    }

    public void setBoissons(BoissonsDTO boissons) {
        this.boissons = boissons;
    }

    public DessertDTO getDessert() {
        return dessert;
    }

    public void setDessert(DessertDTO dessert) {
        this.dessert = dessert;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CommandeDetailsDTO)) {
            return false;
        }

        CommandeDetailsDTO commandeDetailsDTO = (CommandeDetailsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, commandeDetailsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommandeDetailsDTO{" +
            "id=" + getId() +
            ", prix=" + getPrix() +
            ", etat='" + getEtat() + "'" +
            ", commande=" + getCommande() +
            ", fastFood=" + getFastFood() +
            ", plat=" + getPlat() +
            ", boissons=" + getBoissons() +
            ", dessert=" + getDessert() +
            "}";
    }
}
