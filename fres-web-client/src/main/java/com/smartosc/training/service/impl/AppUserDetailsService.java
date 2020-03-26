package com.smartosc.training.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.smartosc.training.config.ServicesConfig;
import com.smartosc.training.dto.RoleDTO;
import com.smartosc.training.dto.UserDTO;
import com.smartosc.training.request.LoginRequest;
import com.smartosc.training.security.AppUserDetails;
@Service
public class AppUserDetailsService implements UserDetailsService {

	protected final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private UserServiceImpl userService;
	@Autowired
	private HttpServletRequest httpRequest;
	@Autowired
	private RoleServiceImpl roleService;
	@Autowired
	private RestTemplateServiceImpl restTemplateService;
	@Autowired
	private ServicesConfig servicesConfig;
	@Autowired
	private PasswordEncoder encoder;
	
	@Override
	public UserDetails loadUserByUsername(String username){
		String password = httpRequest.getParameter("password");
		String url = servicesConfig.getHotsName() + servicesConfig.getToken();
		System.out.println(url);
		logger.info("loadUserByUsername username= {}", username);
		logger.info("loadUserByUsername password= {}", password);

		LoginRequest loginRequest = new LoginRequest(username, password);
		List<GrantedAuthority> authorities = new ArrayList<>();
		
		String token = restTemplateService.getToken(url, HttpMethod.POST, null, loginRequest);
		List<RoleDTO> roles = roleService.findByUserName(username,token);
		
		for(RoleDTO item : roles){
			authorities.add(new SimpleGrantedAuthority(item.getName()));
			System.out.println("role"+item.getName());
		}
		
		UserDTO user =  userService.findByName(username,token);
		AppUserDetails appUserDetails = new AppUserDetails(username, encoder.encode(password), true, true, true, true, authorities);
					
		appUserDetails.setJwtToken(token);
		appUserDetails.setUser(user);
		
		logger.info("token = {}", token);
		return appUserDetails;
	}

	public AppUserDetailsService() {
	}
	
}