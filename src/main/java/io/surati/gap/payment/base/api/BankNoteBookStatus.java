package io.surati.gap.payment.base.api;

public enum BankNoteBookStatus {

	NONE("Aucun"),
	REGISTERED("Répertorié"),
	IN_USE("En utilisation"),
	BLOCKED("Bloqué"),
	TERMINATED("Terminé");

	private final String title;

	private BankNoteBookStatus(final String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return this.title;
	}
}
