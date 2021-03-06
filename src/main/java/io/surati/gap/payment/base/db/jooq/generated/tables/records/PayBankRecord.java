/*
 * This file is generated by jOOQ.
 */
package io.surati.gap.payment.base.db.jooq.generated.tables.records;


import io.surati.gap.payment.base.db.jooq.generated.tables.PayBank;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record5;
import org.jooq.Row5;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class PayBankRecord extends UpdatableRecordImpl<PayBankRecord> implements Record5<Long, String, String, String, String> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.pay_bank.id</code>.
     */
    public void setId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.pay_bank.id</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>public.pay_bank.representative</code>.
     */
    public void setRepresentative(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.pay_bank.representative</code>.
     */
    public String getRepresentative() {
        return (String) get(1);
    }

    /**
     * Setter for <code>public.pay_bank.representative_position</code>.
     */
    public void setRepresentativePosition(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.pay_bank.representative_position</code>.
     */
    public String getRepresentativePosition() {
        return (String) get(2);
    }

    /**
     * Setter for <code>public.pay_bank.representative_civility</code>.
     */
    public void setRepresentativeCivility(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>public.pay_bank.representative_civility</code>.
     */
    public String getRepresentativeCivility() {
        return (String) get(3);
    }

    /**
     * Setter for <code>public.pay_bank.headquarters</code>.
     */
    public void setHeadquarters(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>public.pay_bank.headquarters</code>.
     */
    public String getHeadquarters() {
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
    public Row5<Long, String, String, String, String> fieldsRow() {
        return (Row5) super.fieldsRow();
    }

    @Override
    public Row5<Long, String, String, String, String> valuesRow() {
        return (Row5) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return PayBank.PAY_BANK.ID;
    }

    @Override
    public Field<String> field2() {
        return PayBank.PAY_BANK.REPRESENTATIVE;
    }

    @Override
    public Field<String> field3() {
        return PayBank.PAY_BANK.REPRESENTATIVE_POSITION;
    }

    @Override
    public Field<String> field4() {
        return PayBank.PAY_BANK.REPRESENTATIVE_CIVILITY;
    }

    @Override
    public Field<String> field5() {
        return PayBank.PAY_BANK.HEADQUARTERS;
    }

    @Override
    public Long component1() {
        return getId();
    }

    @Override
    public String component2() {
        return getRepresentative();
    }

    @Override
    public String component3() {
        return getRepresentativePosition();
    }

    @Override
    public String component4() {
        return getRepresentativeCivility();
    }

    @Override
    public String component5() {
        return getHeadquarters();
    }

    @Override
    public Long value1() {
        return getId();
    }

    @Override
    public String value2() {
        return getRepresentative();
    }

    @Override
    public String value3() {
        return getRepresentativePosition();
    }

    @Override
    public String value4() {
        return getRepresentativeCivility();
    }

    @Override
    public String value5() {
        return getHeadquarters();
    }

    @Override
    public PayBankRecord value1(Long value) {
        setId(value);
        return this;
    }

    @Override
    public PayBankRecord value2(String value) {
        setRepresentative(value);
        return this;
    }

    @Override
    public PayBankRecord value3(String value) {
        setRepresentativePosition(value);
        return this;
    }

    @Override
    public PayBankRecord value4(String value) {
        setRepresentativeCivility(value);
        return this;
    }

    @Override
    public PayBankRecord value5(String value) {
        setHeadquarters(value);
        return this;
    }

    @Override
    public PayBankRecord values(Long value1, String value2, String value3, String value4, String value5) {
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
     * Create a detached PayBankRecord
     */
    public PayBankRecord() {
        super(PayBank.PAY_BANK);
    }

    /**
     * Create a detached, initialised PayBankRecord
     */
    public PayBankRecord(Long id, String representative, String representativePosition, String representativeCivility, String headquarters) {
        super(PayBank.PAY_BANK);

        setId(id);
        setRepresentative(representative);
        setRepresentativePosition(representativePosition);
        setRepresentativeCivility(representativeCivility);
        setHeadquarters(headquarters);
    }
}
