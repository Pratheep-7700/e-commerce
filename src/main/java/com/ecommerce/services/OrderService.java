package com.ecommerce.services;

import com.ecommerce.models.Cart;
import com.ecommerce.models.Coupon;
import com.ecommerce.models.Order;
import com.ecommerce.models.Payment;
import com.ecommerce.models.Product;
import com.ecommerce.models.Shipment;
import com.ecommerce.repository.CouponRepository;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.strategy.PaymentMethod;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class OrderService {
	private final OrderRepository orders;
	private final ProductRepository products;
	private final CouponRepository coupons;
	private final CartService carts;
	private final PaymentService payments;
	private final AtomicLong nextOrderId = new AtomicLong(1);

	public OrderService(OrderRepository orders, ProductRepository products, CouponRepository coupons,
						CartService carts, PaymentService payments) {
		this.orders = orders;
		this.products = products;
		this.coupons = coupons;
		this.carts = carts;
		this.payments = payments;
	}

	public Order checkout(long customerId, String couponCode, String address, PaymentMethod method) {
		Cart cart = carts.getCart(customerId);
		if (cart.isEmpty()) throw new IllegalStateException("Cart is empty");
		if (address == null || address.isBlank()) throw new IllegalArgumentException("Shipping address is required");
		if (method == null) throw new IllegalArgumentException("Payment method is required");
		for (Cart.CartItem item : cart.getItems()) {
			Product current = products.findById(item.getProduct().getId())
					.orElseThrow(() -> new IllegalStateException("A cart product is no longer available"));
			if (current.getStock() < item.getQuantity()) throw new IllegalStateException("Insufficient stock for " + current.getName());
		}

		BigDecimal subtotal = cart.subtotal();
		BigDecimal discount = BigDecimal.ZERO;
		String normalizedCode = couponCode == null || couponCode.isBlank() ? null : couponCode.trim().toUpperCase();
		if (normalizedCode != null) {
			Coupon coupon = coupons.findByCode(normalizedCode).orElseThrow(() -> new IllegalArgumentException("Coupon not found"));
			discount = coupon.calculateDiscount(subtotal);
		}
		Payment payment = payments.pay(subtotal.subtract(discount), method);
		List<Order.OrderLine> lines = cart.getItems().stream()
				.map(item -> new Order.OrderLine(item.getProduct().getId(), item.getProduct().getName(),
						item.getProduct().getPrice(), item.getQuantity())).toList();
		for (Cart.CartItem item : cart.getItems()) {
			products.findById(item.getProduct().getId()).orElseThrow().removeStock(item.getQuantity());
		}
		Order order = new Order(nextOrderId.getAndIncrement(), customerId, lines, subtotal, discount, normalizedCode, payment);
		order.setShipment(new Shipment("EC-" + order.getId(), address));
		orders.save(order);
		carts.clear(customerId);
		return order;
	}

	public Collection<Order> listOrders() { return orders.findAll(); }
	public Collection<Order> listCustomerOrders(long customerId) { return orders.findByCustomerId(customerId); }
	public Order getOrder(long id) { return orders.findById(id).orElseThrow(() -> new IllegalArgumentException("Order not found")); }

	public void updateOrderStatus(long orderId, Order.Status status) {
		Order order = getOrder(orderId);
		order.setStatus(status);
		if (order.getShipment() != null) {
			if (status == Order.Status.SHIPPED) order.getShipment().setStatus(Shipment.Status.SHIPPED);
			if (status == Order.Status.DELIVERED) order.getShipment().setStatus(Shipment.Status.DELIVERED);
		}
	}
}