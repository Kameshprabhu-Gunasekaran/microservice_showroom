package com.userService.service;

import com.common.entity.User;
import com.userService.dto.RegisterRequestDTO;
import com.userService.dto.ResponseDTO;
import com.userService.exception.BadRequestServiceException;
import com.userService.repository.RoleRepository;
import com.userService.repository.UserRepository;
import com.userService.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RoleService roleService;

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository,
                       RoleRepository roleRepository, RoleService roleService) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.roleService = roleService;
    }

    public ResponseDTO create(User user) {

        user.setCreatedBy("SYSTEM");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        final User savedUser = this.userRepository.save(user);
        return new ResponseDTO(HttpStatus.CREATED.value(), Constant.CREATE, user);
    }

    public ResponseDTO retrieveAll() {
        final List<User> users = this.userRepository.findAll();
        return new ResponseDTO(HttpStatus.OK.value(), Constant.RETRIEVE, users);
    }

    public ResponseDTO retrieveById(String id) {
        final User user = this.userRepository.findById(id)
                .orElseThrow(() -> new BadRequestServiceException(Constant.ID_DOES_NOT_EXIST));
        return new ResponseDTO(HttpStatus.OK.value(), Constant.RETRIEVE, user);
    }

    public ResponseDTO update(String id, User updatedUser) {
        final User existingUser = this.userRepository.findById(id)
                .orElseThrow(() -> new BadRequestServiceException(Constant.ID_DOES_NOT_EXIST));

        existingUser.setUserName(updatedUser.getUserName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        existingUser.setUpdatedBy("SYSTEM");

        final User savedUser = this.userRepository.save(existingUser);
        return new ResponseDTO(HttpStatus.OK.value(), Constant.UPDATE, savedUser);
    }

    public ResponseDTO delete(String id) {
        if (!userRepository.existsById(id)) {
            throw new BadRequestServiceException(Constant.ID_DOES_NOT_EXIST);
        }
        this.userRepository.deleteById(id);
        return new ResponseDTO(HttpStatus.OK.value(), Constant.DELETE, null);
    }

    public ResponseDTO register(RegisterRequestDTO request) {
        if (this.userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BadRequestServiceException(Constant.EMAIL_ALREADY_EXIST + request.getEmail());
        }

        final User user = new User();
        user.setUserName(request.getUserName());
        user.setEmail(request.getEmail());
        final String encrypt= passwordEncoder.encode(request.getPassword());
        user.setPassword(encrypt);
        user.setCreatedBy("SYSTEM");

        final User savedUser = this.userRepository.save(user);
        return new ResponseDTO(HttpStatus.CREATED.value(), Constant.CREATE, savedUser);
    }
}