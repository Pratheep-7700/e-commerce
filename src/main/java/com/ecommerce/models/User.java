package com.ecommerce.models;

import java.util.Objects;

public class User {
	private final long id;
	private String name;
	private String email;

	public User(long id, String name, String email) {
		if (id <= 0) {
			throw new IllegalArgumentException("User id must be positive");
		}
		this.id = id;
		setName(name);
		setEmail(email);
	}

	public long getId() { return id; }
	public String getName() { return name; }
	public String getEmail() { return email; }

	public void setName(String name) {
		if (name == null || name.isBlank()) {
			throw new IllegalArgumentException("Name is required");
		}
		this.name = name.trim();
	}

	public void setEmail(String email) {
		if (email == null || !email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
			throw new IllegalArgumentException("A valid email is required");
		}
		this.email = email.trim().toLowerCase();
	}

	@Override
	public boolean equals(Object other) {
		return other instanceof User user && id == user.id;
	}

	@Override
	public int hashCode() { return Objects.hash(id); }
}