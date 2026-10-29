package com.ecommerce.strategy;

import com.ecommerce.models.Payment;
import java.math.BigDecimal;

public interface PaymentMethod {
	String getName();
	Payment pay(BigDecimal amount);
}