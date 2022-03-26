package io.surati.gap.payment.api;

public enum PaymentMeanType {

	NONE("Aucun", false),
	CHEQUE("Chèque", true),
	BILLET_A_ORDRE("Billet à ordre", true),
	LETTRE_DE_CHANGE("Lettre de change", true);
	
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
