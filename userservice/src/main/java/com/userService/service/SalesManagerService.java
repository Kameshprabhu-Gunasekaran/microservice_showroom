package com.userService.service;

import com.common.entity.SalesManager;
import com.userService.dto.ResponseDTO;
import com.userService.dto.SalesManagerDTO;
import com.userService.repository.SalesManagerRepository;
import com.userService.util.Constant;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SalesManagerService {

    private final SalesManagerRepository salesManagerRepository;

    public SalesManagerService(SalesManagerRepository salesManagerRepository) {
        this.salesManagerRepository = salesManagerRepository;
    }

    public ResponseDTO create(SalesManagerDTO dto) {
        final SalesManager manager = mapToEntity(dto);
        final SalesManager saved = this.salesManagerRepository.save(manager);
        return new ResponseDTO(HttpStatus.CREATED.value(), Constant.CREATE, mapToDto(saved));
    }

    public ResponseDTO retrieve() {
        final List<SalesManagerDTO> managers = this.salesManagerRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return new ResponseDTO(HttpStatus.OK.value(), Constant.RETRIEVE, managers);
    }

    public ResponseDTO retrieveById(String id) {
        final SalesManager manager = this.salesManagerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("SalesManager not found with id: " + id));
        return new ResponseDTO(HttpStatus.OK.value(), Constant.RETRIEVE, mapToDto(manager));
    }

    public ResponseDTO update(String id, SalesManagerDTO dto) {
        final SalesManager existing = this.salesManagerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("SalesManager not found with id: " + id));

        existing.setName(dto.getName());
        existing.setAddress(dto.getAddress());
        existing.setContactNumber(dto.getContactNumber());
        existing.setShowroomId(dto.getShowroomId());

        final SalesManager updated = this.salesManagerRepository.save(existing);
        return new ResponseDTO(HttpStatus.OK.value(), Constant.UPDATE, mapToDto(updated));
    }

    public ResponseDTO delete(String id) {
        if (!salesManagerRepository.existsById(id)) {
            throw new RuntimeException("SalesManager not found with id: " + id);
        }
        this.salesManagerRepository.deleteById(id);
        return new ResponseDTO(HttpStatus.OK.value(), Constant.DELETE, null);
    }

    private SalesManager mapToEntity(SalesManagerDTO dto) {
        final SalesManager manager = new SalesManager();
        manager.setId(dto.getId());
        manager.setName(dto.getName());
        manager.setAddress(dto.getAddress());
        manager.setContactNumber(dto.getContactNumber());
        manager.setShowroomId(dto.getShowroomId());
        return manager;
    }

    private SalesManagerDTO mapToDto(SalesManager manager) {
        final SalesManagerDTO dto = new SalesManagerDTO();
        dto.setId(manager.getId());
        dto.setName(manager.getName());
        dto.setAddress(manager.getAddress());
        dto.setContactNumber(manager.getContactNumber());
        dto.setShowroomId(manager.getShowroomId());
        return dto;
    }
}
