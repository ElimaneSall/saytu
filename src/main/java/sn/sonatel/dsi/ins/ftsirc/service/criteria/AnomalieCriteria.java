package sn.sonatel.dsi.ins.ftsirc.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link sn.sonatel.dsi.ins.ftsirc.domain.Anomalie} entity. This class is used
 * in {@link sn.sonatel.dsi.ins.ftsirc.web.rest.AnomalieResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /anomalies?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AnomalieCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter libelle;

    private StringFilter etat;

    private IntegerFilter code;

    private LongFilter diagnosticId;

    private Boolean distinct;

    public AnomalieCriteria() {}

    public AnomalieCriteria(AnomalieCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.libelle = other.libelle == null ? null : other.libelle.copy();
        this.etat = other.etat == null ? null : other.etat.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.diagnosticId = other.diagnosticId == null ? null : other.diagnosticId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public AnomalieCriteria copy() {
        return new AnomalieCriteria(this);
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

    public StringFilter getEtat() {
        return etat;
    }

    public StringFilter etat() {
        if (etat == null) {
            etat = new StringFilter();
        }
        return etat;
    }

    public void setEtat(StringFilter etat) {
        this.etat = etat;
    }

    public IntegerFilter getCode() {
        return code;
    }

    public IntegerFilter code() {
        if (code == null) {
            code = new IntegerFilter();
        }
        return code;
    }

    public void setCode(IntegerFilter code) {
        this.code = code;
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
        final AnomalieCriteria that = (AnomalieCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(libelle, that.libelle) &&
            Objects.equals(etat, that.etat) &&
            Objects.equals(code, that.code) &&
            Objects.equals(diagnosticId, that.diagnosticId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, libelle, etat, code, diagnosticId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AnomalieCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (libelle != null ? "libelle=" + libelle + ", " : "") +
            (etat != null ? "etat=" + etat + ", " : "") +
            (code != null ? "code=" + code + ", " : "") +
            (diagnosticId != null ? "diagnosticId=" + diagnosticId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
