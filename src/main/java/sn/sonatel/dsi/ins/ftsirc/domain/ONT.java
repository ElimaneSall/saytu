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
 * A ONT.
 */
@Entity
@Table(name = "ont")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ONT implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "jhi_index", nullable = false)
    private String index;

    @NotNull
    @Column(name = "ont_id", nullable = false)
    private String ontID;

    @NotNull
    @Column(name = "service_id", nullable = false)
    private String serviceId;

    @NotNull
    @Column(name = "slot", nullable = false)
    private String slot;

    @NotNull
    @Column(name = "pon", nullable = false)
    private String pon;

    @NotNull
    @Column(name = "pon_index", nullable = false)
    private String ponIndex;

    @Column(name = "max_up")
    private String maxUp;

    @Column(name = "max_down")
    private String maxDown;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @Column(name = "etat_olt")
    private String etatOlt;

    @Column(name = "status")
    private String status;

    @Column(name = "status_at")
    private LocalDate statusAt;

    @Column(name = "nbre_lignes_couper")
    private Long nbreLignesCouper;

    @JsonIgnoreProperties(value = { "offre", "ont" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Client client;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = { "onts" }, allowSetters = true)
    private OLT olt;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "ont")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "signal", "ont", "anomalies" }, allowSetters = true)
    private Set<Diagnostic> diagnostics = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ont")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "ont" }, allowSetters = true)
    private Set<Metrique> metriques = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ONT id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIndex() {
        return this.index;
    }

    public ONT index(String index) {
        this.setIndex(index);
        return this;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getOntID() {
        return this.ontID;
    }

    public ONT ontID(String ontID) {
        this.setOntID(ontID);
        return this;
    }

    public void setOntID(String ontID) {
        this.ontID = ontID;
    }

    public String getServiceId() {
        return this.serviceId;
    }

    public ONT serviceId(String serviceId) {
        this.setServiceId(serviceId);
        return this;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getSlot() {
        return this.slot;
    }

    public ONT slot(String slot) {
        this.setSlot(slot);
        return this;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    public String getPon() {
        return this.pon;
    }

    public ONT pon(String pon) {
        this.setPon(pon);
        return this;
    }

    public void setPon(String pon) {
        this.pon = pon;
    }

    public String getPonIndex() {
        return this.ponIndex;
    }

    public ONT ponIndex(String ponIndex) {
        this.setPonIndex(ponIndex);
        return this;
    }

    public void setPonIndex(String ponIndex) {
        this.ponIndex = ponIndex;
    }

    public String getMaxUp() {
        return this.maxUp;
    }

    public ONT maxUp(String maxUp) {
        this.setMaxUp(maxUp);
        return this;
    }

    public void setMaxUp(String maxUp) {
        this.maxUp = maxUp;
    }

    public String getMaxDown() {
        return this.maxDown;
    }

    public ONT maxDown(String maxDown) {
        this.setMaxDown(maxDown);
        return this;
    }

    public void setMaxDown(String maxDown) {
        this.maxDown = maxDown;
    }

    public LocalDate getCreatedAt() {
        return this.createdAt;
    }

    public ONT createdAt(LocalDate createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getUpdatedAt() {
        return this.updatedAt;
    }

    public ONT updatedAt(LocalDate updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getEtatOlt() {
        return this.etatOlt;
    }

    public ONT etatOlt(String etatOlt) {
        this.setEtatOlt(etatOlt);
        return this;
    }

    public void setEtatOlt(String etatOlt) {
        this.etatOlt = etatOlt;
    }

    public String getStatus() {
        return this.status;
    }

    public ONT status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getStatusAt() {
        return this.statusAt;
    }

    public ONT statusAt(LocalDate statusAt) {
        this.setStatusAt(statusAt);
        return this;
    }

    public void setStatusAt(LocalDate statusAt) {
        this.statusAt = statusAt;
    }

    public Long getNbreLignesCouper() {
        return this.nbreLignesCouper;
    }

    public ONT nbreLignesCouper(Long nbreLignesCouper) {
        this.setNbreLignesCouper(nbreLignesCouper);
        return this;
    }

    public void setNbreLignesCouper(Long nbreLignesCouper) {
        this.nbreLignesCouper = nbreLignesCouper;
    }

    public Client getClient() {
        return this.client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public ONT client(Client client) {
        this.setClient(client);
        return this;
    }

    public OLT getOlt() {
        return this.olt;
    }

    public void setOlt(OLT oLT) {
        this.olt = oLT;
    }

    public ONT olt(OLT oLT) {
        this.setOlt(oLT);
        return this;
    }

    public Set<Diagnostic> getDiagnostics() {
        return this.diagnostics;
    }

    public void setDiagnostics(Set<Diagnostic> diagnostics) {
        if (this.diagnostics != null) {
            this.diagnostics.forEach(i -> i.setOnt(null));
        }
        if (diagnostics != null) {
            diagnostics.forEach(i -> i.setOnt(this));
        }
        this.diagnostics = diagnostics;
    }

    public ONT diagnostics(Set<Diagnostic> diagnostics) {
        this.setDiagnostics(diagnostics);
        return this;
    }

    public ONT addDiagnostic(Diagnostic diagnostic) {
        this.diagnostics.add(diagnostic);
        diagnostic.setOnt(this);
        return this;
    }

    public ONT removeDiagnostic(Diagnostic diagnostic) {
        this.diagnostics.remove(diagnostic);
        diagnostic.setOnt(null);
        return this;
    }

    public Set<Metrique> getMetriques() {
        return this.metriques;
    }

    public void setMetriques(Set<Metrique> metriques) {
        if (this.metriques != null) {
            this.metriques.forEach(i -> i.setOnt(null));
        }
        if (metriques != null) {
            metriques.forEach(i -> i.setOnt(this));
        }
        this.metriques = metriques;
    }

    public ONT metriques(Set<Metrique> metriques) {
        this.setMetriques(metriques);
        return this;
    }

    public ONT addMetrique(Metrique metrique) {
        this.metriques.add(metrique);
        metrique.setOnt(this);
        return this;
    }

    public ONT removeMetrique(Metrique metrique) {
        this.metriques.remove(metrique);
        metrique.setOnt(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ONT)) {
            return false;
        }
        return getId() != null && getId().equals(((ONT) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ONT{" +
            "id=" + getId() +
            ", index='" + getIndex() + "'" +
            ", ontID='" + getOntID() + "'" +
            ", serviceId='" + getServiceId() + "'" +
            ", slot='" + getSlot() + "'" +
            ", pon='" + getPon() + "'" +
            ", ponIndex='" + getPonIndex() + "'" +
            ", maxUp='" + getMaxUp() + "'" +
            ", maxDown='" + getMaxDown() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", etatOlt='" + getEtatOlt() + "'" +
            ", status='" + getStatus() + "'" +
            ", statusAt='" + getStatusAt() + "'" +
            ", nbreLignesCouper=" + getNbreLignesCouper() +
            "}";
    }
}
