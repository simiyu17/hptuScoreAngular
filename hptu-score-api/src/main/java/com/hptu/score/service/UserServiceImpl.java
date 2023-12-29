package com.hptu.score.service;

import com.hptu.score.dto.UserDto;
import com.hptu.score.entity.User;
import com.hptu.score.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Objects;

@ApplicationScoped
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public User createUser(UserDto user) {
        return this.userRepository.save(User.createUser(user));
    }

    @Override
    public User findUserById(Long userId) {
        return this.userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("No user found with Id"));
    }

    @Override
    public User findUserByUsername(String userName) {
        return this.userRepository.findByUsername(userName).orElse(null);
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
            adminUser = User.createUser(new UserDto(null, "Admin", "User", "admin@admin.com", "Admin", "5464336455", "Admin"));
            adminUser.setForceChangePass(true);
            this.userRepository.save(adminUser);
        }

    }

}
