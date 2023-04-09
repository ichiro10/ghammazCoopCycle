package ghammaz.coopcycle.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Produit.
 */
@Table("produit")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Produit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("id_prod")
    private Integer idProd;

    @NotNull(message = "must not be null")
    @Column("nom_prod")
    private String nomProd;

    @NotNull(message = "must not be null")
    @DecimalMin(value = "0")
    @Column("prix_prod")
    private Float prixProd;

    @Transient
    private Livraison livraison;

    @Transient
    @JsonIgnoreProperties(value = { "produits", "livraisons" }, allowSetters = true)
    private Client client;

    @Transient
    @JsonIgnoreProperties(value = { "produits" }, allowSetters = true)
    private Restaurant restaurant;

    @Column("livraison_id")
    private Long livraisonId;

    @Column("client_id")
    private Long clientId;

    @Column("restaurant_id")
    private Long restaurantId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Produit id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIdProd() {
        return this.idProd;
    }

    public Produit idProd(Integer idProd) {
        this.setIdProd(idProd);
        return this;
    }

    public void setIdProd(Integer idProd) {
        this.idProd = idProd;
    }

    public String getNomProd() {
        return this.nomProd;
    }

    public Produit nomProd(String nomProd) {
        this.setNomProd(nomProd);
        return this;
    }

    public void setNomProd(String nomProd) {
        this.nomProd = nomProd;
    }

    public Float getPrixProd() {
        return this.prixProd;
    }

    public Produit prixProd(Float prixProd) {
        this.setPrixProd(prixProd);
        return this;
    }

    public void setPrixProd(Float prixProd) {
        this.prixProd = prixProd;
    }

    public Livraison getLivraison() {
        return this.livraison;
    }

    public void setLivraison(Livraison livraison) {
        this.livraison = livraison;
        this.livraisonId = livraison != null ? livraison.getId() : null;
    }

    public Produit livraison(Livraison livraison) {
        this.setLivraison(livraison);
        return this;
    }

    public Client getClient() {
        return this.client;
    }

    public void setClient(Client client) {
        this.client = client;
        this.clientId = client != null ? client.getId() : null;
    }

    public Produit client(Client client) {
        this.setClient(client);
        return this;
    }

    public Restaurant getRestaurant() {
        return this.restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
        this.restaurantId = restaurant != null ? restaurant.getId() : null;
    }

    public Produit restaurant(Restaurant restaurant) {
        this.setRestaurant(restaurant);
        return this;
    }

    public Long getLivraisonId() {
        return this.livraisonId;
    }

    public void setLivraisonId(Long livraison) {
        this.livraisonId = livraison;
    }

    public Long getClientId() {
        return this.clientId;
    }

    public void setClientId(Long client) {
        this.clientId = client;
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
        if (!(o instanceof Produit)) {
            return false;
        }
        return id != null && id.equals(((Produit) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Produit{" +
            "id=" + getId() +
            ", idProd=" + getIdProd() +
            ", nomProd='" + getNomProd() + "'" +
            ", prixProd=" + getPrixProd() +
            "}";
    }
}
