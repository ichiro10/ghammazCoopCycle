package ghammaz.coopcycle.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Livreur.
 */
@Table("livreur")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Livreur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("id_livreur")
    private Integer idLivreur;

    @NotNull(message = "must not be null")
    @Size(min = 2)
    @Column("nom_livreur")
    private String nomLivreur;

    @NotNull(message = "must not be null")
    @Size(min = 2)
    @Column("prenom_livreur")
    private String prenomLivreur;

    @NotNull(message = "must not be null")
    @Pattern(regexp = "(+\\d+)?[0-9 ]+")
    @Column("tel_livreur")
    private String telLivreur;

    @Transient
    @JsonIgnoreProperties(value = { "client", "produit", "livreur" }, allowSetters = true)
    private Set<Livraison> livraisons = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "livreurs" }, allowSetters = true)
    private Association association;

    @Column("association_id")
    private Long associationId;

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

    public Integer getIdLivreur() {
        return this.idLivreur;
    }

    public Livreur idLivreur(Integer idLivreur) {
        this.setIdLivreur(idLivreur);
        return this;
    }

    public void setIdLivreur(Integer idLivreur) {
        this.idLivreur = idLivreur;
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

    public String getTelLivreur() {
        return this.telLivreur;
    }

    public Livreur telLivreur(String telLivreur) {
        this.setTelLivreur(telLivreur);
        return this;
    }

    public void setTelLivreur(String telLivreur) {
        this.telLivreur = telLivreur;
    }

    public Set<Livraison> getLivraisons() {
        return this.livraisons;
    }

    public void setLivraisons(Set<Livraison> livraisons) {
        if (this.livraisons != null) {
            this.livraisons.forEach(i -> i.setLivreur(null));
        }
        if (livraisons != null) {
            livraisons.forEach(i -> i.setLivreur(this));
        }
        this.livraisons = livraisons;
    }

    public Livreur livraisons(Set<Livraison> livraisons) {
        this.setLivraisons(livraisons);
        return this;
    }

    public Livreur addLivraison(Livraison livraison) {
        this.livraisons.add(livraison);
        livraison.setLivreur(this);
        return this;
    }

    public Livreur removeLivraison(Livraison livraison) {
        this.livraisons.remove(livraison);
        livraison.setLivreur(null);
        return this;
    }

    public Association getAssociation() {
        return this.association;
    }

    public void setAssociation(Association association) {
        this.association = association;
        this.associationId = association != null ? association.getId() : null;
    }

    public Livreur association(Association association) {
        this.setAssociation(association);
        return this;
    }

    public Long getAssociationId() {
        return this.associationId;
    }

    public void setAssociationId(Long association) {
        this.associationId = association;
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
            ", idLivreur=" + getIdLivreur() +
            ", nomLivreur='" + getNomLivreur() + "'" +
            ", prenomLivreur='" + getPrenomLivreur() + "'" +
            ", telLivreur='" + getTelLivreur() + "'" +
            "}";
    }
}
