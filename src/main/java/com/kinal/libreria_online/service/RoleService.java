package com.kinal.libreria_online.service;

import com.kinal.libreria_online.model.Role;
import com.kinal.libreria_online.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @PostConstruct
    public void init() {

        crearRolesPorDefecto();

    }

    public void crearRolesPorDefecto() {

        if(roleRepository.count() == 0) {

            roleRepository.save(new Role("admin"));
            roleRepository.save(new Role("user"));

        }

    }

    public boolean existeRol(String roleName) {

        return roleRepository.findByRoleName(roleName).isPresent();

    }

}
