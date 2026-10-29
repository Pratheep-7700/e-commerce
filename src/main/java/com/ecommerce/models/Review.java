package com.ecommerce.models;

import java.time.Instant;

public class Review {
	private final long id;
	private final long productId;
	private final long customerId;
	private final int rating;
	private final String comment;
	private final Instant createdAt;

	public Review(long id, long productId, long customerId, int rating, String comment) {
		if (id <= 0 || productId <= 0 || customerId <= 0 || rating < 1 || rating > 5) {
			throw new IllegalArgumentException("Review ids must be positive and rating must be between 1 and 5");
		}
		this.id = id;
		this.productId = productId;
		this.customerId = customerId;
		this.rating = rating;
		this.comment = comment == null ? "" : comment.trim();
		this.createdAt = Instant.now();
	}

	public long getId() { return id; }
	public long getProductId() { return productId; }
	public long getCustomerId() { return customerId; }
	public int getRating() { return rating; }
	public String getComment() { return comment; }
	public Instant getCreatedAt() { return createdAt; }
}