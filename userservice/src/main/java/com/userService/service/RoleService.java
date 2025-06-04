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
        final User user = this.userRepository.findById(roleDTO.getUserId()).orElseThrow(() -> new BadRequestServiceException(Constant.NOT_FOUND));
        final Role role = new Role();
        role.setUser(user);
        role.setRole(roleDTO.getRole());
        role.setCreatedBy("SYSTEM");
        role.setUpdatedBy("SYSTEM");
        return new ResponseDTO(HttpStatus.OK.value(), Constant.CREATE, this.roleRepository.save(role));
    }

    public ResponseDTO retrieveAll() {
        final List<Role> roles = this.roleRepository.findAll();
        return new ResponseDTO(HttpStatus.OK.value(), Constant.RETRIEVE, roles);
    }

    public ResponseDTO retrieveById(String id) {
        final Role role = this.roleRepository.findById(id).orElseThrow(() -> new BadRequestServiceException(Constant.ID_DOES_NOT_EXIST));
        return new ResponseDTO(HttpStatus.OK.value(), Constant.RETRIEVE, role);
    }

    public ResponseDTO update(String id, String updatedRole) {
        final Role existingRole = this.roleRepository.findById(id).orElseThrow(() -> new BadRequestServiceException(Constant.ID_DOES_NOT_EXIST));

        existingRole.setRole(updatedRole);
        existingRole.setUpdatedBy("SYSTEM");
        final Role updated = this.roleRepository.save(existingRole);
        return new ResponseDTO(HttpStatus.OK.value(), Constant.UPDATE, updated);
    }

    public ResponseDTO delete(String id) {
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