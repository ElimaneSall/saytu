package sn.sonatel.dsi.ins.ftsirc.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link sn.sonatel.dsi.ins.ftsirc.domain.Metrique} entity. This class is used
 * in {@link sn.sonatel.dsi.ins.ftsirc.web.rest.MetriqueResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /metriques?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MetriqueCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter oltPower;

    private StringFilter ontPower;

    private LocalDateFilter createdAt;

    private LongFilter ontId;

    private Boolean distinct;

    public MetriqueCriteria() {}

    public MetriqueCriteria(MetriqueCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.oltPower = other.optionalOltPower().map(StringFilter::copy).orElse(null);
        this.ontPower = other.optionalOntPower().map(StringFilter::copy).orElse(null);
        this.createdAt = other.optionalCreatedAt().map(LocalDateFilter::copy).orElse(null);
        this.ontId = other.optionalOntId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public MetriqueCriteria copy() {
        return new MetriqueCriteria(this);
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

    public StringFilter getOltPower() {
        return oltPower;
    }

    public Optional<StringFilter> optionalOltPower() {
        return Optional.ofNullable(oltPower);
    }

    public StringFilter oltPower() {
        if (oltPower == null) {
            setOltPower(new StringFilter());
        }
        return oltPower;
    }

    public void setOltPower(StringFilter oltPower) {
        this.oltPower = oltPower;
    }

    public StringFilter getOntPower() {
        return ontPower;
    }

    public Optional<StringFilter> optionalOntPower() {
        return Optional.ofNullable(ontPower);
    }

    public StringFilter ontPower() {
        if (ontPower == null) {
            setOntPower(new StringFilter());
        }
        return ontPower;
    }

    public void setOntPower(StringFilter ontPower) {
        this.ontPower = ontPower;
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

    public LongFilter getOntId() {
        return ontId;
    }

    public Optional<LongFilter> optionalOntId() {
        return Optional.ofNullable(ontId);
    }

    public LongFilter ontId() {
        if (ontId == null) {
            setOntId(new LongFilter());
        }
        return ontId;
    }

    public void setOntId(LongFilter ontId) {
        this.ontId = ontId;
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
        final MetriqueCriteria that = (MetriqueCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(oltPower, that.oltPower) &&
            Objects.equals(ontPower, that.ontPower) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(ontId, that.ontId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, oltPower, ontPower, createdAt, ontId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MetriqueCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalOltPower().map(f -> "oltPower=" + f + ", ").orElse("") +
            optionalOntPower().map(f -> "ontPower=" + f + ", ").orElse("") +
            optionalCreatedAt().map(f -> "createdAt=" + f + ", ").orElse("") +
            optionalOntId().map(f -> "ontId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
