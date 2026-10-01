package com.example.helloworld.service.UsersService;

import com.example.helloworld.dto.Users.UserRequest;
import com.example.helloworld.entity.UsersEntity.User;

import java.util.List;

public interface UserService {

    User createUser(UserRequest request);

    List<User> getAllUsers();

    User getUserById(Long id);
}