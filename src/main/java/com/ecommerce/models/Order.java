package com.ecommerce.models;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class Order {
	public enum Status { PLACED, SHIPPED, DELIVERED, CANCELLED }

	private final long id;
	private final long customerId;
	private final List<OrderLine> items;
	private final BigDecimal subtotal;
	private final BigDecimal discount;
	private final BigDecimal total;
	private final String couponCode;
	private final Payment payment;
	private final Instant createdAt;
	private Status status;
	private Shipment shipment;

	public Order(long id, long customerId, List<OrderLine> items, BigDecimal subtotal,
				 BigDecimal discount, String couponCode, Payment payment) {
		if (id <= 0 || customerId <= 0 || items == null || items.isEmpty()) {
			throw new IllegalArgumentException("Order requires positive ids and at least one item");
		}
		this.id = id;
		this.customerId = customerId;
		this.items = List.copyOf(items);
		this.subtotal = subtotal;
		this.discount = discount;
		this.total = subtotal.subtract(discount);
		this.couponCode = couponCode;
		this.payment = payment;
		this.createdAt = Instant.now();
		this.status = Status.PLACED;
	}

	public long getId() { return id; }
	public long getCustomerId() { return customerId; }
	public List<OrderLine> getItems() { return items; }
	public BigDecimal getSubtotal() { return subtotal; }
	public BigDecimal getDiscount() { return discount; }
	public BigDecimal getTotal() { return total; }
	public String getCouponCode() { return couponCode; }
	public Payment getPayment() { return payment; }
	public Instant getCreatedAt() { return createdAt; }
	public Status getStatus() { return status; }
	public Shipment getShipment() { return shipment; }

	public void setStatus(Status status) {
		if (status == null) throw new IllegalArgumentException("Order status is required");
		this.status = status;
	}

	public void setShipment(Shipment shipment) { this.shipment = shipment; }

	public static final class OrderLine {
		private final long productId;
		private final String productName;
		private final BigDecimal unitPrice;
		private final int quantity;

		public OrderLine(long productId, String productName, BigDecimal unitPrice, int quantity) {
			this.productId = productId;
			this.productName = productName;
			this.unitPrice = unitPrice;
			this.quantity = quantity;
		}

		public long getProductId() { return productId; }
		public String getProductName() { return productName; }
		public BigDecimal getUnitPrice() { return unitPrice; }
		public int getQuantity() { return quantity; }
		public BigDecimal getLineTotal() { return unitPrice.multiply(BigDecimal.valueOf(quantity)); }
	}
}