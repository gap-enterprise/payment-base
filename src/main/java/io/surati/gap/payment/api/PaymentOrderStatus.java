package io.surati.gap.payment.api;

public enum PaymentOrderStatus {

	NONE("Aucun"),
	TO_PREPARE("A préparer"),
	IN_WAITING_FOR_PAYMENT("En attente de paiement"),
	EXECUTED("Exécuté"),
	CANCELLED("Annulé");

	private final String title;

	private PaymentOrderStatus(final String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return this.title;
	}
}
