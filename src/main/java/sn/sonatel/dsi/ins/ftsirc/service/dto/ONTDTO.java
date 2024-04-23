package sn.sonatel.dsi.ins.ftsirc.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link sn.sonatel.dsi.ins.ftsirc.domain.ONT} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ONTDTO implements Serializable {

    private Long id;

    @NotNull
    private String index;

    @NotNull
    private String ontIP;

    @NotNull
    private String serviceId;

    @NotNull
    private String slot;

    @NotNull
    private String pon;

    @NotNull
    private String ponIndex;

    private String maxUp;

    private String maxDown;

    private LocalDate createdAt;

    private LocalDate updatedAt;

    private ClientDTO client;

    private OLTDTO olt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getOntIP() {
        return ontIP;
    }

    public void setOntIP(String ontIP) {
        this.ontIP = ontIP;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    public String getPon() {
        return pon;
    }

    public void setPon(String pon) {
        this.pon = pon;
    }

    public String getPonIndex() {
        return ponIndex;
    }

    public void setPonIndex(String ponIndex) {
        this.ponIndex = ponIndex;
    }

    public String getMaxUp() {
        return maxUp;
    }

    public void setMaxUp(String maxUp) {
        this.maxUp = maxUp;
    }

    public String getMaxDown() {
        return maxDown;
    }

    public void setMaxDown(String maxDown) {
        this.maxDown = maxDown;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public ClientDTO getClient() {
        return client;
    }

    public void setClient(ClientDTO client) {
        this.client = client;
    }

    public OLTDTO getOlt() {
        return olt;
    }

    public void setOlt(OLTDTO olt) {
        this.olt = olt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ONTDTO)) {
            return false;
        }

        ONTDTO oNTDTO = (ONTDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, oNTDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ONTDTO{" +
            "id=" + getId() +
            ", index='" + getIndex() + "'" +
            ", ontIP='" + getOntIP() + "'" +
            ", serviceId='" + getServiceId() + "'" +
            ", slot='" + getSlot() + "'" +
            ", pon='" + getPon() + "'" +
            ", ponIndex='" + getPonIndex() + "'" +
            ", maxUp='" + getMaxUp() + "'" +
            ", maxDown='" + getMaxDown() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", client=" + getClient() +
            ", olt=" + getOlt() +
            "}";
    }
}
