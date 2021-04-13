package com.example.userservice.user;


import com.example.userservice.user.dto.UserDto;

public interface UserService {
    UserDto createUser(UserDto userDto);

    UserDto getUserByUserId(String userId);
    Iterable<User> getUserByAll();
}
