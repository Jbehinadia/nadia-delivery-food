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
 * A Menu.
 */
@Table("menu")
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("nom_menu")
    private String nomMenu;

    @Transient
    @JsonIgnoreProperties(value = { "commandeDetails", "menu" }, allowSetters = true)
    private Set<FastFood> fastFoods = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "commandeDetails", "menu" }, allowSetters = true)
    private Set<Plat> plats = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "commandeDetails", "menu" }, allowSetters = true)
    private Set<Dessert> desserts = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "commandeDetails", "menu" }, allowSetters = true)
    private Set<Boissons> boissons = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "commandes" }, allowSetters = true)
    private Restaurant restaurant;

    @Column("restaurant_id")
    private Long restaurantId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Menu id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomMenu() {
        return this.nomMenu;
    }

    public Menu nomMenu(String nomMenu) {
        this.setNomMenu(nomMenu);
        return this;
    }

    public void setNomMenu(String nomMenu) {
        this.nomMenu = nomMenu;
    }

    public Set<FastFood> getFastFoods() {
        return this.fastFoods;
    }

    public void setFastFoods(Set<FastFood> fastFoods) {
        if (this.fastFoods != null) {
            this.fastFoods.forEach(i -> i.setMenu(null));
        }
        if (fastFoods != null) {
            fastFoods.forEach(i -> i.setMenu(this));
        }
        this.fastFoods = fastFoods;
    }

    public Menu fastFoods(Set<FastFood> fastFoods) {
        this.setFastFoods(fastFoods);
        return this;
    }

    public Menu addFastFood(FastFood fastFood) {
        this.fastFoods.add(fastFood);
        fastFood.setMenu(this);
        return this;
    }

    public Menu removeFastFood(FastFood fastFood) {
        this.fastFoods.remove(fastFood);
        fastFood.setMenu(null);
        return this;
    }

    public Set<Plat> getPlats() {
        return this.plats;
    }

    public void setPlats(Set<Plat> plats) {
        if (this.plats != null) {
            this.plats.forEach(i -> i.setMenu(null));
        }
        if (plats != null) {
            plats.forEach(i -> i.setMenu(this));
        }
        this.plats = plats;
    }

    public Menu plats(Set<Plat> plats) {
        this.setPlats(plats);
        return this;
    }

    public Menu addPlat(Plat plat) {
        this.plats.add(plat);
        plat.setMenu(this);
        return this;
    }

    public Menu removePlat(Plat plat) {
        this.plats.remove(plat);
        plat.setMenu(null);
        return this;
    }

    public Set<Dessert> getDesserts() {
        return this.desserts;
    }

    public void setDesserts(Set<Dessert> desserts) {
        if (this.desserts != null) {
            this.desserts.forEach(i -> i.setMenu(null));
        }
        if (desserts != null) {
            desserts.forEach(i -> i.setMenu(this));
        }
        this.desserts = desserts;
    }

    public Menu desserts(Set<Dessert> desserts) {
        this.setDesserts(desserts);
        return this;
    }

    public Menu addDessert(Dessert dessert) {
        this.desserts.add(dessert);
        dessert.setMenu(this);
        return this;
    }

    public Menu removeDessert(Dessert dessert) {
        this.desserts.remove(dessert);
        dessert.setMenu(null);
        return this;
    }

    public Set<Boissons> getBoissons() {
        return this.boissons;
    }

    public void setBoissons(Set<Boissons> boissons) {
        if (this.boissons != null) {
            this.boissons.forEach(i -> i.setMenu(null));
        }
        if (boissons != null) {
            boissons.forEach(i -> i.setMenu(this));
        }
        this.boissons = boissons;
    }

    public Menu boissons(Set<Boissons> boissons) {
        this.setBoissons(boissons);
        return this;
    }

    public Menu addBoissons(Boissons boissons) {
        this.boissons.add(boissons);
        boissons.setMenu(this);
        return this;
    }

    public Menu removeBoissons(Boissons boissons) {
        this.boissons.remove(boissons);
        boissons.setMenu(null);
        return this;
    }

    public Restaurant getRestaurant() {
        return this.restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
        this.restaurantId = restaurant != null ? restaurant.getId() : null;
    }

    public Menu restaurant(Restaurant restaurant) {
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
        if (!(o instanceof Menu)) {
            return false;
        }
        return id != null && id.equals(((Menu) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Menu{" +
            "id=" + getId() +
            ", nomMenu='" + getNomMenu() + "'" +
            "}";
    }
}
