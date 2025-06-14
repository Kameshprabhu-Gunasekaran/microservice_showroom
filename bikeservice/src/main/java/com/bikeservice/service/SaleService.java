package com.bikeservice.service;

import com.bikeservice.dto.ResponseDTO;
import com.bikeservice.dto.SaleDTO;
import com.bikeservice.exception.BadRequestServiceException;
import com.bikeservice.repository.SaleRepository;
import com.bikeservice.util.Constant;
import com.common.entity.Sale;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SaleService {

    private final SaleRepository saleRepository;

    public SaleService(final SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    public ResponseDTO create(final Sale sale) {

        final String email = SecurityContextHolder.getContext().getAuthentication().getName();
        sale.setCreatedBy(email);
        final Sale savedSale = this.saleRepository.save(sale);
        return new ResponseDTO(HttpStatus.CREATED.value(), Constant.CREATE, sale);
    }

    public ResponseDTO retrieve() {
        final List<SaleDTO> sales = this.saleRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return new ResponseDTO(HttpStatus.OK.value(), Constant.RETRIEVE, sales);
    }

    public ResponseDTO retrieveById(final String id) {
        final Sale sale = this.saleRepository.findById(id)
                .orElseThrow(() -> new BadRequestServiceException(Constant.ID_DOES_NOT_EXIST + id));
        return new ResponseDTO(HttpStatus.OK.value(), Constant.RETRIEVE, mapToDto(sale));
    }

    public ResponseDTO update(final String id, final SaleDTO updatedDto) {
        final Sale existingSale = this.saleRepository.findById(id)
                .orElseThrow(() -> new BadRequestServiceException(Constant.ID_DOES_NOT_EXIST + id));

        existingSale.setSalesDate(updatedDto.getSalesDate());
        existingSale.setSalesPrice(updatedDto.getSalesPrice());
        existingSale.setBikeId(updatedDto.getBikeId());
        existingSale.setCustomerId(updatedDto.getCustomerId());

        final Sale updatedSale = this.saleRepository.save(existingSale);
        return new ResponseDTO(HttpStatus.OK.value(), Constant.UPDATE, mapToDto(updatedSale));
    }

    public ResponseDTO delete(final String id) {
        if (!saleRepository.existsById(id)) {
            throw new BadRequestServiceException(Constant.ID_DOES_NOT_EXIST + id);
        }
        this.saleRepository.deleteById(id);
        return new ResponseDTO(HttpStatus.OK.value(), Constant.DELETE, null);
    }

    private SaleDTO mapToDto(final Sale sale) {
        final SaleDTO dto = new SaleDTO();
        dto.setId(sale.getId());
        dto.setSalesDate(sale.getSalesDate());
        dto.setSalesPrice(sale.getSalesPrice());
        dto.setBikeId(sale.getBikeId());
        dto.setCustomerId(sale.getCustomerId());
        return dto;
    }
}
