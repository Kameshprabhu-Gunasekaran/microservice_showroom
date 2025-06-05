package com.userService.service;

import com.common.entity.Salesman;
import com.userService.dto.ResponseDTO;
import com.userService.dto.SalesmanDTO;
import com.userService.exception.BadRequestServiceException;
import com.userService.repository.SalesmanRepository;
import com.userService.util.Constant;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SalesmanService {

    private final SalesmanRepository salesmanRepository;

    public SalesmanService(final SalesmanRepository salesmanRepository) {
        this.salesmanRepository = salesmanRepository;
    }

    public ResponseDTO create(final Salesman salesman) {

        final String email = SecurityContextHolder.getContext().getAuthentication().getName();
        salesman.setCreatedBy(email);
        final Salesman saved = this.salesmanRepository.save(salesman);
        return new ResponseDTO(HttpStatus.CREATED.value(), Constant.CREATE, salesman);
    }

    public ResponseDTO retrieve() {
        final List<SalesmanDTO> salesmen = this.salesmanRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return new ResponseDTO(HttpStatus.OK.value(), Constant.RETRIEVE, salesmen);
    }

    public ResponseDTO retrieveById(final String id) {
        final Salesman salesman = this.salesmanRepository.findById(id)
                .orElseThrow(() -> new BadRequestServiceException(Constant.ID_DOES_NOT_EXIST + id));
        return new ResponseDTO(HttpStatus.OK.value(), Constant.RETRIEVE, mapToDto(salesman));
    }

    public ResponseDTO update(final String id, final SalesmanDTO dto) {
        final Salesman existing = this.salesmanRepository.findById(id)
                .orElseThrow(() -> new BadRequestServiceException(Constant.ID_DOES_NOT_EXIST + id));

        existing.setName(dto.getName());
        existing.setSalary(dto.getSalary());
        existing.setExperience(dto.getExperience());
        existing.setAddress(dto.getAddress());
        existing.setContactNumber(dto.getContactNumber());
        existing.setSalesManagerId(dto.getSalesManagerId());

        final Salesman updated = this.salesmanRepository.save(existing);
        return new ResponseDTO(HttpStatus.OK.value(), Constant.UPDATE, mapToDto(updated));
    }

    public ResponseDTO delete(final String id) {
        if (!salesmanRepository.existsById(id)) {
            throw new BadRequestServiceException(Constant.ID_DOES_NOT_EXIST + id);
        }
        this.salesmanRepository.deleteById(id);
        return new ResponseDTO(HttpStatus.OK.value(), Constant.DELETE, null);
    }

    private SalesmanDTO mapToDto(final Salesman salesman) {
        final SalesmanDTO dto = new SalesmanDTO();
        dto.setId(salesman.getId());
        dto.setName(salesman.getName());
        dto.setSalary(salesman.getSalary());
        dto.setExperience(salesman.getExperience());
        dto.setAddress(salesman.getAddress());
        dto.setContactNumber(salesman.getContactNumber());
        dto.setSalesManagerId(salesman.getSalesManagerId());
        return dto;
    }
}
