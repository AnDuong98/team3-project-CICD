package com.smartosc.training.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import com.smartosc.training.dto.APIResponse;

public interface RestTemplateService {
	public <T> T getSomething(String url, HttpMethod method, HttpHeaders headers, Object body,
			ParameterizedTypeReference<APIResponse<T>> reponseType);

	public String getToken(String url, HttpMethod method, HttpHeaders headers, Object body);

	public <T> APIResponse<T> exchangePaging(String url, HttpMethod method, HttpHeaders headers, Object body);

}