package com.security.springbootnewjwtauth.loader;


import com.security.springbootnewjwtauth.domain.Role;
import com.security.springbootnewjwtauth.domain.User;
import com.security.springbootnewjwtauth.enums.ERole;
import com.security.springbootnewjwtauth.repository.RoleRepository;
import com.security.springbootnewjwtauth.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class DbLoader implements ApplicationRunner {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        if (roleRepository.count() == 0)
            Arrays.stream(ERole.values()).map(Role::new).forEach(role -> roleRepository.save(role));

        if (userRepository.count() == 0) {

            BCryptPasswordEncoder bCryptPasswordEncoder =  new BCryptPasswordEncoder();

            userRepository.save(new User("root@root.rot","root",bCryptPasswordEncoder.encode("root"),List.of(roleRepository.findByName(ERole.ROLE_ADMIN),roleRepository.findByName(ERole.ROLE_USER))));
            userRepository.save(new User("admin@admin.adm","admin",bCryptPasswordEncoder.encode("admin"),List.of(roleRepository.findByName(ERole.ROLE_ADMIN))));
            userRepository.save(new User("user@user.usr","user",bCryptPasswordEncoder.encode("user"), List.of(roleRepository.findByName(ERole.ROLE_USER))));

        }


    }
}
