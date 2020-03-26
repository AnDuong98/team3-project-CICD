package com.smartosc.training.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.smartosc.training.dto.UserDTO;
import com.smartosc.training.service.impl.UserServiceImpl;

@RestController
public class UserAPI {
	@Autowired
	private UserServiceImpl userService;

	@GetMapping(value = "/admin/user")
	public List<UserDTO> showUser() {
		return userService.showUser();
	}
	
	@GetMapping(value = "/admin/user/{id}")
	public UserDTO findById(@PathVariable("id") Long id) {
		return userService.findById(id);
	}
	
	@GetMapping(value = "/admin/user/count")
	public int countUser() {
		return userService.countUser();
	}
	
//	@PostMapping(value = "/admin/user")
//	public UserDTO createUser(@ModelAttribute("model") UserDTO User) {
//		return userService.createUser(User);
//	}
	
//	@PutMapping(value = "/admin/user")
//	public UserDTO updateUser(@ModelAttribute("model") UserDTO User) {
//		return userService.updateUser(User);
//	}
	
	@PostMapping(value = "/admin/user")
	public ModelAndView createUser(@ModelAttribute("model") UserDTO User) {
		userService.createUser(User);
		return new ModelAndView("redirect:/user");
	}
	
	@PutMapping(value = "/admin/user")
	public String updateUser(@ModelAttribute("model") UserDTO User) {
		userService.updateUser(User);
		return "redirect:/user";
	}
	
	@DeleteMapping(value = "/admin/user")
	public void deleteUser(@RequestBody Long[] ids, HttpServletRequest request) {
		userService.deleteUser(ids);
	}


}
