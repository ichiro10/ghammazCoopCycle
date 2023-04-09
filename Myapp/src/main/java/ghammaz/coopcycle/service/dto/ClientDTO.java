package ghammaz.coopcycle.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link ghammaz.coopcycle.domain.Client} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ClientDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private Integer idClient;

    @NotNull(message = "must not be null")
    private String prenomClient;

    @NotNull(message = "must not be null")
    @Size(min = 2)
    private String nomClient;

    @NotNull(message = "must not be null")
    @Size(min = 2)
    private String adresseClient;

    @NotNull(message = "must not be null")
    @Pattern(regexp = "[a-zA-Z0-9.]+@[a-zA-Z0-9.]+.[a-z]+")
    private String email;

    @NotNull(message = "must not be null")
    @Pattern(regexp = "(+\\d+)?[0-9 ]+")
    private String telCLient;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIdClient() {
        return idClient;
    }

    public void setIdClient(Integer idClient) {
        this.idClient = idClient;
    }

    public String getPrenomClient() {
        return prenomClient;
    }

    public void setPrenomClient(String prenomClient) {
        this.prenomClient = prenomClient;
    }

    public String getNomClient() {
        return nomClient;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public String getAdresseClient() {
        return adresseClient;
    }

    public void setAdresseClient(String adresseClient) {
        this.adresseClient = adresseClient;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelCLient() {
        return telCLient;
    }

    public void setTelCLient(String telCLient) {
        this.telCLient = telCLient;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClientDTO)) {
            return false;
        }

        ClientDTO clientDTO = (ClientDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, clientDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClientDTO{" +
            "id=" + getId() +
            ", idClient=" + getIdClient() +
            ", prenomClient='" + getPrenomClient() + "'" +
            ", nomClient='" + getNomClient() + "'" +
            ", adresseClient='" + getAdresseClient() + "'" +
            ", email='" + getEmail() + "'" +
            ", telCLient='" + getTelCLient() + "'" +
            "}";
    }
}
