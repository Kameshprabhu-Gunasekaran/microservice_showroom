package com.bikeservice.service;

import com.bikeservice.dto.SaleDTO;
import com.bikeservice.repository.SaleRepository;
import com.common.entity.Sale;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SaleService {
    private final SaleRepository saleRepository;

    public SaleService(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    public SaleDTO createSale(SaleDTO saleDto) {
        Sale sale = mapToEntity(saleDto);
        Sale savedSale = saleRepository.save(sale);
        return mapToDto(savedSale);
    }

    public List<SaleDTO> getAllSales() {
        return saleRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public SaleDTO getSaleById(String id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sale not found with id: " + id));
        return mapToDto(sale);
    }

    public SaleDTO updateSale(String id, SaleDTO updatedDto) {
        Sale existingSale = saleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sale not found with id: " + id));

        existingSale.setSalesDate(updatedDto.getSalesDate());
        existingSale.setSalesPrice(updatedDto.getSalesPrice());
        existingSale.setBikeId(updatedDto.getBikeId());
        existingSale.setCustomerId(updatedDto.getCustomerId());

        Sale updatedSale = saleRepository.save(existingSale);
        return mapToDto(updatedSale);
    }

    public void deleteSale(String id) {
        if (!saleRepository.existsById(id)) {
            throw new RuntimeException("Sale not found with id: " + id);
        }
        saleRepository.deleteById(id);
    }

    private SaleDTO mapToDto(Sale sale) {
        SaleDTO dto = new SaleDTO();
        dto.setId(sale.getId());
        dto.setSalesDate(sale.getSalesDate());
        dto.setSalesPrice(sale.getSalesPrice());
        dto.setBikeId(sale.getBikeId());
        dto.setCustomerId(sale.getCustomerId());
        return dto;
    }

    private Sale mapToEntity(SaleDTO dto) {
        Sale sale = new Sale();
        sale.setSalesDate(dto.getSalesDate());
        sale.setSalesPrice(dto.getSalesPrice());
        sale.setBikeId(dto.getBikeId());
        sale.setCustomerId(dto.getCustomerId());
        return sale;
    }
}
