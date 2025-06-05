package com.userService.service;

import com.common.entity.Role;
import com.common.entity.User;
import com.userService.dto.RegisterRequestDTO;
import com.userService.dto.ResponseDTO;
import com.userService.dto.UserWithRoleDTO;
import com.userService.exception.BadRequestServiceException;
import com.userService.repository.RoleRepository;
import com.userService.repository.UserRepository;
import com.userService.util.Constant;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RoleService roleService;

    public UserService(final PasswordEncoder passwordEncoder, final UserRepository userRepository,
                       RoleRepository roleRepository, RoleService roleService) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.roleService = roleService;
    }

    public ResponseDTO create(final User user) {

        final String email = SecurityContextHolder.getContext().getAuthentication().getName();
        user.setCreatedBy(email);
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        final User savedUser = this.userRepository.save(user);
        return new ResponseDTO(HttpStatus.CREATED.value(), Constant.CREATE, user);
    }

    public ResponseDTO retrieveAll() {
        final List<User> users = this.userRepository.findAll();
        return new ResponseDTO(HttpStatus.OK.value(), Constant.RETRIEVE, users);
    }

    public ResponseDTO retrieveById(final String id) {
        final User user = this.userRepository.findById(id)
                .orElseThrow(() -> new BadRequestServiceException(Constant.ID_DOES_NOT_EXIST));
        return new ResponseDTO(HttpStatus.OK.value(), Constant.RETRIEVE, user);
    }

    public ResponseDTO update(final String id, final User updatedUser) {
        final User existingUser = this.userRepository.findById(id)
                .orElseThrow(() -> new BadRequestServiceException(Constant.ID_DOES_NOT_EXIST));

        existingUser.setUserName(updatedUser.getUserName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        existingUser.setUpdatedBy(email);

        final User savedUser = this.userRepository.save(existingUser);
        return new ResponseDTO(HttpStatus.OK.value(), Constant.UPDATE, savedUser);
    }

    public ResponseDTO delete(final String id) {
        if (!userRepository.existsById(id)) {
            throw new BadRequestServiceException(Constant.ID_DOES_NOT_EXIST);
        }
        this.userRepository.deleteById(id);
        return new ResponseDTO(HttpStatus.OK.value(), Constant.DELETE, null);
    }

    public ResponseDTO register(final RegisterRequestDTO request) {
        if (this.userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BadRequestServiceException(Constant.EMAIL_ALREADY_EXIST + request.getEmail());
        }

        final User user = new User();
        user.setUserName(request.getUserName());
        user.setEmail(request.getEmail());
        final String encrypt= passwordEncoder.encode(request.getPassword());
        user.setPassword(encrypt);
        final String email = SecurityContextHolder.getContext().getAuthentication().getName();
        user.setCreatedBy(email);

        final User savedUser = this.userRepository.save(user);
        return new ResponseDTO(HttpStatus.CREATED.value(), Constant.CREATE, savedUser);
    }

    public User getUserWithEmail(final String email) {
        final User user = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestServiceException(Constant.EMAIL_NOT_FOUND + email));

        final Role role = this.roleRepository.findByUserId(user.getId());

        final UserWithRoleDTO dto = new UserWithRoleDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setRole(role.getRole());

        return user;
    }
}