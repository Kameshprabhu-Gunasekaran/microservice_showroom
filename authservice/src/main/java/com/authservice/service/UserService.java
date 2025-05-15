package com.authservice.service;

import com.authservice.dto.ResponseDTO;
import com.authservice.repository.UserRepository;
import com.authservice.util.Constant;
import com.common.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseDTO create(User user) {
        if (this.userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists: " + user.getEmail());
        }

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
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return new ResponseDTO(Constant.RETRIEVE, user, String.valueOf(HttpStatus.OK.value()));
    }

    public ResponseDTO update(String id, User updatedUser) {
        final User existingUser = this.userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

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
            throw new RuntimeException("User not found with id: " + id);
        }
        this.userRepository.deleteById(id);
        return new ResponseDTO(Constant.DELETE, null, String.valueOf(HttpStatus.OK.value()));
    }
}
