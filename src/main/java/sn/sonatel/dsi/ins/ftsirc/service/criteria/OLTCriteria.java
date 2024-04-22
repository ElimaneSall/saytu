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

    private StringFilter nom;

    private StringFilter ip;

    private StringFilter vendeur;

    private StringFilter etat;

    private LocalDateFilter createdAt;

    private LocalDateFilter updatedAt;

    private LongFilter adresseId;

    private LongFilter ontId;

    private Boolean distinct;

    public OLTCriteria() {}

    public OLTCriteria(OLTCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.nom = other.optionalNom().map(StringFilter::copy).orElse(null);
        this.ip = other.optionalIp().map(StringFilter::copy).orElse(null);
        this.vendeur = other.optionalVendeur().map(StringFilter::copy).orElse(null);
        this.etat = other.optionalEtat().map(StringFilter::copy).orElse(null);
        this.createdAt = other.optionalCreatedAt().map(LocalDateFilter::copy).orElse(null);
        this.updatedAt = other.optionalUpdatedAt().map(LocalDateFilter::copy).orElse(null);
        this.adresseId = other.optionalAdresseId().map(LongFilter::copy).orElse(null);
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

    public StringFilter getNom() {
        return nom;
    }

    public Optional<StringFilter> optionalNom() {
        return Optional.ofNullable(nom);
    }

    public StringFilter nom() {
        if (nom == null) {
            setNom(new StringFilter());
        }
        return nom;
    }

    public void setNom(StringFilter nom) {
        this.nom = nom;
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

    public LongFilter getAdresseId() {
        return adresseId;
    }

    public Optional<LongFilter> optionalAdresseId() {
        return Optional.ofNullable(adresseId);
    }

    public LongFilter adresseId() {
        if (adresseId == null) {
            setAdresseId(new LongFilter());
        }
        return adresseId;
    }

    public void setAdresseId(LongFilter adresseId) {
        this.adresseId = adresseId;
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
            Objects.equals(nom, that.nom) &&
            Objects.equals(ip, that.ip) &&
            Objects.equals(vendeur, that.vendeur) &&
            Objects.equals(etat, that.etat) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(adresseId, that.adresseId) &&
            Objects.equals(ontId, that.ontId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nom, ip, vendeur, etat, createdAt, updatedAt, adresseId, ontId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OLTCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNom().map(f -> "nom=" + f + ", ").orElse("") +
            optionalIp().map(f -> "ip=" + f + ", ").orElse("") +
            optionalVendeur().map(f -> "vendeur=" + f + ", ").orElse("") +
            optionalEtat().map(f -> "etat=" + f + ", ").orElse("") +
            optionalCreatedAt().map(f -> "createdAt=" + f + ", ").orElse("") +
            optionalUpdatedAt().map(f -> "updatedAt=" + f + ", ").orElse("") +
            optionalAdresseId().map(f -> "adresseId=" + f + ", ").orElse("") +
            optionalOntId().map(f -> "ontId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
