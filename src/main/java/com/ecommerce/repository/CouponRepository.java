package com.ecommerce.repository;

import com.ecommerce.models.Coupon;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class CouponRepository {
	private final Map<String, Coupon> coupons = new LinkedHashMap<>();

	public Coupon save(Coupon coupon) { coupons.put(coupon.getCode(), coupon); return coupon; }
	public Optional<Coupon> findByCode(String code) {
		return Optional.ofNullable(coupons.get(code == null ? "" : code.trim().toUpperCase()));
	}
	public Collection<Coupon> findAll() { return java.util.List.copyOf(coupons.values()); }
}