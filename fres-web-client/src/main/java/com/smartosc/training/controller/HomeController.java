package com.smartosc.training.controller;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.smartosc.training.dto.UserDTO;
import com.smartosc.training.service.impl.UserServiceImpl;
import com.smartosc.training.utils.SecurityUtil;

@Controller
public class HomeController {
	
	
	@Autowired
	public UserServiceImpl userServiceImpl;
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	@RequestMapping(value="/login", method = RequestMethod.GET)
	public String login( Model model) {
		return "login";
	}
	
	
	@RequestMapping(value = {"/home","/"}, method = RequestMethod.GET)
    public String loginSuccessfulPage(Model model) {
        return "./products/index";
    }
	
	
	@RequestMapping(value= "/user/{name}",method = RequestMethod.GET)
	public String updateUser(Model modelMap, @PathVariable("name") String name,@ModelAttribute("user")UserDTO user) {
		user = userServiceImpl.findByName(name,SecurityUtil.getJWTToken());
		modelMap.addAttribute("user", user);
		return "users/update";
	}
	
	@RequestMapping(value= "/user/{name}",method = RequestMethod.POST)
	public String updateUserInfo(Model modelMap, @PathVariable("name") String name,@ModelAttribute("user")UserDTO user) {
		userServiceImpl.updateUserInfo(user, new Long(user.getId()));
		return "redirect:/home";
	}
	
	@RequestMapping(value = "/register",method = RequestMethod.POST)
	public String registerUser(Model model,@ModelAttribute("user") UserDTO user) {
		userServiceImpl.registerUser(user);
		return "login";
	}
	@RequestMapping(value = "/register",method = RequestMethod.GET)
	public String registerUser(Model model) {
		return "register";
	}
	
	@RequestMapping(value = "/403", method = RequestMethod.GET)
    public String accessDenied(Model model, Principal principal) {
 
        if (principal != null) {
            
 
            model.addAttribute("userInfo", SecurityUtil.getPrincipal());
 
            String message = "Hi " + SecurityUtil.getPrincipal().getUsername()//
                    + "<br> You do not have permission to access this page!";
            model.addAttribute("message", message);
        }
        return "403Page";
    }
}
