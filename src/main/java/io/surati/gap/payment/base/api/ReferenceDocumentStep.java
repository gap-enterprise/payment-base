package io.surati.gap.payment.base.api;

public enum ReferenceDocumentStep {

	NONE("Aucun"),
	TO_TREAT("A traiter"),
	SELECTED("Sélectionné"),
	IN_PREPARATION_FOR("En préparation"),
	IN_PAYMENT("En paiement"),
	ARCHIVED("Archivé");

	private final String title;

	private ReferenceDocumentStep(final String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return this.title;
	}
}
