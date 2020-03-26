package com.smartosc.training.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import com.smartosc.training.entity.AddressEntity;
import com.smartosc.training.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.smartosc.training.dto.APIResponse;
import com.smartosc.training.dto.UserDTO;
import com.smartosc.training.entity.UserEntity;
import com.smartosc.training.exception.NotFoundException;
import com.smartosc.training.service.IUserService;
import com.smartosc.training.service.impl.ControlUserService;

import static com.smartosc.training.exception.NotFoundException.supplier;

@RestController
public class UserController {
	@Autowired
	private IUserService userService;
	@Autowired
	private ControlUserService userSer;
	
	private UserDTO user;
	
	@GetMapping(value = "/user")
	public ResponseEntity<?> showUser() throws NotFoundException {
		List<UserDTO> userList = new ArrayList<>();
		try {
			userList = userService.findAll();
		} catch (Exception e) {
			// TODO: handle exception
			throw new NotFoundException(supplier(UserEntity.class.getSimpleName()).get().getMessage());
		}
		return new ResponseEntity<>(new APIResponse<List<?>>(HttpStatus.OK.value(),"done" ,userList), HttpStatus.OK);
	}
	
	@GetMapping(value = "/user/{id}")
	public ResponseEntity<?> showUerById(@PathVariable("id") Long id) throws IllegalArgumentException, NotFoundException {
		try {
			user = userService.findById(id);
		} catch (Exception e) {
			throw new NotFoundException(supplier(UserEntity.class.getSimpleName(), id).get().getMessage());
		}
		return new ResponseEntity<>(new APIResponse<>(HttpStatus.OK.value(),"done" ,user), HttpStatus.OK);
	}
	
	@GetMapping(value = "/user/count")
	public ResponseEntity<?> countUser() {
		return new ResponseEntity<>(new APIResponse<>(HttpStatus.OK.value(),"done" ,userService.totalItem()), HttpStatus.OK);
	}
	
	@PostMapping(value = "/user")
	public ResponseEntity<?> createNew(@RequestBody @Valid UserDTO model) throws MethodArgumentNotValidException {
		user = userService.save(model);
		return new ResponseEntity<>(new APIResponse<>(HttpStatus.OK.value(),"done", user), HttpStatus.OK);
	}
	
	@PutMapping(value = "/user")
	public ResponseEntity<?> updateUser(@RequestBody @Valid UserDTO model)
			throws NotFoundException, MethodArgumentNotValidException {
		
		if (userService.findById(model.getId()) == null) {
			throw new NotFoundException(supplier(UserEntity.class.getSimpleName(), model.getId()).get().getMessage());
		}
		userService.save(model);
		return new ResponseEntity<>(new APIResponse<>(HttpStatus.OK.value(),"done", user), HttpStatus.OK);
	}
	
	@GetMapping(value = "/user/username/{name}")
	public ResponseEntity<?> showUserByName(@PathVariable("name") String name) 
			throws NotFoundException, IllegalArgumentException{
		if (userService.findByUserame(name) == null) {
			throw new NotFoundException(supplier(UserEntity.class.getSimpleName()).get().getMessage());
		}
		return new ResponseEntity<>(new APIResponse<>(HttpStatus.OK.value(),"done" ,userService.findByUserame(name)), HttpStatus.OK);
	}
	
	@GetMapping(value = "/user/update/{id}")
	public ResponseEntity<?> showUserByIdByNam(@PathVariable("id") Long id) 
			throws NotFoundException, IllegalArgumentException{
		user = userSer.getUserById(id);
		if (user.getId() == null) {
			throw new NotFoundException(supplier(UserEntity.class.getSimpleName(), id).get().getMessage());
		}
		return new ResponseEntity<>(new APIResponse<>(HttpStatus.OK.value(),"done" ,user), HttpStatus.OK);
	}
	
	@PostMapping(value = "/user/update/{id}")
	public ResponseEntity<?> updateUerByIdByNam(@PathVariable("id") Long id,@RequestBody @Valid UserDTO userDTO) 
			throws NotFoundException, MethodArgumentNotValidException{
		if (userSer.getUserById(id) == null) {
			throw new NotFoundException(supplier(UserEntity.class.getSimpleName(), id).get().getMessage());
		}
		userSer.updateUser(id, userDTO);
		return new ResponseEntity<>(new APIResponse<>(HttpStatus.OK.value(), "Create success", userDTO),HttpStatus.OK);
	}

	@GetMapping(value = "/report")
	public ResponseEntity empReport()throws NotFoundException,MethodArgumentNotValidException{
		userService.generateReport();
		return new ResponseEntity(new APIResponse<>(HttpStatus.OK.value(),"Export success"),HttpStatus.OK);
	}

	@PostMapping(value = "/addUser")
	public ResponseEntity addAlotUser(@RequestBody List<UserDTO> userDTOS)throws NotFoundException,MethodArgumentNotValidException{
		return new ResponseEntity(new APIResponse<>(HttpStatus.OK.value(),"Import Success",userService.registerAlot(userDTOS)),HttpStatus.OK);
	}

}
