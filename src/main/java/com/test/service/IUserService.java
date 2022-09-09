package com.test.service;

import java.util.Optional;

import com.test.entity.User;

public interface IUserService {
	
	Integer save(User user);
	Optional<User> findByUsername(String username);

}
