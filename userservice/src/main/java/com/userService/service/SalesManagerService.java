package com.userService.service;

import com.common.entity.SalesManager;
import com.common.entity.User;
import com.userService.dto.ResponseDTO;
import com.userService.dto.SalesManagerDTO;
import com.userService.exception.BadRequestServiceException;
import com.userService.repository.SalesManagerRepository;
import com.userService.util.Constant;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SalesManagerService {

    private final SalesManagerRepository salesManagerRepository;

    public SalesManagerService(final SalesManagerRepository salesManagerRepository) {
        this.salesManagerRepository = salesManagerRepository;
    }

    public ResponseDTO create(final SalesManager salesManager) {

        final String email = SecurityContextHolder.getContext().getAuthentication().getName();
        salesManager.setCreatedBy(email);
        final SalesManager saved = this.salesManagerRepository.save(salesManager);
        return new ResponseDTO(HttpStatus.CREATED.value(), Constant.CREATE, mapToDto(saved));
    }

    public ResponseDTO retrieve() {
        final List<SalesManagerDTO> managers = this.salesManagerRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return new ResponseDTO(HttpStatus.OK.value(), Constant.RETRIEVE, managers);
    }

    public ResponseDTO retrieveById(final String id) {
        final SalesManager manager = this.salesManagerRepository.findById(id)
                .orElseThrow(() -> new BadRequestServiceException(Constant.ID_DOES_NOT_EXIST + id));
        return new ResponseDTO(HttpStatus.OK.value(), Constant.RETRIEVE, mapToDto(manager));
    }

    public ResponseDTO update(final String id, final SalesManagerDTO dto) {
        final SalesManager existing = this.salesManagerRepository.findById(id)
                .orElseThrow(() -> new BadRequestServiceException(Constant.ID_DOES_NOT_EXIST + id));

        existing.setName(dto.getName());
        existing.setAddress(dto.getAddress());
        existing.setContactNumber(dto.getContactNumber());
        existing.setShowroomId(dto.getShowroomId());

        final SalesManager updated = this.salesManagerRepository.save(existing);
        return new ResponseDTO(HttpStatus.OK.value(), Constant.UPDATE, mapToDto(updated));
    }

    public ResponseDTO delete(final String id) {
        if (!salesManagerRepository.existsById(id)) {
            throw new BadRequestServiceException(Constant.ID_DOES_NOT_EXIST + id);
        }
        this.salesManagerRepository.deleteById(id);
        return new ResponseDTO(HttpStatus.OK.value(), Constant.DELETE, null);
    }

    private SalesManagerDTO mapToDto(final SalesManager manager) {
        final SalesManagerDTO dto = new SalesManagerDTO();
        dto.setId(manager.getId());
        dto.setName(manager.getName());
        dto.setAddress(manager.getAddress());
        dto.setContactNumber(manager.getContactNumber());
        dto.setShowroomId(manager.getShowroomId());
        return dto;
    }
}
