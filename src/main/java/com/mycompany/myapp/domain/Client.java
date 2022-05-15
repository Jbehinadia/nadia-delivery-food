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
 * A Client.
 */
@Table("client")
public class Client implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("nom_client")
    private String nomClient;

    @Column("prenom_client")
    private String prenomClient;

    @Column("adresse_client")
    private String adresseClient;

    @Column("num_client")
    private String numClient;

    @Transient
    @JsonIgnoreProperties(value = { "commandeDetails", "livreur", "client" }, allowSetters = true)
    private Set<Commande> commandes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Client id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomClient() {
        return this.nomClient;
    }

    public Client nomClient(String nomClient) {
        this.setNomClient(nomClient);
        return this;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public String getPrenomClient() {
        return this.prenomClient;
    }

    public Client prenomClient(String prenomClient) {
        this.setPrenomClient(prenomClient);
        return this;
    }

    public void setPrenomClient(String prenomClient) {
        this.prenomClient = prenomClient;
    }

    public String getAdresseClient() {
        return this.adresseClient;
    }

    public Client adresseClient(String adresseClient) {
        this.setAdresseClient(adresseClient);
        return this;
    }

    public void setAdresseClient(String adresseClient) {
        this.adresseClient = adresseClient;
    }

    public String getNumClient() {
        return this.numClient;
    }

    public Client numClient(String numClient) {
        this.setNumClient(numClient);
        return this;
    }

    public void setNumClient(String numClient) {
        this.numClient = numClient;
    }

    public Set<Commande> getCommandes() {
        return this.commandes;
    }

    public void setCommandes(Set<Commande> commandes) {
        if (this.commandes != null) {
            this.commandes.forEach(i -> i.setClient(null));
        }
        if (commandes != null) {
            commandes.forEach(i -> i.setClient(this));
        }
        this.commandes = commandes;
    }

    public Client commandes(Set<Commande> commandes) {
        this.setCommandes(commandes);
        return this;
    }

    public Client addCommande(Commande commande) {
        this.commandes.add(commande);
        commande.setClient(this);
        return this;
    }

    public Client removeCommande(Commande commande) {
        this.commandes.remove(commande);
        commande.setClient(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Client)) {
            return false;
        }
        return id != null && id.equals(((Client) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Client{" +
            "id=" + getId() +
            ", nomClient='" + getNomClient() + "'" +
            ", prenomClient='" + getPrenomClient() + "'" +
            ", adresseClient='" + getAdresseClient() + "'" +
            ", numClient='" + getNumClient() + "'" +
            "}";
    }
}
