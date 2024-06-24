package com.hptu.authentication.api;

import com.hptu.authentication.dto.UserDto;
import com.hptu.authentication.dto.UserPassChangeDto;
import com.hptu.authentication.service.UserService;
import com.hptu.shared.dto.ApiResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;

    @PutMapping("change-password")
    public ResponseEntity<ApiResponseDto> changePassword(@Valid @RequestBody UserPassChangeDto changePasswordDto){
        this.userService.updateUserPassword(changePasswordDto);
        return new ResponseEntity<>(new ApiResponseDto(true, "Password successfully updated !!"), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAvailableUsers(){
        return new ResponseEntity<>(this.userService.getAllUsers(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ApiResponseDto> createUser(@Valid @RequestBody UserDto newUser){
        this.userService.createUser(newUser);
        return new ResponseEntity<>(new ApiResponseDto(true, "User created !!"), HttpStatus.CREATED);
    }

    @PutMapping("{userId}")
    public ResponseEntity<ApiResponseDto> createUser(@PathVariable("userId") Long userId, @Valid @RequestBody UserDto newUser){
        this.userService.updateUser(userId, newUser);
        return new ResponseEntity<>(new ApiResponseDto(true, "User updated !!"), HttpStatus.OK);
    }

    @GetMapping("{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable("userId") Long userId){
        return new ResponseEntity<>(this.userService.findUserById(userId), HttpStatus.OK);
    }
}
