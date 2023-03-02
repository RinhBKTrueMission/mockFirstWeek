package com.example.mockfirstweek.service;

import com.example.mockfirstweek.model.User;
import com.example.mockfirstweek.reponsitory.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AccountDetailService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User account= userRepository.findByUsername(username).
                orElseThrow(()->new UsernameNotFoundException("Account not found with username: "+username));
        return AccountService.build(account);
    }
}
