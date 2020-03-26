package com.smartosc.training.security.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.smartosc.training.dto.APIResponse;
import com.smartosc.training.dto.UserDTO;
import com.smartosc.training.entity.JwtRequest;
import com.smartosc.training.security.service.JwtUserDetailService;
import com.smartosc.training.security.utils.JWTUtils;

@RestController
@CrossOrigin
public class SecurityController {

	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JWTUtils jwtTokenUtil;

	@Autowired
	private JwtUserDetailService userDetailsService;

	@PostMapping(value = "/authenticate")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody @Valid JwtRequest authenticationRequest) throws Exception {

		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		
		if (!userDetails.isAccountNonLocked()) {
			return new ResponseEntity(new APIResponse(HttpStatus.OK.value(), "this account has been locked"), HttpStatus.OK);
		}
		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

		final String token = jwtTokenUtil.generateToken(userDetails);

		return ResponseEntity.ok(token);
	}

	@PostMapping(value = "/register")
	public ResponseEntity<?> saveUser(@RequestBody UserDTO user) throws Exception {
		return new ResponseEntity(new APIResponse(HttpStatus.OK.value(), "successfully", userDetailsService.save(user)), HttpStatus.OK);
	}

	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new BadCredentialsException("INVALID_CREDENTIALS", e);
		}
	}
}