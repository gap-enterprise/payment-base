package io.surati.gap.payment.api;

public enum PaymentCancelReason {

	NONE("Aucun"),
	ERREUR_IMPRESSION("Erreur d'impression"),
	REJET_BANQUE("Rejet de la banque"),
	VOL_FORMULE("Vol de la formule"),
	PERTE_FORMULE("Perte de la formule"),
	UTILISATION_FRAUDULEUSE("Utilisation frauduleuse"),
	AUTRE("Autre");

	private final String title;

	private PaymentCancelReason(final String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return this.title;
	}
}
