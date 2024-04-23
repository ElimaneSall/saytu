package sn.sonatel.dsi.ins.ftsirc.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link sn.sonatel.dsi.ins.ftsirc.domain.OLT} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OLTDTO implements Serializable {

    private Long id;

    @NotNull
    private String libelle;

    @NotNull
    private String ip;

    @NotNull
    private String vendeur;

    private String typeEquipment;

    private String codeEquipment;

    private String adresse;

    private String emplacement;

    private String typeCarte;

    private String latitude;

    private String longitude;

    private String capacite;

    private String etat;

    private LocalDate createdAt;

    private LocalDate updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getVendeur() {
        return vendeur;
    }

    public void setVendeur(String vendeur) {
        this.vendeur = vendeur;
    }

    public String getTypeEquipment() {
        return typeEquipment;
    }

    public void setTypeEquipment(String typeEquipment) {
        this.typeEquipment = typeEquipment;
    }

    public String getCodeEquipment() {
        return codeEquipment;
    }

    public void setCodeEquipment(String codeEquipment) {
        this.codeEquipment = codeEquipment;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getEmplacement() {
        return emplacement;
    }

    public void setEmplacement(String emplacement) {
        this.emplacement = emplacement;
    }

    public String getTypeCarte() {
        return typeCarte;
    }

    public void setTypeCarte(String typeCarte) {
        this.typeCarte = typeCarte;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getCapacite() {
        return capacite;
    }

    public void setCapacite(String capacite) {
        this.capacite = capacite;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OLTDTO)) {
            return false;
        }

        OLTDTO oLTDTO = (OLTDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, oLTDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OLTDTO{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", ip='" + getIp() + "'" +
            ", vendeur='" + getVendeur() + "'" +
            ", typeEquipment='" + getTypeEquipment() + "'" +
            ", codeEquipment='" + getCodeEquipment() + "'" +
            ", adresse='" + getAdresse() + "'" +
            ", emplacement='" + getEmplacement() + "'" +
            ", typeCarte='" + getTypeCarte() + "'" +
            ", latitude='" + getLatitude() + "'" +
            ", longitude='" + getLongitude() + "'" +
            ", capacite='" + getCapacite() + "'" +
            ", etat='" + getEtat() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
