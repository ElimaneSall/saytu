package sn.sonatel.dsi.ins.ftsirc.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
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
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.libelle = other.optionalLibelle().map(StringFilter::copy).orElse(null);
        this.ip = other.optionalIp().map(StringFilter::copy).orElse(null);
        this.vendeur = other.optionalVendeur().map(StringFilter::copy).orElse(null);
        this.typeEquipment = other.optionalTypeEquipment().map(StringFilter::copy).orElse(null);
        this.codeEquipment = other.optionalCodeEquipment().map(StringFilter::copy).orElse(null);
        this.adresse = other.optionalAdresse().map(StringFilter::copy).orElse(null);
        this.emplacement = other.optionalEmplacement().map(StringFilter::copy).orElse(null);
        this.typeCarte = other.optionalTypeCarte().map(StringFilter::copy).orElse(null);
        this.latitude = other.optionalLatitude().map(StringFilter::copy).orElse(null);
        this.longitude = other.optionalLongitude().map(StringFilter::copy).orElse(null);
        this.capacite = other.optionalCapacite().map(StringFilter::copy).orElse(null);
        this.etat = other.optionalEtat().map(StringFilter::copy).orElse(null);
        this.createdAt = other.optionalCreatedAt().map(LocalDateFilter::copy).orElse(null);
        this.updatedAt = other.optionalUpdatedAt().map(LocalDateFilter::copy).orElse(null);
        this.ontId = other.optionalOntId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public OLTCriteria copy() {
        return new OLTCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getLibelle() {
        return libelle;
    }

    public Optional<StringFilter> optionalLibelle() {
        return Optional.ofNullable(libelle);
    }

    public StringFilter libelle() {
        if (libelle == null) {
            setLibelle(new StringFilter());
        }
        return libelle;
    }

    public void setLibelle(StringFilter libelle) {
        this.libelle = libelle;
    }

    public StringFilter getIp() {
        return ip;
    }

    public Optional<StringFilter> optionalIp() {
        return Optional.ofNullable(ip);
    }

    public StringFilter ip() {
        if (ip == null) {
            setIp(new StringFilter());
        }
        return ip;
    }

    public void setIp(StringFilter ip) {
        this.ip = ip;
    }

    public StringFilter getVendeur() {
        return vendeur;
    }

    public Optional<StringFilter> optionalVendeur() {
        return Optional.ofNullable(vendeur);
    }

    public StringFilter vendeur() {
        if (vendeur == null) {
            setVendeur(new StringFilter());
        }
        return vendeur;
    }

    public void setVendeur(StringFilter vendeur) {
        this.vendeur = vendeur;
    }

    public StringFilter getTypeEquipment() {
        return typeEquipment;
    }

    public Optional<StringFilter> optionalTypeEquipment() {
        return Optional.ofNullable(typeEquipment);
    }

    public StringFilter typeEquipment() {
        if (typeEquipment == null) {
            setTypeEquipment(new StringFilter());
        }
        return typeEquipment;
    }

    public void setTypeEquipment(StringFilter typeEquipment) {
        this.typeEquipment = typeEquipment;
    }

    public StringFilter getCodeEquipment() {
        return codeEquipment;
    }

    public Optional<StringFilter> optionalCodeEquipment() {
        return Optional.ofNullable(codeEquipment);
    }

    public StringFilter codeEquipment() {
        if (codeEquipment == null) {
            setCodeEquipment(new StringFilter());
        }
        return codeEquipment;
    }

    public void setCodeEquipment(StringFilter codeEquipment) {
        this.codeEquipment = codeEquipment;
    }

    public StringFilter getAdresse() {
        return adresse;
    }

    public Optional<StringFilter> optionalAdresse() {
        return Optional.ofNullable(adresse);
    }

    public StringFilter adresse() {
        if (adresse == null) {
            setAdresse(new StringFilter());
        }
        return adresse;
    }

    public void setAdresse(StringFilter adresse) {
        this.adresse = adresse;
    }

    public StringFilter getEmplacement() {
        return emplacement;
    }

    public Optional<StringFilter> optionalEmplacement() {
        return Optional.ofNullable(emplacement);
    }

    public StringFilter emplacement() {
        if (emplacement == null) {
            setEmplacement(new StringFilter());
        }
        return emplacement;
    }

    public void setEmplacement(StringFilter emplacement) {
        this.emplacement = emplacement;
    }

    public StringFilter getTypeCarte() {
        return typeCarte;
    }

    public Optional<StringFilter> optionalTypeCarte() {
        return Optional.ofNullable(typeCarte);
    }

    public StringFilter typeCarte() {
        if (typeCarte == null) {
            setTypeCarte(new StringFilter());
        }
        return typeCarte;
    }

    public void setTypeCarte(StringFilter typeCarte) {
        this.typeCarte = typeCarte;
    }

    public StringFilter getLatitude() {
        return latitude;
    }

    public Optional<StringFilter> optionalLatitude() {
        return Optional.ofNullable(latitude);
    }

    public StringFilter latitude() {
        if (latitude == null) {
            setLatitude(new StringFilter());
        }
        return latitude;
    }

    public void setLatitude(StringFilter latitude) {
        this.latitude = latitude;
    }

    public StringFilter getLongitude() {
        return longitude;
    }

    public Optional<StringFilter> optionalLongitude() {
        return Optional.ofNullable(longitude);
    }

    public StringFilter longitude() {
        if (longitude == null) {
            setLongitude(new StringFilter());
        }
        return longitude;
    }

    public void setLongitude(StringFilter longitude) {
        this.longitude = longitude;
    }

    public StringFilter getCapacite() {
        return capacite;
    }

    public Optional<StringFilter> optionalCapacite() {
        return Optional.ofNullable(capacite);
    }

    public StringFilter capacite() {
        if (capacite == null) {
            setCapacite(new StringFilter());
        }
        return capacite;
    }

    public void setCapacite(StringFilter capacite) {
        this.capacite = capacite;
    }

    public StringFilter getEtat() {
        return etat;
    }

    public Optional<StringFilter> optionalEtat() {
        return Optional.ofNullable(etat);
    }

    public StringFilter etat() {
        if (etat == null) {
            setEtat(new StringFilter());
        }
        return etat;
    }

    public void setEtat(StringFilter etat) {
        this.etat = etat;
    }

    public LocalDateFilter getCreatedAt() {
        return createdAt;
    }

    public Optional<LocalDateFilter> optionalCreatedAt() {
        return Optional.ofNullable(createdAt);
    }

    public LocalDateFilter createdAt() {
        if (createdAt == null) {
            setCreatedAt(new LocalDateFilter());
        }
        return createdAt;
    }

    public void setCreatedAt(LocalDateFilter createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateFilter getUpdatedAt() {
        return updatedAt;
    }

    public Optional<LocalDateFilter> optionalUpdatedAt() {
        return Optional.ofNullable(updatedAt);
    }

    public LocalDateFilter updatedAt() {
        if (updatedAt == null) {
            setUpdatedAt(new LocalDateFilter());
        }
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateFilter updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LongFilter getOntId() {
        return ontId;
    }

    public Optional<LongFilter> optionalOntId() {
        return Optional.ofNullable(ontId);
    }

    public LongFilter ontId() {
        if (ontId == null) {
            setOntId(new LongFilter());
        }
        return ontId;
    }

    public void setOntId(LongFilter ontId) {
        this.ontId = ontId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
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
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalLibelle().map(f -> "libelle=" + f + ", ").orElse("") +
            optionalIp().map(f -> "ip=" + f + ", ").orElse("") +
            optionalVendeur().map(f -> "vendeur=" + f + ", ").orElse("") +
            optionalTypeEquipment().map(f -> "typeEquipment=" + f + ", ").orElse("") +
            optionalCodeEquipment().map(f -> "codeEquipment=" + f + ", ").orElse("") +
            optionalAdresse().map(f -> "adresse=" + f + ", ").orElse("") +
            optionalEmplacement().map(f -> "emplacement=" + f + ", ").orElse("") +
            optionalTypeCarte().map(f -> "typeCarte=" + f + ", ").orElse("") +
            optionalLatitude().map(f -> "latitude=" + f + ", ").orElse("") +
            optionalLongitude().map(f -> "longitude=" + f + ", ").orElse("") +
            optionalCapacite().map(f -> "capacite=" + f + ", ").orElse("") +
            optionalEtat().map(f -> "etat=" + f + ", ").orElse("") +
            optionalCreatedAt().map(f -> "createdAt=" + f + ", ").orElse("") +
            optionalUpdatedAt().map(f -> "updatedAt=" + f + ", ").orElse("") +
            optionalOntId().map(f -> "ontId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
