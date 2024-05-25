package com.hptu.authentication.service;

import com.hptu.authentication.domain.AppUser;
import com.hptu.authentication.dto.AuthRequestDto;
import com.hptu.authentication.dto.AuthResponseDto;
import com.hptu.authentication.dto.UserDto;
import com.hptu.authentication.dto.UserPassChangeDto;

import java.util.List;

public interface UserService {

    void createUser(UserDto user);

    void updateUser(Long userId, UserDto user);

    void updateUserPassword(UserPassChangeDto userPassChangeDto);

    UserDto findUserById(Long userId);

    AppUser findUserByUsername(String userName);

    AuthResponseDto authenticateUser(AuthRequestDto authRequestDto);

    List<UserDto> getAllUsers();

    AppUser currentUser();
}
