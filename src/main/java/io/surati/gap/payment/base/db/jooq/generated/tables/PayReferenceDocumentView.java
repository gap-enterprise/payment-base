/*
 * This file is generated by jOOQ.
 */
package io.surati.gap.payment.base.db.jooq.generated.tables;


import io.surati.gap.payment.base.db.jooq.generated.Public;
import io.surati.gap.payment.base.db.jooq.generated.tables.records.PayReferenceDocumentViewRecord;

import java.time.LocalDate;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row21;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class PayReferenceDocumentView extends TableImpl<PayReferenceDocumentViewRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.pay_reference_document_view</code>
     */
    public static final PayReferenceDocumentView PAY_REFERENCE_DOCUMENT_VIEW = new PayReferenceDocumentView();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<PayReferenceDocumentViewRecord> getRecordType() {
        return PayReferenceDocumentViewRecord.class;
    }

    /**
     * The column <code>public.pay_reference_document_view.id</code>.
     */
    public final TableField<PayReferenceDocumentViewRecord, Long> ID = createField(DSL.name("id"), SQLDataType.BIGINT, this, "");

    /**
     * The column <code>public.pay_reference_document_view.type_id</code>.
     */
    public final TableField<PayReferenceDocumentViewRecord, String> TYPE_ID = createField(DSL.name("type_id"), SQLDataType.VARCHAR(25), this, "");

    /**
     * The column <code>public.pay_reference_document_view.date</code>.
     */
    public final TableField<PayReferenceDocumentViewRecord, LocalDate> DATE = createField(DSL.name("date"), SQLDataType.LOCALDATE, this, "");

    /**
     * The column <code>public.pay_reference_document_view.reference</code>.
     */
    public final TableField<PayReferenceDocumentViewRecord, String> REFERENCE = createField(DSL.name("reference"), SQLDataType.VARCHAR(50), this, "");

    /**
     * The column <code>public.pay_reference_document_view.internal_reference</code>.
     */
    public final TableField<PayReferenceDocumentViewRecord, String> INTERNAL_REFERENCE = createField(DSL.name("internal_reference"), SQLDataType.VARCHAR(50), this, "");

    /**
     * The column <code>public.pay_reference_document_view.object</code>.
     */
    public final TableField<PayReferenceDocumentViewRecord, String> OBJECT = createField(DSL.name("object"), SQLDataType.VARCHAR(100), this, "");

    /**
     * The column <code>public.pay_reference_document_view.place</code>.
     */
    public final TableField<PayReferenceDocumentViewRecord, String> PLACE = createField(DSL.name("place"), SQLDataType.VARCHAR(50), this, "");

    /**
     * The column <code>public.pay_reference_document_view.amount</code>.
     */
    public final TableField<PayReferenceDocumentViewRecord, Double> AMOUNT = createField(DSL.name("amount"), SQLDataType.DOUBLE, this, "");

    /**
     * The column <code>public.pay_reference_document_view.deposit_date</code>.
     */
    public final TableField<PayReferenceDocumentViewRecord, LocalDate> DEPOSIT_DATE = createField(DSL.name("deposit_date"), SQLDataType.LOCALDATE, this, "");

    /**
     * The column <code>public.pay_reference_document_view.entry_date</code>.
     */
    public final TableField<PayReferenceDocumentViewRecord, LocalDate> ENTRY_DATE = createField(DSL.name("entry_date"), SQLDataType.LOCALDATE, this, "");

    /**
     * The column <code>public.pay_reference_document_view.beneficiary_id</code>.
     */
    public final TableField<PayReferenceDocumentViewRecord, Long> BENEFICIARY_ID = createField(DSL.name("beneficiary_id"), SQLDataType.BIGINT, this, "");

    /**
     * The column <code>public.pay_reference_document_view.step_id</code>.
     */
    public final TableField<PayReferenceDocumentViewRecord, String> STEP_ID = createField(DSL.name("step_id"), SQLDataType.VARCHAR(25), this, "");

    /**
     * The column <code>public.pay_reference_document_view.author_id</code>.
     */
    public final TableField<PayReferenceDocumentViewRecord, Long> AUTHOR_ID = createField(DSL.name("author_id"), SQLDataType.BIGINT, this, "");

    /**
     * The column <code>public.pay_reference_document_view.advanced_amount</code>.
     */
    public final TableField<PayReferenceDocumentViewRecord, Double> ADVANCED_AMOUNT = createField(DSL.name("advanced_amount"), SQLDataType.DOUBLE, this, "");

    /**
     * The column <code>public.pay_reference_document_view.worker_id</code>.
     */
    public final TableField<PayReferenceDocumentViewRecord, Long> WORKER_ID = createField(DSL.name("worker_id"), SQLDataType.BIGINT, this, "");

    /**
     * The column <code>public.pay_reference_document_view.status_id</code>.
     */
    public final TableField<PayReferenceDocumentViewRecord, String> STATUS_ID = createField(DSL.name("status_id"), SQLDataType.CLOB, this, "");

    /**
     * The column <code>public.pay_reference_document_view.amount_paid</code>.
     */
    public final TableField<PayReferenceDocumentViewRecord, Double> AMOUNT_PAID = createField(DSL.name("amount_paid"), SQLDataType.DOUBLE, this, "");

    /**
     * The column <code>public.pay_reference_document_view.amount_left</code>.
     */
    public final TableField<PayReferenceDocumentViewRecord, Double> AMOUNT_LEFT = createField(DSL.name("amount_left"), SQLDataType.DOUBLE, this, "");

    /**
     * The column <code>public.pay_reference_document_view.beneficiary_name</code>.
     */
    public final TableField<PayReferenceDocumentViewRecord, String> BENEFICIARY_NAME = createField(DSL.name("beneficiary_name"), SQLDataType.VARCHAR(100), this, "");

    /**
     * The column <code>public.pay_reference_document_view.beneficiary_abbreviated</code>.
     */
    public final TableField<PayReferenceDocumentViewRecord, String> BENEFICIARY_ABBREVIATED = createField(DSL.name("beneficiary_abbreviated"), SQLDataType.VARCHAR(100), this, "");

    /**
     * The column <code>public.pay_reference_document_view.beneficiary_code</code>.
     */
    public final TableField<PayReferenceDocumentViewRecord, String> BENEFICIARY_CODE = createField(DSL.name("beneficiary_code"), SQLDataType.VARCHAR(50), this, "");

    private PayReferenceDocumentView(Name alias, Table<PayReferenceDocumentViewRecord> aliased) {
        this(alias, aliased, null);
    }

    private PayReferenceDocumentView(Name alias, Table<PayReferenceDocumentViewRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.view("create view \"pay_reference_document_view\" as  SELECT rd.id,\n    rd.type_id,\n    rd.date,\n    rd.reference,\n    rd.internal_reference,\n    rd.object,\n    rd.place,\n    rd.amount,\n    rd.deposit_date,\n    rd.entry_date,\n    rd.beneficiary_id,\n    rd.step_id,\n    rd.author_id,\n    rd.advanced_amount,\n    rd.worker_id,\n        CASE\n            WHEN (rda.amount_left = (0)::double precision) THEN 'PAID'::text\n            WHEN (rda.amount_paid > (0)::double precision) THEN 'PAID_PARTIALLY'::text\n            ELSE 'WAITING_FOR_PAYMENT'::text\n        END AS status_id,\n        CASE\n            WHEN (rda.id IS NULL) THEN (0)::double precision\n            ELSE rda.amount_paid\n        END AS amount_paid,\n        CASE\n            WHEN (rda.id IS NULL) THEN rd.amount\n            ELSE rda.amount_left\n        END AS amount_left,\n    ps.name AS beneficiary_name,\n    tp.abbreviated AS beneficiary_abbreviated,\n    tp.code AS beneficiary_code\n   FROM (((pay_reference_document rd\n     LEFT JOIN pay_reference_document_amount_view rda ON ((rda.id = rd.id)))\n     LEFT JOIN pay_third_party tp ON ((tp.id = rd.beneficiary_id)))\n     LEFT JOIN ad_person ps ON ((ps.id = rd.beneficiary_id)));"));
    }

    /**
     * Create an aliased <code>public.pay_reference_document_view</code> table reference
     */
    public PayReferenceDocumentView(String alias) {
        this(DSL.name(alias), PAY_REFERENCE_DOCUMENT_VIEW);
    }

    /**
     * Create an aliased <code>public.pay_reference_document_view</code> table reference
     */
    public PayReferenceDocumentView(Name alias) {
        this(alias, PAY_REFERENCE_DOCUMENT_VIEW);
    }

    /**
     * Create a <code>public.pay_reference_document_view</code> table reference
     */
    public PayReferenceDocumentView() {
        this(DSL.name("pay_reference_document_view"), null);
    }

    public <O extends Record> PayReferenceDocumentView(Table<O> child, ForeignKey<O, PayReferenceDocumentViewRecord> key) {
        super(child, key, PAY_REFERENCE_DOCUMENT_VIEW);
    }

    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    @Override
    public PayReferenceDocumentView as(String alias) {
        return new PayReferenceDocumentView(DSL.name(alias), this);
    }

    @Override
    public PayReferenceDocumentView as(Name alias) {
        return new PayReferenceDocumentView(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public PayReferenceDocumentView rename(String name) {
        return new PayReferenceDocumentView(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public PayReferenceDocumentView rename(Name name) {
        return new PayReferenceDocumentView(name, null);
    }

    // -------------------------------------------------------------------------
    // Row21 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row21<Long, String, LocalDate, String, String, String, String, Double, LocalDate, LocalDate, Long, String, Long, Double, Long, String, Double, Double, String, String, String> fieldsRow() {
        return (Row21) super.fieldsRow();
    }
}
