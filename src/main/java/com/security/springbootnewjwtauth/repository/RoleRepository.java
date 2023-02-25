package com.security.springbootnewjwtauth.repository;

import com.security.springbootnewjwtauth.domain.Role;
import com.security.springbootnewjwtauth.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {

    Role findByName(ERole eRole);

}
