package sn.sonatel.dsi.ins.ftsirc.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
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
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.index = other.optionalIndex().map(StringFilter::copy).orElse(null);
        this.statutONT = other.optionalStatutONT().map(StatutONTFilter::copy).orElse(null);
        this.debitUp = other.optionalDebitUp().map(StringFilter::copy).orElse(null);
        this.debitDown = other.optionalDebitDown().map(StringFilter::copy).orElse(null);
        this.dateDiagnostic = other.optionalDateDiagnostic().map(LocalDateFilter::copy).orElse(null);
        this.typeDiagnostic = other.optionalTypeDiagnostic().map(TypeDiagnosticFilter::copy).orElse(null);
        this.signalId = other.optionalSignalId().map(LongFilter::copy).orElse(null);
        this.ontId = other.optionalOntId().map(LongFilter::copy).orElse(null);
        this.anomalieId = other.optionalAnomalieId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public DiagnosticCriteria copy() {
        return new DiagnosticCriteria(this);
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

    public StatutONTFilter getStatutONT() {
        return statutONT;
    }

    public Optional<StatutONTFilter> optionalStatutONT() {
        return Optional.ofNullable(statutONT);
    }

    public StatutONTFilter statutONT() {
        if (statutONT == null) {
            setStatutONT(new StatutONTFilter());
        }
        return statutONT;
    }

    public void setStatutONT(StatutONTFilter statutONT) {
        this.statutONT = statutONT;
    }

    public StringFilter getDebitUp() {
        return debitUp;
    }

    public Optional<StringFilter> optionalDebitUp() {
        return Optional.ofNullable(debitUp);
    }

    public StringFilter debitUp() {
        if (debitUp == null) {
            setDebitUp(new StringFilter());
        }
        return debitUp;
    }

    public void setDebitUp(StringFilter debitUp) {
        this.debitUp = debitUp;
    }

    public StringFilter getDebitDown() {
        return debitDown;
    }

    public Optional<StringFilter> optionalDebitDown() {
        return Optional.ofNullable(debitDown);
    }

    public StringFilter debitDown() {
        if (debitDown == null) {
            setDebitDown(new StringFilter());
        }
        return debitDown;
    }

    public void setDebitDown(StringFilter debitDown) {
        this.debitDown = debitDown;
    }

    public LocalDateFilter getDateDiagnostic() {
        return dateDiagnostic;
    }

    public Optional<LocalDateFilter> optionalDateDiagnostic() {
        return Optional.ofNullable(dateDiagnostic);
    }

    public LocalDateFilter dateDiagnostic() {
        if (dateDiagnostic == null) {
            setDateDiagnostic(new LocalDateFilter());
        }
        return dateDiagnostic;
    }

    public void setDateDiagnostic(LocalDateFilter dateDiagnostic) {
        this.dateDiagnostic = dateDiagnostic;
    }

    public TypeDiagnosticFilter getTypeDiagnostic() {
        return typeDiagnostic;
    }

    public Optional<TypeDiagnosticFilter> optionalTypeDiagnostic() {
        return Optional.ofNullable(typeDiagnostic);
    }

    public TypeDiagnosticFilter typeDiagnostic() {
        if (typeDiagnostic == null) {
            setTypeDiagnostic(new TypeDiagnosticFilter());
        }
        return typeDiagnostic;
    }

    public void setTypeDiagnostic(TypeDiagnosticFilter typeDiagnostic) {
        this.typeDiagnostic = typeDiagnostic;
    }

    public LongFilter getSignalId() {
        return signalId;
    }

    public Optional<LongFilter> optionalSignalId() {
        return Optional.ofNullable(signalId);
    }

    public LongFilter signalId() {
        if (signalId == null) {
            setSignalId(new LongFilter());
        }
        return signalId;
    }

    public void setSignalId(LongFilter signalId) {
        this.signalId = signalId;
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

    public LongFilter getAnomalieId() {
        return anomalieId;
    }

    public Optional<LongFilter> optionalAnomalieId() {
        return Optional.ofNullable(anomalieId);
    }

    public LongFilter anomalieId() {
        if (anomalieId == null) {
            setAnomalieId(new LongFilter());
        }
        return anomalieId;
    }

    public void setAnomalieId(LongFilter anomalieId) {
        this.anomalieId = anomalieId;
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
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalIndex().map(f -> "index=" + f + ", ").orElse("") +
            optionalStatutONT().map(f -> "statutONT=" + f + ", ").orElse("") +
            optionalDebitUp().map(f -> "debitUp=" + f + ", ").orElse("") +
            optionalDebitDown().map(f -> "debitDown=" + f + ", ").orElse("") +
            optionalDateDiagnostic().map(f -> "dateDiagnostic=" + f + ", ").orElse("") +
            optionalTypeDiagnostic().map(f -> "typeDiagnostic=" + f + ", ").orElse("") +
            optionalSignalId().map(f -> "signalId=" + f + ", ").orElse("") +
            optionalOntId().map(f -> "ontId=" + f + ", ").orElse("") +
            optionalAnomalieId().map(f -> "anomalieId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
