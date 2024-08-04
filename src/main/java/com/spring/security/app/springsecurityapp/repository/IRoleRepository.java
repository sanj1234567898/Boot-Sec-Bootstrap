package com.spring.security.app.springsecurityapp.repository;

import com.spring.security.app.springsecurityapp.models.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRoleRepository extends CrudRepository<Role, Long> {
    Optional<Role> findRoleByName(String name);
}
