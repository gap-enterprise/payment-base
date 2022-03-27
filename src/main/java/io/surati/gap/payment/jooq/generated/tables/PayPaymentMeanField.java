/*
 * This file is generated by jOOQ.
 */
package io.surati.gap.payment.jooq.generated.tables;


import io.surati.gap.payment.jooq.generated.Keys;
import io.surati.gap.payment.jooq.generated.Public;
import io.surati.gap.payment.jooq.generated.tables.records.PayPaymentMeanFieldRecord;

import java.util.Arrays;
import java.util.List;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row6;
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
public class PayPaymentMeanField extends TableImpl<PayPaymentMeanFieldRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.pay_payment_mean_field</code>
     */
    public static final PayPaymentMeanField PAY_PAYMENT_MEAN_FIELD = new PayPaymentMeanField();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<PayPaymentMeanFieldRecord> getRecordType() {
        return PayPaymentMeanFieldRecord.class;
    }

    /**
     * The column <code>public.pay_payment_mean_field.type_id</code>.
     */
    public final TableField<PayPaymentMeanFieldRecord, String> TYPE_ID = createField(DSL.name("type_id"), SQLDataType.VARCHAR(50).nullable(false), this, "");

    /**
     * The column <code>public.pay_payment_mean_field.mean_id</code>.
     */
    public final TableField<PayPaymentMeanFieldRecord, Long> MEAN_ID = createField(DSL.name("mean_id"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>public.pay_payment_mean_field.x</code>.
     */
    public final TableField<PayPaymentMeanFieldRecord, Double> X = createField(DSL.name("x"), SQLDataType.DOUBLE.nullable(false).defaultValue(DSL.field("'0'", SQLDataType.DOUBLE)), this, "");

    /**
     * The column <code>public.pay_payment_mean_field.y</code>.
     */
    public final TableField<PayPaymentMeanFieldRecord, Double> Y = createField(DSL.name("y"), SQLDataType.DOUBLE.nullable(false).defaultValue(DSL.field("'0'", SQLDataType.DOUBLE)), this, "");

    /**
     * The column <code>public.pay_payment_mean_field.visible</code>.
     */
    public final TableField<PayPaymentMeanFieldRecord, Boolean> VISIBLE = createField(DSL.name("visible"), SQLDataType.BOOLEAN.defaultValue(DSL.field("TRUE", SQLDataType.BOOLEAN)), this, "");

    /**
     * The column <code>public.pay_payment_mean_field.width</code>.
     */
    public final TableField<PayPaymentMeanFieldRecord, Double> WIDTH = createField(DSL.name("width"), SQLDataType.DOUBLE.nullable(false), this, "");

    private PayPaymentMeanField(Name alias, Table<PayPaymentMeanFieldRecord> aliased) {
        this(alias, aliased, null);
    }

    private PayPaymentMeanField(Name alias, Table<PayPaymentMeanFieldRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.pay_payment_mean_field</code> table reference
     */
    public PayPaymentMeanField(String alias) {
        this(DSL.name(alias), PAY_PAYMENT_MEAN_FIELD);
    }

    /**
     * Create an aliased <code>public.pay_payment_mean_field</code> table reference
     */
    public PayPaymentMeanField(Name alias) {
        this(alias, PAY_PAYMENT_MEAN_FIELD);
    }

    /**
     * Create a <code>public.pay_payment_mean_field</code> table reference
     */
    public PayPaymentMeanField() {
        this(DSL.name("pay_payment_mean_field"), null);
    }

    public <O extends Record> PayPaymentMeanField(Table<O> child, ForeignKey<O, PayPaymentMeanFieldRecord> key) {
        super(child, key, PAY_PAYMENT_MEAN_FIELD);
    }

    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    @Override
    public UniqueKey<PayPaymentMeanFieldRecord> getPrimaryKey() {
        return Keys.PAY_PAYMENT_MEAN_FIELD_PKEY;
    }

    @Override
    public List<UniqueKey<PayPaymentMeanFieldRecord>> getKeys() {
        return Arrays.<UniqueKey<PayPaymentMeanFieldRecord>>asList(Keys.PAY_PAYMENT_MEAN_FIELD_PKEY);
    }

    @Override
    public List<ForeignKey<PayPaymentMeanFieldRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<PayPaymentMeanFieldRecord, ?>>asList(Keys.PAY_PAYMENT_MEAN_FIELD_MEAN_ID_FKEY);
    }

    private transient PayPaymentMean _payPaymentMean;

    public PayPaymentMean payPaymentMean() {
        if (_payPaymentMean == null)
            _payPaymentMean = new PayPaymentMean(this, Keys.PAY_PAYMENT_MEAN_FIELD_MEAN_ID_FKEY);

        return _payPaymentMean;
    }

    @Override
    public PayPaymentMeanField as(String alias) {
        return new PayPaymentMeanField(DSL.name(alias), this);
    }

    @Override
    public PayPaymentMeanField as(Name alias) {
        return new PayPaymentMeanField(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public PayPaymentMeanField rename(String name) {
        return new PayPaymentMeanField(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public PayPaymentMeanField rename(Name name) {
        return new PayPaymentMeanField(name, null);
    }

    // -------------------------------------------------------------------------
    // Row6 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row6<String, Long, Double, Double, Boolean, Double> fieldsRow() {
        return (Row6) super.fieldsRow();
    }
}
