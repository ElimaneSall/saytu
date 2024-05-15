package sn.sonatel.dsi.ins.ftsirc.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link sn.sonatel.dsi.ins.ftsirc.domain.OLT} entity. This class is used
 * in {@link sn.sonatel.dsi.ins.ftsirc.web.rest.OLTResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /olts?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OLTCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter libelle;

    private StringFilter ip;

    private StringFilter vendeur;

    private StringFilter typeEquipment;

    private StringFilter codeEquipment;

    private StringFilter adresse;

    private StringFilter emplacement;

    private StringFilter typeCarte;

    private StringFilter latitude;

    private StringFilter longitude;

    private StringFilter capacite;

    private StringFilter etat;

    private LocalDateFilter createdAt;

    private LocalDateFilter updatedAt;

    private LongFilter ontId;

    private Boolean distinct;

    public OLTCriteria() {}

    public OLTCriteria(OLTCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.libelle = other.libelle == null ? null : other.libelle.copy();
        this.ip = other.ip == null ? null : other.ip.copy();
        this.vendeur = other.vendeur == null ? null : other.vendeur.copy();
        this.typeEquipment = other.typeEquipment == null ? null : other.typeEquipment.copy();
        this.codeEquipment = other.codeEquipment == null ? null : other.codeEquipment.copy();
        this.adresse = other.adresse == null ? null : other.adresse.copy();
        this.emplacement = other.emplacement == null ? null : other.emplacement.copy();
        this.typeCarte = other.typeCarte == null ? null : other.typeCarte.copy();
        this.latitude = other.latitude == null ? null : other.latitude.copy();
        this.longitude = other.longitude == null ? null : other.longitude.copy();
        this.capacite = other.capacite == null ? null : other.capacite.copy();
        this.etat = other.etat == null ? null : other.etat.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.ontId = other.ontId == null ? null : other.ontId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public OLTCriteria copy() {
        return new OLTCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getLibelle() {
        return libelle;
    }

    public StringFilter libelle() {
        if (libelle == null) {
            libelle = new StringFilter();
        }
        return libelle;
    }

    public void setLibelle(StringFilter libelle) {
        this.libelle = libelle;
    }

    public StringFilter getIp() {
        return ip;
    }

    public StringFilter ip() {
        if (ip == null) {
            ip = new StringFilter();
        }
        return ip;
    }

    public void setIp(StringFilter ip) {
        this.ip = ip;
    }

    public StringFilter getVendeur() {
        return vendeur;
    }

    public StringFilter vendeur() {
        if (vendeur == null) {
            vendeur = new StringFilter();
        }
        return vendeur;
    }

    public void setVendeur(StringFilter vendeur) {
        this.vendeur = vendeur;
    }

    public StringFilter getTypeEquipment() {
        return typeEquipment;
    }

    public StringFilter typeEquipment() {
        if (typeEquipment == null) {
            typeEquipment = new StringFilter();
        }
        return typeEquipment;
    }

    public void setTypeEquipment(StringFilter typeEquipment) {
        this.typeEquipment = typeEquipment;
    }

    public StringFilter getCodeEquipment() {
        return codeEquipment;
    }

    public StringFilter codeEquipment() {
        if (codeEquipment == null) {
            codeEquipment = new StringFilter();
        }
        return codeEquipment;
    }

    public void setCodeEquipment(StringFilter codeEquipment) {
        this.codeEquipment = codeEquipment;
    }

    public StringFilter getAdresse() {
        return adresse;
    }

    public StringFilter adresse() {
        if (adresse == null) {
            adresse = new StringFilter();
        }
        return adresse;
    }

    public void setAdresse(StringFilter adresse) {
        this.adresse = adresse;
    }

    public StringFilter getEmplacement() {
        return emplacement;
    }

    public StringFilter emplacement() {
        if (emplacement == null) {
            emplacement = new StringFilter();
        }
        return emplacement;
    }

    public void setEmplacement(StringFilter emplacement) {
        this.emplacement = emplacement;
    }

    public StringFilter getTypeCarte() {
        return typeCarte;
    }

    public StringFilter typeCarte() {
        if (typeCarte == null) {
            typeCarte = new StringFilter();
        }
        return typeCarte;
    }

    public void setTypeCarte(StringFilter typeCarte) {
        this.typeCarte = typeCarte;
    }

    public StringFilter getLatitude() {
        return latitude;
    }

    public StringFilter latitude() {
        if (latitude == null) {
            latitude = new StringFilter();
        }
        return latitude;
    }

    public void setLatitude(StringFilter latitude) {
        this.latitude = latitude;
    }

    public StringFilter getLongitude() {
        return longitude;
    }

    public StringFilter longitude() {
        if (longitude == null) {
            longitude = new StringFilter();
        }
        return longitude;
    }

    public void setLongitude(StringFilter longitude) {
        this.longitude = longitude;
    }

    public StringFilter getCapacite() {
        return capacite;
    }

    public StringFilter capacite() {
        if (capacite == null) {
            capacite = new StringFilter();
        }
        return capacite;
    }

    public void setCapacite(StringFilter capacite) {
        this.capacite = capacite;
    }

    public StringFilter getEtat() {
        return etat;
    }

    public StringFilter etat() {
        if (etat == null) {
            etat = new StringFilter();
        }
        return etat;
    }

    public void setEtat(StringFilter etat) {
        this.etat = etat;
    }

    public LocalDateFilter getCreatedAt() {
        return createdAt;
    }

    public LocalDateFilter createdAt() {
        if (createdAt == null) {
            createdAt = new LocalDateFilter();
        }
        return createdAt;
    }

    public void setCreatedAt(LocalDateFilter createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateFilter getUpdatedAt() {
        return updatedAt;
    }

    public LocalDateFilter updatedAt() {
        if (updatedAt == null) {
            updatedAt = new LocalDateFilter();
        }
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateFilter updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LongFilter getOntId() {
        return ontId;
    }

    public LongFilter ontId() {
        if (ontId == null) {
            ontId = new LongFilter();
        }
        return ontId;
    }

    public void setOntId(LongFilter ontId) {
        this.ontId = ontId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final OLTCriteria that = (OLTCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(libelle, that.libelle) &&
            Objects.equals(ip, that.ip) &&
            Objects.equals(vendeur, that.vendeur) &&
            Objects.equals(typeEquipment, that.typeEquipment) &&
            Objects.equals(codeEquipment, that.codeEquipment) &&
            Objects.equals(adresse, that.adresse) &&
            Objects.equals(emplacement, that.emplacement) &&
            Objects.equals(typeCarte, that.typeCarte) &&
            Objects.equals(latitude, that.latitude) &&
            Objects.equals(longitude, that.longitude) &&
            Objects.equals(capacite, that.capacite) &&
            Objects.equals(etat, that.etat) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(ontId, that.ontId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            libelle,
            ip,
            vendeur,
            typeEquipment,
            codeEquipment,
            adresse,
            emplacement,
            typeCarte,
            latitude,
            longitude,
            capacite,
            etat,
            createdAt,
            updatedAt,
            ontId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OLTCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (libelle != null ? "libelle=" + libelle + ", " : "") +
            (ip != null ? "ip=" + ip + ", " : "") +
            (vendeur != null ? "vendeur=" + vendeur + ", " : "") +
            (typeEquipment != null ? "typeEquipment=" + typeEquipment + ", " : "") +
            (codeEquipment != null ? "codeEquipment=" + codeEquipment + ", " : "") +
            (adresse != null ? "adresse=" + adresse + ", " : "") +
            (emplacement != null ? "emplacement=" + emplacement + ", " : "") +
            (typeCarte != null ? "typeCarte=" + typeCarte + ", " : "") +
            (latitude != null ? "latitude=" + latitude + ", " : "") +
            (longitude != null ? "longitude=" + longitude + ", " : "") +
            (capacite != null ? "capacite=" + capacite + ", " : "") +
            (etat != null ? "etat=" + etat + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (updatedAt != null ? "updatedAt=" + updatedAt + ", " : "") +
            (ontId != null ? "ontId=" + ontId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
