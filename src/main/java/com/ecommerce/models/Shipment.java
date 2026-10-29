package com.ecommerce.models;

import java.time.Instant;

public class Shipment {
	public enum Status { PREPARING, SHIPPED, DELIVERED }

	private final String trackingNumber;
	private final String address;
	private final Instant createdAt;
	private Status status;

	public Shipment(String trackingNumber, String address) {
		if (trackingNumber == null || trackingNumber.isBlank() || address == null || address.isBlank()) {
			throw new IllegalArgumentException("Tracking number and address are required");
		}
		this.trackingNumber = trackingNumber;
		this.address = address.trim();
		this.createdAt = Instant.now();
		this.status = Status.PREPARING;
	}

	public String getTrackingNumber() { return trackingNumber; }
	public String getAddress() { return address; }
	public Instant getCreatedAt() { return createdAt; }
	public Status getStatus() { return status; }
	public void setStatus(Status status) {
		if (status == null) throw new IllegalArgumentException("Shipment status is required");
		this.status = status;
	}
}