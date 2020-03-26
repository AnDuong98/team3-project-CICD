package com.smartosc.training.security.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	DataSource dataSource;
	
	@Autowired
	private JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	
	@Autowired
	private UserDetailsService jwtUserDetailService;
	
	@Autowired
	private RequestFilter requestFilter;
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
		auth.userDetailsService(jwtUserDetailService).passwordEncoder(passwordEncoder());
	}
	
	@Bean
    public BCryptPasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder;
    }
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();	
	}
	
	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		
		httpSecurity.cors().and().csrf().disable()
			.authorizeRequests()
//	    		.antMatchers("/authenticate","/register","/products/**","/orders/**","/categories**").permitAll()
//	    		.antMatchers(HttpMethod.POST, "/orders**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
//	    		.antMatchers(HttpMethod.GET, "/orders**").hasAnyAuthority("ROLE_USER")
//	    		.antMatchers(HttpMethod.GET, "/user/**").hasAnyAuthority("ROLE_ADMIN","ROLE_USER")
//	    		.antMatchers(HttpMethod.POST, "/user/**").hasAnyAuthority("ROLE_ADMIN","ROLE_USER")
//	    		.antMatchers(HttpMethod.GET, "/role/**").hasAnyAuthority("ROLE_ADMIN","ROLE_USER")
//	    		.antMatchers(HttpMethod.POST, "/products**").hasAnyAuthority("ROLE_ADMIN")
//	    		.antMatchers(HttpMethod.PUT, "/products**").hasAnyAuthority("ROLE_ADMIN")
	    		.anyRequest().permitAll()
//	    		.authenticated()
	    		.and()
	    	.formLogin()
	    		.loginPage("/login")
	    		.loginProcessingUrl("/login-action").permitAll()
	    		.usernameParameter("username")
	    		.passwordParameter("password")
	    		.failureUrl("/login?error=true")
	    		.defaultSuccessUrl("/index")
	    		.and()
	    	.logout()
	    		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
	    		.logoutSuccessUrl("/login")
	    		.and()
	    	.exceptionHandling()
	    		.authenticationEntryPoint(jwtAuthenticationEntryPoint);
	    	
	    httpSecurity.addFilterBefore(requestFilter,UsernamePasswordAuthenticationFilter.class);
	}
	
	@Override
	  public void configure(WebSecurity web) throws Exception {
		  web
		  .ignoring()
		  .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
	  }
}