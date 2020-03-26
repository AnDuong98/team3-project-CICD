package com.smartosc.training.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.smartosc.training.dto.APIResponse;
import com.smartosc.training.exception.RestTempalteException;
import com.smartosc.training.service.RestTemplateService;

@Service
public class RestTemplateServiceImpl implements RestTemplateService {
	private static final Logger LOOGER = LoggerFactory.getLogger(RestTemplateServiceImpl.class);
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Override
	public <T> T getSomething(String url, HttpMethod method, HttpHeaders headers, Object body, ParameterizedTypeReference<APIResponse<T>> reponseType) {
		try {
			HttpEntity<Object> entity = new HttpEntity<>(body, headers);
			ResponseEntity<APIResponse<T>> res = restTemplate.exchange(url, method, entity, reponseType);
			if (res.getStatusCodeValue() >= HttpStatus.OK.value() && res.getStatusCodeValue() < HttpStatus.MULTIPLE_CHOICES.value()) {
				return res.getBody().getData();
			}
			LOOGER.error(res.getBody().getMessage());
			throw new RestTempalteException(res.getBody().getMessage());
		} catch (Exception e) {
			throw new RestTempalteException(e.getMessage(), e);
		}
	}

	public <T> APIResponse<T> exchangePaging(String url, HttpMethod method, HttpHeaders headers, Object body) {
		try {
			HttpEntity<Object> entity = new HttpEntity<>(body, headers);
			ParameterizedTypeReference<APIResponse<T>> reponseType = new ParameterizedTypeReference<APIResponse<T>>() {	};
			ResponseEntity<APIResponse<T>> res = restTemplate.exchange(url, method, entity, reponseType);
			if (res.getStatusCodeValue() >= HttpStatus.OK.value() && res.getStatusCodeValue() < HttpStatus.MULTIPLE_CHOICES.value()) {
				return res.getBody();
			}
			LOOGER.error(res.getBody().getMessage());
			throw new RestTempalteException(res.getBody().getMessage());
		} catch (Exception e) {
			throw new RestTempalteException(e.getMessage(), e);
		}
	}


	@Override
	public String getToken(String url, HttpMethod method, HttpHeaders headers, Object body) {
		try {
			HttpEntity<Object> entity = new HttpEntity<>(body, headers);
			ResponseEntity<String> res = restTemplate.exchange(url, method, entity, String.class);
			if (res.getStatusCodeValue() >= HttpStatus.OK.value() && res.getStatusCodeValue() < HttpStatus.MULTIPLE_CHOICES.value()) {
				return res.getBody();
			}
			LOOGER.error(res.getBody());
			throw new RestTempalteException(res.getBody());
		} catch (Exception e) {
			throw new RestTempalteException(e.getMessage(), e);
		}
	}

}
