package com.authservice.service;

import com.authservice.dto.ResponseDTO;
import com.authservice.dto.RoleDTO;
import com.authservice.exception.BadRequestServiceException;
import com.authservice.repository.RoleRepository;
import com.authservice.repository.UserRepository;
import com.authservice.util.Constant;
import com.common.entity.ERole;
import com.common.entity.Role;
import com.common.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public RoleService(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    public ResponseDTO create(RoleDTO roleDTO) {
        final User user=this.userRepository.findById(roleDTO.getUserId()).orElseThrow(()->new BadRequestServiceException(Constant.NOT_FOUND));
        final Role role = new Role();
        role.setUser(user);
        role.setRole(roleDTO.getRole());
        role.setCreatedBy("SYSTEM");
        role.setUpdatedBy("SYSTEM");
        return new ResponseDTO(Constant.CREATE, this.roleRepository.save(role),HttpStatus.OK.getReasonPhrase());
    }

    public ResponseDTO retrieveAll() {
        final List<Role> roles = this.roleRepository.findAll();
        return new ResponseDTO(Constant.RETRIEVE, roles, String.valueOf(HttpStatus.OK.value()));
    }

    public ResponseDTO retrieveById(String id) {
        final Role role = this.roleRepository.findById(id)
                .orElseThrow(() -> new BadRequestServiceException(Constant.ID_DOES_NOT_EXIST));
        return new ResponseDTO(Constant.RETRIEVE, role, String.valueOf(HttpStatus.OK.value()));
    }

    public ResponseDTO update(String id, ERole updatedRole) {
        final Role existingRole = this.roleRepository.findById(id)
                .orElseThrow(() -> new BadRequestServiceException(Constant.ID_DOES_NOT_EXIST));

        existingRole.setRole(updatedRole);
        existingRole.setUpdatedBy("SYSTEM");
        final Role updated = this.roleRepository.save(existingRole);
        return new ResponseDTO(Constant.UPDATE, updated, String.valueOf(HttpStatus.OK.value()));
    }

    public ResponseDTO delete(String id) {
        if (!roleRepository.existsById(id)) {
            throw new BadRequestServiceException(Constant.ID_DOES_NOT_EXIST);
        }
        this.roleRepository.deleteById(id);
        return new ResponseDTO(Constant.DELETE, null, String.valueOf(HttpStatus.OK.value()));
    }

    public Role getRoleByEnum(ERole role) {
        return roleRepository.findByRole(role)
                .orElseThrow(() -> new BadRequestServiceException("Role not found: " + role.name()));
    }
}

