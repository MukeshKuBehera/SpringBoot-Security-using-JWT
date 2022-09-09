package com.test.jwtutil;

import java.util.Base64;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {
	
	@Value("${app.secret}")
	private String secret;
	
	//6-validate user-name in token and database, expire date 
	public boolean validDateToken(String token,String username) {
		
		String tokenUserName=getUsername(token);
		
		return (username.equals(tokenUserName)&& !isTokentExpire(token));
	}
	
	//5-validate expire date 
	
	public boolean isTokentExpire(String token) {
		
		Date expdate=expireDate(token);
		
		return expdate.before(new Date(System.currentTimeMillis()));
	}
	
	
	//4-read user details/subject
	public String getUsername(String token) {
		
		return getClaims(token).getSubject();
	}
	
	//3-read expire date 
	public Date expireDate(String token) {
		
		return getClaims(token).getExpiration();
	}
	
	//step-2: claims token
	public Claims getClaims(String token) {
		
		return Jwts.parser()
				.setSigningKey(Base64.getEncoder().encode(secret.getBytes()))
				.parseClaimsJws(token)
				.getBody();
	}
	
	
	
	//step-1:generate token
	public String generateToken(String subject) {
		
		return Jwts.builder()
				.setSubject(subject)
				.setIssuer("HDFC BANK")
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis()+TimeUnit.MINUTES.toMillis(10)))
				.signWith(SignatureAlgorithm.HS256,secret.getBytes())
				.compact();
		
		
		
	}
	

}
