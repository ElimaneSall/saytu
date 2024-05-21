package sn.sonatel.dsi.ins.ftsirc.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import sn.sonatel.dsi.ins.ftsirc.domain.enumeration.StatutONT;
import sn.sonatel.dsi.ins.ftsirc.domain.enumeration.TypeDiagnostic;

/**
 * A DTO for the {@link sn.sonatel.dsi.ins.ftsirc.domain.Diagnostic} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DiagnosticDTO implements Serializable {

    private Long id;

    private StatutONT statutONT;

    private LocalDate dateDiagnostic;

    private TypeDiagnostic typeDiagnostic;

    private String debitUp;

    private String debitDown;

    private String powerONT;

    private String powerOLT;

    private SignalDTO signal;

    private ONTDTO ont;

    private Set<AnomalieDTO> anomalies = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StatutONT getStatutONT() {
        return statutONT;
    }

    public void setStatutONT(StatutONT statutONT) {
        this.statutONT = statutONT;
    }

    public LocalDate getDateDiagnostic() {
        return dateDiagnostic;
    }

    public void setDateDiagnostic(LocalDate dateDiagnostic) {
        this.dateDiagnostic = dateDiagnostic;
    }

    public TypeDiagnostic getTypeDiagnostic() {
        return typeDiagnostic;
    }

    public void setTypeDiagnostic(TypeDiagnostic typeDiagnostic) {
        this.typeDiagnostic = typeDiagnostic;
    }

    public String getDebitUp() {
        return debitUp;
    }

    public void setDebitUp(String debitUp) {
        this.debitUp = debitUp;
    }

    public String getDebitDown() {
        return debitDown;
    }

    public void setDebitDown(String debitDown) {
        this.debitDown = debitDown;
    }

    public String getPowerONT() {
        return powerONT;
    }

    public void setPowerONT(String powerONT) {
        this.powerONT = powerONT;
    }

    public String getPowerOLT() {
        return powerOLT;
    }

    public void setPowerOLT(String powerOLT) {
        this.powerOLT = powerOLT;
    }

    public SignalDTO getSignal() {
        return signal;
    }

    public void setSignal(SignalDTO signal) {
        this.signal = signal;
    }

    public ONTDTO getOnt() {
        return ont;
    }

    public void setOnt(ONTDTO ont) {
        this.ont = ont;
    }

    public Set<AnomalieDTO> getAnomalies() {
        return anomalies;
    }

    public void setAnomalies(Set<AnomalieDTO> anomalies) {
        this.anomalies = anomalies;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DiagnosticDTO)) {
            return false;
        }

        DiagnosticDTO diagnosticDTO = (DiagnosticDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, diagnosticDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DiagnosticDTO{" +
            "id=" + getId() +
            ", statutONT='" + getStatutONT() + "'" +
            ", dateDiagnostic='" + getDateDiagnostic() + "'" +
            ", typeDiagnostic='" + getTypeDiagnostic() + "'" +
            ", debitUp='" + getDebitUp() + "'" +
            ", debitDown='" + getDebitDown() + "'" +
            ", powerONT='" + getPowerONT() + "'" +
            ", powerOLT='" + getPowerOLT() + "'" +
            ", signal=" + getSignal() +
            ", ont=" + getOnt() +
            ", anomalies=" + getAnomalies() +
            "}";
    }
}
