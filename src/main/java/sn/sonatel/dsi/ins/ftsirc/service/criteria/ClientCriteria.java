package sn.sonatel.dsi.ins.ftsirc.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link sn.sonatel.dsi.ins.ftsirc.domain.Client} entity. This class is used
 * in {@link sn.sonatel.dsi.ins.ftsirc.web.rest.ClientResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /clients?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ClientCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter nclient;

    private StringFilter nom;

    private StringFilter prenom;

    private StringFilter etat;

    private StringFilter numeroFixe;

    private StringFilter contactMobileClient;

    private BooleanFilter isDoublons;

    private LongFilter offreId;

    private LongFilter ontId;

    private Boolean distinct;

    public ClientCriteria() {}

    public ClientCriteria(ClientCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.nclient = other.optionalNclient().map(LongFilter::copy).orElse(null);
        this.nom = other.optionalNom().map(StringFilter::copy).orElse(null);
        this.prenom = other.optionalPrenom().map(StringFilter::copy).orElse(null);
        this.etat = other.optionalEtat().map(StringFilter::copy).orElse(null);
        this.numeroFixe = other.optionalNumeroFixe().map(StringFilter::copy).orElse(null);
        this.contactMobileClient = other.optionalContactMobileClient().map(StringFilter::copy).orElse(null);
        this.isDoublons = other.optionalIsDoublons().map(BooleanFilter::copy).orElse(null);
        this.offreId = other.optionalOffreId().map(LongFilter::copy).orElse(null);
        this.ontId = other.optionalOntId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ClientCriteria copy() {
        return new ClientCriteria(this);
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

    public LongFilter getNclient() {
        return nclient;
    }

    public Optional<LongFilter> optionalNclient() {
        return Optional.ofNullable(nclient);
    }

    public LongFilter nclient() {
        if (nclient == null) {
            setNclient(new LongFilter());
        }
        return nclient;
    }

    public void setNclient(LongFilter nclient) {
        this.nclient = nclient;
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

    public StringFilter getPrenom() {
        return prenom;
    }

    public Optional<StringFilter> optionalPrenom() {
        return Optional.ofNullable(prenom);
    }

    public StringFilter prenom() {
        if (prenom == null) {
            setPrenom(new StringFilter());
        }
        return prenom;
    }

    public void setPrenom(StringFilter prenom) {
        this.prenom = prenom;
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

    public StringFilter getNumeroFixe() {
        return numeroFixe;
    }

    public Optional<StringFilter> optionalNumeroFixe() {
        return Optional.ofNullable(numeroFixe);
    }

    public StringFilter numeroFixe() {
        if (numeroFixe == null) {
            setNumeroFixe(new StringFilter());
        }
        return numeroFixe;
    }

    public void setNumeroFixe(StringFilter numeroFixe) {
        this.numeroFixe = numeroFixe;
    }

    public StringFilter getContactMobileClient() {
        return contactMobileClient;
    }

    public Optional<StringFilter> optionalContactMobileClient() {
        return Optional.ofNullable(contactMobileClient);
    }

    public StringFilter contactMobileClient() {
        if (contactMobileClient == null) {
            setContactMobileClient(new StringFilter());
        }
        return contactMobileClient;
    }

    public void setContactMobileClient(StringFilter contactMobileClient) {
        this.contactMobileClient = contactMobileClient;
    }

    public BooleanFilter getIsDoublons() {
        return isDoublons;
    }

    public Optional<BooleanFilter> optionalIsDoublons() {
        return Optional.ofNullable(isDoublons);
    }

    public BooleanFilter isDoublons() {
        if (isDoublons == null) {
            setIsDoublons(new BooleanFilter());
        }
        return isDoublons;
    }

    public void setIsDoublons(BooleanFilter isDoublons) {
        this.isDoublons = isDoublons;
    }

    public LongFilter getOffreId() {
        return offreId;
    }

    public Optional<LongFilter> optionalOffreId() {
        return Optional.ofNullable(offreId);
    }

    public LongFilter offreId() {
        if (offreId == null) {
            setOffreId(new LongFilter());
        }
        return offreId;
    }

    public void setOffreId(LongFilter offreId) {
        this.offreId = offreId;
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
        final ClientCriteria that = (ClientCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nclient, that.nclient) &&
            Objects.equals(nom, that.nom) &&
            Objects.equals(prenom, that.prenom) &&
            Objects.equals(etat, that.etat) &&
            Objects.equals(numeroFixe, that.numeroFixe) &&
            Objects.equals(contactMobileClient, that.contactMobileClient) &&
            Objects.equals(isDoublons, that.isDoublons) &&
            Objects.equals(offreId, that.offreId) &&
            Objects.equals(ontId, that.ontId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nclient, nom, prenom, etat, numeroFixe, contactMobileClient, isDoublons, offreId, ontId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClientCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNclient().map(f -> "nclient=" + f + ", ").orElse("") +
            optionalNom().map(f -> "nom=" + f + ", ").orElse("") +
            optionalPrenom().map(f -> "prenom=" + f + ", ").orElse("") +
            optionalEtat().map(f -> "etat=" + f + ", ").orElse("") +
            optionalNumeroFixe().map(f -> "numeroFixe=" + f + ", ").orElse("") +
            optionalContactMobileClient().map(f -> "contactMobileClient=" + f + ", ").orElse("") +
            optionalIsDoublons().map(f -> "isDoublons=" + f + ", ").orElse("") +
            optionalOffreId().map(f -> "offreId=" + f + ", ").orElse("") +
            optionalOntId().map(f -> "ontId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
