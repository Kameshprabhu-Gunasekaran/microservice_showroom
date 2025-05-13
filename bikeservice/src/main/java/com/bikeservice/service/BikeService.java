package com.bikeservice.service;

import com.bikeservice.dto.BikeDTO;
import com.bikeservice.repository.BikeRepository;
import com.common.entity.Bike;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BikeService {
    private final BikeRepository bikeRepository;

    public BikeService(BikeRepository bikeRepository) {
        this.bikeRepository = bikeRepository;
    }

    public BikeDTO createBike(BikeDTO bikeDto) {
        com.common.entity.Bike bike = mapToEntityForCreate(bikeDto);
        Bike savedBike = bikeRepository.save(bike);
        return mapToDto(savedBike);
    }

    public List<BikeDTO> getAllBikes() {
        return bikeRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public BikeDTO getBikeById(String id) {
        Bike bike = bikeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bike not found with id: " + id));
        return mapToDto(bike);
    }

    public BikeDTO updateBike(String id, BikeDTO updatedDto) {
        Bike existingBike = bikeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bike not found with id: " + id));

        existingBike.setName(updatedDto.getName());
        existingBike.setCc(updatedDto.getCc());
        existingBike.setMileage(updatedDto.getMileage());
        existingBike.setPrice(updatedDto.getPrice());
        existingBike.setStock(updatedDto.getStock());
        existingBike.setSalesmanId(updatedDto.getSalesmanId());

        Bike updatedBike = bikeRepository.save(existingBike);
        return mapToDto(updatedBike);
    }

    public void deleteBike(String id) {
        if (!bikeRepository.existsById(id)) {
            throw new RuntimeException("Bike not found with id: " + id);
        }
        bikeRepository.deleteById(id);
    }

    private BikeDTO mapToDto(Bike bike) {
        BikeDTO dto = new BikeDTO();
        dto.setId(bike.getId());
        dto.setName(bike.getName());
        dto.setCc(bike.getCc());
        dto.setMileage(bike.getMileage());
        dto.setPrice(bike.getPrice());
        dto.setStock(bike.getStock());
        dto.setSalesmanId(bike.getSalesmanId());
        return dto;
    }

    private Bike mapToEntityForCreate(BikeDTO dto) {
        Bike bike = new Bike();
        bike.setName(dto.getName());
        bike.setCc(dto.getCc());
        bike.setMileage(dto.getMileage());
        bike.setPrice(dto.getPrice());
        bike.setStock(dto.getStock());
        bike.setSalesmanId(dto.getSalesmanId());
        return bike;
    }
}
