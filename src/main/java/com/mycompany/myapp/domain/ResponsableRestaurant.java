package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A ResponsableRestaurant.
 */
@Table("responsable_restaurant")
public class ResponsableRestaurant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("nom_responsable")
    private String nomResponsable;

    @Column("prenom_responsable")
    private String prenomResponsable;

    @Column("adresse_responsable")
    private String adresseResponsable;

    @Column("num_responsable")
    private String numResponsable;

    @Transient
    private Restaurant restaurant;

    @Column("restaurant_id")
    private Long restaurantId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ResponsableRestaurant id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomResponsable() {
        return this.nomResponsable;
    }

    public ResponsableRestaurant nomResponsable(String nomResponsable) {
        this.setNomResponsable(nomResponsable);
        return this;
    }

    public void setNomResponsable(String nomResponsable) {
        this.nomResponsable = nomResponsable;
    }

    public String getPrenomResponsable() {
        return this.prenomResponsable;
    }

    public ResponsableRestaurant prenomResponsable(String prenomResponsable) {
        this.setPrenomResponsable(prenomResponsable);
        return this;
    }

    public void setPrenomResponsable(String prenomResponsable) {
        this.prenomResponsable = prenomResponsable;
    }

    public String getAdresseResponsable() {
        return this.adresseResponsable;
    }

    public ResponsableRestaurant adresseResponsable(String adresseResponsable) {
        this.setAdresseResponsable(adresseResponsable);
        return this;
    }

    public void setAdresseResponsable(String adresseResponsable) {
        this.adresseResponsable = adresseResponsable;
    }

    public String getNumResponsable() {
        return this.numResponsable;
    }

    public ResponsableRestaurant numResponsable(String numResponsable) {
        this.setNumResponsable(numResponsable);
        return this;
    }

    public void setNumResponsable(String numResponsable) {
        this.numResponsable = numResponsable;
    }

    public Restaurant getRestaurant() {
        return this.restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
        this.restaurantId = restaurant != null ? restaurant.getId() : null;
    }

    public ResponsableRestaurant restaurant(Restaurant restaurant) {
        this.setRestaurant(restaurant);
        return this;
    }

    public Long getRestaurantId() {
        return this.restaurantId;
    }

    public void setRestaurantId(Long restaurant) {
        this.restaurantId = restaurant;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ResponsableRestaurant)) {
            return false;
        }
        return id != null && id.equals(((ResponsableRestaurant) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ResponsableRestaurant{" +
            "id=" + getId() +
            ", nomResponsable='" + getNomResponsable() + "'" +
            ", prenomResponsable='" + getPrenomResponsable() + "'" +
            ", adresseResponsable='" + getAdresseResponsable() + "'" +
            ", numResponsable='" + getNumResponsable() + "'" +
            "}";
    }
}
