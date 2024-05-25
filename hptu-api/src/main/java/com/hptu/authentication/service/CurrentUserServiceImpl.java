package com.hptu.authentication.service;

import com.hptu.authentication.domain.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CurrentUserServiceImpl implements UserDetailsService {

    private final AppUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        final var appUser = userRepository.findByUsername(username)
                .orElse(null);
        if(Objects.isNull(appUser)){
            return null;
        }
        final var grantedAuthorities = new HashSet<GrantedAuthority>(Collections.singleton(new SimpleGrantedAuthority(appUser.getRole())));
        return new org.springframework.security.core.userdetails.User(appUser.getUsername(), appUser.getPassword(), grantedAuthorities);
    }


}
