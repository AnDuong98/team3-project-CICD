package com.smartosc.training.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.smartosc.training.service.impl.AppUserDetailsService;

@Configuration
public class AuthenticationConfiguration extends GlobalAuthenticationConfigurerAdapter {
	
    @Bean
    public UserDetailsService getUserDetailsService() {
        return new AppUserDetailsService();
    }
    
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
	@Override
	public void init(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(getUserDetailsService())
        .passwordEncoder(getPasswordEncoder());
		auth.eraseCredentials(false);
	}
}
