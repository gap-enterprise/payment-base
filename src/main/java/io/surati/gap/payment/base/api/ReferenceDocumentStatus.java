package io.surati.gap.payment.base.api;

public enum ReferenceDocumentStatus {

	NONE("Aucun"),
	WAITING_FOR_PAYMENT("En attente de paiement"),
	PAID_PARTIALLY("Payé partiellement"),
	PAID("Payé");

	private final String title;

	private ReferenceDocumentStatus(final String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return this.title;
	}
}
