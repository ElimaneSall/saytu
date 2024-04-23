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
    @Column(name = "libelle", nullable = false)
    private String libelle;

    @NotNull
    @Column(name = "ip", nullable = false)
    private String ip;

    @NotNull
    @Column(name = "vendeur", nullable = false)
    private String vendeur;

    @Column(name = "type_equipment")
    private String typeEquipment;

    @Column(name = "code_equipment")
    private String codeEquipment;

    @Column(name = "adresse")
    private String adresse;

    @Column(name = "emplacement")
    private String emplacement;

    @Column(name = "type_carte")
    private String typeCarte;

    @Column(name = "latitude")
    private String latitude;

    @Column(name = "longitude")
    private String longitude;

    @Column(name = "capacite")
    private String capacite;

    @Column(name = "etat")
    private String etat;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

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

    public String getLibelle() {
        return this.libelle;
    }

    public OLT libelle(String libelle) {
        this.setLibelle(libelle);
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
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

    public String getTypeEquipment() {
        return this.typeEquipment;
    }

    public OLT typeEquipment(String typeEquipment) {
        this.setTypeEquipment(typeEquipment);
        return this;
    }

    public void setTypeEquipment(String typeEquipment) {
        this.typeEquipment = typeEquipment;
    }

    public String getCodeEquipment() {
        return this.codeEquipment;
    }

    public OLT codeEquipment(String codeEquipment) {
        this.setCodeEquipment(codeEquipment);
        return this;
    }

    public void setCodeEquipment(String codeEquipment) {
        this.codeEquipment = codeEquipment;
    }

    public String getAdresse() {
        return this.adresse;
    }

    public OLT adresse(String adresse) {
        this.setAdresse(adresse);
        return this;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getEmplacement() {
        return this.emplacement;
    }

    public OLT emplacement(String emplacement) {
        this.setEmplacement(emplacement);
        return this;
    }

    public void setEmplacement(String emplacement) {
        this.emplacement = emplacement;
    }

    public String getTypeCarte() {
        return this.typeCarte;
    }

    public OLT typeCarte(String typeCarte) {
        this.setTypeCarte(typeCarte);
        return this;
    }

    public void setTypeCarte(String typeCarte) {
        this.typeCarte = typeCarte;
    }

    public String getLatitude() {
        return this.latitude;
    }

    public OLT latitude(String latitude) {
        this.setLatitude(latitude);
        return this;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return this.longitude;
    }

    public OLT longitude(String longitude) {
        this.setLongitude(longitude);
        return this;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getCapacite() {
        return this.capacite;
    }

    public OLT capacite(String capacite) {
        this.setCapacite(capacite);
        return this;
    }

    public void setCapacite(String capacite) {
        this.capacite = capacite;
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
