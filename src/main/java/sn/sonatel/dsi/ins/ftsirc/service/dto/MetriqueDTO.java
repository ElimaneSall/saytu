package sn.sonatel.dsi.ins.ftsirc.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link sn.sonatel.dsi.ins.ftsirc.domain.Metrique} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MetriqueDTO implements Serializable {

    private Long id;

    @NotNull
    private String oltPower;

    @NotNull
    private String ontPower;

    private LocalDate createdAt;

    private ONTDTO ont;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOltPower() {
        return oltPower;
    }

    public void setOltPower(String oltPower) {
        this.oltPower = oltPower;
    }

    public String getOntPower() {
        return ontPower;
    }

    public void setOntPower(String ontPower) {
        this.ontPower = ontPower;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public ONTDTO getOnt() {
        return ont;
    }

    public void setOnt(ONTDTO ont) {
        this.ont = ont;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MetriqueDTO)) {
            return false;
        }

        MetriqueDTO metriqueDTO = (MetriqueDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, metriqueDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MetriqueDTO{" +
            "id=" + getId() +
            ", oltPower='" + getOltPower() + "'" +
            ", ontPower='" + getOntPower() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", ont=" + getOnt() +
            "}";
    }
}
