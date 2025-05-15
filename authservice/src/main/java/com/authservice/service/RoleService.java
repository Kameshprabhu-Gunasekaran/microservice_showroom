package com.authservice.service;

import com.authservice.dto.ResponseDTO;
import com.authservice.repository.RoleRepository;
import com.authservice.util.Constant;
import com.common.entity.ERole;
import com.common.entity.Role;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public ResponseDTO create(ERole eRole) {
        Role role = new Role();
        role.setRole(eRole);
        role.setCreatedBy("SYSTEM");
        Role savedRole = roleRepository.save(role);
        return new ResponseDTO(Constant.CREATE,savedRole,String.valueOf(HttpStatus.CREATED.value()));
    }

    public ResponseDTO retrieveAll() {
        List<Role> roles = roleRepository.findAll();
        return new ResponseDTO(Constant.RETRIEVE, roles, String.valueOf(HttpStatus.OK.value()));
    }

    public ResponseDTO retrieveById(String id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));
        return new ResponseDTO(Constant.RETRIEVE, role, String.valueOf(HttpStatus.OK.value()));
    }

    public ResponseDTO update(String id, ERole updatedRole) {
        Role existingRole = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));

        existingRole.setRole(updatedRole);
        existingRole.setUpdatedBy("SYSTEM");
        Role updated = roleRepository.save(existingRole);
        return new ResponseDTO(Constant.UPDATE, updated, String.valueOf(HttpStatus.OK.value()));
    }

    public ResponseDTO delete(String id) {
        if (!roleRepository.existsById(id)) {
            throw new RuntimeException("Role not found with id: " + id);
        }
        roleRepository.deleteById(id);
        return new ResponseDTO(Constant.DELETE, null, String.valueOf(HttpStatus.OK.value()));
    }
}

