package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Restaurant.
 */
@Table("restaurant")
public class Restaurant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("nom_restaurant")
    private String nomRestaurant;

    @Column("adresse_restaurant")
    private String adresseRestaurant;

    @Column("num_restaurant")
    private String numRestaurant;

    @Transient
    @JsonIgnoreProperties(value = { "fastFoods", "plats", "desserts", "boissons", "restaurant" }, allowSetters = true)
    private Set<Menu> commandes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Restaurant id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomRestaurant() {
        return this.nomRestaurant;
    }

    public Restaurant nomRestaurant(String nomRestaurant) {
        this.setNomRestaurant(nomRestaurant);
        return this;
    }

    public void setNomRestaurant(String nomRestaurant) {
        this.nomRestaurant = nomRestaurant;
    }

    public String getAdresseRestaurant() {
        return this.adresseRestaurant;
    }

    public Restaurant adresseRestaurant(String adresseRestaurant) {
        this.setAdresseRestaurant(adresseRestaurant);
        return this;
    }

    public void setAdresseRestaurant(String adresseRestaurant) {
        this.adresseRestaurant = adresseRestaurant;
    }

    public String getNumRestaurant() {
        return this.numRestaurant;
    }

    public Restaurant numRestaurant(String numRestaurant) {
        this.setNumRestaurant(numRestaurant);
        return this;
    }

    public void setNumRestaurant(String numRestaurant) {
        this.numRestaurant = numRestaurant;
    }

    public Set<Menu> getCommandes() {
        return this.commandes;
    }

    public void setCommandes(Set<Menu> menus) {
        if (this.commandes != null) {
            this.commandes.forEach(i -> i.setRestaurant(null));
        }
        if (menus != null) {
            menus.forEach(i -> i.setRestaurant(this));
        }
        this.commandes = menus;
    }

    public Restaurant commandes(Set<Menu> menus) {
        this.setCommandes(menus);
        return this;
    }

    public Restaurant addCommande(Menu menu) {
        this.commandes.add(menu);
        menu.setRestaurant(this);
        return this;
    }

    public Restaurant removeCommande(Menu menu) {
        this.commandes.remove(menu);
        menu.setRestaurant(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Restaurant)) {
            return false;
        }
        return id != null && id.equals(((Restaurant) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Restaurant{" +
            "id=" + getId() +
            ", nomRestaurant='" + getNomRestaurant() + "'" +
            ", adresseRestaurant='" + getAdresseRestaurant() + "'" +
            ", numRestaurant='" + getNumRestaurant() + "'" +
            "}";
    }
}
