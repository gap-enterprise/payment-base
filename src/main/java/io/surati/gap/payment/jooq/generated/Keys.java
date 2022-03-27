/*
 * This file is generated by jOOQ.
 */
package io.surati.gap.payment.jooq.generated;


import io.surati.gap.payment.jooq.generated.tables.PayBank;
import io.surati.gap.payment.jooq.generated.tables.PayBankAccount;
import io.surati.gap.payment.jooq.generated.tables.PayBankAccountAccountingSetting;
import io.surati.gap.payment.jooq.generated.tables.PayBankNote;
import io.surati.gap.payment.jooq.generated.tables.PayBankNoteBook;
import io.surati.gap.payment.jooq.generated.tables.PayPayment;
import io.surati.gap.payment.jooq.generated.tables.PayPaymentBatch;
import io.surati.gap.payment.jooq.generated.tables.PayPaymentExport;
import io.surati.gap.payment.jooq.generated.tables.PayPaymentMean;
import io.surati.gap.payment.jooq.generated.tables.PayPaymentMeanField;
import io.surati.gap.payment.jooq.generated.tables.PayPaymentOrder;
import io.surati.gap.payment.jooq.generated.tables.PayPaymentOrderGroup;
import io.surati.gap.payment.jooq.generated.tables.PayReferenceDocument;
import io.surati.gap.payment.jooq.generated.tables.PayThirdParty;
import io.surati.gap.payment.jooq.generated.tables.PayThirdPartyFamily;
import io.surati.gap.payment.jooq.generated.tables.PayThirdPartyPaymentMeanAllowed;
import io.surati.gap.payment.jooq.generated.tables.records.PayBankAccountAccountingSettingRecord;
import io.surati.gap.payment.jooq.generated.tables.records.PayBankAccountRecord;
import io.surati.gap.payment.jooq.generated.tables.records.PayBankNoteBookRecord;
import io.surati.gap.payment.jooq.generated.tables.records.PayBankNoteRecord;
import io.surati.gap.payment.jooq.generated.tables.records.PayBankRecord;
import io.surati.gap.payment.jooq.generated.tables.records.PayPaymentBatchRecord;
import io.surati.gap.payment.jooq.generated.tables.records.PayPaymentExportRecord;
import io.surati.gap.payment.jooq.generated.tables.records.PayPaymentMeanFieldRecord;
import io.surati.gap.payment.jooq.generated.tables.records.PayPaymentMeanRecord;
import io.surati.gap.payment.jooq.generated.tables.records.PayPaymentOrderGroupRecord;
import io.surati.gap.payment.jooq.generated.tables.records.PayPaymentOrderRecord;
import io.surati.gap.payment.jooq.generated.tables.records.PayPaymentRecord;
import io.surati.gap.payment.jooq.generated.tables.records.PayReferenceDocumentRecord;
import io.surati.gap.payment.jooq.generated.tables.records.PayThirdPartyFamilyRecord;
import io.surati.gap.payment.jooq.generated.tables.records.PayThirdPartyPaymentMeanAllowedRecord;
import io.surati.gap.payment.jooq.generated.tables.records.PayThirdPartyRecord;

import org.jooq.ForeignKey;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;


