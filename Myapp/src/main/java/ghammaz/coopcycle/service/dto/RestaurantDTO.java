package ghammaz.coopcycle.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link ghammaz.coopcycle.domain.Restaurant} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RestaurantDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private Integer idRest;

    @NotNull(message = "must not be null")
    private String nomRest;

    @NotNull(message = "must not be null")
    @Size(max = 50)
    private String adresseRest;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIdRest() {
        return idRest;
    }

    public void setIdRest(Integer idRest) {
        this.idRest = idRest;
    }

    public String getNomRest() {
        return nomRest;
    }

    public void setNomRest(String nomRest) {
        this.nomRest = nomRest;
    }

    public String getAdresseRest() {
        return adresseRest;
    }

    public void setAdresseRest(String adresseRest) {
        this.adresseRest = adresseRest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RestaurantDTO)) {
            return false;
        }

        RestaurantDTO restaurantDTO = (RestaurantDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, restaurantDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RestaurantDTO{" +
            "id=" + getId() +
            ", idRest=" + getIdRest() +
            ", nomRest='" + getNomRest() + "'" +
            ", adresseRest='" + getAdresseRest() + "'" +
            "}";
    }
}
