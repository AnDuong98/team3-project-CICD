package com.smartosc.training.security.utils;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWTUtils implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2266779871542827505L;
	
	public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;
	
//	@Value("${jwt.secret}")
	@Value("mySecret")
	private String secret;
	
	//retrieve username from jwt token
	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}
	
	public Date getIssuedAtDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getIssuedAt);
    }
	
	//retrieve expiration from jwt token
	public Date getExpirationFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration); 
	}
	
	//service: retrieve any information from token
	public Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}
	
	// get Claim from token
	public <T> T getClaimFromToken(String token, Function<Claims, T> claimResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimResolver.apply(claims);
	}
	
	//Check if token has expired
	public boolean isExpiredToken(String token) {
		final Date expiration = getExpirationFromToken(token);
		return expiration.before(new Date());
	}
	
	//generate token for user
	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		return doGenerateToken(claims, userDetails.getUsername());
	}
	
	/* 
	 * 1 - define claim for token like expiration, issuer, subject, id...
	 * 
	 * 2 - Sign the JWT using HS512 algorithm that requires a secret key
	 * 
	 * */
	private String doGenerateToken(Map<String, Object> claims, String subject) {
		
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(subject)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
				.signWith(SignatureAlgorithm.HS512, secret) // sign JWT to algorithm
				.compact();
	}
	//validate token
	public boolean validateToken(String token, UserDetails userDetails) {
		final String userName = getUsernameFromToken(token);
		return (userName.equals(userDetails.getUsername()) && !isExpiredToken(token));
	}

}
