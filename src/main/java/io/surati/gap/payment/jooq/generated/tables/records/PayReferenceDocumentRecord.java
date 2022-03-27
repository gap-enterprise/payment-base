/*
 * This file is generated by jOOQ.
 */
package io.surati.gap.payment.jooq.generated.tables.records;


import io.surati.gap.payment.jooq.generated.tables.PayReferenceDocument;

import java.time.LocalDate;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record18;
import org.jooq.Row18;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class PayReferenceDocumentRecord extends UpdatableRecordImpl<PayReferenceDocumentRecord> implements Record18<Long, String, LocalDate, String, String, String, Double, LocalDate, LocalDate, Long, String, String, String, Double, Double, Long, Double, Long> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.pay_reference_document.id</code>.
     */
    public void setId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.pay_reference_document.id</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>public.pay_reference_document.type_id</code>.
     */
    public void setTypeId(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.pay_reference_document.type_id</code>.
     */
    public String getTypeId() {
        return (String) get(1);
    }

    /**
     * Setter for <code>public.pay_reference_document.date</code>.
     */
    public void setDate(LocalDate value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.pay_reference_document.date</code>.
     */
    public LocalDate getDate() {
        return (LocalDate) get(2);
    }

    /**
     * Setter for <code>public.pay_reference_document.reference</code>.
     */
    public void setReference(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>public.pay_reference_document.reference</code>.
     */
    public String getReference() {
        return (String) get(3);
    }

    /**
     * Setter for <code>public.pay_reference_document.object</code>.
     */
    public void setObject(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>public.pay_reference_document.object</code>.
     */
    public String getObject() {
        return (String) get(4);
    }

    /**
     * Setter for <code>public.pay_reference_document.place</code>.
     */
    public void setPlace(String value) {
        set(5, value);
    }

    /**
     * Getter for <code>public.pay_reference_document.place</code>.
     */
    public String getPlace() {
        return (String) get(5);
    }

    /**
     * Setter for <code>public.pay_reference_document.amount</code>.
     */
    public void setAmount(Double value) {
        set(6, value);
    }

    /**
     * Getter for <code>public.pay_reference_document.amount</code>.
     */
    public Double getAmount() {
        return (Double) get(6);
    }

    /**
     * Setter for <code>public.pay_reference_document.deposit_date</code>.
     */
    public void setDepositDate(LocalDate value) {
        set(7, value);
    }

    /**
     * Getter for <code>public.pay_reference_document.deposit_date</code>.
     */
    public LocalDate getDepositDate() {
        return (LocalDate) get(7);
    }

    /**
     * Setter for <code>public.pay_reference_document.entry_date</code>.
     */
    public void setEntryDate(LocalDate value) {
        set(8, value);
    }

    /**
     * Getter for <code>public.pay_reference_document.entry_date</code>.
     */
    public LocalDate getEntryDate() {
        return (LocalDate) get(8);
    }

    /**
     * Setter for <code>public.pay_reference_document.issuer_id</code>.
     */
    public void setIssuerId(Long value) {
        set(9, value);
    }

    /**
     * Getter for <code>public.pay_reference_document.issuer_id</code>.
     */
    public Long getIssuerId() {
        return (Long) get(9);
    }

    /**
     * Setter for <code>public.pay_reference_document.other_reference</code>.
     */
    public void setOtherReference(String value) {
        set(10, value);
    }

    /**
     * Getter for <code>public.pay_reference_document.other_reference</code>.
     */
    public String getOtherReference() {
        return (String) get(10);
    }

    /**
     * Setter for <code>public.pay_reference_document.status_id</code>.
     */
    public void setStatusId(String value) {
        set(11, value);
    }

    /**
     * Getter for <code>public.pay_reference_document.status_id</code>.
     */
    public String getStatusId() {
        return (String) get(11);
    }

    /**
     * Setter for <code>public.pay_reference_document.step_id</code>.
     */
    public void setStepId(String value) {
        set(12, value);
    }

    /**
     * Getter for <code>public.pay_reference_document.step_id</code>.
     */
    public String getStepId() {
        return (String) get(12);
    }

    /**
     * Setter for <code>public.pay_reference_document.amount_paid</code>.
     */
    public void setAmountPaid(Double value) {
        set(13, value);
    }

    /**
     * Getter for <code>public.pay_reference_document.amount_paid</code>.
     */
    public Double getAmountPaid() {
        return (Double) get(13);
    }

    /**
     * Setter for <code>public.pay_reference_document.amount_left</code>.
     */
    public void setAmountLeft(Double value) {
        set(14, value);
    }

    /**
     * Getter for <code>public.pay_reference_document.amount_left</code>.
     */
    public Double getAmountLeft() {
        return (Double) get(14);
    }

    /**
     * Setter for <code>public.pay_reference_document.author_id</code>.
     */
    public void setAuthorId(Long value) {
        set(15, value);
    }

    /**
     * Getter for <code>public.pay_reference_document.author_id</code>.
     */
    public Long getAuthorId() {
        return (Long) get(15);
    }

    /**
     * Setter for <code>public.pay_reference_document.advanced_amount</code>.
     */
    public void setAdvancedAmount(Double value) {
        set(16, value);
    }

    /**
     * Getter for <code>public.pay_reference_document.advanced_amount</code>.
     */
    public Double getAdvancedAmount() {
        return (Double) get(16);
    }

    /**
     * Setter for <code>public.pay_reference_document.worker_id</code>.
     */
    public void setWorkerId(Long value) {
        set(17, value);
    }

    /**
     * Getter for <code>public.pay_reference_document.worker_id</code>.
     */
    public Long getWorkerId() {
        return (Long) get(17);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record18 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row18<Long, String, LocalDate, String, String, String, Double, LocalDate, LocalDate, Long, String, String, String, Double, Double, Long, Double, Long> fieldsRow() {
        return (Row18) super.fieldsRow();
    }

    @Override
    public Row18<Long, String, LocalDate, String, String, String, Double, LocalDate, LocalDate, Long, String, String, String, Double, Double, Long, Double, Long> valuesRow() {
        return (Row18) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return PayReferenceDocument.PAY_REFERENCE_DOCUMENT.ID;
    }

    @Override
    public Field<String> field2() {
        return PayReferenceDocument.PAY_REFERENCE_DOCUMENT.TYPE_ID;
    }

    @Override
    public Field<LocalDate> field3() {
        return PayReferenceDocument.PAY_REFERENCE_DOCUMENT.DATE;
    }

    @Override
    public Field<String> field4() {
        return PayReferenceDocument.PAY_REFERENCE_DOCUMENT.REFERENCE;
    }

    @Override
    public Field<String> field5() {
        return PayReferenceDocument.PAY_REFERENCE_DOCUMENT.OBJECT;
    }

    @Override
    public Field<String> field6() {
        return PayReferenceDocument.PAY_REFERENCE_DOCUMENT.PLACE;
    }

    @Override
    public Field<Double> field7() {
        return PayReferenceDocument.PAY_REFERENCE_DOCUMENT.AMOUNT;
    }

    @Override
    public Field<LocalDate> field8() {
        return PayReferenceDocument.PAY_REFERENCE_DOCUMENT.DEPOSIT_DATE;
    }

    @Override
    public Field<LocalDate> field9() {
        return PayReferenceDocument.PAY_REFERENCE_DOCUMENT.ENTRY_DATE;
    }

    @Override
    public Field<Long> field10() {
        return PayReferenceDocument.PAY_REFERENCE_DOCUMENT.ISSUER_ID;
    }

    @Override
    public Field<String> field11() {
        return PayReferenceDocument.PAY_REFERENCE_DOCUMENT.OTHER_REFERENCE;
    }

    @Override
    public Field<String> field12() {
        return PayReferenceDocument.PAY_REFERENCE_DOCUMENT.STATUS_ID;
    }

    @Override
    public Field<String> field13() {
        return PayReferenceDocument.PAY_REFERENCE_DOCUMENT.STEP_ID;
    }

    @Override
    public Field<Double> field14() {
        return PayReferenceDocument.PAY_REFERENCE_DOCUMENT.AMOUNT_PAID;
    }

    @Override
    public Field<Double> field15() {
        return PayReferenceDocument.PAY_REFERENCE_DOCUMENT.AMOUNT_LEFT;
    }

    @Override
    public Field<Long> field16() {
        return PayReferenceDocument.PAY_REFERENCE_DOCUMENT.AUTHOR_ID;
    }

    @Override
    public Field<Double> field17() {
        return PayReferenceDocument.PAY_REFERENCE_DOCUMENT.ADVANCED_AMOUNT;
    }

    @Override
    public Field<Long> field18() {
        return PayReferenceDocument.PAY_REFERENCE_DOCUMENT.WORKER_ID;
    }

    @Override
    public Long component1() {
        return getId();
    }

    @Override
    public String component2() {
        return getTypeId();
    }

    @Override
    public LocalDate component3() {
        return getDate();
    }

    @Override
    public String component4() {
        return getReference();
    }

    @Override
    public String component5() {
        return getObject();
    }

    @Override
    public String component6() {
        return getPlace();
    }

    @Override
    public Double component7() {
        return getAmount();
    }

    @Override
    public LocalDate component8() {
        return getDepositDate();
    }

    @Override
    public LocalDate component9() {
        return getEntryDate();
    }

    @Override
    public Long component10() {
        return getIssuerId();
    }

    @Override
    public String component11() {
        return getOtherReference();
    }

    @Override
    public String component12() {
        return getStatusId();
    }

    @Override
    public String component13() {
        return getStepId();
    }

    @Override
    public Double component14() {
        return getAmountPaid();
    }

    @Override
    public Double component15() {
        return getAmountLeft();
    }

    @Override
    public Long component16() {
        return getAuthorId();
    }

    @Override
    public Double component17() {
        return getAdvancedAmount();
    }

    @Override
    public Long component18() {
        return getWorkerId();
    }

    @Override
    public Long value1() {
        return getId();
    }

    @Override
    public String value2() {
        return getTypeId();
    }

    @Override
    public LocalDate value3() {
        return getDate();
    }

    @Override
    public String value4() {
        return getReference();
    }

    @Override
    public String value5() {
        return getObject();
    }

    @Override
    public String value6() {
        return getPlace();
    }

    @Override
    public Double value7() {
        return getAmount();
    }

    @Override
    public LocalDate value8() {
        return getDepositDate();
    }

    @Override
    public LocalDate value9() {
        return getEntryDate();
    }

    @Override
    public Long value10() {
        return getIssuerId();
    }

    @Override
    public String value11() {
        return getOtherReference();
    }

    @Override
    public String value12() {
        return getStatusId();
    }

    @Override
    public String value13() {
        return getStepId();
    }

    @Override
    public Double value14() {
        return getAmountPaid();
    }

    @Override
    public Double value15() {
        return getAmountLeft();
    }

    @Override
    public Long value16() {
        return getAuthorId();
    }

    @Override
    public Double value17() {
        return getAdvancedAmount();
    }

    @Override
    public Long value18() {
        return getWorkerId();
    }

    @Override
    public PayReferenceDocumentRecord value1(Long value) {
        setId(value);
        return this;
    }

    @Override
    public PayReferenceDocumentRecord value2(String value) {
        setTypeId(value);
        return this;
    }

    @Override
    public PayReferenceDocumentRecord value3(LocalDate value) {
        setDate(value);
        return this;
    }

    @Override
    public PayReferenceDocumentRecord value4(String value) {
        setReference(value);
        return this;
    }

    @Override
    public PayReferenceDocumentRecord value5(String value) {
        setObject(value);
        return this;
    }

    @Override
    public PayReferenceDocumentRecord value6(String value) {
        setPlace(value);
        return this;
    }

    @Override
    public PayReferenceDocumentRecord value7(Double value) {
        setAmount(value);
        return this;
    }

    @Override
    public PayReferenceDocumentRecord value8(LocalDate value) {
        setDepositDate(value);
        return this;
    }

    @Override
    public PayReferenceDocumentRecord value9(LocalDate value) {
        setEntryDate(value);
        return this;
    }

    @Override
    public PayReferenceDocumentRecord value10(Long value) {
        setIssuerId(value);
        return this;
    }

    @Override
    public PayReferenceDocumentRecord value11(String value) {
        setOtherReference(value);
        return this;
    }

    @Override
    public PayReferenceDocumentRecord value12(String value) {
        setStatusId(value);
        return this;
    }

    @Override
    public PayReferenceDocumentRecord value13(String value) {
        setStepId(value);
        return this;
    }

    @Override
    public PayReferenceDocumentRecord value14(Double value) {
        setAmountPaid(value);
        return this;
    }

    @Override
    public PayReferenceDocumentRecord value15(Double value) {
        setAmountLeft(value);
        return this;
    }

    @Override
    public PayReferenceDocumentRecord value16(Long value) {
        setAuthorId(value);
        return this;
    }

    @Override
    public PayReferenceDocumentRecord value17(Double value) {
        setAdvancedAmount(value);
        return this;
    }

    @Override
    public PayReferenceDocumentRecord value18(Long value) {
        setWorkerId(value);
        return this;
    }

    @Override
    public PayReferenceDocumentRecord values(Long value1, String value2, LocalDate value3, String value4, String value5, String value6, Double value7, LocalDate value8, LocalDate value9, Long value10, String value11, String value12, String value13, Double value14, Double value15, Long value16, Double value17, Long value18) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        value9(value9);
        value10(value10);
        value11(value11);
        value12(value12);
        value13(value13);
        value14(value14);
        value15(value15);
        value16(value16);
        value17(value17);
        value18(value18);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached PayReferenceDocumentRecord
     */
    public PayReferenceDocumentRecord() {
        super(PayReferenceDocument.PAY_REFERENCE_DOCUMENT);
    }

    /**
     * Create a detached, initialised PayReferenceDocumentRecord
     */
    public PayReferenceDocumentRecord(Long id, String typeId, LocalDate date, String reference, String object, String place, Double amount, LocalDate depositDate, LocalDate entryDate, Long issuerId, String otherReference, String statusId, String stepId, Double amountPaid, Double amountLeft, Long authorId, Double advancedAmount, Long workerId) {
        super(PayReferenceDocument.PAY_REFERENCE_DOCUMENT);

        setId(id);
        setTypeId(typeId);
        setDate(date);
        setReference(reference);
        setObject(object);
        setPlace(place);
        setAmount(amount);
        setDepositDate(depositDate);
        setEntryDate(entryDate);
        setIssuerId(issuerId);
        setOtherReference(otherReference);
        setStatusId(statusId);
        setStepId(stepId);
        setAmountPaid(amountPaid);
        setAmountLeft(amountLeft);
        setAuthorId(authorId);
        setAdvancedAmount(advancedAmount);
        setWorkerId(workerId);
    }
}
