package com.ecommerce.strategy;

import com.ecommerce.models.Payment;
import java.math.BigDecimal;
import java.util.UUID;

public class CodPayment implements PaymentMethod {
	@Override public String getName() { return "Cash on delivery"; }
	@Override public Payment pay(BigDecimal amount) {
		return new Payment(UUID.randomUUID().toString(), getName(), amount, Payment.Status.COMPLETED);
	}
}