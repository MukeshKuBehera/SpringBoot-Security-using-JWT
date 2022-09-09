package com.test.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.test.jwtutil.JwtUtil;

@Component
public class SecurityFilter extends OncePerRequestFilter{
	
	@Autowired
	private JwtUtil util;
	
	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO read token from authentication head
		String token=request.getHeader("Authorization");
		
		if(token!=null) {
			
			String username=util.getUsername(token);
			
			//user should not be empty and context-auth must be empty
			if(username!=null && SecurityContextHolder.getContext()== null) {
				
				UserDetails user=userDetailsService.loadUserByUsername(username);
				
				//validate token
				boolean isValid=util.validDateToken(token, username);
				
				if(isValid){
					UsernamePasswordAuthenticationToken authToken
					= new UsernamePasswordAuthenticationToken(username, user.getPassword(),user.getAuthorities());
					
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					
					//final object stored in SecurityContext with user details(user-name,password)
					
					SecurityContextHolder.getContext().setAuthentication(authToken);
					
				}//end-of isValid
				
			}//end of user should not be empty and context-auth must be empty
		}//end of token not null
		
	}

}
