package io.surati.gap.payment.base.api;

public enum PaymentMeanFieldType {

	MONTANT_EN_CHIFFRES("Montant en chiffres"),
	MONTANT_EN_LETTRES_LIGNE_1("Montant en lettres ligne 1"),
	MONTANT_EN_LETTRES_LIGNE_2("Montant en lettres ligne 2"),
	BENEFICIAIRE("Bénéficiaire"),
	VILLE_EDITION("Ville d'édition"),
	DATE_EDITION("Date d'édition"),
	DATE_ECHEANCE("Date d'échéance"),
	MENTION_SUPPLEMENTAIRE_1("Mention supplémentaire 1"),
	MENTION_SUPPLEMENTAIRE_2("Mention supplémentaire 2");

	private final String title;

	private PaymentMeanFieldType(final String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return this.title;
	}
}
