package com.spring.security.app.springsecurityapp.services;

import com.spring.security.app.springsecurityapp.models.Role;
import com.spring.security.app.springsecurityapp.repository.IRoleRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

public interface IRoleService {
    Optional<Role> findRoleByName(String name);
}
