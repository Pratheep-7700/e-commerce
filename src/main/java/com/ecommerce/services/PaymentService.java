package com.ecommerce.services;

import com.ecommerce.models.Payment;
import com.ecommerce.strategy.PaymentMethod;
import java.math.BigDecimal;

public class PaymentService {
	public Payment pay(BigDecimal amount, PaymentMethod method) {
		if (amount == null || amount.signum() < 0 || method == null) {
			throw new IllegalArgumentException("Amount and payment method are required");
		}
		return method.pay(amount);
	}
}