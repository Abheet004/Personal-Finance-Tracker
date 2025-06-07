package com.abheet.finance.service;



import com.abheet.finance.entity.User;


public interface UserService {

	public boolean regUser(User user);
	public User loginUser(String email, String password);
	public User findByEmail(String email);
}
