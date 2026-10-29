package com.ecommerce.models;

import java.math.BigDecimal;
import java.time.Instant;

public class Payment {
	public enum Status { COMPLETED, FAILED }

	private final String transactionId;
	private final String method;
	private final BigDecimal amount;
	private final Status status;
	private final Instant createdAt;

	public Payment(String transactionId, String method, BigDecimal amount, Status status) {
		if (transactionId == null || transactionId.isBlank() || method == null || method.isBlank()
				|| amount == null || amount.signum() < 0 || status == null) {
			throw new IllegalArgumentException("Valid payment details are required");
		}
		this.transactionId = transactionId;
		this.method = method;
		this.amount = amount;
		this.status = status;
		this.createdAt = Instant.now();
	}

	public String getTransactionId() { return transactionId; }
	public String getMethod() { return method; }
	public BigDecimal getAmount() { return amount; }
	public Status getStatus() { return status; }
	public Instant getCreatedAt() { return createdAt; }
}