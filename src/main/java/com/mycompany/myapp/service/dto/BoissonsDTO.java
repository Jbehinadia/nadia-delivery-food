package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Boissons} entity.
 */
public class BoissonsDTO implements Serializable {

    private Long id;

    private String idBoissons;

    private String nomBoissons;

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

    public String getIdBoissons() {
        return idBoissons;
    }

    public void setIdBoissons(String idBoissons) {
        this.idBoissons = idBoissons;
    }

    public String getNomBoissons() {
        return nomBoissons;
    }

    public void setNomBoissons(String nomBoissons) {
        this.nomBoissons = nomBoissons;
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
        if (!(o instanceof BoissonsDTO)) {
            return false;
        }

        BoissonsDTO boissonsDTO = (BoissonsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, boissonsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BoissonsDTO{" +
            "id=" + getId() +
            ", idBoissons='" + getIdBoissons() + "'" +
            ", nomBoissons='" + getNomBoissons() + "'" +
            ", imagePath='" + getImagePath() + "'" +
            ", prix=" + getPrix() +
            ", remisePerc=" + getRemisePerc() +
            ", remiceVal=" + getRemiceVal() +
            ", menu=" + getMenu() +
            "}";
    }
}
