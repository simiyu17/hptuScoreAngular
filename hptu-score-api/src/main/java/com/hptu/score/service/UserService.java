package com.hptu.score.service;

import com.hptu.score.dto.UserDto;
import com.hptu.score.entity.User;

import java.util.List;

public interface UserService {

    User createUser(UserDto user);

    User findUserById(Long userId);

    User findUserByUsername(String userName);

    List<User> getAllUsers();

    void createDefaultUser();
}
