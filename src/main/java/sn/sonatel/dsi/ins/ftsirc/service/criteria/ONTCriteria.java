package sn.sonatel.dsi.ins.ftsirc.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
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

    private StringFilter ontIP;

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
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.index = other.optionalIndex().map(StringFilter::copy).orElse(null);
        this.ontIP = other.optionalOntIP().map(StringFilter::copy).orElse(null);
        this.serviceId = other.optionalServiceId().map(StringFilter::copy).orElse(null);
        this.slot = other.optionalSlot().map(StringFilter::copy).orElse(null);
        this.pon = other.optionalPon().map(StringFilter::copy).orElse(null);
        this.ponIndex = other.optionalPonIndex().map(StringFilter::copy).orElse(null);
        this.maxUp = other.optionalMaxUp().map(StringFilter::copy).orElse(null);
        this.maxDown = other.optionalMaxDown().map(StringFilter::copy).orElse(null);
        this.createdAt = other.optionalCreatedAt().map(LocalDateFilter::copy).orElse(null);
        this.updatedAt = other.optionalUpdatedAt().map(LocalDateFilter::copy).orElse(null);
        this.clientId = other.optionalClientId().map(LongFilter::copy).orElse(null);
        this.oltId = other.optionalOltId().map(LongFilter::copy).orElse(null);
        this.diagnosticId = other.optionalDiagnosticId().map(LongFilter::copy).orElse(null);
        this.metriqueId = other.optionalMetriqueId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ONTCriteria copy() {
        return new ONTCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getIndex() {
        return index;
    }

    public Optional<StringFilter> optionalIndex() {
        return Optional.ofNullable(index);
    }

    public StringFilter index() {
        if (index == null) {
            setIndex(new StringFilter());
        }
        return index;
    }

    public void setIndex(StringFilter index) {
        this.index = index;
    }

    public StringFilter getOntIP() {
        return ontIP;
    }

    public Optional<StringFilter> optionalOntIP() {
        return Optional.ofNullable(ontIP);
    }

    public StringFilter ontIP() {
        if (ontIP == null) {
            setOntIP(new StringFilter());
        }
        return ontIP;
    }

    public void setOntIP(StringFilter ontIP) {
        this.ontIP = ontIP;
    }

    public StringFilter getServiceId() {
        return serviceId;
    }

    public Optional<StringFilter> optionalServiceId() {
        return Optional.ofNullable(serviceId);
    }

    public StringFilter serviceId() {
        if (serviceId == null) {
            setServiceId(new StringFilter());
        }
        return serviceId;
    }

    public void setServiceId(StringFilter serviceId) {
        this.serviceId = serviceId;
    }

    public StringFilter getSlot() {
        return slot;
    }

    public Optional<StringFilter> optionalSlot() {
        return Optional.ofNullable(slot);
    }

    public StringFilter slot() {
        if (slot == null) {
            setSlot(new StringFilter());
        }
        return slot;
    }

    public void setSlot(StringFilter slot) {
        this.slot = slot;
    }

    public StringFilter getPon() {
        return pon;
    }

    public Optional<StringFilter> optionalPon() {
        return Optional.ofNullable(pon);
    }

    public StringFilter pon() {
        if (pon == null) {
            setPon(new StringFilter());
        }
        return pon;
    }

    public void setPon(StringFilter pon) {
        this.pon = pon;
    }

    public StringFilter getPonIndex() {
        return ponIndex;
    }

    public Optional<StringFilter> optionalPonIndex() {
        return Optional.ofNullable(ponIndex);
    }

    public StringFilter ponIndex() {
        if (ponIndex == null) {
            setPonIndex(new StringFilter());
        }
        return ponIndex;
    }

    public void setPonIndex(StringFilter ponIndex) {
        this.ponIndex = ponIndex;
    }

    public StringFilter getMaxUp() {
        return maxUp;
    }

    public Optional<StringFilter> optionalMaxUp() {
        return Optional.ofNullable(maxUp);
    }

    public StringFilter maxUp() {
        if (maxUp == null) {
            setMaxUp(new StringFilter());
        }
        return maxUp;
    }

    public void setMaxUp(StringFilter maxUp) {
        this.maxUp = maxUp;
    }

    public StringFilter getMaxDown() {
        return maxDown;
    }

    public Optional<StringFilter> optionalMaxDown() {
        return Optional.ofNullable(maxDown);
    }

    public StringFilter maxDown() {
        if (maxDown == null) {
            setMaxDown(new StringFilter());
        }
        return maxDown;
    }

    public void setMaxDown(StringFilter maxDown) {
        this.maxDown = maxDown;
    }

    public LocalDateFilter getCreatedAt() {
        return createdAt;
    }

    public Optional<LocalDateFilter> optionalCreatedAt() {
        return Optional.ofNullable(createdAt);
    }

    public LocalDateFilter createdAt() {
        if (createdAt == null) {
            setCreatedAt(new LocalDateFilter());
        }
        return createdAt;
    }

    public void setCreatedAt(LocalDateFilter createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateFilter getUpdatedAt() {
        return updatedAt;
    }

    public Optional<LocalDateFilter> optionalUpdatedAt() {
        return Optional.ofNullable(updatedAt);
    }

    public LocalDateFilter updatedAt() {
        if (updatedAt == null) {
            setUpdatedAt(new LocalDateFilter());
        }
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateFilter updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LongFilter getClientId() {
        return clientId;
    }

    public Optional<LongFilter> optionalClientId() {
        return Optional.ofNullable(clientId);
    }

    public LongFilter clientId() {
        if (clientId == null) {
            setClientId(new LongFilter());
        }
        return clientId;
    }

    public void setClientId(LongFilter clientId) {
        this.clientId = clientId;
    }

    public LongFilter getOltId() {
        return oltId;
    }

    public Optional<LongFilter> optionalOltId() {
        return Optional.ofNullable(oltId);
    }

    public LongFilter oltId() {
        if (oltId == null) {
            setOltId(new LongFilter());
        }
        return oltId;
    }

    public void setOltId(LongFilter oltId) {
        this.oltId = oltId;
    }

    public LongFilter getDiagnosticId() {
        return diagnosticId;
    }

    public Optional<LongFilter> optionalDiagnosticId() {
        return Optional.ofNullable(diagnosticId);
    }

    public LongFilter diagnosticId() {
        if (diagnosticId == null) {
            setDiagnosticId(new LongFilter());
        }
        return diagnosticId;
    }

    public void setDiagnosticId(LongFilter diagnosticId) {
        this.diagnosticId = diagnosticId;
    }

    public LongFilter getMetriqueId() {
        return metriqueId;
    }

    public Optional<LongFilter> optionalMetriqueId() {
        return Optional.ofNullable(metriqueId);
    }

    public LongFilter metriqueId() {
        if (metriqueId == null) {
            setMetriqueId(new LongFilter());
        }
        return metriqueId;
    }

    public void setMetriqueId(LongFilter metriqueId) {
        this.metriqueId = metriqueId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
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
            Objects.equals(ontIP, that.ontIP) &&
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
            ontIP,
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
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalIndex().map(f -> "index=" + f + ", ").orElse("") +
            optionalOntIP().map(f -> "ontIP=" + f + ", ").orElse("") +
            optionalServiceId().map(f -> "serviceId=" + f + ", ").orElse("") +
            optionalSlot().map(f -> "slot=" + f + ", ").orElse("") +
            optionalPon().map(f -> "pon=" + f + ", ").orElse("") +
            optionalPonIndex().map(f -> "ponIndex=" + f + ", ").orElse("") +
            optionalMaxUp().map(f -> "maxUp=" + f + ", ").orElse("") +
            optionalMaxDown().map(f -> "maxDown=" + f + ", ").orElse("") +
            optionalCreatedAt().map(f -> "createdAt=" + f + ", ").orElse("") +
            optionalUpdatedAt().map(f -> "updatedAt=" + f + ", ").orElse("") +
            optionalClientId().map(f -> "clientId=" + f + ", ").orElse("") +
            optionalOltId().map(f -> "oltId=" + f + ", ").orElse("") +
            optionalDiagnosticId().map(f -> "diagnosticId=" + f + ", ").orElse("") +
            optionalMetriqueId().map(f -> "metriqueId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
