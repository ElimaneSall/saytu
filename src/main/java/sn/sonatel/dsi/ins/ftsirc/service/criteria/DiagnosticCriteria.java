package sn.sonatel.dsi.ins.ftsirc.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import sn.sonatel.dsi.ins.ftsirc.domain.enumeration.StatutONT;
import sn.sonatel.dsi.ins.ftsirc.domain.enumeration.TypeDiagnostic;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link sn.sonatel.dsi.ins.ftsirc.domain.Diagnostic} entity. This class is used
 * in {@link sn.sonatel.dsi.ins.ftsirc.web.rest.DiagnosticResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /diagnostics?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DiagnosticCriteria implements Serializable, Criteria {

    /**
     * Class for filtering StatutONT
     */
    public static class StatutONTFilter extends Filter<StatutONT> {

        public StatutONTFilter() {}

        public StatutONTFilter(StatutONTFilter filter) {
            super(filter);
        }

        @Override
        public StatutONTFilter copy() {
            return new StatutONTFilter(this);
        }
    }

    /**
     * Class for filtering TypeDiagnostic
     */
    public static class TypeDiagnosticFilter extends Filter<TypeDiagnostic> {

        public TypeDiagnosticFilter() {}

        public TypeDiagnosticFilter(TypeDiagnosticFilter filter) {
            super(filter);
        }

        @Override
        public TypeDiagnosticFilter copy() {
            return new TypeDiagnosticFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter index;

    private StatutONTFilter statutONT;

    private StringFilter debitUp;

    private StringFilter debitDown;

    private LocalDateFilter dateDiagnostic;

    private TypeDiagnosticFilter typeDiagnostic;

    private LongFilter signalId;

    private LongFilter ontId;

    private LongFilter anomalieId;

    private Boolean distinct;

    public DiagnosticCriteria() {}

    public DiagnosticCriteria(DiagnosticCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.index = other.index == null ? null : other.index.copy();
        this.statutONT = other.statutONT == null ? null : other.statutONT.copy();
        this.debitUp = other.debitUp == null ? null : other.debitUp.copy();
        this.debitDown = other.debitDown == null ? null : other.debitDown.copy();
        this.dateDiagnostic = other.dateDiagnostic == null ? null : other.dateDiagnostic.copy();
        this.typeDiagnostic = other.typeDiagnostic == null ? null : other.typeDiagnostic.copy();
        this.signalId = other.signalId == null ? null : other.signalId.copy();
        this.ontId = other.ontId == null ? null : other.ontId.copy();
        this.anomalieId = other.anomalieId == null ? null : other.anomalieId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public DiagnosticCriteria copy() {
        return new DiagnosticCriteria(this);
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

    public StatutONTFilter getStatutONT() {
        return statutONT;
    }

    public StatutONTFilter statutONT() {
        if (statutONT == null) {
            statutONT = new StatutONTFilter();
        }
        return statutONT;
    }

    public void setStatutONT(StatutONTFilter statutONT) {
        this.statutONT = statutONT;
    }

    public StringFilter getDebitUp() {
        return debitUp;
    }

    public StringFilter debitUp() {
        if (debitUp == null) {
            debitUp = new StringFilter();
        }
        return debitUp;
    }

    public void setDebitUp(StringFilter debitUp) {
        this.debitUp = debitUp;
    }

    public StringFilter getDebitDown() {
        return debitDown;
    }

    public StringFilter debitDown() {
        if (debitDown == null) {
            debitDown = new StringFilter();
        }
        return debitDown;
    }

    public void setDebitDown(StringFilter debitDown) {
        this.debitDown = debitDown;
    }

    public LocalDateFilter getDateDiagnostic() {
        return dateDiagnostic;
    }

    public LocalDateFilter dateDiagnostic() {
        if (dateDiagnostic == null) {
            dateDiagnostic = new LocalDateFilter();
        }
        return dateDiagnostic;
    }

    public void setDateDiagnostic(LocalDateFilter dateDiagnostic) {
        this.dateDiagnostic = dateDiagnostic;
    }

    public TypeDiagnosticFilter getTypeDiagnostic() {
        return typeDiagnostic;
    }

    public TypeDiagnosticFilter typeDiagnostic() {
        if (typeDiagnostic == null) {
            typeDiagnostic = new TypeDiagnosticFilter();
        }
        return typeDiagnostic;
    }

    public void setTypeDiagnostic(TypeDiagnosticFilter typeDiagnostic) {
        this.typeDiagnostic = typeDiagnostic;
    }

    public LongFilter getSignalId() {
        return signalId;
    }

    public LongFilter signalId() {
        if (signalId == null) {
            signalId = new LongFilter();
        }
        return signalId;
    }

    public void setSignalId(LongFilter signalId) {
        this.signalId = signalId;
    }

    public LongFilter getOntId() {
        return ontId;
    }

    public LongFilter ontId() {
        if (ontId == null) {
            ontId = new LongFilter();
        }
        return ontId;
    }

    public void setOntId(LongFilter ontId) {
        this.ontId = ontId;
    }

    public LongFilter getAnomalieId() {
        return anomalieId;
    }

    public LongFilter anomalieId() {
        if (anomalieId == null) {
            anomalieId = new LongFilter();
        }
        return anomalieId;
    }

    public void setAnomalieId(LongFilter anomalieId) {
        this.anomalieId = anomalieId;
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
        final DiagnosticCriteria that = (DiagnosticCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(index, that.index) &&
            Objects.equals(statutONT, that.statutONT) &&
            Objects.equals(debitUp, that.debitUp) &&
            Objects.equals(debitDown, that.debitDown) &&
            Objects.equals(dateDiagnostic, that.dateDiagnostic) &&
            Objects.equals(typeDiagnostic, that.typeDiagnostic) &&
            Objects.equals(signalId, that.signalId) &&
            Objects.equals(ontId, that.ontId) &&
            Objects.equals(anomalieId, that.anomalieId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            index,
            statutONT,
            debitUp,
            debitDown,
            dateDiagnostic,
            typeDiagnostic,
            signalId,
            ontId,
            anomalieId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DiagnosticCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (index != null ? "index=" + index + ", " : "") +
            (statutONT != null ? "statutONT=" + statutONT + ", " : "") +
            (debitUp != null ? "debitUp=" + debitUp + ", " : "") +
            (debitDown != null ? "debitDown=" + debitDown + ", " : "") +
            (dateDiagnostic != null ? "dateDiagnostic=" + dateDiagnostic + ", " : "") +
            (typeDiagnostic != null ? "typeDiagnostic=" + typeDiagnostic + ", " : "") +
            (signalId != null ? "signalId=" + signalId + ", " : "") +
            (ontId != null ? "ontId=" + ontId + ", " : "") +
            (anomalieId != null ? "anomalieId=" + anomalieId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
