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
import sn.sonatel.dsi.ins.ftsirc.domain.enumeration.StatutONT;
import sn.sonatel.dsi.ins.ftsirc.domain.enumeration.TypeDiagnostic;

/**
 * A Diagnostic.
 */
@Entity
@Table(name = "diagnostic")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Diagnostic implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "jhi_index", nullable = false)
    private String index;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut_ont")
    private StatutONT statutONT;

    @Column(name = "debit_up")
    private String debitUp;

    @Column(name = "debit_down")
    private String debitDown;

    @Column(name = "date_diagnostic")
    private LocalDate dateDiagnostic;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_diagnostic")
    private TypeDiagnostic typeDiagnostic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "diagnostics" }, allowSetters = true)
    private Signal signal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "client", "olt", "diagnostics", "metriques" }, allowSetters = true)
    private ONT ont;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_diagnostic__anomalie",
        joinColumns = @JoinColumn(name = "diagnostic_id"),
        inverseJoinColumns = @JoinColumn(name = "anomalie_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "diagnostics" }, allowSetters = true)
    private Set<Anomalie> anomalies = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Diagnostic id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIndex() {
        return this.index;
    }

    public Diagnostic index(String index) {
        this.setIndex(index);
        return this;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public StatutONT getStatutONT() {
        return this.statutONT;
    }

    public Diagnostic statutONT(StatutONT statutONT) {
        this.setStatutONT(statutONT);
        return this;
    }

    public void setStatutONT(StatutONT statutONT) {
        this.statutONT = statutONT;
    }

    public String getDebitUp() {
        return this.debitUp;
    }

    public Diagnostic debitUp(String debitUp) {
        this.setDebitUp(debitUp);
        return this;
    }

    public void setDebitUp(String debitUp) {
        this.debitUp = debitUp;
    }

    public String getDebitDown() {
        return this.debitDown;
    }

    public Diagnostic debitDown(String debitDown) {
        this.setDebitDown(debitDown);
        return this;
    }

    public void setDebitDown(String debitDown) {
        this.debitDown = debitDown;
    }

    public LocalDate getDateDiagnostic() {
        return this.dateDiagnostic;
    }

    public Diagnostic dateDiagnostic(LocalDate dateDiagnostic) {
        this.setDateDiagnostic(dateDiagnostic);
        return this;
    }

    public void setDateDiagnostic(LocalDate dateDiagnostic) {
        this.dateDiagnostic = dateDiagnostic;
    }

    public TypeDiagnostic getTypeDiagnostic() {
        return this.typeDiagnostic;
    }

    public Diagnostic typeDiagnostic(TypeDiagnostic typeDiagnostic) {
        this.setTypeDiagnostic(typeDiagnostic);
        return this;
    }

    public void setTypeDiagnostic(TypeDiagnostic typeDiagnostic) {
        this.typeDiagnostic = typeDiagnostic;
    }

    public Signal getSignal() {
        return this.signal;
    }

    public void setSignal(Signal signal) {
        this.signal = signal;
    }

    public Diagnostic signal(Signal signal) {
        this.setSignal(signal);
        return this;
    }

    public ONT getOnt() {
        return this.ont;
    }

    public void setOnt(ONT oNT) {
        this.ont = oNT;
    }

    public Diagnostic ont(ONT oNT) {
        this.setOnt(oNT);
        return this;
    }

    public Set<Anomalie> getAnomalies() {
        return this.anomalies;
    }

    public void setAnomalies(Set<Anomalie> anomalies) {
        this.anomalies = anomalies;
    }

    public Diagnostic anomalies(Set<Anomalie> anomalies) {
        this.setAnomalies(anomalies);
        return this;
    }

    public Diagnostic addAnomalie(Anomalie anomalie) {
        this.anomalies.add(anomalie);
        return this;
    }

    public Diagnostic removeAnomalie(Anomalie anomalie) {
        this.anomalies.remove(anomalie);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Diagnostic)) {
            return false;
        }
        return getId() != null && getId().equals(((Diagnostic) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Diagnostic{" +
            "id=" + getId() +
            ", index='" + getIndex() + "'" +
            ", statutONT='" + getStatutONT() + "'" +
            ", debitUp='" + getDebitUp() + "'" +
            ", debitDown='" + getDebitDown() + "'" +
            ", dateDiagnostic='" + getDateDiagnostic() + "'" +
            ", typeDiagnostic='" + getTypeDiagnostic() + "'" +
            "}";
    }
}
