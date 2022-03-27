/*
 * This file is generated by jOOQ.
 */
package io.surati.gap.payment.jooq.generated.tables.records;


import io.surati.gap.payment.jooq.generated.tables.PayPaymentBatch;

import java.time.LocalDate;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record5;
import org.jooq.Row5;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class PayPaymentBatchRecord extends UpdatableRecordImpl<PayPaymentBatchRecord> implements Record5<Long, LocalDate, String, Long, String> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.pay_payment_batch.id</code>.
     */
    public void setId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.pay_payment_batch.id</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>public.pay_payment_batch.date</code>.
     */
    public void setDate(LocalDate value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.pay_payment_batch.date</code>.
     */
    public LocalDate getDate() {
        return (LocalDate) get(1);
    }

    /**
     * Setter for <code>public.pay_payment_batch.mean_type_id</code>.
     */
    public void setMeanTypeId(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.pay_payment_batch.mean_type_id</code>.
     */
    public String getMeanTypeId() {
        return (String) get(2);
    }

    /**
     * Setter for <code>public.pay_payment_batch.account_id</code>.
     */
    public void setAccountId(Long value) {
        set(3, value);
    }

    /**
     * Getter for <code>public.pay_payment_batch.account_id</code>.
     */
    public Long getAccountId() {
        return (Long) get(3);
    }

    /**
     * Setter for <code>public.pay_payment_batch.status_id</code>.
     */
    public void setStatusId(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>public.pay_payment_batch.status_id</code>.
     */
    public String getStatusId() {
        return (String) get(4);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record5 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row5<Long, LocalDate, String, Long, String> fieldsRow() {
        return (Row5) super.fieldsRow();
    }

    @Override
    public Row5<Long, LocalDate, String, Long, String> valuesRow() {
        return (Row5) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return PayPaymentBatch.PAY_PAYMENT_BATCH.ID;
    }

    @Override
    public Field<LocalDate> field2() {
        return PayPaymentBatch.PAY_PAYMENT_BATCH.DATE;
    }

    @Override
    public Field<String> field3() {
        return PayPaymentBatch.PAY_PAYMENT_BATCH.MEAN_TYPE_ID;
    }

    @Override
    public Field<Long> field4() {
        return PayPaymentBatch.PAY_PAYMENT_BATCH.ACCOUNT_ID;
    }

    @Override
    public Field<String> field5() {
        return PayPaymentBatch.PAY_PAYMENT_BATCH.STATUS_ID;
    }

    @Override
    public Long component1() {
        return getId();
    }

    @Override
    public LocalDate component2() {
        return getDate();
    }

    @Override
    public String component3() {
        return getMeanTypeId();
    }

    @Override
    public Long component4() {
        return getAccountId();
    }

    @Override
    public String component5() {
        return getStatusId();
    }

    @Override
    public Long value1() {
        return getId();
    }

    @Override
    public LocalDate value2() {
        return getDate();
    }

    @Override
    public String value3() {
        return getMeanTypeId();
    }

    @Override
    public Long value4() {
        return getAccountId();
    }

    @Override
    public String value5() {
        return getStatusId();
    }

    @Override
    public PayPaymentBatchRecord value1(Long value) {
        setId(value);
        return this;
    }

    @Override
    public PayPaymentBatchRecord value2(LocalDate value) {
        setDate(value);
        return this;
    }

    @Override
    public PayPaymentBatchRecord value3(String value) {
        setMeanTypeId(value);
        return this;
    }

    @Override
    public PayPaymentBatchRecord value4(Long value) {
        setAccountId(value);
        return this;
    }

    @Override
    public PayPaymentBatchRecord value5(String value) {
        setStatusId(value);
        return this;
    }

    @Override
    public PayPaymentBatchRecord values(Long value1, LocalDate value2, String value3, Long value4, String value5) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached PayPaymentBatchRecord
     */
    public PayPaymentBatchRecord() {
        super(PayPaymentBatch.PAY_PAYMENT_BATCH);
    }

    /**
     * Create a detached, initialised PayPaymentBatchRecord
     */
    public PayPaymentBatchRecord(Long id, LocalDate date, String meanTypeId, Long accountId, String statusId) {
        super(PayPaymentBatch.PAY_PAYMENT_BATCH);

        setId(id);
        setDate(date);
        setMeanTypeId(meanTypeId);
        setAccountId(accountId);
        setStatusId(statusId);
    }
}
