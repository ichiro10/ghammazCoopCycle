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
 * A Association.
 */
@Table("association")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Association implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("id_asso")
    private Integer idAsso;

    @NotNull(message = "must not be null")
    @Column("nom_asso")
    private String nomAsso;

    @Transient
    @JsonIgnoreProperties(value = { "livraisons", "association" }, allowSetters = true)
    private Set<Livreur> livreurs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Association id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIdAsso() {
        return this.idAsso;
    }

    public Association idAsso(Integer idAsso) {
        this.setIdAsso(idAsso);
        return this;
    }

    public void setIdAsso(Integer idAsso) {
        this.idAsso = idAsso;
    }

    public String getNomAsso() {
        return this.nomAsso;
    }

    public Association nomAsso(String nomAsso) {
        this.setNomAsso(nomAsso);
        return this;
    }

    public void setNomAsso(String nomAsso) {
        this.nomAsso = nomAsso;
    }

    public Set<Livreur> getLivreurs() {
        return this.livreurs;
    }

    public void setLivreurs(Set<Livreur> livreurs) {
        if (this.livreurs != null) {
            this.livreurs.forEach(i -> i.setAssociation(null));
        }
        if (livreurs != null) {
            livreurs.forEach(i -> i.setAssociation(this));
        }
        this.livreurs = livreurs;
    }

    public Association livreurs(Set<Livreur> livreurs) {
        this.setLivreurs(livreurs);
        return this;
    }

    public Association addLivreur(Livreur livreur) {
        this.livreurs.add(livreur);
        livreur.setAssociation(this);
        return this;
    }

    public Association removeLivreur(Livreur livreur) {
        this.livreurs.remove(livreur);
        livreur.setAssociation(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Association)) {
            return false;
        }
        return id != null && id.equals(((Association) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Association{" +
            "id=" + getId() +
            ", idAsso=" + getIdAsso() +
            ", nomAsso='" + getNomAsso() + "'" +
            "}";
    }
}
