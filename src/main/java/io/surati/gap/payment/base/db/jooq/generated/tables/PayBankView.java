/*
 * This file is generated by jOOQ.
 */
package io.surati.gap.payment.base.db.jooq.generated.tables;


import io.surati.gap.payment.base.db.jooq.generated.Public;
import io.surati.gap.payment.base.db.jooq.generated.tables.records.PayBankViewRecord;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row10;
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
public class PayBankView extends TableImpl<PayBankViewRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.pay_bank_view</code>
     */
    public static final PayBankView PAY_BANK_VIEW = new PayBankView();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<PayBankViewRecord> getRecordType() {
        return PayBankViewRecord.class;
    }

    /**
     * The column <code>public.pay_bank_view.id</code>.
     */
    public final TableField<PayBankViewRecord, Long> ID = createField(DSL.name("id"), SQLDataType.BIGINT, this, "");

    /**
     * The column <code>public.pay_bank_view.code</code>.
     */
    public final TableField<PayBankViewRecord, String> CODE = createField(DSL.name("code"), SQLDataType.VARCHAR(50), this, "");

    /**
     * The column <code>public.pay_bank_view.abbreviated</code>.
     */
    public final TableField<PayBankViewRecord, String> ABBREVIATED = createField(DSL.name("abbreviated"), SQLDataType.VARCHAR(100), this, "");

    /**
     * The column <code>public.pay_bank_view.family_id</code>.
     */
    public final TableField<PayBankViewRecord, Long> FAMILY_ID = createField(DSL.name("family_id"), SQLDataType.BIGINT, this, "");

    /**
     * The column <code>public.pay_bank_view.payment_deadline</code>.
     */
    public final TableField<PayBankViewRecord, Short> PAYMENT_DEADLINE = createField(DSL.name("payment_deadline"), SQLDataType.SMALLINT, this, "");

    /**
     * The column <code>public.pay_bank_view.name</code>.
     */
    public final TableField<PayBankViewRecord, String> NAME = createField(DSL.name("name"), SQLDataType.VARCHAR(100), this, "");

    /**
     * The column <code>public.pay_bank_view.representative</code>.
     */
    public final TableField<PayBankViewRecord, String> REPRESENTATIVE = createField(DSL.name("representative"), SQLDataType.VARCHAR(50), this, "");

    /**
     * The column <code>public.pay_bank_view.representative_position</code>.
     */
    public final TableField<PayBankViewRecord, String> REPRESENTATIVE_POSITION = createField(DSL.name("representative_position"), SQLDataType.VARCHAR(50), this, "");

    /**
     * The column <code>public.pay_bank_view.representative_civility</code>.
     */
    public final TableField<PayBankViewRecord, String> REPRESENTATIVE_CIVILITY = createField(DSL.name("representative_civility"), SQLDataType.VARCHAR(15), this, "");

    /**
     * The column <code>public.pay_bank_view.headquarters</code>.
     */
    public final TableField<PayBankViewRecord, String> HEADQUARTERS = createField(DSL.name("headquarters"), SQLDataType.VARCHAR(50), this, "");

    private PayBankView(Name alias, Table<PayBankViewRecord> aliased) {
        this(alias, aliased, null);
    }

    private PayBankView(Name alias, Table<PayBankViewRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.view("create view \"pay_bank_view\" as  SELECT tp.id,\n    tp.code,\n    tp.abbreviated,\n    tp.family_id,\n    tp.payment_deadline,\n    tp.name,\n    bk.representative,\n    bk.representative_position,\n    bk.representative_civility,\n    bk.headquarters\n   FROM (pay_bank bk\n     LEFT JOIN pay_third_party_view tp ON ((tp.id = bk.id)));"));
    }

    /**
     * Create an aliased <code>public.pay_bank_view</code> table reference
     */
    public PayBankView(String alias) {
        this(DSL.name(alias), PAY_BANK_VIEW);
    }

    /**
     * Create an aliased <code>public.pay_bank_view</code> table reference
     */
    public PayBankView(Name alias) {
        this(alias, PAY_BANK_VIEW);
    }

    /**
     * Create a <code>public.pay_bank_view</code> table reference
     */
    public PayBankView() {
        this(DSL.name("pay_bank_view"), null);
    }

    public <O extends Record> PayBankView(Table<O> child, ForeignKey<O, PayBankViewRecord> key) {
        super(child, key, PAY_BANK_VIEW);
    }

    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    @Override
    public PayBankView as(String alias) {
        return new PayBankView(DSL.name(alias), this);
    }

    @Override
    public PayBankView as(Name alias) {
        return new PayBankView(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public PayBankView rename(String name) {
        return new PayBankView(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public PayBankView rename(Name name) {
        return new PayBankView(name, null);
    }

    // -------------------------------------------------------------------------
    // Row10 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row10<Long, String, String, Long, Short, String, String, String, String, String> fieldsRow() {
        return (Row10) super.fieldsRow();
    }
}
