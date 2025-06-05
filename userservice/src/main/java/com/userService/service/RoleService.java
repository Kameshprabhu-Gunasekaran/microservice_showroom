package com.userService.service;

import com.common.entity.Role;
import com.common.entity.User;
import com.userService.dto.ResponseDTO;
import com.userService.dto.RoleDTO;
import com.userService.exception.BadRequestServiceException;
import com.userService.repository.RoleRepository;
import com.userService.repository.UserRepository;
import com.userService.util.Constant;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public RoleService(final RoleRepository roleRepository, final UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    public ResponseDTO create(final RoleDTO roleDTO) {
        final User user = this.userRepository.findById(roleDTO.getUserId()).orElseThrow(() -> new BadRequestServiceException(Constant.NOT_FOUND));
        final Role role = new Role();
        role.setUser(user);
        role.setRole(roleDTO.getRole());
        final String email = SecurityContextHolder.getContext().getAuthentication().getName();
        role.setCreatedBy(email);
        role.setUpdatedBy(email);
        return new ResponseDTO(HttpStatus.OK.value(), Constant.CREATE, this.roleRepository.save(role));
    }

    public ResponseDTO retrieveAll() {
        final List<Role> roles = this.roleRepository.findAll();
        return new ResponseDTO(HttpStatus.OK.value(), Constant.RETRIEVE, roles);
    }

    public ResponseDTO retrieveById(final String id) {
        final Role role = this.roleRepository.findById(id).orElseThrow(() -> new BadRequestServiceException(Constant.ID_DOES_NOT_EXIST));
        return new ResponseDTO(HttpStatus.OK.value(), Constant.RETRIEVE, role);
    }

    public ResponseDTO update(final String id, final String updatedRole) {
        final Role existingRole = this.roleRepository.findById(id).orElseThrow(() -> new BadRequestServiceException(Constant.ID_DOES_NOT_EXIST));

        existingRole.setRole(updatedRole);
        existingRole.setUpdatedBy(updatedRole);
        final Role updated = this.roleRepository.save(existingRole);
        return new ResponseDTO(HttpStatus.OK.value(), Constant.UPDATE, updated);
    }

    public ResponseDTO delete(final String id) {
        if (!roleRepository.existsById(id)) {
            throw new BadRequestServiceException(Constant.ID_DOES_NOT_EXIST);
        }
        this.roleRepository.deleteById(id);
        return new ResponseDTO(HttpStatus.OK.value(), Constant.DELETE, null);
    }

    public Role findUser(final String id) {
        return this.roleRepository.findByUserId(id);
    }
}