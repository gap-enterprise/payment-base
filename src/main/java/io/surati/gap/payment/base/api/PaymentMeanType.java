package io.surati.gap.payment.base.api;

public enum PaymentMeanType {

	NONE("Aucun", false),
	CHQ("Chèque", true),
	BO("Billet à ordre", true),
	LC("Lettre de change", true),
	OV("Ordre de virement", false),
	CB("Carte bancaire", false),
	MM("Mobile money", false),
	BDC("Bon de caisse", false);
	
	private final String title;

	private final boolean isbanknote;

	private PaymentMeanType(final String title, final boolean isbanknote) {
		this.title = title;
		this.isbanknote = isbanknote;
	}

	@Override
	public String toString() {
		return this.title;
	}

	public boolean isBankNote() {
		return this.isbanknote;
	}
}
