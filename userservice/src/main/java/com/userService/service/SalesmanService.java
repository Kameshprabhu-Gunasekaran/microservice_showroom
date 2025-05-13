package com.userService.service;

import com.common.entity.Salesman;
import com.userService.dto.SalesmanDTO;
import com.userService.repository.SalesmanRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SalesmanService {
    private final SalesmanRepository salesmanRepository;

    public SalesmanService(SalesmanRepository salesmanRepository) {
        this.salesmanRepository = salesmanRepository;
    }

    public SalesmanDTO createSalesman(SalesmanDTO dto) {
        Salesman salesman = mapToEntity(dto);
        Salesman saved = salesmanRepository.save(salesman);
        return mapToDto(saved);
    }

    public List<SalesmanDTO> getAllSalesmen() {
        return salesmanRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public SalesmanDTO getSalesmanById(String id) {
        Salesman salesman = salesmanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Salesman not found with id: " + id));
        return mapToDto(salesman);
    }

    public SalesmanDTO updateSalesman(String id, SalesmanDTO dto) {
        Salesman existing = salesmanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Salesman not found with id: " + id));

        existing.setName(dto.getName());
        existing.setSalary(dto.getSalary());
        existing.setExperience(dto.getExperience());
        existing.setAddress(dto.getAddress());
        existing.setContactNumber(dto.getContactNumber());
        existing.setSalesManagerId(dto.getSalesManagerId());

        Salesman updated = salesmanRepository.save(existing);
        return mapToDto(updated);
    }

    public void deleteSalesman(String id) {
        if (!salesmanRepository.existsById(id)) {
            throw new RuntimeException("Salesman not found with id: " + id);
        }
        salesmanRepository.deleteById(id);
    }

    private SalesmanDTO mapToDto(Salesman salesman) {
        SalesmanDTO dto = new SalesmanDTO();
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
        Salesman salesman = new Salesman();
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
