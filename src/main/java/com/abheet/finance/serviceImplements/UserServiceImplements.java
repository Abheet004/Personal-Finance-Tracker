package com.abheet.finance.serviceImplements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abheet.finance.entity.User;
import com.abheet.finance.repository.UserRepo;
import com.abheet.finance.service.UserService;

@Service
public class UserServiceImplements implements UserService {

	@Autowired
	private UserRepo repo;
	
	@Override
	public boolean regUser(User user) {
		// TODO Auto-generated method stub
		try {
			User existingUser = repo.findByEmail(user.getEmail());
			if (existingUser != null) {
				// Email already exists
				return false;
			}
			repo.save(user);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public User findByEmail(String email) {
	    return repo.findByEmail(email);
	}

	
	@Override
	public User loginUser(String email, String password) {
		// TODO Auto-generated method stub
		User validUser=repo.findByEmail(email);
		if (validUser != null && validUser.getPassword().equals(password)) {
			return validUser;
		}
		return null;
	}

}
