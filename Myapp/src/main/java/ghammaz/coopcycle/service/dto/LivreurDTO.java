package ghammaz.coopcycle.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link ghammaz.coopcycle.domain.Livreur} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LivreurDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private Integer idLivreur;

    @NotNull(message = "must not be null")
    @Size(min = 2)
    private String nomLivreur;

    @NotNull(message = "must not be null")
    @Size(min = 2)
    private String prenomLivreur;

    @NotNull(message = "must not be null")
    @Pattern(regexp = "(+\\d+)?[0-9 ]+")
    private String telLivreur;

    private AssociationDTO association;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIdLivreur() {
        return idLivreur;
    }

    public void setIdLivreur(Integer idLivreur) {
        this.idLivreur = idLivreur;
    }

    public String getNomLivreur() {
        return nomLivreur;
    }

    public void setNomLivreur(String nomLivreur) {
        this.nomLivreur = nomLivreur;
    }

    public String getPrenomLivreur() {
        return prenomLivreur;
    }

    public void setPrenomLivreur(String prenomLivreur) {
        this.prenomLivreur = prenomLivreur;
    }

    public String getTelLivreur() {
        return telLivreur;
    }

    public void setTelLivreur(String telLivreur) {
        this.telLivreur = telLivreur;
    }

    public AssociationDTO getAssociation() {
        return association;
    }

    public void setAssociation(AssociationDTO association) {
        this.association = association;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LivreurDTO)) {
            return false;
        }

        LivreurDTO livreurDTO = (LivreurDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, livreurDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LivreurDTO{" +
            "id=" + getId() +
            ", idLivreur=" + getIdLivreur() +
            ", nomLivreur='" + getNomLivreur() + "'" +
            ", prenomLivreur='" + getPrenomLivreur() + "'" +
            ", telLivreur='" + getTelLivreur() + "'" +
            ", association=" + getAssociation() +
            "}";
    }
}
