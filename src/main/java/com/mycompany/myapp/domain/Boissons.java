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
 * A Boissons.
 */
@Table("boissons")
public class Boissons implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("nom_boissons")
    private String nomBoissons;

    @Column("image_path")
    private String imagePath;

    @Column("prix")
    private Double prix;

    @Column("remise_perc")
    private Double remisePerc;

    @Column("remice_val")
    private Double remiceVal;

    @Transient
    @JsonIgnoreProperties(value = { "commande", "fastFood", "plat", "boissons", "dessert" }, allowSetters = true)
    private Set<CommandeDetails> commandeDetails = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "fastFoods", "plats", "desserts", "boissons", "restaurant" }, allowSetters = true)
    private Menu menu;

    @Column("menu_id")
    private Long menuId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Boissons id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomBoissons() {
        return this.nomBoissons;
    }

    public Boissons nomBoissons(String nomBoissons) {
        this.setNomBoissons(nomBoissons);
        return this;
    }

    public void setNomBoissons(String nomBoissons) {
        this.nomBoissons = nomBoissons;
    }

    public String getImagePath() {
        return this.imagePath;
    }

    public Boissons imagePath(String imagePath) {
        this.setImagePath(imagePath);
        return this;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Double getPrix() {
        return this.prix;
    }

    public Boissons prix(Double prix) {
        this.setPrix(prix);
        return this;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
    }

    public Double getRemisePerc() {
        return this.remisePerc;
    }

    public Boissons remisePerc(Double remisePerc) {
        this.setRemisePerc(remisePerc);
        return this;
    }

    public void setRemisePerc(Double remisePerc) {
        this.remisePerc = remisePerc;
    }

    public Double getRemiceVal() {
        return this.remiceVal;
    }

    public Boissons remiceVal(Double remiceVal) {
        this.setRemiceVal(remiceVal);
        return this;
    }

    public void setRemiceVal(Double remiceVal) {
        this.remiceVal = remiceVal;
    }

    public Set<CommandeDetails> getCommandeDetails() {
        return this.commandeDetails;
    }

    public void setCommandeDetails(Set<CommandeDetails> commandeDetails) {
        if (this.commandeDetails != null) {
            this.commandeDetails.forEach(i -> i.setBoissons(null));
        }
        if (commandeDetails != null) {
            commandeDetails.forEach(i -> i.setBoissons(this));
        }
        this.commandeDetails = commandeDetails;
    }

    public Boissons commandeDetails(Set<CommandeDetails> commandeDetails) {
        this.setCommandeDetails(commandeDetails);
        return this;
    }

    public Boissons addCommandeDetails(CommandeDetails commandeDetails) {
        this.commandeDetails.add(commandeDetails);
        commandeDetails.setBoissons(this);
        return this;
    }

    public Boissons removeCommandeDetails(CommandeDetails commandeDetails) {
        this.commandeDetails.remove(commandeDetails);
        commandeDetails.setBoissons(null);
        return this;
    }

    public Menu getMenu() {
        return this.menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
        this.menuId = menu != null ? menu.getId() : null;
    }

    public Boissons menu(Menu menu) {
        this.setMenu(menu);
        return this;
    }

    public Long getMenuId() {
        return this.menuId;
    }

    public void setMenuId(Long menu) {
        this.menuId = menu;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Boissons)) {
            return false;
        }
        return id != null && id.equals(((Boissons) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Boissons{" +
            "id=" + getId() +
            ", nomBoissons='" + getNomBoissons() + "'" +
            ", imagePath='" + getImagePath() + "'" +
            ", prix=" + getPrix() +
            ", remisePerc=" + getRemisePerc() +
            ", remiceVal=" + getRemiceVal() +
            "}";
    }
}
