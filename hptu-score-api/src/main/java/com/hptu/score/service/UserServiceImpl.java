package com.hptu.score.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hptu.score.dto.AuthRequestDto;
import com.hptu.score.dto.AuthResponseDto;
import com.hptu.score.dto.UserDto;
import com.hptu.score.dto.UserPassChangeDto;
import com.hptu.score.entity.User;
import com.hptu.score.exception.UserNotAuthenticatedException;
import com.hptu.score.exception.UserNotFoundException;
import com.hptu.score.repository.UserRepository;
import com.hptu.score.util.AuthTokenUtil;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;

@ApplicationScoped
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final AuthTokenUtil authTokenUtil;

    public UserServiceImpl(UserRepository userRepository, AuthTokenUtil authTokenUtil) {
        this.userRepository = userRepository;
        this.authTokenUtil = authTokenUtil;
    }

    @Transactional
    @Override
    public User createUser(UserDto user) {
        try {
            return this.userRepository.save(User.createUser(user));
        }catch (Exception e){
            throw new IllegalArgumentException(e);
        }

    }

    @Transactional
    @Override
    public User updateUser(Long userId, UserDto user) {
        var currentUser = this.findUserById(userId);
        try {
            currentUser.updateUser(user);
            return this.userRepository.save(currentUser);
        }catch (Exception e){
            throw new IllegalArgumentException(e);
        }

    }


    @Override
    public User updateUserPassword(User user, UserPassChangeDto userPassChangeDto) {
        if(Objects.isNull(user) || !BcryptUtil.matches(userPassChangeDto.password(), user.getPassword())){
            throw new UserNotAuthenticatedException("Invalid current password!!");
        }
        if (!StringUtils.equals(userPassChangeDto.newPass(), userPassChangeDto.passConfirm())){
            throw new UserNotAuthenticatedException("New password must match with confirmation password!!");
        }
        user.setPassword(userPassChangeDto.newPass());
        return this.userRepository.save(user);
    }

    @Override
    public User findUserById(Long userId) {
        return this.userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("No user found with Id"));
    }

    @Override
    public User findUserByUsername(String userName) {
        return this.userRepository.findByUsername(userName).orElse(null);
    }

    @Override
    public AuthResponseDto authenticateUser(AuthRequestDto authRequestDto) {
        var user = findUserByUsername(authRequestDto.username());
        if(Objects.isNull(user) || !BcryptUtil.matches(authRequestDto.password(), user.getPassword())){
            throw new UserNotAuthenticatedException("Invalid username and/or password!!");
        }
        return new AuthResponseDto(true, "User Authenticated!!", authTokenUtil.generateJwt(user));
    }

    @Override
    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    @Transactional
    @Override
    public void createDefaultUser() {
        User adminUser = this.findUserByUsername("admin@admin.com");
        if(Objects.isNull(adminUser)){
            adminUser = User.createUser(new UserDto(null, "Admin", "User", "admin@admin.com", "Administrator", "5464336455", true, true));
            adminUser.setForceChangePass(true);
            this.userRepository.save(adminUser);
        }

    }

}
