package sn.sonatel.dsi.ins.ftsirc.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link sn.sonatel.dsi.ins.ftsirc.domain.Signal} entity. This class is used
 * in {@link sn.sonatel.dsi.ins.ftsirc.web.rest.SignalResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /signals?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SignalCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter libelle;

    private DoubleFilter seuilMin;

    private DoubleFilter seuilMax;

    private LongFilter diagnosticId;

    private Boolean distinct;

    public SignalCriteria() {}

    public SignalCriteria(SignalCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.libelle = other.libelle == null ? null : other.libelle.copy();
        this.seuilMin = other.seuilMin == null ? null : other.seuilMin.copy();
        this.seuilMax = other.seuilMax == null ? null : other.seuilMax.copy();
        this.diagnosticId = other.diagnosticId == null ? null : other.diagnosticId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public SignalCriteria copy() {
        return new SignalCriteria(this);
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

    public StringFilter getLibelle() {
        return libelle;
    }

    public StringFilter libelle() {
        if (libelle == null) {
            libelle = new StringFilter();
        }
        return libelle;
    }

    public void setLibelle(StringFilter libelle) {
        this.libelle = libelle;
    }

    public DoubleFilter getSeuilMin() {
        return seuilMin;
    }

    public DoubleFilter seuilMin() {
        if (seuilMin == null) {
            seuilMin = new DoubleFilter();
        }
        return seuilMin;
    }

    public void setSeuilMin(DoubleFilter seuilMin) {
        this.seuilMin = seuilMin;
    }

    public DoubleFilter getSeuilMax() {
        return seuilMax;
    }

    public DoubleFilter seuilMax() {
        if (seuilMax == null) {
            seuilMax = new DoubleFilter();
        }
        return seuilMax;
    }

    public void setSeuilMax(DoubleFilter seuilMax) {
        this.seuilMax = seuilMax;
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
        final SignalCriteria that = (SignalCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(libelle, that.libelle) &&
            Objects.equals(seuilMin, that.seuilMin) &&
            Objects.equals(seuilMax, that.seuilMax) &&
            Objects.equals(diagnosticId, that.diagnosticId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, libelle, seuilMin, seuilMax, diagnosticId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SignalCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (libelle != null ? "libelle=" + libelle + ", " : "") +
            (seuilMin != null ? "seuilMin=" + seuilMin + ", " : "") +
            (seuilMax != null ? "seuilMax=" + seuilMax + ", " : "") +
            (diagnosticId != null ? "diagnosticId=" + diagnosticId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
