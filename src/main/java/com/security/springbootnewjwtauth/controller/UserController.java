package com.security.springbootnewjwtauth.controller;


import com.security.springbootnewjwtauth.domain.User;
import com.security.springbootnewjwtauth.enums.ERole;
import com.security.springbootnewjwtauth.model.request.AuthRequest;
import com.security.springbootnewjwtauth.model.request.UserRequestDto;
import com.security.springbootnewjwtauth.model.response.AuthResponse;
import com.security.springbootnewjwtauth.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/versions/1/auth")
public class UserController {


    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @SecurityRequirement(name = "bearerAuth")
    @PostMapping(path = "/user")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public User createUser(@RequestBody UserRequestDto userRequestDto){
        return userService.createUser(userRequestDto);
    }

    @PutMapping(path = "/user/{id}")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public User updateUser(@PathVariable("id") long id ,@RequestBody UserRequestDto userRequestDto){
        return userService.updateUser(id, userRequestDto);
    }


    @PutMapping(path = "/user/{id}/role/add")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public User addRoleForUser(@PathVariable("id") long id ,@RequestParam ERole eRole){
        return userService.addRole(id,eRole);
    }

    @PutMapping(path = "/user/{id}/role/remove")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public User removeRoleForUser(@PathVariable("id") long id , @RequestParam ERole eRole){
        return userService.removeRole(id, eRole);
    }

    @GetMapping(path = "/user")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")
    public List<User> getUsers(){
        return userService.getUsers();
    }

    @DeleteMapping(path = "/user/{id}")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public void deleteUser(@PathVariable("id") long id){
         userService.deleteUser(id);
    }

    @PostMapping(path = "/login")
    public AuthResponse login(@RequestBody AuthRequest authRequest){
        return userService.login(authRequest);
    }



}
