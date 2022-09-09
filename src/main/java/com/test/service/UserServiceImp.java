package com.test.service;

import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.test.entity.User;
import com.test.repository.UserRepository;
@Service
public class UserServiceImp implements IUserService,UserDetailsService {
	
	@Autowired
	private UserRepository repo;
	
	@Autowired
	private BCryptPasswordEncoder pwdEncoder;

	//save user
	@Transactional
	public Integer save(User user) {
		
		//Encode password
		user.setPassword(pwdEncoder.encode(user.getPassword()));
		
		
		return repo.save(user).getId();
		
	}

	//get user by username
	@Transactional(readOnly = true)
	public Optional<User> findByUsername(String username) {
		
		return repo.findByUsername(username);
	}

	
	
	//==============================================================
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		
		Optional<User> opt=findByUsername(username);
		
		if(opt.isEmpty()) {
			
			throw new UsernameNotFoundException("user not exist...!");
		}
		//read user from database
		User user=opt.get();
		
		return new org.springframework.security.core.userdetails.User(username, user.getPassword(),user.getRoles()
				.stream().map(role->new SimpleGrantedAuthority(role)).collect(Collectors.toList()));
	}
	
	

}
