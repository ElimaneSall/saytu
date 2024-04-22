package sn.sonatel.dsi.ins.ftsirc.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Metrique.
 */
@Entity
@Table(name = "metrique")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Metrique implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "olt_power", nullable = false)
    private String oltPower;

    @NotNull
    @Column(name = "ont_power", nullable = false)
    private String ontPower;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "client", "olt", "diagnostics", "metriques" }, allowSetters = true)
    private ONT ont;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Metrique id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOltPower() {
        return this.oltPower;
    }

    public Metrique oltPower(String oltPower) {
        this.setOltPower(oltPower);
        return this;
    }

    public void setOltPower(String oltPower) {
        this.oltPower = oltPower;
    }

    public String getOntPower() {
        return this.ontPower;
    }

    public Metrique ontPower(String ontPower) {
        this.setOntPower(ontPower);
        return this;
    }

    public void setOntPower(String ontPower) {
        this.ontPower = ontPower;
    }

    public LocalDate getCreatedAt() {
        return this.createdAt;
    }

    public Metrique createdAt(LocalDate createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public ONT getOnt() {
        return this.ont;
    }

    public void setOnt(ONT oNT) {
        this.ont = oNT;
    }

    public Metrique ont(ONT oNT) {
        this.setOnt(oNT);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Metrique)) {
            return false;
        }
        return getId() != null && getId().equals(((Metrique) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Metrique{" +
            "id=" + getId() +
            ", oltPower='" + getOltPower() + "'" +
            ", ontPower='" + getOntPower() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}
