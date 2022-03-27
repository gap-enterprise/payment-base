/*
 * This file is generated by jOOQ.
 */
package io.surati.gap.payment.jooq.generated.tables.records;


import io.surati.gap.payment.jooq.generated.tables.PayPaymentExport;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record3;
import org.jooq.Row3;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class PayPaymentExportRecord extends UpdatableRecordImpl<PayPaymentExportRecord> implements Record3<Long, Long, Boolean> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.pay_payment_export.id</code>.
     */
    public void setId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.pay_payment_export.id</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>public.pay_payment_export.payment_id</code>.
     */
    public void setPaymentId(Long value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.pay_payment_export.payment_id</code>.
     */
    public Long getPaymentId() {
        return (Long) get(1);
    }

    /**
     * Setter for <code>public.pay_payment_export.is_done</code>.
     */
    public void setIsDone(Boolean value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.pay_payment_export.is_done</code>.
     */
    public Boolean getIsDone() {
        return (Boolean) get(2);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record3 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row3<Long, Long, Boolean> fieldsRow() {
        return (Row3) super.fieldsRow();
    }

    @Override
    public Row3<Long, Long, Boolean> valuesRow() {
        return (Row3) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return PayPaymentExport.PAY_PAYMENT_EXPORT.ID;
    }

    @Override
    public Field<Long> field2() {
        return PayPaymentExport.PAY_PAYMENT_EXPORT.PAYMENT_ID;
    }

    @Override
    public Field<Boolean> field3() {
        return PayPaymentExport.PAY_PAYMENT_EXPORT.IS_DONE;
    }

    @Override
    public Long component1() {
        return getId();
    }

    @Override
    public Long component2() {
        return getPaymentId();
    }

    @Override
    public Boolean component3() {
        return getIsDone();
    }

    @Override
    public Long value1() {
        return getId();
    }

    @Override
    public Long value2() {
        return getPaymentId();
    }

    @Override
    public Boolean value3() {
        return getIsDone();
    }

    @Override
    public PayPaymentExportRecord value1(Long value) {
        setId(value);
        return this;
    }

    @Override
    public PayPaymentExportRecord value2(Long value) {
        setPaymentId(value);
        return this;
    }

    @Override
    public PayPaymentExportRecord value3(Boolean value) {
        setIsDone(value);
        return this;
    }

    @Override
    public PayPaymentExportRecord values(Long value1, Long value2, Boolean value3) {
        value1(value1);
        value2(value2);
        value3(value3);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached PayPaymentExportRecord
     */
    public PayPaymentExportRecord() {
        super(PayPaymentExport.PAY_PAYMENT_EXPORT);
    }

    /**
     * Create a detached, initialised PayPaymentExportRecord
     */
    public PayPaymentExportRecord(Long id, Long paymentId, Boolean isDone) {
        super(PayPaymentExport.PAY_PAYMENT_EXPORT);

        setId(id);
        setPaymentId(paymentId);
        setIsDone(isDone);
    }
}
