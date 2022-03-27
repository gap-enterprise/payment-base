package io.surati.gap.payment.base.api;

public enum PaymentStatus {

	NONE("Aucun"),
	TO_PRINT("A imprimer"),
	ISSUED("Émis"),
	CANCELLED("Annulé");

	private final String title;

	private PaymentStatus(final String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return this.title;
	}
}
