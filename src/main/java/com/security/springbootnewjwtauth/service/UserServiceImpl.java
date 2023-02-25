package com.security.springbootnewjwtauth.service;

import com.security.springbootnewjwtauth.domain.Role;
import com.security.springbootnewjwtauth.domain.User;
import com.security.springbootnewjwtauth.enums.ERole;
import com.security.springbootnewjwtauth.jwt.JwtTokenUtil;
import com.security.springbootnewjwtauth.model.request.AuthRequest;
import com.security.springbootnewjwtauth.model.request.UserRequestDto;
import com.security.springbootnewjwtauth.model.response.AuthResponse;
import com.security.springbootnewjwtauth.repository.RoleRepository;
import com.security.springbootnewjwtauth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    @Lazy
    private AuthenticationManager authManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }


    @Override
    public User createUser(UserRequestDto userRequestDto) {

        BCryptPasswordEncoder bCryptPasswordEncoder =  new BCryptPasswordEncoder();

        User newUser = new User();
        newUser.setEmail(userRequestDto.getEmail());
        newUser.setUsername(userRequestDto.getUsername());
        newUser.setPassword(bCryptPasswordEncoder.encode(userRequestDto.getPassword()));

        return userRepository.save(newUser);
    }

    @Override
    public User updateUser(long id, UserRequestDto userRequestDto) {


        BCryptPasswordEncoder bCryptPasswordEncoder =  new BCryptPasswordEncoder();

        User currentUser = userRepository.findById(id).orElseThrow();
        if (userRequestDto.getUsername() !=null && !userRequestDto.getUsername().isEmpty())
        currentUser.setUsername(userRequestDto.getUsername());
        if (userRequestDto.getEmail() !=null && !userRequestDto.getEmail().isEmpty())
            currentUser.setEmail(userRequestDto.getEmail());
        if (userRequestDto.getPassword() !=null && !userRequestDto.getPassword().isEmpty())
            currentUser.setPassword(bCryptPasswordEncoder.encode(userRequestDto.getPassword()));


            return userRepository.save(currentUser);
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public User addRole(long userId, ERole eRole) {
        User user = userRepository.findById(userId).orElseThrow();
        Role role = roleRepository.findByName(eRole);

        user.getRoles().add(role);
        user.getRoles().stream().forEach(r -> r.setUsers(Arrays.asList(user)));

        return userRepository.save(user);

    }

    @Override
    public User removeRole(long userId, ERole eRole) {
        User user = userRepository.findById(userId).orElseThrow();
        Role role = roleRepository.findByName(eRole);

        if (role != null) user.getRoles().remove(role);
        user.getRoles().stream().forEach(r -> r.setUsers(Arrays.asList(user)));

        return userRepository.save(user);
    }

    @Override
    public void deleteUser(long id) {
       userRepository.deleteById(id);
    }

    @Override
    public AuthResponse login(AuthRequest authRequest) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        User authenticatedUser = userRepository.findByUsername(authRequest.getUsername());
        if (authenticatedUser ==null){
            throw  new BadCredentialsException("Geçersiz kullanıcı adı ya da şifre");
        }

        boolean isPasswordMatch = passwordEncoder.matches(authRequest.getPassword(),authenticatedUser.getPassword());
        if (!isPasswordMatch){
            throw new BadCredentialsException("Şifreler eşleşmedi");
        }

        try {
            Authentication authentication =authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(),authRequest.getPassword(),new ArrayList<>()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

        }catch (Exception e){
            throw new IllegalStateException("Authentication failed " + e);
        }

        String accessToken = jwtTokenUtil.generateAccessToken(authenticatedUser);
        AuthResponse response = new AuthResponse(authenticatedUser.getEmail(),accessToken);

        return response;

    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) throw  new BadCredentialsException("Geçersiz Kullanıcı adı ya da şifre");

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),user.getPassword(),new ArrayList<>());

    }
}
