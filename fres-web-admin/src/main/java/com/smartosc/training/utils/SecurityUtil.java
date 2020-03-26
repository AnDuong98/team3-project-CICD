package com.smartosc.training.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.smartosc.training.security.AppUserDetails;

public class SecurityUtil {
	public static AppUserDetails getPrincipal() {
		AppUserDetails myUser = (AppUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return myUser;
	}
	
	public static void logOut() {
		SecurityContextHolder.clearContext();
	}
	@SuppressWarnings("unchecked")
	public static List<String> getAuthorities() {
		List<String> results = new ArrayList<>();
		List<GrantedAuthority> authorities = (List<GrantedAuthority>) (SecurityContextHolder.getContext()
				.getAuthentication().getAuthorities());
		for (GrantedAuthority authority : authorities) {
			results.add(authority.getAuthority());
		}
		return results;
	}

	public static String getJWTToken() {
		if (SecurityContextHolder.getContext() != null
				&& SecurityContextHolder.getContext().getAuthentication() != null
				&& SecurityContextHolder.getContext().getAuthentication().getCredentials() != null) {

			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (principal instanceof AppUserDetails) {

				AppUserDetails userDetail = (AppUserDetails) principal;
				return userDetail.getJwtToken();
			}
		}

		return StringUtils.EMPTY;
	}

}
