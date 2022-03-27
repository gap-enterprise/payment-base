package io.surati.gap.payment.base.impl;

import io.surati.gap.payment.base.api.Point;

public final class PointImpl implements Point {

	private final double x;
	
	private final double y;
	
	public PointImpl(final double x, final double y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public double x() {
		return this.x;
	}

	@Override
	public double y() {
		return this.y;
	}

}
