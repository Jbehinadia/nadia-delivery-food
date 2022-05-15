package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Dessert} entity.
 */
public class DessertDTO implements Serializable {

    private Long id;

    private String nomDessert;

    private String imagePath;

    private Double prix;

    private Double remisePerc;

    private Double remiceVal;

    private MenuDTO menu;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomDessert() {
        return nomDessert;
    }

    public void setNomDessert(String nomDessert) {
        this.nomDessert = nomDessert;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Double getPrix() {
        return prix;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
    }

    public Double getRemisePerc() {
        return remisePerc;
    }

    public void setRemisePerc(Double remisePerc) {
        this.remisePerc = remisePerc;
    }

    public Double getRemiceVal() {
        return remiceVal;
    }

    public void setRemiceVal(Double remiceVal) {
        this.remiceVal = remiceVal;
    }

    public MenuDTO getMenu() {
        return menu;
    }

    public void setMenu(MenuDTO menu) {
        this.menu = menu;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DessertDTO)) {
            return false;
        }

        DessertDTO dessertDTO = (DessertDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, dessertDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DessertDTO{" +
            "id=" + getId() +
            ", nomDessert='" + getNomDessert() + "'" +
            ", imagePath='" + getImagePath() + "'" +
            ", prix=" + getPrix() +
            ", remisePerc=" + getRemisePerc() +
            ", remiceVal=" + getRemiceVal() +
            ", menu=" + getMenu() +
            "}";
    }
}
