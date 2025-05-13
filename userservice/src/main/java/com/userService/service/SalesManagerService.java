package com.userService.service;

import com.common.entity.SalesManager;
import com.userService.dto.SalesManagerDTO;
import com.userService.repository.SalesManagerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SalesManagerService {
    private final SalesManagerRepository salesManagerRepository;

    public SalesManagerService(SalesManagerRepository salesManagerRepository) {
        this.salesManagerRepository = salesManagerRepository;
    }

    public SalesManagerDTO create(SalesManagerDTO dto) {
        SalesManager manager = mapToEntity(dto);
        SalesManager saved = salesManagerRepository.save(manager);
        return mapToDto(saved);
    }

    public List<SalesManagerDTO> getAll() {
        return salesManagerRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public SalesManagerDTO getById(String id) {
        SalesManager manager = salesManagerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("SalesManager not found with id: " + id));
        return mapToDto(manager);
    }

    public SalesManagerDTO update(String id, SalesManagerDTO dto) {
        SalesManager existing = salesManagerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("SalesManager not found with id: " + id));

        existing.setName(dto.getName());
        existing.setAddress(dto.getAddress());
        existing.setContactNumber(dto.getContactNumber());
        existing.setShowroomId(dto.getShowroomId());

        return mapToDto(salesManagerRepository.save(existing));
    }

    public void delete(String id) {
        if (!salesManagerRepository.existsById(id)) {
            throw new RuntimeException("SalesManager not found with id: " + id);
        }
        salesManagerRepository.deleteById(id);
    }

    private SalesManager mapToEntity(SalesManagerDTO dto) {
        SalesManager manager = new SalesManager();
        manager.setId(dto.getId());
        manager.setName(dto.getName());
        manager.setAddress(dto.getAddress());
        manager.setContactNumber(dto.getContactNumber());
        manager.setShowroomId(dto.getShowroomId());
        return manager;
    }

    private SalesManagerDTO mapToDto(SalesManager manager) {
        SalesManagerDTO dto = new SalesManagerDTO();
        dto.setId(manager.getId());
        dto.setName(manager.getName());
        dto.setAddress(manager.getAddress());
        dto.setContactNumber(manager.getContactNumber());
        dto.setShowroomId(manager.getShowroomId());
        return dto;
    }
}
