package sn.sonatel.dsi.ins.ftsirc.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A OLT.
 */
@Entity
@Table(name = "olt")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OLT implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nom", nullable = false)
    private String nom;

    @NotNull
    @Column(name = "ip", nullable = false)
    private String ip;

    @NotNull
    @Column(name = "vendeur", nullable = false)
    private String vendeur;

    @NotNull
    @Column(name = "etat", nullable = false)
    private String etat;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @JsonIgnoreProperties(value = { "olt" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Adresse adresse;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "olt")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "client", "olt", "diagnostics", "metriques" }, allowSetters = true)
    private Set<ONT> onts = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public OLT id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public OLT nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getIp() {
        return this.ip;
    }

    public OLT ip(String ip) {
        this.setIp(ip);
        return this;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getVendeur() {
        return this.vendeur;
    }

    public OLT vendeur(String vendeur) {
        this.setVendeur(vendeur);
        return this;
    }

    public void setVendeur(String vendeur) {
        this.vendeur = vendeur;
    }

    public String getEtat() {
        return this.etat;
    }

    public OLT etat(String etat) {
        this.setEtat(etat);
        return this;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public LocalDate getCreatedAt() {
        return this.createdAt;
    }

    public OLT createdAt(LocalDate createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getUpdatedAt() {
        return this.updatedAt;
    }

    public OLT updatedAt(LocalDate updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Adresse getAdresse() {
        return this.adresse;
    }

    public void setAdresse(Adresse adresse) {
        this.adresse = adresse;
    }

    public OLT adresse(Adresse adresse) {
        this.setAdresse(adresse);
        return this;
    }

    public Set<ONT> getOnts() {
        return this.onts;
    }

    public void setOnts(Set<ONT> oNTS) {
        if (this.onts != null) {
            this.onts.forEach(i -> i.setOlt(null));
        }
        if (oNTS != null) {
            oNTS.forEach(i -> i.setOlt(this));
        }
        this.onts = oNTS;
    }

    public OLT onts(Set<ONT> oNTS) {
        this.setOnts(oNTS);
        return this;
    }

    public OLT addOnt(ONT oNT) {
        this.onts.add(oNT);
        oNT.setOlt(this);
        return this;
    }

    public OLT removeOnt(ONT oNT) {
        this.onts.remove(oNT);
        oNT.setOlt(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OLT)) {
            return false;
        }
        return getId() != null && getId().equals(((OLT) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OLT{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", ip='" + getIp() + "'" +
            ", vendeur='" + getVendeur() + "'" +
            ", etat='" + getEtat() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
