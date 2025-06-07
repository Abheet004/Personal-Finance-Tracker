package com.abheet.finance.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.abheet.finance.entity.User;
import com.abheet.finance.service.UserService;

import jakarta.servlet.http.HttpSession;


@Controller
public class UserController {
	
	@Autowired
	private UserService userService;

	@GetMapping("/regPage")
	public String openRegPage(Model model) {
		model.addAttribute("user",new User());
		return "register";
	}
	
	@PostMapping("/regForm")
	public String submitRegForm(@ModelAttribute("user") User user,Model model) {
		boolean status=userService.regUser(user);
		if(status) {
			model.addAttribute("Success","User registered Successfully");
			return "login";
		}else {
			model.addAttribute("errorMsg", "Email already exists or registration failed");
			return "register";
		}
		
	}
	
	@GetMapping("/loginPage")
	public String openLoginPage(Model model) {
		model.addAttribute("user",new User());
		return "login";
	}
	
	@PostMapping("/loginForm")
	public String submitLoginForm(@ModelAttribute("user") User user , Model model, HttpSession session) {
	    User validUser = userService.loginUser(user.getEmail(), user.getPassword());
	    if(validUser != null) {
	        session.setAttribute("loggedUser", validUser); // ✅ Save user to session
	        return "redirect:/dashboard";
	    } else {
	        model.addAttribute("errorMsg", "Invalid Email or Password");
	        return "login";
	    }
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
	    session.invalidate(); // ✅ Destroys the current session
	    return "redirect:/loginPage"; // ✅ Redirect to login page
	}


	
	
}
