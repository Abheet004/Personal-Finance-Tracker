package com.abheet.finance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abheet.finance.entity.User;

public interface UserRepo extends JpaRepository<User, Integer>{

	User findByEmail(String email);
	
	
}