/**
 * A class modelling foreign key relationships and constraints of tables in 
 * public.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<PayBankRecord> PAY_BANK_PKEY = Internal.createUniqueKey(PayBank.PAY_BANK, DSL.name("pay_bank_pkey"), new TableField[] { PayBank.PAY_BANK.ID }, true);
    public static final UniqueKey<PayBankAccountRecord> PAY_BANK_ACCOUNT_PKEY = Internal.createUniqueKey(PayBankAccount.PAY_BANK_ACCOUNT, DSL.name("pay_bank_account_pkey"), new TableField[] { PayBankAccount.PAY_BANK_ACCOUNT.ID }, true);
    public static final UniqueKey<PayBankNoteRecord> PAY_BANK_NOTE_PKEY = Internal.createUniqueKey(PayBankNote.PAY_BANK_NOTE, DSL.name("pay_bank_note_pkey"), new TableField[] { PayBankNote.PAY_BANK_NOTE.ID }, true);
    public static final UniqueKey<PayBankNoteBookRecord> PAY_BANK_NOTE_BOOK_PKEY = Internal.createUniqueKey(PayBankNoteBook.PAY_BANK_NOTE_BOOK, DSL.name("pay_bank_note_book_pkey"), new TableField[] { PayBankNoteBook.PAY_BANK_NOTE_BOOK.ID }, true);
    public static final UniqueKey<PayPaymentRecord> PAY_PAYMENT_PKEY = Internal.createUniqueKey(PayPayment.PAY_PAYMENT, DSL.name("pay_payment_pkey"), new TableField[] { PayPayment.PAY_PAYMENT.ID }, true);
    public static final UniqueKey<PayPaymentBatchRecord> PAY_PAYMENT_BATCH_PKEY = Internal.createUniqueKey(PayPaymentBatch.PAY_PAYMENT_BATCH, DSL.name("pay_payment_batch_pkey"), new TableField[] { PayPaymentBatch.PAY_PAYMENT_BATCH.ID }, true);
    public static final UniqueKey<PayPaymentExportRecord> PAY_PAYMENT_EXPORT_PKEY = Internal.createUniqueKey(PayPaymentExport.PAY_PAYMENT_EXPORT, DSL.name("pay_payment_export_pkey"), new TableField[] { PayPaymentExport.PAY_PAYMENT_EXPORT.ID }, true);
    public static final UniqueKey<PayPaymentMeanRecord> PAY_PAYMENT_MEAN_PKEY = Internal.createUniqueKey(PayPaymentMean.PAY_PAYMENT_MEAN, DSL.name("pay_payment_mean_pkey"), new TableField[] { PayPaymentMean.PAY_PAYMENT_MEAN.ID }, true);
    public static final UniqueKey<PayPaymentMeanFieldRecord> PAY_PAYMENT_MEAN_FIELD_PKEY = Internal.createUniqueKey(PayPaymentMeanField.PAY_PAYMENT_MEAN_FIELD, DSL.name("pay_payment_mean_field_pkey"), new TableField[] { PayPaymentMeanField.PAY_PAYMENT_MEAN_FIELD.TYPE_ID, PayPaymentMeanField.PAY_PAYMENT_MEAN_FIELD.MEAN_ID }, true);
    public static final UniqueKey<PayPaymentOrderRecord> PAY_PAYMENT_ORDER_PKEY = Internal.createUniqueKey(PayPaymentOrder.PAY_PAYMENT_ORDER, DSL.name("pay_payment_order_pkey"), new TableField[] { PayPaymentOrder.PAY_PAYMENT_ORDER.ID }, true);
    public static final UniqueKey<PayPaymentOrderGroupRecord> PAY_PAYMENT_ORDER_GROUP_PKEY = Internal.createUniqueKey(PayPaymentOrderGroup.PAY_PAYMENT_ORDER_GROUP, DSL.name("pay_payment_order_group_pkey"), new TableField[] { PayPaymentOrderGroup.PAY_PAYMENT_ORDER_GROUP.ID }, true);
    public static final UniqueKey<PayReferenceDocumentRecord> PAY_REFERENCE_DOCUMENT_PKEY = Internal.createUniqueKey(PayReferenceDocument.PAY_REFERENCE_DOCUMENT, DSL.name("pay_reference_document_pkey"), new TableField[] { PayReferenceDocument.PAY_REFERENCE_DOCUMENT.ID }, true);
    public static final UniqueKey<PayThirdPartyRecord> PAY_THIRD_PARTY_CODE_KEY = Internal.createUniqueKey(PayThirdParty.PAY_THIRD_PARTY, DSL.name("pay_third_party_code_key"), new TableField[] { PayThirdParty.PAY_THIRD_PARTY.CODE }, true);
    public static final UniqueKey<PayThirdPartyRecord> PAY_THIRD_PARTY_PKEY = Internal.createUniqueKey(PayThirdParty.PAY_THIRD_PARTY, DSL.name("pay_third_party_pkey"), new TableField[] { PayThirdParty.PAY_THIRD_PARTY.ID }, true);
    public static final UniqueKey<PayThirdPartyFamilyRecord> PAY_THIRD_PARTY_FAMILY_CODE_KEY = Internal.createUniqueKey(PayThirdPartyFamily.PAY_THIRD_PARTY_FAMILY, DSL.name("pay_third_party_family_code_key"), new TableField[] { PayThirdPartyFamily.PAY_THIRD_PARTY_FAMILY.CODE }, true);
    public static final UniqueKey<PayThirdPartyFamilyRecord> PAY_THIRD_PARTY_FAMILY_PKEY = Internal.createUniqueKey(PayThirdPartyFamily.PAY_THIRD_PARTY_FAMILY, DSL.name("pay_third_party_family_pkey"), new TableField[] { PayThirdPartyFamily.PAY_THIRD_PARTY_FAMILY.ID }, true);
    public static final UniqueKey<PayThirdPartyPaymentMeanAllowedRecord> PAY_THIRD_PARTY_PAYMENT_MEAN_ALLOWED_PKEY = Internal.createUniqueKey(PayThirdPartyPaymentMeanAllowed.PAY_THIRD_PARTY_PAYMENT_MEAN_ALLOWED, DSL.name("pay_third_party_payment_mean_allowed_pkey"), new TableField[] { PayThirdPartyPaymentMeanAllowed.PAY_THIRD_PARTY_PAYMENT_MEAN_ALLOWED.THIRD_PARTY_ID, PayThirdPartyPaymentMeanAllowed.PAY_THIRD_PARTY_PAYMENT_MEAN_ALLOWED.MEAN_TYPE_ID }, true);

    // -------------------------------------------------------------------------
    // FOREIGN KEY definitions
    // -------------------------------------------------------------------------

    public static final ForeignKey<PayBankRecord, PayThirdPartyRecord> PAY_BANK_ID_FKEY = Internal.createForeignKey(PayBank.PAY_BANK, DSL.name("pay_bank_id_fkey"), new TableField[] { PayBank.PAY_BANK.ID }, Keys.PAY_THIRD_PARTY_PKEY, new TableField[] { PayThirdParty.PAY_THIRD_PARTY.ID }, true);
    public static final ForeignKey<PayBankAccountRecord, PayBankRecord> PAY_BANK_ACCOUNT_BANK_ID_FKEY = Internal.createForeignKey(PayBankAccount.PAY_BANK_ACCOUNT, DSL.name("pay_bank_account_bank_id_fkey"), new TableField[] { PayBankAccount.PAY_BANK_ACCOUNT.BANK_ID }, Keys.PAY_BANK_PKEY, new TableField[] { PayBank.PAY_BANK.ID }, true);
    public static final ForeignKey<PayBankAccountRecord, PayThirdPartyRecord> PAY_BANK_ACCOUNT_HOLDER_ID_FKEY = Internal.createForeignKey(PayBankAccount.PAY_BANK_ACCOUNT, DSL.name("pay_bank_account_holder_id_fkey"), new TableField[] { PayBankAccount.PAY_BANK_ACCOUNT.HOLDER_ID }, Keys.PAY_THIRD_PARTY_PKEY, new TableField[] { PayThirdParty.PAY_THIRD_PARTY.ID }, true);
    public static final ForeignKey<PayBankAccountAccountingSettingRecord, PayBankAccountRecord> PAY_BANK_ACCOUNT_ACCOUNTING_SETTING_ACCOUNT_ID_FKEY = Internal.createForeignKey(PayBankAccountAccountingSetting.PAY_BANK_ACCOUNT_ACCOUNTING_SETTING, DSL.name("pay_bank_account_accounting_setting_account_id_fkey"), new TableField[] { PayBankAccountAccountingSetting.PAY_BANK_ACCOUNT_ACCOUNTING_SETTING.ACCOUNT_ID }, Keys.PAY_BANK_ACCOUNT_PKEY, new TableField[] { PayBankAccount.PAY_BANK_ACCOUNT.ID }, true);
    public static final ForeignKey<PayBankNoteRecord, PayBankNoteBookRecord> PAY_BANK_NOTE_BOOK_ID_FKEY = Internal.createForeignKey(PayBankNote.PAY_BANK_NOTE, DSL.name("pay_bank_note_book_id_fkey"), new TableField[] { PayBankNote.PAY_BANK_NOTE.BOOK_ID }, Keys.PAY_BANK_NOTE_BOOK_PKEY, new TableField[] { PayBankNoteBook.PAY_BANK_NOTE_BOOK.ID }, true);
    public static final ForeignKey<PayBankNoteRecord, PayPaymentRecord> PAY_BANK_NOTE_ID_FKEY = Internal.createForeignKey(PayBankNote.PAY_BANK_NOTE, DSL.name("pay_bank_note_id_fkey"), new TableField[] { PayBankNote.PAY_BANK_NOTE.ID }, Keys.PAY_PAYMENT_PKEY, new TableField[] { PayPayment.PAY_PAYMENT.ID }, true);
    public static final ForeignKey<PayBankNoteBookRecord, PayBankAccountRecord> PAY_BANK_NOTE_BOOK_ACCOUNT_ID_FKEY = Internal.createForeignKey(PayBankNoteBook.PAY_BANK_NOTE_BOOK, DSL.name("pay_bank_note_book_account_id_fkey"), new TableField[] { PayBankNoteBook.PAY_BANK_NOTE_BOOK.ACCOUNT_ID }, Keys.PAY_BANK_ACCOUNT_PKEY, new TableField[] { PayBankAccount.PAY_BANK_ACCOUNT.ID }, true);
    public static final ForeignKey<PayPaymentRecord, PayPaymentBatchRecord> PAY_PAYMENT_BATCH_ID_FKEY = Internal.createForeignKey(PayPayment.PAY_PAYMENT, DSL.name("pay_payment_batch_id_fkey"), new TableField[] { PayPayment.PAY_PAYMENT.BATCH_ID }, Keys.PAY_PAYMENT_BATCH_PKEY, new TableField[] { PayPaymentBatch.PAY_PAYMENT_BATCH.ID }, true);
    public static final ForeignKey<PayPaymentRecord, PayThirdPartyRecord> PAY_PAYMENT_BENEFICIARY_ID_FKEY = Internal.createForeignKey(PayPayment.PAY_PAYMENT, DSL.name("pay_payment_beneficiary_id_fkey"), new TableField[] { PayPayment.PAY_PAYMENT.BENEFICIARY_ID }, Keys.PAY_THIRD_PARTY_PKEY, new TableField[] { PayThirdParty.PAY_THIRD_PARTY.ID }, true);
    public static final ForeignKey<PayPaymentRecord, PayPaymentOrderGroupRecord> PAY_PAYMENT_GROUP_ID_FKEY = Internal.createForeignKey(PayPayment.PAY_PAYMENT, DSL.name("pay_payment_group_id_fkey"), new TableField[] { PayPayment.PAY_PAYMENT.GROUP_ID }, Keys.PAY_PAYMENT_ORDER_GROUP_PKEY, new TableField[] { PayPaymentOrderGroup.PAY_PAYMENT_ORDER_GROUP.ID }, true);
    public static final ForeignKey<PayPaymentRecord, PayThirdPartyRecord> PAY_PAYMENT_ISSUER_ID_FKEY = Internal.createForeignKey(PayPayment.PAY_PAYMENT, DSL.name("pay_payment_issuer_id_fkey"), new TableField[] { PayPayment.PAY_PAYMENT.ISSUER_ID }, Keys.PAY_THIRD_PARTY_PKEY, new TableField[] { PayThirdParty.PAY_THIRD_PARTY.ID }, true);
    public static final ForeignKey<PayPaymentBatchRecord, PayBankAccountRecord> PAY_PAYMENT_BATCH_ACCOUNT_ID_FKEY = Internal.createForeignKey(PayPaymentBatch.PAY_PAYMENT_BATCH, DSL.name("pay_payment_batch_account_id_fkey"), new TableField[] { PayPaymentBatch.PAY_PAYMENT_BATCH.ACCOUNT_ID }, Keys.PAY_BANK_ACCOUNT_PKEY, new TableField[] { PayBankAccount.PAY_BANK_ACCOUNT.ID }, true);
    public static final ForeignKey<PayPaymentExportRecord, PayPaymentRecord> PAY_PAYMENT_EXPORT_PAYMENT_ID_FKEY = Internal.createForeignKey(PayPaymentExport.PAY_PAYMENT_EXPORT, DSL.name("pay_payment_export_payment_id_fkey"), new TableField[] { PayPaymentExport.PAY_PAYMENT_EXPORT.PAYMENT_ID }, Keys.PAY_PAYMENT_PKEY, new TableField[] { PayPayment.PAY_PAYMENT.ID }, true);
    public static final ForeignKey<PayPaymentMeanRecord, PayBankRecord> PAY_PAYMENT_MEAN_BANK_ID_FKEY = Internal.createForeignKey(PayPaymentMean.PAY_PAYMENT_MEAN, DSL.name("pay_payment_mean_bank_id_fkey"), new TableField[] { PayPaymentMean.PAY_PAYMENT_MEAN.BANK_ID }, Keys.PAY_BANK_PKEY, new TableField[] { PayBank.PAY_BANK.ID }, true);
    public static final ForeignKey<PayPaymentMeanFieldRecord, PayPaymentMeanRecord> PAY_PAYMENT_MEAN_FIELD_MEAN_ID_FKEY = Internal.createForeignKey(PayPaymentMeanField.PAY_PAYMENT_MEAN_FIELD, DSL.name("pay_payment_mean_field_mean_id_fkey"), new TableField[] { PayPaymentMeanField.PAY_PAYMENT_MEAN_FIELD.MEAN_ID }, Keys.PAY_PAYMENT_MEAN_PKEY, new TableField[] { PayPaymentMean.PAY_PAYMENT_MEAN.ID }, true);
    public static final ForeignKey<PayPaymentOrderRecord, PayThirdPartyRecord> PAY_PAYMENT_ORDER_BENEFICIARY_ID_FKEY = Internal.createForeignKey(PayPaymentOrder.PAY_PAYMENT_ORDER, DSL.name("pay_payment_order_beneficiary_id_fkey"), new TableField[] { PayPaymentOrder.PAY_PAYMENT_ORDER.BENEFICIARY_ID }, Keys.PAY_THIRD_PARTY_PKEY, new TableField[] { PayThirdParty.PAY_THIRD_PARTY.ID }, true);
    public static final ForeignKey<PayPaymentOrderRecord, PayPaymentOrderGroupRecord> PAY_PAYMENT_ORDER_GROUP_ID_FKEY = Internal.createForeignKey(PayPaymentOrder.PAY_PAYMENT_ORDER, DSL.name("pay_payment_order_group_id_fkey"), new TableField[] { PayPaymentOrder.PAY_PAYMENT_ORDER.GROUP_ID }, Keys.PAY_PAYMENT_ORDER_GROUP_PKEY, new TableField[] { PayPaymentOrderGroup.PAY_PAYMENT_ORDER_GROUP.ID }, true);
    public static final ForeignKey<PayPaymentOrderRecord, PayReferenceDocumentRecord> PAY_PAYMENT_ORDER_REFERENCE_DOCUMENT_ID_FKEY = Internal.createForeignKey(PayPaymentOrder.PAY_PAYMENT_ORDER, DSL.name("pay_payment_order_reference_document_id_fkey"), new TableField[] { PayPaymentOrder.PAY_PAYMENT_ORDER.REFERENCE_DOCUMENT_ID }, Keys.PAY_REFERENCE_DOCUMENT_PKEY, new TableField[] { PayReferenceDocument.PAY_REFERENCE_DOCUMENT.ID }, true);
    public static final ForeignKey<PayPaymentOrderGroupRecord, PayBankAccountRecord> PAY_PAYMENT_ORDER_GROUP_ACCOUNT_ID_FKEY = Internal.createForeignKey(PayPaymentOrderGroup.PAY_PAYMENT_ORDER_GROUP, DSL.name("pay_payment_order_group_account_id_fkey"), new TableField[] { PayPaymentOrderGroup.PAY_PAYMENT_ORDER_GROUP.ACCOUNT_ID }, Keys.PAY_BANK_ACCOUNT_PKEY, new TableField[] { PayBankAccount.PAY_BANK_ACCOUNT.ID }, true);
    public static final ForeignKey<PayPaymentOrderGroupRecord, PayThirdPartyRecord> PAY_PAYMENT_ORDER_GROUP_THIRD_PARTY_ID_FKEY = Internal.createForeignKey(PayPaymentOrderGroup.PAY_PAYMENT_ORDER_GROUP, DSL.name("pay_payment_order_group_third_party_id_fkey"), new TableField[] { PayPaymentOrderGroup.PAY_PAYMENT_ORDER_GROUP.THIRD_PARTY_ID }, Keys.PAY_THIRD_PARTY_PKEY, new TableField[] { PayThirdParty.PAY_THIRD_PARTY.ID }, true);
    public static final ForeignKey<PayReferenceDocumentRecord, PayThirdPartyRecord> PAY_REFERENCE_DOCUMENT_ISSUER_ID_FKEY = Internal.createForeignKey(PayReferenceDocument.PAY_REFERENCE_DOCUMENT, DSL.name("pay_reference_document_issuer_id_fkey"), new TableField[] { PayReferenceDocument.PAY_REFERENCE_DOCUMENT.ISSUER_ID }, Keys.PAY_THIRD_PARTY_PKEY, new TableField[] { PayThirdParty.PAY_THIRD_PARTY.ID }, true);
    public static final ForeignKey<PayThirdPartyRecord, PayThirdPartyFamilyRecord> PAY_THIRD_PARTY_FAMILY_ID_FKEY = Internal.createForeignKey(PayThirdParty.PAY_THIRD_PARTY, DSL.name("pay_third_party_family_id_fkey"), new TableField[] { PayThirdParty.PAY_THIRD_PARTY.FAMILY_ID }, Keys.PAY_THIRD_PARTY_FAMILY_PKEY, new TableField[] { PayThirdPartyFamily.PAY_THIRD_PARTY_FAMILY.ID }, true);
    public static final ForeignKey<PayThirdPartyPaymentMeanAllowedRecord, PayThirdPartyRecord> PAY_THIRD_PARTY_PAYMENT_MEAN_ALLOWED_THIRD_PARTY_ID_FKEY = Internal.createForeignKey(PayThirdPartyPaymentMeanAllowed.PAY_THIRD_PARTY_PAYMENT_MEAN_ALLOWED, DSL.name("pay_third_party_payment_mean_allowed_third_party_id_fkey"), new TableField[] { PayThirdPartyPaymentMeanAllowed.PAY_THIRD_PARTY_PAYMENT_MEAN_ALLOWED.THIRD_PARTY_ID }, Keys.PAY_THIRD_PARTY_PKEY, new TableField[] { PayThirdParty.PAY_THIRD_PARTY.ID }, true);
}
