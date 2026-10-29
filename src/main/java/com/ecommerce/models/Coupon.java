package com.ecommerce.models;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Coupon {
	private final String code;
	private final BigDecimal discountPercent;
	private boolean active;

	public Coupon(String code, BigDecimal discountPercent) {
		if (code == null || code.isBlank() || discountPercent == null
				|| discountPercent.signum() <= 0 || discountPercent.compareTo(BigDecimal.valueOf(100)) > 0) {
			throw new IllegalArgumentException("Coupon code and discount from 0 to 100 are required");
		}
		this.code = code.trim().toUpperCase();
		this.discountPercent = discountPercent;
		this.active = true;
	}

	public String getCode() { return code; }
	public BigDecimal getDiscountPercent() { return discountPercent; }
	public boolean isActive() { return active; }
	public void setActive(boolean active) { this.active = active; }

	public BigDecimal calculateDiscount(BigDecimal subtotal) {
		if (!active) throw new IllegalStateException("Coupon is inactive");
		if (subtotal == null || subtotal.signum() < 0) throw new IllegalArgumentException("Subtotal cannot be negative");
		return subtotal.multiply(discountPercent).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
	}
}