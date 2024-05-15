package sn.sonatel.dsi.ins.ftsirc.service.criteria;

import java.io.Serializable;
import java.util.Objects;
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
        this.id = other.id == null ? null : other.id.copy();
        this.nclient = other.nclient == null ? null : other.nclient.copy();
        this.nom = other.nom == null ? null : other.nom.copy();
        this.prenom = other.prenom == null ? null : other.prenom.copy();
        this.etat = other.etat == null ? null : other.etat.copy();
        this.numeroFixe = other.numeroFixe == null ? null : other.numeroFixe.copy();
        this.contactMobileClient = other.contactMobileClient == null ? null : other.contactMobileClient.copy();
        this.isDoublons = other.isDoublons == null ? null : other.isDoublons.copy();
        this.offreId = other.offreId == null ? null : other.offreId.copy();
        this.ontId = other.ontId == null ? null : other.ontId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ClientCriteria copy() {
        return new ClientCriteria(this);
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

    public LongFilter getNclient() {
        return nclient;
    }

    public LongFilter nclient() {
        if (nclient == null) {
            nclient = new LongFilter();
        }
        return nclient;
    }

    public void setNclient(LongFilter nclient) {
        this.nclient = nclient;
    }

    public StringFilter getNom() {
        return nom;
    }

    public StringFilter nom() {
        if (nom == null) {
            nom = new StringFilter();
        }
        return nom;
    }

    public void setNom(StringFilter nom) {
        this.nom = nom;
    }

    public StringFilter getPrenom() {
        return prenom;
    }

    public StringFilter prenom() {
        if (prenom == null) {
            prenom = new StringFilter();
        }
        return prenom;
    }

    public void setPrenom(StringFilter prenom) {
        this.prenom = prenom;
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

    public StringFilter getNumeroFixe() {
        return numeroFixe;
    }

    public StringFilter numeroFixe() {
        if (numeroFixe == null) {
            numeroFixe = new StringFilter();
        }
        return numeroFixe;
    }

    public void setNumeroFixe(StringFilter numeroFixe) {
        this.numeroFixe = numeroFixe;
    }

    public StringFilter getContactMobileClient() {
        return contactMobileClient;
    }

    public StringFilter contactMobileClient() {
        if (contactMobileClient == null) {
            contactMobileClient = new StringFilter();
        }
        return contactMobileClient;
    }

    public void setContactMobileClient(StringFilter contactMobileClient) {
        this.contactMobileClient = contactMobileClient;
    }

    public BooleanFilter getIsDoublons() {
        return isDoublons;
    }

    public BooleanFilter isDoublons() {
        if (isDoublons == null) {
            isDoublons = new BooleanFilter();
        }
        return isDoublons;
    }

    public void setIsDoublons(BooleanFilter isDoublons) {
        this.isDoublons = isDoublons;
    }

    public LongFilter getOffreId() {
        return offreId;
    }

    public LongFilter offreId() {
        if (offreId == null) {
            offreId = new LongFilter();
        }
        return offreId;
    }

    public void setOffreId(LongFilter offreId) {
        this.offreId = offreId;
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
            (id != null ? "id=" + id + ", " : "") +
            (nclient != null ? "nclient=" + nclient + ", " : "") +
            (nom != null ? "nom=" + nom + ", " : "") +
            (prenom != null ? "prenom=" + prenom + ", " : "") +
            (etat != null ? "etat=" + etat + ", " : "") +
            (numeroFixe != null ? "numeroFixe=" + numeroFixe + ", " : "") +
            (contactMobileClient != null ? "contactMobileClient=" + contactMobileClient + ", " : "") +
            (isDoublons != null ? "isDoublons=" + isDoublons + ", " : "") +
            (offreId != null ? "offreId=" + offreId + ", " : "") +
            (ontId != null ? "ontId=" + ontId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
