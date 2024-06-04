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
 * A Anomalie.
 */
@Entity
@Table(name = "anomalie")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Anomalie implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "libelle", nullable = false, unique = true)
    private String libelle;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "etat")
    private String etat;

    @Lob
    @Column(name = "recommandation")
    private String recommandation;

    @Column(name = "code")
    private Integer code;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "anomalies")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "signal", "ont", "anomalies" }, allowSetters = true)
    private Set<Diagnostic> diagnostics = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Anomalie id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return this.libelle;
    }

    public Anomalie libelle(String libelle) {
        this.setLibelle(libelle);
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getDescription() {
        return this.description;
    }

    public Anomalie description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEtat() {
        return this.etat;
    }

    public Anomalie etat(String etat) {
        this.setEtat(etat);
        return this;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getRecommandation() {
        return this.recommandation;
    }

    public Anomalie recommandation(String recommandation) {
        this.setRecommandation(recommandation);
        return this;
    }

    public void setRecommandation(String recommandation) {
        this.recommandation = recommandation;
    }

    public Integer getCode() {
        return this.code;
    }

    public Anomalie code(Integer code) {
        this.setCode(code);
        return this;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Set<Diagnostic> getDiagnostics() {
        return this.diagnostics;
    }

    public void setDiagnostics(Set<Diagnostic> diagnostics) {
        if (this.diagnostics != null) {
            this.diagnostics.forEach(i -> i.removeAnomalie(this));
        }
        if (diagnostics != null) {
            diagnostics.forEach(i -> i.addAnomalie(this));
        }
        this.diagnostics = diagnostics;
    }

    public Anomalie diagnostics(Set<Diagnostic> diagnostics) {
        this.setDiagnostics(diagnostics);
        return this;
    }

    public Anomalie addDiagnostic(Diagnostic diagnostic) {
        this.diagnostics.add(diagnostic);
        diagnostic.getAnomalies().add(this);
        return this;
    }

    public Anomalie removeDiagnostic(Diagnostic diagnostic) {
        this.diagnostics.remove(diagnostic);
        diagnostic.getAnomalies().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Anomalie)) {
            return false;
        }
        return getId() != null && getId().equals(((Anomalie) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Anomalie{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", description='" + getDescription() + "'" +
            ", etat='" + getEtat() + "'" +
            ", recommandation='" + getRecommandation() + "'" +
            ", code=" + getCode() +
            "}";
    }
}
