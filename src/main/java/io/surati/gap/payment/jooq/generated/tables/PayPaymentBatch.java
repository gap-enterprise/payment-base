/*
 * This file is generated by jOOQ.
 */
package io.surati.gap.payment.jooq.generated.tables;


import io.surati.gap.payment.jooq.generated.Keys;
import io.surati.gap.payment.jooq.generated.Public;
import io.surati.gap.payment.jooq.generated.tables.records.PayPaymentBatchRecord;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row5;
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
public class PayPaymentBatch extends TableImpl<PayPaymentBatchRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.pay_payment_batch</code>
     */
    public static final PayPaymentBatch PAY_PAYMENT_BATCH = new PayPaymentBatch();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<PayPaymentBatchRecord> getRecordType() {
        return PayPaymentBatchRecord.class;
    }

    /**
     * The column <code>public.pay_payment_batch.id</code>.
     */
    public final TableField<PayPaymentBatchRecord, Long> ID = createField(DSL.name("id"), SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column <code>public.pay_payment_batch.date</code>.
     */
    public final TableField<PayPaymentBatchRecord, LocalDate> DATE = createField(DSL.name("date"), SQLDataType.LOCALDATE.nullable(false), this, "");

    /**
     * The column <code>public.pay_payment_batch.mean_type_id</code>.
     */
    public final TableField<PayPaymentBatchRecord, String> MEAN_TYPE_ID = createField(DSL.name("mean_type_id"), SQLDataType.VARCHAR(25).nullable(false), this, "");

    /**
     * The column <code>public.pay_payment_batch.account_id</code>.
     */
    public final TableField<PayPaymentBatchRecord, Long> ACCOUNT_ID = createField(DSL.name("account_id"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>public.pay_payment_batch.status_id</code>.
     */
    public final TableField<PayPaymentBatchRecord, String> STATUS_ID = createField(DSL.name("status_id"), SQLDataType.VARCHAR(25).defaultValue(DSL.field("'TO_PRINT'", SQLDataType.VARCHAR)), this, "");

    private PayPaymentBatch(Name alias, Table<PayPaymentBatchRecord> aliased) {
        this(alias, aliased, null);
    }

    private PayPaymentBatch(Name alias, Table<PayPaymentBatchRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.pay_payment_batch</code> table reference
     */
    public PayPaymentBatch(String alias) {
        this(DSL.name(alias), PAY_PAYMENT_BATCH);
    }

    /**
     * Create an aliased <code>public.pay_payment_batch</code> table reference
     */
    public PayPaymentBatch(Name alias) {
        this(alias, PAY_PAYMENT_BATCH);
    }

    /**
     * Create a <code>public.pay_payment_batch</code> table reference
     */
    public PayPaymentBatch() {
        this(DSL.name("pay_payment_batch"), null);
    }

    public <O extends Record> PayPaymentBatch(Table<O> child, ForeignKey<O, PayPaymentBatchRecord> key) {
        super(child, key, PAY_PAYMENT_BATCH);
    }

    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    @Override
    public Identity<PayPaymentBatchRecord, Long> getIdentity() {
        return (Identity<PayPaymentBatchRecord, Long>) super.getIdentity();
    }

    @Override
    public UniqueKey<PayPaymentBatchRecord> getPrimaryKey() {
        return Keys.PAY_PAYMENT_BATCH_PKEY;
    }

    @Override
    public List<UniqueKey<PayPaymentBatchRecord>> getKeys() {
        return Arrays.<UniqueKey<PayPaymentBatchRecord>>asList(Keys.PAY_PAYMENT_BATCH_PKEY);
    }

    @Override
    public List<ForeignKey<PayPaymentBatchRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<PayPaymentBatchRecord, ?>>asList(Keys.PAY_PAYMENT_BATCH_ACCOUNT_ID_FKEY);
    }

    private transient PayBankAccount _payBankAccount;

    public PayBankAccount payBankAccount() {
        if (_payBankAccount == null)
            _payBankAccount = new PayBankAccount(this, Keys.PAY_PAYMENT_BATCH_ACCOUNT_ID_FKEY);

        return _payBankAccount;
    }

    @Override
    public PayPaymentBatch as(String alias) {
        return new PayPaymentBatch(DSL.name(alias), this);
    }

    @Override
    public PayPaymentBatch as(Name alias) {
        return new PayPaymentBatch(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public PayPaymentBatch rename(String name) {
        return new PayPaymentBatch(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public PayPaymentBatch rename(Name name) {
        return new PayPaymentBatch(name, null);
    }

    // -------------------------------------------------------------------------
    // Row5 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row5<Long, LocalDate, String, Long, String> fieldsRow() {
        return (Row5) super.fieldsRow();
    }
}
