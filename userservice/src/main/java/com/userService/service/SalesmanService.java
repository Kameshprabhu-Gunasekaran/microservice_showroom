package com.userService.service;

import com.common.entity.Salesman;
import com.userService.dto.ResponseDTO;
import com.userService.dto.SalesmanDTO;
import com.userService.exception.BadRequestServiceException;
import com.userService.repository.SalesmanRepository;
import com.userService.util.Constant;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SalesmanService {

    private final SalesmanRepository salesmanRepository;

    public SalesmanService(SalesmanRepository salesmanRepository) {
        this.salesmanRepository = salesmanRepository;
    }

    public ResponseDTO create(SalesmanDTO dto) {
        final Salesman salesman = mapToEntity(dto);
        final Salesman saved = this.salesmanRepository.save(salesman);
        return new ResponseDTO(HttpStatus.CREATED.value(), Constant.CREATE, mapToDto(saved));
    }

    public ResponseDTO retrieve() {
        final List<SalesmanDTO> salesmen = this.salesmanRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return new ResponseDTO(HttpStatus.OK.value(), Constant.RETRIEVE, salesmen);
    }

    public ResponseDTO retrieveById(String id) {
        final Salesman salesman = this.salesmanRepository.findById(id)
                .orElseThrow(() -> new BadRequestServiceException(Constant.ID_DOES_NOT_EXIST + id));
        return new ResponseDTO(HttpStatus.OK.value(), Constant.RETRIEVE, mapToDto(salesman));
    }

    public ResponseDTO update(String id, SalesmanDTO dto) {
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

    public ResponseDTO delete(String id) {
        if (!salesmanRepository.existsById(id)) {
            throw new BadRequestServiceException(Constant.ID_DOES_NOT_EXIST + id);
        }
        this.salesmanRepository.deleteById(id);
        return new ResponseDTO(HttpStatus.OK.value(), Constant.DELETE, null);
    }

    private SalesmanDTO mapToDto(Salesman salesman) {
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

    private Salesman mapToEntity(SalesmanDTO dto) {
        final Salesman salesman = new Salesman();
        salesman.setId(dto.getId());
        salesman.setName(dto.getName());
        salesman.setSalary(dto.getSalary());
        salesman.setExperience(dto.getExperience());
        salesman.setAddress(dto.getAddress());
        salesman.setContactNumber(dto.getContactNumber());
        salesman.setSalesManagerId(dto.getSalesManagerId());
        return salesman;
    }
}
