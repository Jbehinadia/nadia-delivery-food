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
 * A Livreur.
 */
@Table("livreur")
public class Livreur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("nom_livreur")
    private String nomLivreur;

    @Column("prenom_livreur")
    private String prenomLivreur;

    @Column("adresse_livreur")
    private String adresseLivreur;

    @Column("num_livreur")
    private String numLivreur;

    @Transient
    @JsonIgnoreProperties(value = { "commandeDetails", "livreur", "client" }, allowSetters = true)
    private Set<Commande> commandes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Livreur id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomLivreur() {
        return this.nomLivreur;
    }

    public Livreur nomLivreur(String nomLivreur) {
        this.setNomLivreur(nomLivreur);
        return this;
    }

    public void setNomLivreur(String nomLivreur) {
        this.nomLivreur = nomLivreur;
    }

    public String getPrenomLivreur() {
        return this.prenomLivreur;
    }

    public Livreur prenomLivreur(String prenomLivreur) {
        this.setPrenomLivreur(prenomLivreur);
        return this;
    }

    public void setPrenomLivreur(String prenomLivreur) {
        this.prenomLivreur = prenomLivreur;
    }

    public String getAdresseLivreur() {
        return this.adresseLivreur;
    }

    public Livreur adresseLivreur(String adresseLivreur) {
        this.setAdresseLivreur(adresseLivreur);
        return this;
    }

    public void setAdresseLivreur(String adresseLivreur) {
        this.adresseLivreur = adresseLivreur;
    }

    public String getNumLivreur() {
        return this.numLivreur;
    }

    public Livreur numLivreur(String numLivreur) {
        this.setNumLivreur(numLivreur);
        return this;
    }

    public void setNumLivreur(String numLivreur) {
        this.numLivreur = numLivreur;
    }

    public Set<Commande> getCommandes() {
        return this.commandes;
    }

    public void setCommandes(Set<Commande> commandes) {
        if (this.commandes != null) {
            this.commandes.forEach(i -> i.setLivreur(null));
        }
        if (commandes != null) {
            commandes.forEach(i -> i.setLivreur(this));
        }
        this.commandes = commandes;
    }

    public Livreur commandes(Set<Commande> commandes) {
        this.setCommandes(commandes);
        return this;
    }

    public Livreur addCommande(Commande commande) {
        this.commandes.add(commande);
        commande.setLivreur(this);
        return this;
    }

    public Livreur removeCommande(Commande commande) {
        this.commandes.remove(commande);
        commande.setLivreur(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Livreur)) {
            return false;
        }
        return id != null && id.equals(((Livreur) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Livreur{" +
            "id=" + getId() +
            ", nomLivreur='" + getNomLivreur() + "'" +
            ", prenomLivreur='" + getPrenomLivreur() + "'" +
            ", adresseLivreur='" + getAdresseLivreur() + "'" +
            ", numLivreur='" + getNumLivreur() + "'" +
            "}";
    }
}
