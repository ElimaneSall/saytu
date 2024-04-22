package sn.sonatel.dsi.ins.ftsirc.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Client.
 */
@Entity
@Table(name = "client")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Client implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nclient", nullable = false)
    private Long nclient;

    @NotNull
    @Column(name = "nom", nullable = false)
    private String nom;

    @NotNull
    @Column(name = "prenom", nullable = false)
    private String prenom;

    @NotNull
    @Column(name = "etat", nullable = false)
    private String etat;

    @NotNull
    @Column(name = "numero_fixe", nullable = false)
    private String numeroFixe;

    @NotNull
    @Column(name = "contact_mobile_client", nullable = false)
    private String contactMobileClient;

    @NotNull
    @Column(name = "is_doublons", nullable = false)
    private Boolean isDoublons;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "clients" }, allowSetters = true)
    private Offre offre;

    @JsonIgnoreProperties(value = { "client", "olt", "diagnostics", "metriques" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "client")
    private ONT ont;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Client id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNclient() {
        return this.nclient;
    }

    public Client nclient(Long nclient) {
        this.setNclient(nclient);
        return this;
    }

    public void setNclient(Long nclient) {
        this.nclient = nclient;
    }

    public String getNom() {
        return this.nom;
    }

    public Client nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public Client prenom(String prenom) {
        this.setPrenom(prenom);
        return this;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEtat() {
        return this.etat;
    }

    public Client etat(String etat) {
        this.setEtat(etat);
        return this;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getNumeroFixe() {
        return this.numeroFixe;
    }

    public Client numeroFixe(String numeroFixe) {
        this.setNumeroFixe(numeroFixe);
        return this;
    }

    public void setNumeroFixe(String numeroFixe) {
        this.numeroFixe = numeroFixe;
    }

    public String getContactMobileClient() {
        return this.contactMobileClient;
    }

    public Client contactMobileClient(String contactMobileClient) {
        this.setContactMobileClient(contactMobileClient);
        return this;
    }

    public void setContactMobileClient(String contactMobileClient) {
        this.contactMobileClient = contactMobileClient;
    }

    public Boolean getIsDoublons() {
        return this.isDoublons;
    }

    public Client isDoublons(Boolean isDoublons) {
        this.setIsDoublons(isDoublons);
        return this;
    }

    public void setIsDoublons(Boolean isDoublons) {
        this.isDoublons = isDoublons;
    }

    public Offre getOffre() {
        return this.offre;
    }

    public void setOffre(Offre offre) {
        this.offre = offre;
    }

    public Client offre(Offre offre) {
        this.setOffre(offre);
        return this;
    }

    public ONT getOnt() {
        return this.ont;
    }

    public void setOnt(ONT oNT) {
        if (this.ont != null) {
            this.ont.setClient(null);
        }
        if (oNT != null) {
            oNT.setClient(this);
        }
        this.ont = oNT;
    }

    public Client ont(ONT oNT) {
        this.setOnt(oNT);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Client)) {
            return false;
        }
        return getId() != null && getId().equals(((Client) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Client{" +
            "id=" + getId() +
            ", nclient=" + getNclient() +
            ", nom='" + getNom() + "'" +
            ", prenom='" + getPrenom() + "'" +
            ", etat='" + getEtat() + "'" +
            ", numeroFixe='" + getNumeroFixe() + "'" +
            ", contactMobileClient='" + getContactMobileClient() + "'" +
            ", isDoublons='" + getIsDoublons() + "'" +
            "}";
    }
}
