package com.ecommerce.repository;

import com.ecommerce.models.User;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class UserRepository {
	private final Map<Long, User> users = new LinkedHashMap<>();

	public User save(User user) { users.put(user.getId(), user); return user; }
	public Optional<User> findById(long id) { return Optional.ofNullable(users.get(id)); }
	public Optional<User> findByEmail(String email) {
		return users.values().stream().filter(user -> user.getEmail().equalsIgnoreCase(email)).findFirst();
	}
	public Collection<User> findAll() { return java.util.List.copyOf(users.values()); }
}