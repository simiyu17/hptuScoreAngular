package com.hptu.authentication.service;


import com.hptu.authentication.domain.AppUser;
import com.hptu.authentication.domain.AppUserRepository;
import com.hptu.authentication.dto.AuthRequestDto;
import com.hptu.authentication.dto.AuthResponseDto;
import com.hptu.authentication.dto.UserDto;
import com.hptu.authentication.dto.UserPassChangeDto;
import com.hptu.authentication.exception.UserNotAuthenticatedException;
import com.hptu.authentication.exception.UserNotFoundException;
import com.hptu.authentication.filter.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final AppUserRepository userRepository;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    @Override
    public void createUser(UserDto user) {
        try {
            this.userRepository.save(AppUser.createUser(user, passwordEncoder));
        }catch (Exception e){
            throw new IllegalArgumentException(e);
        }

    }

    @Transactional
    @Override
    public void updateUser(Long userId, UserDto user) {
        var currentUser = this.userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("No user found with Id"));
        try {
            currentUser.updateUser(user);
            this.userRepository.save(currentUser);
        }catch (Exception e){
            throw new IllegalArgumentException(e);
        }

    }

    @Override
    public void updateUserPassword(UserPassChangeDto userPassChangeDto) {
        if (!StringUtils.equals(userPassChangeDto.newPass(), userPassChangeDto.passConfirm())){
            throw new UserNotAuthenticatedException("New password must match with confirmation password!!");
        }
        final var currentUser = currentUser();
        if (this.passwordEncoder.matches(userPassChangeDto.password(), currentUser.getPassword())){
            currentUser.setPassword(this.passwordEncoder.encode(userPassChangeDto.newPass()));
            currentUser.setForceChangePass(false);
        }else {
            throw new UserNotFoundException("Invalid current password!!");
        }
        this.userRepository.save(currentUser);
    }

    @Override
    public UserDto findUserById(Long userId) {
        return this.userRepository.findById(userId)
                .map(u -> new UserDto(u.getId(), u.getFirstName(), u.getLastName(), u.getUsername(), u.getDesignation(), u.getCellPhone(), u.isActive(), u.isAdmin()))
                .orElseThrow(() -> new UserNotFoundException("No user found with Id"));
    }

    @Override
    public AppUser findUserByUsername(String userName) {
        return this.userRepository.findByUsername(userName).orElse(null);
    }


    @Override
    public AuthResponseDto authenticateUser(AuthRequestDto authRequestDto) {
        createDefaultUser();
        final var userDetails = userDetailsService.loadUserByUsername(authRequestDto.username());
        if (Objects.isNull(userDetails)) {
            throw  new UserNotFoundException("Bad User Credentials !!");
        }else {
            final var user = findUserByUsername(userDetails.getUsername());
            this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDetails, authRequestDto.password(), userDetails.getAuthorities()));
            return new AuthResponseDto(true, "User Authenticated!!", JwtTokenProvider.createToken(user));
        }
    }

    @Override
    public List<UserDto> getAllUsers() {
        return this.userRepository.findAll().stream().map(u -> new UserDto(u.getId(), u.getFirstName(), u.getLastName(), u.getUsername(), u.getDesignation(), u.getCellPhone(), u.isActive(), u.isAdmin())).toList();
    }

    @Override
    public AppUser currentUser() {
        AppUser u = null;
        final var context = SecurityContextHolder.getContext();
        final var authentication = context.getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            u = this.findUserByUsername(currentUserName);
        }
        return u;
    }


    private void createDefaultUser() {
        AppUser adminUser = this.findUserByUsername("admin@admin.com");
        if(Objects.isNull(adminUser)){
            adminUser = AppUser.createUser(new UserDto(null, "Admin", "User", "admin@admin.com", "Administrator", "5464336455", true, true), passwordEncoder);
            adminUser.setForceChangePass(true);
            this.userRepository.save(adminUser);
        }

    }

}
