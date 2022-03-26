package io.surati.gap.payment.impl;

import io.surati.gap.payment.api.PaymentCondition;
import io.surati.gap.payment.api.ThirdParty;
import io.surati.gap.payment.api.ThirdPartyFamily;

public final class ThirdPartySample implements ThirdParty {

	@Override
	public Long id() {
		return 0L;
	}

	@Override
	public String name() {
		return "SURATI";
	}

	@Override
	public void update(String name) {
		throw new UnsupportedOperationException("ThirdPartySample#update");
	}

	@Override
	public String code() {
		return "SUR";
	}

	@Override
	public String abbreviated() {
		return "SURATI";
	}

	@Override
	public void update(String code, String name, String abbreviated) {
		throw new UnsupportedOperationException("ThirdPartySample#update");
	}

	@Override
	public ThirdPartyFamily family() {
		return null;
	}

	@Override
	public void assign(ThirdPartyFamily family) {
		throw new UnsupportedOperationException("ThirdPartySample#assign");
	}

	@Override
	public PaymentCondition paymentCondition() {
		return null;
	}

}
