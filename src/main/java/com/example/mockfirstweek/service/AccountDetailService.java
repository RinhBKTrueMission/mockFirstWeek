package com.example.mockfirstweek.service;

import com.example.mockfirstweek.model.Account;
import com.example.mockfirstweek.reponsitory.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AccountDetailService implements UserDetailsService {
    @Autowired
    AccountRepository accountRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account=accountRepository.findByUsername(username).
                orElseThrow(()->new UsernameNotFoundException("Account not found with username: "+username));
        return AccountService.build(account);
    }
}
