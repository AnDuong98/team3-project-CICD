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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.smartosc.training.config.ServicesConfig;
import com.smartosc.training.dto.RoleDTO;
import com.smartosc.training.request.LoginRequest;
import com.smartosc.training.security.AppUserDetails;
import com.smartosc.training.utils.EncrytedPasswordUtil;
@Service
public class AppUserDetailsService implements UserDetailsService {

	protected final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private HttpServletRequest httpRequest;
	@Autowired
	private RoleServiceImpl roleService;
	@Autowired
	private RestTemplateServiceImpl restTemplateService;
	@Autowired
	private ServicesConfig servicesConfig;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
		try {
			String password = httpRequest.getParameter("password");
			String url = servicesConfig.getHotsName() + servicesConfig.getToken();
			logger.info("loadUserByUsername username= {}", username);
			logger.info("loadUserByUsername password= {}", password);
			
	
			LoginRequest loginRequest = new LoginRequest(username, password);
			List<GrantedAuthority> authorities = new ArrayList<>();
			
			String token = restTemplateService.getToken(url, HttpMethod.POST, null, loginRequest);
			logger.info("userInfo = {}", token);
			List<RoleDTO> roles = roleService.findByUserName(username, token);
			for(RoleDTO item : roles){
				authorities.add(new SimpleGrantedAuthority(item.getName()));
			}
			
			AppUserDetails appUserDetails = new AppUserDetails(username, EncrytedPasswordUtil.encrytePassword(password), true, true, true, true, authorities);
			appUserDetails.setJwtToken(token);
			logger.info("token= {}", token);
			return appUserDetails;	
		}catch(Exception e) {
			throw new UsernameNotFoundException("Username not found!", e);
		}
	}
}