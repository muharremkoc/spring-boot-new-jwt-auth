package com.security.springbootnewjwtauth.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.security.springbootnewjwtauth.enums.ERole;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    public ERole name;

    @ManyToMany(mappedBy = "roles",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JsonIgnore
    private List<User> users;

    public Role() {
    }

    public Role(Long id, ERole name) {
        this.id = id;
        this.name = name;
    }

    public Role(ERole name) {
        this.name = name;
    }

    public Role(Long id, ERole name, List<User> users) {
        this.id = id;
        this.name = name;
        this.users = users;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ERole getName() {
        return name;
    }

    public void setName(ERole eRole) {
        this.name = eRole;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
