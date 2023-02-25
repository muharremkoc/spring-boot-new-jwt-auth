package com.security.springbootnewjwtauth.domain;




import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")

public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String username;

    private String password;



    @ManyToMany(cascade = CascadeType.DETACH,fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = {
                    @JoinColumn(name = "user_id", referencedColumnName = "id",
                            nullable = true, updatable = true)},
            inverseJoinColumns = {
                    @JoinColumn(name = "role_id", referencedColumnName = "id",
                            nullable = true, updatable = true)})
    private List<Role> roles;


    public User() {
    }


    public User(Long id, String email, String username, String password) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public User(Long id, String email, String username, String password, List<Role> roles) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    @JsonIgnore

    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    @JsonIgnore

    public boolean isEnabled() {
        return false;
    }



    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        ObjectMapper mapper = new ObjectMapper();

        List<Role> a = new ArrayList<>(roles);
        for (int i = 0; i < a.size() ; i++){
            Object object = a.get(i);
            Role role = mapper.convertValue(object, Role.class);
            authorities.add(new SimpleGrantedAuthority(role.getName().name()));
        }

        return authorities;

    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }

    public User(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public User(String email, String username, String password, List<Role> roles) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
