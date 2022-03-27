/*
 * This file is generated by jOOQ.
 */
package io.surati.gap.payment.jooq.generated.tables;


import io.surati.gap.payment.jooq.generated.Public;
import io.surati.gap.payment.jooq.generated.tables.records.PayBankAccountViewRecord;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row7;
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
public class PayBankAccountView extends TableImpl<PayBankAccountViewRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.pay_bank_account_view</code>
     */
    public static final PayBankAccountView PAY_BANK_ACCOUNT_VIEW = new PayBankAccountView();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<PayBankAccountViewRecord> getRecordType() {
        return PayBankAccountViewRecord.class;
    }

    /**
     * The column <code>public.pay_bank_account_view.id</code>.
     */
    public final TableField<PayBankAccountViewRecord, Long> ID = createField(DSL.name("id"), SQLDataType.BIGINT, this, "");

    /**
     * The column <code>public.pay_bank_account_view.branch_code</code>.
     */
    public final TableField<PayBankAccountViewRecord, String> BRANCH_CODE = createField(DSL.name("branch_code"), SQLDataType.VARCHAR(5), this, "");

    /**
     * The column <code>public.pay_bank_account_view.number</code>.
     */
    public final TableField<PayBankAccountViewRecord, String> NUMBER = createField(DSL.name("number"), SQLDataType.VARCHAR(12), this, "");

    /**
     * The column <code>public.pay_bank_account_view.key</code>.
     */
    public final TableField<PayBankAccountViewRecord, String> KEY = createField(DSL.name("key"), SQLDataType.VARCHAR(2), this, "");

    /**
     * The column <code>public.pay_bank_account_view.bank_id</code>.
     */
    public final TableField<PayBankAccountViewRecord, Long> BANK_ID = createField(DSL.name("bank_id"), SQLDataType.BIGINT, this, "");

    /**
     * The column <code>public.pay_bank_account_view.holder_id</code>.
     */
    public final TableField<PayBankAccountViewRecord, Long> HOLDER_ID = createField(DSL.name("holder_id"), SQLDataType.BIGINT, this, "");

    /**
     * The column <code>public.pay_bank_account_view.full_number</code>.
     */
    public final TableField<PayBankAccountViewRecord, String> FULL_NUMBER = createField(DSL.name("full_number"), SQLDataType.VARCHAR(69), this, "");

    private PayBankAccountView(Name alias, Table<PayBankAccountViewRecord> aliased) {
        this(alias, aliased, null);
    }

    private PayBankAccountView(Name alias, Table<PayBankAccountViewRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.view("CREATE FORCE VIEW \"public\".\"pay_bank_account_view\"(\"id\", \"branch_code\", \"number\", \"key\", \"bank_id\", \"holder_id\", \"full_number\") AS\nSELECT\n    \"ba\".\"id\",\n    \"ba\".\"branch_code\",\n    \"ba\".\"number\",\n    \"ba\".\"key\",\n    \"ba\".\"bank_id\",\n    \"ba\".\"holder_id\",\n    CONCAT(\"b\".\"code\", \"ba\".\"branch_code\", \"ba\".\"number\", \"ba\".\"key\") AS \"full_number\"\nFROM \"public\".\"pay_bank_account\" \"ba\"\nLEFT OUTER JOIN \"public\".\"pay_third_party\" \"b\"\n    ON \"b\".\"id\" = \"ba\".\"bank_id\""));
    }

    /**
     * Create an aliased <code>public.pay_bank_account_view</code> table reference
     */
    public PayBankAccountView(String alias) {
        this(DSL.name(alias), PAY_BANK_ACCOUNT_VIEW);
    }

    /**
     * Create an aliased <code>public.pay_bank_account_view</code> table reference
     */
    public PayBankAccountView(Name alias) {
        this(alias, PAY_BANK_ACCOUNT_VIEW);
    }

    /**
     * Create a <code>public.pay_bank_account_view</code> table reference
     */
    public PayBankAccountView() {
        this(DSL.name("pay_bank_account_view"), null);
    }

    public <O extends Record> PayBankAccountView(Table<O> child, ForeignKey<O, PayBankAccountViewRecord> key) {
        super(child, key, PAY_BANK_ACCOUNT_VIEW);
    }

    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    @Override
    public PayBankAccountView as(String alias) {
        return new PayBankAccountView(DSL.name(alias), this);
    }

    @Override
    public PayBankAccountView as(Name alias) {
        return new PayBankAccountView(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public PayBankAccountView rename(String name) {
        return new PayBankAccountView(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public PayBankAccountView rename(Name name) {
        return new PayBankAccountView(name, null);
    }

    // -------------------------------------------------------------------------
    // Row7 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row7<Long, String, String, String, Long, Long, String> fieldsRow() {
        return (Row7) super.fieldsRow();
    }
}
