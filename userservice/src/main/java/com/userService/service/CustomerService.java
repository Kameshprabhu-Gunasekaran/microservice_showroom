package com.userService.service;

import com.common.entity.Customer;
import com.userService.dto.CustomerDTO;
import com.userService.dto.ResponseDTO;
import com.userService.exception.BadRequestServiceException;
import com.userService.repository.CustomerRepository;
import com.userService.util.Constant;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public ResponseDTO create(CustomerDTO dto) {
        final Customer customer = mapToEntity(dto);
        final Customer saved = this.customerRepository.save(customer);
        return new ResponseDTO(HttpStatus.CREATED.value(), Constant.CREATE, mapToDto(saved));
    }

    public ResponseDTO retrieveAll() {
        final List<CustomerDTO> customers = this.customerRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return new ResponseDTO(HttpStatus.OK.value(), Constant.RETRIEVE, customers);
    }

    public ResponseDTO retrieveById(String id) {
        final Customer customer = this.customerRepository.findById(id)
                .orElseThrow(() -> new BadRequestServiceException(Constant.ID_DOES_NOT_EXIST + id));
        return new ResponseDTO(HttpStatus.OK.value(), Constant.RETRIEVE, mapToDto(customer));
    }

    public ResponseDTO update(String id, CustomerDTO dto) {
        final Customer existing = this.customerRepository.findById(id)
                .orElseThrow(() -> new BadRequestServiceException(Constant.ID_DOES_NOT_EXIST + id));

        existing.setName(dto.getName());
        existing.setEmail(dto.getEmail());
        existing.setAddress(dto.getAddress());
        existing.setContactNumber(dto.getContactNumber());
        existing.setSalesmanId(dto.getSalesmanId());

        final Customer updated = this.customerRepository.save(existing);
        return new ResponseDTO(HttpStatus.OK.value(), Constant.UPDATE, mapToDto(updated));
    }

    public ResponseDTO delete(String id) {
        if (!customerRepository.existsById(id)) {
            throw new BadRequestServiceException(Constant.ID_DOES_NOT_EXIST + id);
        }
        this.customerRepository.deleteById(id);
        return new ResponseDTO(HttpStatus.OK.value(), Constant.DELETE, null);
    }

    private CustomerDTO mapToDto(Customer customer) {
        final CustomerDTO dto = new CustomerDTO();
        dto.setId(customer.getId());
        dto.setName(customer.getName());
        dto.setEmail(customer.getEmail());
        dto.setAddress(customer.getAddress());
        dto.setContactNumber(customer.getContactNumber());
        dto.setSalesmanId(customer.getSalesmanId());
        return dto;
    }

    private Customer mapToEntity(CustomerDTO dto) {
        final Customer customer = new Customer();
        customer.setId(dto.getId());
        customer.setName(dto.getName());
        customer.setEmail(dto.getEmail());
        customer.setAddress(dto.getAddress());
        customer.setContactNumber(dto.getContactNumber());
        customer.setSalesmanId(dto.getSalesmanId());
        return customer;
    }
}
