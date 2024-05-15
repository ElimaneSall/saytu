package sn.sonatel.dsi.ins.ftsirc.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link sn.sonatel.dsi.ins.ftsirc.domain.ONT} entity. This class is used
 * in {@link sn.sonatel.dsi.ins.ftsirc.web.rest.ONTResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /onts?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ONTCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter index;

    private StringFilter ontID;

    private StringFilter serviceId;

    private StringFilter slot;

    private StringFilter pon;

    private StringFilter ponIndex;

    private StringFilter maxUp;

    private StringFilter maxDown;

    private LocalDateFilter createdAt;

    private LocalDateFilter updatedAt;

    private LongFilter clientId;

    private LongFilter oltId;

    private LongFilter diagnosticId;

    private LongFilter metriqueId;

    private Boolean distinct;

    public ONTCriteria() {}

    public ONTCriteria(ONTCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.index = other.index == null ? null : other.index.copy();
        this.ontID = other.ontID == null ? null : other.ontID.copy();
        this.serviceId = other.serviceId == null ? null : other.serviceId.copy();
        this.slot = other.slot == null ? null : other.slot.copy();
        this.pon = other.pon == null ? null : other.pon.copy();
        this.ponIndex = other.ponIndex == null ? null : other.ponIndex.copy();
        this.maxUp = other.maxUp == null ? null : other.maxUp.copy();
        this.maxDown = other.maxDown == null ? null : other.maxDown.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.clientId = other.clientId == null ? null : other.clientId.copy();
        this.oltId = other.oltId == null ? null : other.oltId.copy();
        this.diagnosticId = other.diagnosticId == null ? null : other.diagnosticId.copy();
        this.metriqueId = other.metriqueId == null ? null : other.metriqueId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ONTCriteria copy() {
        return new ONTCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getIndex() {
        return index;
    }

    public StringFilter index() {
        if (index == null) {
            index = new StringFilter();
        }
        return index;
    }

    public void setIndex(StringFilter index) {
        this.index = index;
    }

    public StringFilter getOntID() {
        return ontID;
    }

    public StringFilter ontID() {
        if (ontID == null) {
            ontID = new StringFilter();
        }
        return ontID;
    }

    public void setOntID(StringFilter ontID) {
        this.ontID = ontID;
    }

    public StringFilter getServiceId() {
        return serviceId;
    }

    public StringFilter serviceId() {
        if (serviceId == null) {
            serviceId = new StringFilter();
        }
        return serviceId;
    }

    public void setServiceId(StringFilter serviceId) {
        this.serviceId = serviceId;
    }

    public StringFilter getSlot() {
        return slot;
    }

    public StringFilter slot() {
        if (slot == null) {
            slot = new StringFilter();
        }
        return slot;
    }

    public void setSlot(StringFilter slot) {
        this.slot = slot;
    }

    public StringFilter getPon() {
        return pon;
    }

    public StringFilter pon() {
        if (pon == null) {
            pon = new StringFilter();
        }
        return pon;
    }

    public void setPon(StringFilter pon) {
        this.pon = pon;
    }

    public StringFilter getPonIndex() {
        return ponIndex;
    }

    public StringFilter ponIndex() {
        if (ponIndex == null) {
            ponIndex = new StringFilter();
        }
        return ponIndex;
    }

    public void setPonIndex(StringFilter ponIndex) {
        this.ponIndex = ponIndex;
    }

    public StringFilter getMaxUp() {
        return maxUp;
    }

    public StringFilter maxUp() {
        if (maxUp == null) {
            maxUp = new StringFilter();
        }
        return maxUp;
    }

    public void setMaxUp(StringFilter maxUp) {
        this.maxUp = maxUp;
    }

    public StringFilter getMaxDown() {
        return maxDown;
    }

    public StringFilter maxDown() {
        if (maxDown == null) {
            maxDown = new StringFilter();
        }
        return maxDown;
    }

    public void setMaxDown(StringFilter maxDown) {
        this.maxDown = maxDown;
    }

    public LocalDateFilter getCreatedAt() {
        return createdAt;
    }

    public LocalDateFilter createdAt() {
        if (createdAt == null) {
            createdAt = new LocalDateFilter();
        }
        return createdAt;
    }

    public void setCreatedAt(LocalDateFilter createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateFilter getUpdatedAt() {
        return updatedAt;
    }

    public LocalDateFilter updatedAt() {
        if (updatedAt == null) {
            updatedAt = new LocalDateFilter();
        }
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateFilter updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LongFilter getClientId() {
        return clientId;
    }

    public LongFilter clientId() {
        if (clientId == null) {
            clientId = new LongFilter();
        }
        return clientId;
    }

    public void setClientId(LongFilter clientId) {
        this.clientId = clientId;
    }

    public LongFilter getOltId() {
        return oltId;
    }

    public LongFilter oltId() {
        if (oltId == null) {
            oltId = new LongFilter();
        }
        return oltId;
    }

    public void setOltId(LongFilter oltId) {
        this.oltId = oltId;
    }

    public LongFilter getDiagnosticId() {
        return diagnosticId;
    }

    public LongFilter diagnosticId() {
        if (diagnosticId == null) {
            diagnosticId = new LongFilter();
        }
        return diagnosticId;
    }

    public void setDiagnosticId(LongFilter diagnosticId) {
        this.diagnosticId = diagnosticId;
    }

    public LongFilter getMetriqueId() {
        return metriqueId;
    }

    public LongFilter metriqueId() {
        if (metriqueId == null) {
            metriqueId = new LongFilter();
        }
        return metriqueId;
    }

    public void setMetriqueId(LongFilter metriqueId) {
        this.metriqueId = metriqueId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ONTCriteria that = (ONTCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(index, that.index) &&
            Objects.equals(ontID, that.ontID) &&
            Objects.equals(serviceId, that.serviceId) &&
            Objects.equals(slot, that.slot) &&
            Objects.equals(pon, that.pon) &&
            Objects.equals(ponIndex, that.ponIndex) &&
            Objects.equals(maxUp, that.maxUp) &&
            Objects.equals(maxDown, that.maxDown) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(clientId, that.clientId) &&
            Objects.equals(oltId, that.oltId) &&
            Objects.equals(diagnosticId, that.diagnosticId) &&
            Objects.equals(metriqueId, that.metriqueId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            index,
            ontID,
            serviceId,
            slot,
            pon,
            ponIndex,
            maxUp,
            maxDown,
            createdAt,
            updatedAt,
            clientId,
            oltId,
            diagnosticId,
            metriqueId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ONTCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (index != null ? "index=" + index + ", " : "") +
            (ontID != null ? "ontID=" + ontID + ", " : "") +
            (serviceId != null ? "serviceId=" + serviceId + ", " : "") +
            (slot != null ? "slot=" + slot + ", " : "") +
            (pon != null ? "pon=" + pon + ", " : "") +
            (ponIndex != null ? "ponIndex=" + ponIndex + ", " : "") +
            (maxUp != null ? "maxUp=" + maxUp + ", " : "") +
            (maxDown != null ? "maxDown=" + maxDown + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (updatedAt != null ? "updatedAt=" + updatedAt + ", " : "") +
            (clientId != null ? "clientId=" + clientId + ", " : "") +
            (oltId != null ? "oltId=" + oltId + ", " : "") +
            (diagnosticId != null ? "diagnosticId=" + diagnosticId + ", " : "") +
            (metriqueId != null ? "metriqueId=" + metriqueId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
