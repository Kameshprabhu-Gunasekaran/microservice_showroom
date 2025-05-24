package com.authservice.service;

import com.authservice.dto.RegisterRequestDTO;
import com.authservice.dto.ResponseDTO;
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
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RoleService roleService;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, RoleService roleService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.roleService = roleService;
    }

    public ResponseDTO create(User user) {

        user.setCreatedBy("SYSTEM");
        final User savedUser = this.userRepository.save(user);
        return new ResponseDTO(Constant.CREATE, savedUser, String.valueOf(HttpStatus.CREATED.value()));
    }

    public ResponseDTO retrieveAll() {
        final List<User> users = this.userRepository.findAll();
        return new ResponseDTO(Constant.RETRIEVE, users, String.valueOf(HttpStatus.OK.value()));
    }

    public ResponseDTO retrieveById(String id) {
        final User user = this.userRepository.findById(id)
                .orElseThrow(() -> new BadRequestServiceException(Constant.ID_DOES_NOT_EXIST));
        return new ResponseDTO(Constant.RETRIEVE, user, String.valueOf(HttpStatus.OK.value()));
    }

    public ResponseDTO update(String id, User updatedUser) {
        final User existingUser = this.userRepository.findById(id)
                .orElseThrow(() -> new BadRequestServiceException(Constant.ID_DOES_NOT_EXIST));

        existingUser.setUserName(updatedUser.getUserName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPassword(updatedUser.getPassword());
        existingUser.setRole(updatedUser.getRole());
        existingUser.setUpdatedBy("SYSTEM");

        final User savedUser = this.userRepository.save(existingUser);
        return new ResponseDTO(Constant.UPDATE, savedUser, String.valueOf(HttpStatus.OK.value()));
    }

    public ResponseDTO delete(String id) {
        if (!userRepository.existsById(id)) {
            throw new BadRequestServiceException(Constant.ID_DOES_NOT_EXIST);
        }
        this.userRepository.deleteById(id);
        return new ResponseDTO(Constant.DELETE, null, String.valueOf(HttpStatus.OK.value()));
    }

    public String getRoleByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(user -> user.getRole().getRole().name())
                .orElseThrow(() -> new BadRequestServiceException("User not found for email: " + email));
    }

    public ResponseDTO register(RegisterRequestDTO request) {
        if (this.userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BadRequestServiceException(Constant.EMAIL_ALREADY_EXIST + request.getEmail());
        }

        Role role = roleService.getRoleByEnum(ERole.valueOf(request.getRole()));

        User user = new User();
        user.setUserName(request.getUserName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRole(role);
        user.setCreatedBy("SYSTEM");

        User savedUser = this.userRepository.save(user);
        return new ResponseDTO(Constant.CREATE, savedUser, String.valueOf(HttpStatus.CREATED.value()));
    }
}
