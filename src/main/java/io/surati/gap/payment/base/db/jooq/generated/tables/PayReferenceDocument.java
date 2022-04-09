/*
 * This file is generated by jOOQ.
 */
package io.surati.gap.payment.base.db.jooq.generated.tables;


import io.surati.gap.payment.base.db.jooq.generated.Keys;
import io.surati.gap.payment.base.db.jooq.generated.Public;
import io.surati.gap.payment.base.db.jooq.generated.tables.records.PayReferenceDocumentRecord;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row15;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class PayReferenceDocument extends TableImpl<PayReferenceDocumentRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.pay_reference_document</code>
     */
    public static final PayReferenceDocument PAY_REFERENCE_DOCUMENT = new PayReferenceDocument();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<PayReferenceDocumentRecord> getRecordType() {
        return PayReferenceDocumentRecord.class;
    }

    /**
     * The column <code>public.pay_reference_document.id</code>.
     */
    public final TableField<PayReferenceDocumentRecord, Long> ID = createField(DSL.name("id"), SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column <code>public.pay_reference_document.type_id</code>.
     */
    public final TableField<PayReferenceDocumentRecord, String> TYPE_ID = createField(DSL.name("type_id"), SQLDataType.VARCHAR(25).nullable(false), this, "");

    /**
     * The column <code>public.pay_reference_document.date</code>.
     */
    public final TableField<PayReferenceDocumentRecord, LocalDate> DATE = createField(DSL.name("date"), SQLDataType.LOCALDATE.nullable(false), this, "");

    /**
     * The column <code>public.pay_reference_document.reference</code>.
     */
    public final TableField<PayReferenceDocumentRecord, String> REFERENCE = createField(DSL.name("reference"), SQLDataType.VARCHAR(50).nullable(false), this, "");

    /**
     * The column <code>public.pay_reference_document.internal_reference</code>.
     */
    public final TableField<PayReferenceDocumentRecord, String> INTERNAL_REFERENCE = createField(DSL.name("internal_reference"), SQLDataType.VARCHAR(50), this, "");

    /**
     * The column <code>public.pay_reference_document.object</code>.
     */
    public final TableField<PayReferenceDocumentRecord, String> OBJECT = createField(DSL.name("object"), SQLDataType.VARCHAR(100).nullable(false), this, "");

    /**
     * The column <code>public.pay_reference_document.place</code>.
     */
    public final TableField<PayReferenceDocumentRecord, String> PLACE = createField(DSL.name("place"), SQLDataType.VARCHAR(50).nullable(false), this, "");

    /**
     * The column <code>public.pay_reference_document.amount</code>.
     */
    public final TableField<PayReferenceDocumentRecord, Double> AMOUNT = createField(DSL.name("amount"), SQLDataType.DOUBLE.nullable(false), this, "");

    /**
     * The column <code>public.pay_reference_document.deposit_date</code>.
     */
    public final TableField<PayReferenceDocumentRecord, LocalDate> DEPOSIT_DATE = createField(DSL.name("deposit_date"), SQLDataType.LOCALDATE, this, "");

    /**
     * The column <code>public.pay_reference_document.entry_date</code>.
     */
    public final TableField<PayReferenceDocumentRecord, LocalDate> ENTRY_DATE = createField(DSL.name("entry_date"), SQLDataType.LOCALDATE.nullable(false), this, "");

    /**
     * The column <code>public.pay_reference_document.beneficiary_id</code>.
     */
    public final TableField<PayReferenceDocumentRecord, Long> BENEFICIARY_ID = createField(DSL.name("beneficiary_id"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>public.pay_reference_document.step_id</code>.
     */
    public final TableField<PayReferenceDocumentRecord, String> STEP_ID = createField(DSL.name("step_id"), SQLDataType.VARCHAR(25).nullable(false), this, "");

    /**
     * The column <code>public.pay_reference_document.author_id</code>.
     */
    public final TableField<PayReferenceDocumentRecord, Long> AUTHOR_ID = createField(DSL.name("author_id"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>public.pay_reference_document.advanced_amount</code>.
     */
    public final TableField<PayReferenceDocumentRecord, Double> ADVANCED_AMOUNT = createField(DSL.name("advanced_amount"), SQLDataType.DOUBLE.nullable(false), this, "");

    /**
     * The column <code>public.pay_reference_document.worker_id</code>.
     */
    public final TableField<PayReferenceDocumentRecord, Long> WORKER_ID = createField(DSL.name("worker_id"), SQLDataType.BIGINT.nullable(false), this, "");

    private PayReferenceDocument(Name alias, Table<PayReferenceDocumentRecord> aliased) {
        this(alias, aliased, null);
    }

    private PayReferenceDocument(Name alias, Table<PayReferenceDocumentRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.pay_reference_document</code> table reference
     */
    public PayReferenceDocument(String alias) {
        this(DSL.name(alias), PAY_REFERENCE_DOCUMENT);
    }

    /**
     * Create an aliased <code>public.pay_reference_document</code> table reference
     */
    public PayReferenceDocument(Name alias) {
        this(alias, PAY_REFERENCE_DOCUMENT);
    }

    /**
     * Create a <code>public.pay_reference_document</code> table reference
     */
    public PayReferenceDocument() {
        this(DSL.name("pay_reference_document"), null);
    }

    public <O extends Record> PayReferenceDocument(Table<O> child, ForeignKey<O, PayReferenceDocumentRecord> key) {
        super(child, key, PAY_REFERENCE_DOCUMENT);
    }

    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    @Override
    public Identity<PayReferenceDocumentRecord, Long> getIdentity() {
        return (Identity<PayReferenceDocumentRecord, Long>) super.getIdentity();
    }

    @Override
    public UniqueKey<PayReferenceDocumentRecord> getPrimaryKey() {
        return Keys.PAY_REFERENCE_DOCUMENT_PKEY;
    }

    @Override
    public List<UniqueKey<PayReferenceDocumentRecord>> getKeys() {
        return Arrays.<UniqueKey<PayReferenceDocumentRecord>>asList(Keys.PAY_REFERENCE_DOCUMENT_PKEY);
    }

    @Override
    public List<ForeignKey<PayReferenceDocumentRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<PayReferenceDocumentRecord, ?>>asList(Keys.PAY_REFERENCE_DOCUMENT__PAY_REFERENCE_DOCUMENT_BENEFICIARY_ID_FKEY);
    }

    private transient PayThirdParty _payThirdParty;

    public PayThirdParty payThirdParty() {
        if (_payThirdParty == null)
            _payThirdParty = new PayThirdParty(this, Keys.PAY_REFERENCE_DOCUMENT__PAY_REFERENCE_DOCUMENT_BENEFICIARY_ID_FKEY);

        return _payThirdParty;
    }

    @Override
    public PayReferenceDocument as(String alias) {
        return new PayReferenceDocument(DSL.name(alias), this);
    }

    @Override
    public PayReferenceDocument as(Name alias) {
        return new PayReferenceDocument(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public PayReferenceDocument rename(String name) {
        return new PayReferenceDocument(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public PayReferenceDocument rename(Name name) {
        return new PayReferenceDocument(name, null);
    }

    // -------------------------------------------------------------------------
    // Row15 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row15<Long, String, LocalDate, String, String, String, String, Double, LocalDate, LocalDate, Long, String, Long, Double, Long> fieldsRow() {
        return (Row15) super.fieldsRow();
    }
}
