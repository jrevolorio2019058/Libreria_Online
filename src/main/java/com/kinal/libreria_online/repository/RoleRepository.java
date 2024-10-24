package com.kinal.libreria_online.repository;

import com.kinal.libreria_online.model.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, String> {

    Optional<Role> findByRoleName(String roleName);

    boolean existsByRoleName(String roleName);

}
