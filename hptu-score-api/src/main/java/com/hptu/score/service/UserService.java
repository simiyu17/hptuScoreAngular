package com.hptu.score.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hptu.score.dto.AuthRequestDto;
import com.hptu.score.dto.AuthResponseDto;
import com.hptu.score.dto.UserDto;
import com.hptu.score.dto.UserPassChangeDto;
import com.hptu.score.entity.User;

import java.util.List;

public interface UserService {

    User createUser(UserDto user);

    User updateUser(Long userId, UserDto user);

    User updateUserPassword(User user, UserPassChangeDto userPassChangeDto);

    User findUserById(Long userId);

    User findUserByUsername(String userName);

    AuthResponseDto authenticateUser(AuthRequestDto authRequestDto);

    List<User> getAllUsers();

    void createDefaultUser();
}
