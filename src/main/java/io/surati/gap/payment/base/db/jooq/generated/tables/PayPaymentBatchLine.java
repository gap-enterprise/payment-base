/*
 * This file is generated by jOOQ.
 */
package io.surati.gap.payment.base.db.jooq.generated.tables;


import io.surati.gap.payment.base.db.jooq.generated.Keys;
import io.surati.gap.payment.base.db.jooq.generated.Public;
import io.surati.gap.payment.base.db.jooq.generated.tables.records.PayPaymentBatchLineRecord;

import java.util.Arrays;
import java.util.List;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row2;
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
public class PayPaymentBatchLine extends TableImpl<PayPaymentBatchLineRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.pay_payment_batch_line</code>
     */
    public static final PayPaymentBatchLine PAY_PAYMENT_BATCH_LINE = new PayPaymentBatchLine();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<PayPaymentBatchLineRecord> getRecordType() {
        return PayPaymentBatchLineRecord.class;
    }

    /**
     * The column <code>public.pay_payment_batch_line.payment_id</code>.
     */
    public final TableField<PayPaymentBatchLineRecord, Long> PAYMENT_ID = createField(DSL.name("payment_id"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>public.pay_payment_batch_line.batch_id</code>.
     */
    public final TableField<PayPaymentBatchLineRecord, Long> BATCH_ID = createField(DSL.name("batch_id"), SQLDataType.BIGINT.nullable(false), this, "");

    private PayPaymentBatchLine(Name alias, Table<PayPaymentBatchLineRecord> aliased) {
        this(alias, aliased, null);
    }

    private PayPaymentBatchLine(Name alias, Table<PayPaymentBatchLineRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.pay_payment_batch_line</code> table reference
     */
    public PayPaymentBatchLine(String alias) {
        this(DSL.name(alias), PAY_PAYMENT_BATCH_LINE);
    }

    /**
     * Create an aliased <code>public.pay_payment_batch_line</code> table reference
     */
    public PayPaymentBatchLine(Name alias) {
        this(alias, PAY_PAYMENT_BATCH_LINE);
    }

    /**
     * Create a <code>public.pay_payment_batch_line</code> table reference
     */
    public PayPaymentBatchLine() {
        this(DSL.name("pay_payment_batch_line"), null);
    }

    public <O extends Record> PayPaymentBatchLine(Table<O> child, ForeignKey<O, PayPaymentBatchLineRecord> key) {
        super(child, key, PAY_PAYMENT_BATCH_LINE);
    }

    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    @Override
    public UniqueKey<PayPaymentBatchLineRecord> getPrimaryKey() {
        return Keys.PAY_PAYMENT_BATCH_LINE_PKEY;
    }

    @Override
    public List<UniqueKey<PayPaymentBatchLineRecord>> getKeys() {
        return Arrays.<UniqueKey<PayPaymentBatchLineRecord>>asList(Keys.PAY_PAYMENT_BATCH_LINE_PKEY);
    }

    @Override
    public List<ForeignKey<PayPaymentBatchLineRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<PayPaymentBatchLineRecord, ?>>asList(Keys.PAY_PAYMENT_BATCH_LINE__PAY_PAYMENT_BATCH_LINE_PAYMENT_ID_FKEY, Keys.PAY_PAYMENT_BATCH_LINE__PAY_PAYMENT_BATCH_LINE_BATCH_ID_FKEY);
    }

    private transient PayPayment _payPayment;
    private transient PayPaymentBatch _payPaymentBatch;

    public PayPayment payPayment() {
        if (_payPayment == null)
            _payPayment = new PayPayment(this, Keys.PAY_PAYMENT_BATCH_LINE__PAY_PAYMENT_BATCH_LINE_PAYMENT_ID_FKEY);

        return _payPayment;
    }

    public PayPaymentBatch payPaymentBatch() {
        if (_payPaymentBatch == null)
            _payPaymentBatch = new PayPaymentBatch(this, Keys.PAY_PAYMENT_BATCH_LINE__PAY_PAYMENT_BATCH_LINE_BATCH_ID_FKEY);

        return _payPaymentBatch;
    }

    @Override
    public PayPaymentBatchLine as(String alias) {
        return new PayPaymentBatchLine(DSL.name(alias), this);
    }

    @Override
    public PayPaymentBatchLine as(Name alias) {
        return new PayPaymentBatchLine(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public PayPaymentBatchLine rename(String name) {
        return new PayPaymentBatchLine(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public PayPaymentBatchLine rename(Name name) {
        return new PayPaymentBatchLine(name, null);
    }

    // -------------------------------------------------------------------------
    // Row2 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row2<Long, Long> fieldsRow() {
        return (Row2) super.fieldsRow();
    }
}