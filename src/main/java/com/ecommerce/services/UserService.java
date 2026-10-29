package com.ecommerce.services;

import com.ecommerce.models.Customer;
import com.ecommerce.models.User;
import com.ecommerce.repository.UserRepository;
import java.util.concurrent.atomic.AtomicLong;

public class UserService {
	private final UserRepository users;
	private final AtomicLong nextId = new AtomicLong(1);

	public UserService(UserRepository users) { this.users = users; }

	public Customer registerCustomer(String name, String email) {
		if (users.findByEmail(email).isPresent()) throw new IllegalArgumentException("Email is already registered");
		long id = nextId.getAndIncrement();
		while (users.findById(id).isPresent()) id = nextId.getAndIncrement();
		Customer customer = new Customer(id, name, email);
		users.save(customer);
		return customer;
	}

	public User findById(long id) {
		return users.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
	}
}