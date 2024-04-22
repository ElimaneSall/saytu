package sn.sonatel.dsi.ins.ftsirc.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Signal.
 */
@Entity
@Table(name = "jhi_signal")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Signal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "libelle", nullable = false, unique = true)
    private String libelle;

    @NotNull
    @Column(name = "seuil_min", nullable = false)
    private Double seuilMin;

    @NotNull
    @Column(name = "seuil_max", nullable = false)
    private Double seuilMax;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "signal")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "typeDiagnostic", "signal", "ont", "anomalies" }, allowSetters = true)
    private Set<Diagnostic> diagnostics = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Signal id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return this.libelle;
    }

    public Signal libelle(String libelle) {
        this.setLibelle(libelle);
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Double getSeuilMin() {
        return this.seuilMin;
    }

    public Signal seuilMin(Double seuilMin) {
        this.setSeuilMin(seuilMin);
        return this;
    }

    public void setSeuilMin(Double seuilMin) {
        this.seuilMin = seuilMin;
    }

    public Double getSeuilMax() {
        return this.seuilMax;
    }

    public Signal seuilMax(Double seuilMax) {
        this.setSeuilMax(seuilMax);
        return this;
    }

    public void setSeuilMax(Double seuilMax) {
        this.seuilMax = seuilMax;
    }

    public Set<Diagnostic> getDiagnostics() {
        return this.diagnostics;
    }

    public void setDiagnostics(Set<Diagnostic> diagnostics) {
        if (this.diagnostics != null) {
            this.diagnostics.forEach(i -> i.setSignal(null));
        }
        if (diagnostics != null) {
            diagnostics.forEach(i -> i.setSignal(this));
        }
        this.diagnostics = diagnostics;
    }

    public Signal diagnostics(Set<Diagnostic> diagnostics) {
        this.setDiagnostics(diagnostics);
        return this;
    }

    public Signal addDiagnostic(Diagnostic diagnostic) {
        this.diagnostics.add(diagnostic);
        diagnostic.setSignal(this);
        return this;
    }

    public Signal removeDiagnostic(Diagnostic diagnostic) {
        this.diagnostics.remove(diagnostic);
        diagnostic.setSignal(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Signal)) {
            return false;
        }
        return getId() != null && getId().equals(((Signal) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Signal{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", seuilMin=" + getSeuilMin() +
            ", seuilMax=" + getSeuilMax() +
            "}";
    }
}
