package sn.sonatel.dsi.ins.ftsirc.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link sn.sonatel.dsi.ins.ftsirc.domain.Client} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ClientDTO implements Serializable {

    private Long id;

    @NotNull
    private Long nclient;

    @NotNull
    private String nom;

    @NotNull
    private String prenom;

    @NotNull
    private String etat;

    @NotNull
    private String numeroFixe;

    @NotNull
    private String contactMobileClient;

    @NotNull
    private Boolean isDoublons;

    private OffreDTO offre;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNclient() {
        return nclient;
    }

    public void setNclient(Long nclient) {
        this.nclient = nclient;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getNumeroFixe() {
        return numeroFixe;
    }

    public void setNumeroFixe(String numeroFixe) {
        this.numeroFixe = numeroFixe;
    }

    public String getContactMobileClient() {
        return contactMobileClient;
    }

    public void setContactMobileClient(String contactMobileClient) {
        this.contactMobileClient = contactMobileClient;
    }

    public Boolean getIsDoublons() {
        return isDoublons;
    }

    public void setIsDoublons(Boolean isDoublons) {
        this.isDoublons = isDoublons;
    }

    public OffreDTO getOffre() {
        return offre;
    }

    public void setOffre(OffreDTO offre) {
        this.offre = offre;
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
            ", nclient=" + getNclient() +
            ", nom='" + getNom() + "'" +
            ", prenom='" + getPrenom() + "'" +
            ", etat='" + getEtat() + "'" +
            ", numeroFixe='" + getNumeroFixe() + "'" +
            ", contactMobileClient='" + getContactMobileClient() + "'" +
            ", isDoublons='" + getIsDoublons() + "'" +
            ", offre=" + getOffre() +
            "}";
    }
}
