package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.FastFood} entity.
 */
public class FastFoodDTO implements Serializable {

    private Long id;

    private String nomFood;

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

    public String getNomFood() {
        return nomFood;
    }

    public void setNomFood(String nomFood) {
        this.nomFood = nomFood;
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
        if (!(o instanceof FastFoodDTO)) {
            return false;
        }

        FastFoodDTO fastFoodDTO = (FastFoodDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, fastFoodDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FastFoodDTO{" +
            "id=" + getId() +
            ", nomFood='" + getNomFood() + "'" +
            ", imagePath='" + getImagePath() + "'" +
            ", prix=" + getPrix() +
            ", remisePerc=" + getRemisePerc() +
            ", remiceVal=" + getRemiceVal() +
            ", menu=" + getMenu() +
            "}";
    }
}
