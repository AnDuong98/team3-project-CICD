package com.smartosc.training.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.smartosc.training.utils.SecurityUtil;

@Controller
public class HandleErrorController {
	@GetMapping("/error")
	public String handleError(HttpServletRequest request, Model model) {
		Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		if(status != null) {
			Integer statusCode = Integer.valueOf(status.toString());
//			if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
//				return "500";
//			} else if (statusCode == HttpStatus.FORBIDDEN.value()) {
//				return "403";
//			} else if (statusCode == HttpStatus.NOT_FOUND.value()) {
//				return "404";
//			}else 
			if (statusCode == 999) {
				return "redirect:/home";
			}
		}
		return "redirect:/home";
	}

}
