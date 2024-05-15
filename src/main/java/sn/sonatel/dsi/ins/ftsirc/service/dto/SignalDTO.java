package sn.sonatel.dsi.ins.ftsirc.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link sn.sonatel.dsi.ins.ftsirc.domain.Signal} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SignalDTO implements Serializable {

    private Long id;

    @NotNull
    private String libelle;

    private Double valueSignal;

    @NotNull
    private Double seuilMin;

    @NotNull
    private Double seuilMax;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Double getValueSignal() {
        return valueSignal;
    }

    public void setValueSignal(Double valueSignal) {
        this.valueSignal = valueSignal;
    }

    public Double getSeuilMin() {
        return seuilMin;
    }

    public void setSeuilMin(Double seuilMin) {
        this.seuilMin = seuilMin;
    }

    public Double getSeuilMax() {
        return seuilMax;
    }

    public void setSeuilMax(Double seuilMax) {
        this.seuilMax = seuilMax;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SignalDTO)) {
            return false;
        }

        SignalDTO signalDTO = (SignalDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, signalDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SignalDTO{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", valueSignal=" + getValueSignal() +
            ", seuilMin=" + getSeuilMin() +
            ", seuilMax=" + getSeuilMax() +
            "}";
    }
}
