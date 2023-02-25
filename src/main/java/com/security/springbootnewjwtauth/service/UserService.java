package com.security.springbootnewjwtauth.service;

import com.security.springbootnewjwtauth.domain.User;
import com.security.springbootnewjwtauth.enums.ERole;
import com.security.springbootnewjwtauth.model.request.AuthRequest;
import com.security.springbootnewjwtauth.model.request.UserRequestDto;
import com.security.springbootnewjwtauth.model.response.AuthResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    User createUser(UserRequestDto userRequestDto);
    User updateUser(long id,UserRequestDto userRequestDto);
    List<User> getUsers();
    User addRole(long userId, ERole eRole);
    User removeRole(long userId,ERole eRole);
    void deleteUser(long id);

    AuthResponse login(AuthRequest authRequest);



}
