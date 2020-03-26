package com.smartosc.training.security;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityUtils {
	private SecurityUtils() {

	}

	public static String getJWTToken(HttpServletRequest request) {
		if (request != null && SecurityContextHolder.getContext() != null
				&& SecurityContextHolder.getContext().getAuthentication() != null
				&& SecurityContextHolder.getContext().getAuthentication().getCredentials() != null) {

			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (principal instanceof UserDetails) {

				AppUserDetails userDetail = (AppUserDetails) principal;
				return userDetail.getJwtToken();
			}
		}

		return StringUtils.EMPTY;
	}
}
