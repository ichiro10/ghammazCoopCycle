package ghammaz.coopcycle.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Livraison.
 */
@Table("livraison")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Livraison implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("id_livraison")
    private Integer idLivraison;

    @NotNull(message = "must not be null")
    @DecimalMin(value = "0")
    @Column("prix_livraison")
    private Float prixLivraison;

    @NotNull(message = "must not be null")
    @Column("duree")
    private Instant duree;

    @NotNull(message = "must not be null")
    @Column("adresse_livraison")
    private String adresseLivraison;

    @Transient
    @JsonIgnoreProperties(value = { "produits", "livraisons" }, allowSetters = true)
    private Client client;

    @Transient
    private Produit produit;

    @Transient
    @JsonIgnoreProperties(value = { "livraisons", "association" }, allowSetters = true)
    private Livreur livreur;

    @Column("client_id")
    private Long clientId;

    @Column("livreur_id")
    private Long livreurId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Livraison id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIdLivraison() {
        return this.idLivraison;
    }

    public Livraison idLivraison(Integer idLivraison) {
        this.setIdLivraison(idLivraison);
        return this;
    }

    public void setIdLivraison(Integer idLivraison) {
        this.idLivraison = idLivraison;
    }

    public Float getPrixLivraison() {
        return this.prixLivraison;
    }

    public Livraison prixLivraison(Float prixLivraison) {
        this.setPrixLivraison(prixLivraison);
        return this;
    }

    public void setPrixLivraison(Float prixLivraison) {
        this.prixLivraison = prixLivraison;
    }

    public Instant getDuree() {
        return this.duree;
    }

    public Livraison duree(Instant duree) {
        this.setDuree(duree);
        return this;
    }

    public void setDuree(Instant duree) {
        this.duree = duree;
    }

    public String getAdresseLivraison() {
        return this.adresseLivraison;
    }

    public Livraison adresseLivraison(String adresseLivraison) {
        this.setAdresseLivraison(adresseLivraison);
        return this;
    }

    public void setAdresseLivraison(String adresseLivraison) {
        this.adresseLivraison = adresseLivraison;
    }

    public Client getClient() {
        return this.client;
    }

    public void setClient(Client client) {
        this.client = client;
        this.clientId = client != null ? client.getId() : null;
    }

    public Livraison client(Client client) {
        this.setClient(client);
        return this;
    }

    public Produit getProduit() {
        return this.produit;
    }

    public void setProduit(Produit produit) {
        if (this.produit != null) {
            this.produit.setLivraison(null);
        }
        if (produit != null) {
            produit.setLivraison(this);
        }
        this.produit = produit;
    }

    public Livraison produit(Produit produit) {
        this.setProduit(produit);
        return this;
    }

    public Livreur getLivreur() {
        return this.livreur;
    }

    public void setLivreur(Livreur livreur) {
        this.livreur = livreur;
        this.livreurId = livreur != null ? livreur.getId() : null;
    }

    public Livraison livreur(Livreur livreur) {
        this.setLivreur(livreur);
        return this;
    }

    public Long getClientId() {
        return this.clientId;
    }

    public void setClientId(Long client) {
        this.clientId = client;
    }

    public Long getLivreurId() {
        return this.livreurId;
    }

    public void setLivreurId(Long livreur) {
        this.livreurId = livreur;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Livraison)) {
            return false;
        }
        return id != null && id.equals(((Livraison) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Livraison{" +
            "id=" + getId() +
            ", idLivraison=" + getIdLivraison() +
            ", prixLivraison=" + getPrixLivraison() +
            ", duree='" + getDuree() + "'" +
            ", adresseLivraison='" + getAdresseLivraison() + "'" +
            "}";
    }
}
