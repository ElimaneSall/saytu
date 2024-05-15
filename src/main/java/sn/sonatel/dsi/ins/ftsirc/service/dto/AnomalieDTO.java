package sn.sonatel.dsi.ins.ftsirc.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link sn.sonatel.dsi.ins.ftsirc.domain.Anomalie} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AnomalieDTO implements Serializable {

    private Long id;

    @NotNull
    private String libelle;

    @Lob
    private String description;

    private String etat;

    @Lob
    private String recommandation;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getRecommandation() {
        return recommandation;
    }

    public void setRecommandation(String recommandation) {
        this.recommandation = recommandation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AnomalieDTO)) {
            return false;
        }

        AnomalieDTO anomalieDTO = (AnomalieDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, anomalieDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AnomalieDTO{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", description='" + getDescription() + "'" +
            ", etat='" + getEtat() + "'" +
            ", recommandation='" + getRecommandation() + "'" +
            "}";
    }
}
