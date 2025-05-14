package com.bikeservice.service;

import com.bikeservice.dto.BikeDTO;
import com.bikeservice.dto.ResponseDTO;
import com.bikeservice.repository.BikeRepository;
import com.bikeservice.util.Constant;
import com.common.entity.Bike;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BikeService {

    private final BikeRepository bikeRepository;

    public BikeService(BikeRepository bikeRepository) {
        this.bikeRepository = bikeRepository;
    }

    public ResponseDTO create(BikeDTO bikeDto) {
        final Bike bike = mapToEntity(bikeDto);
        final Bike savedBike = this.bikeRepository.save(bike);
        return new ResponseDTO(HttpStatus.CREATED.value(), Constant.CREATE, mapToDto(savedBike));
    }

    public ResponseDTO retrieve() {
        final List<BikeDTO> bikes = this.bikeRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return new ResponseDTO(HttpStatus.OK.value(), Constant.RETRIEVE, bikes);
    }

    public ResponseDTO retrieveById(String id) {
        final Bike bike = this.bikeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bike not found with id: " + id));
        return new ResponseDTO(HttpStatus.OK.value(), Constant.RETRIEVE, mapToDto(bike));
    }

    public ResponseDTO update(String id, BikeDTO updatedDto) {
        final Bike existingBike = this.bikeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bike not found with id: " + id));

        existingBike.setName(updatedDto.getName());
        existingBike.setCc(updatedDto.getCc());
        existingBike.setMileage(updatedDto.getMileage());
        existingBike.setPrice(updatedDto.getPrice());
        existingBike.setStock(updatedDto.getStock());
        existingBike.setSalesmanId(updatedDto.getSalesmanId());

        final Bike updatedBike = this.bikeRepository.save(existingBike);
        return new ResponseDTO(HttpStatus.OK.value(), Constant.UPDATE, mapToDto(updatedBike));
    }

    public ResponseDTO delete(String id) {
        if (!bikeRepository.existsById(id)) {
            throw new RuntimeException("Bike not found with id: " + id);
        }
        this.bikeRepository.deleteById(id);
        return new ResponseDTO(HttpStatus.OK.value(), Constant.DELETE, null);
    }

    private BikeDTO mapToDto(Bike bike) {
        final BikeDTO dto = new BikeDTO();
        dto.setId(bike.getId());
        dto.setName(bike.getName());
        dto.setCc(bike.getCc());
        dto.setMileage(bike.getMileage());
        dto.setPrice(bike.getPrice());
        dto.setStock(bike.getStock());
        dto.setSalesmanId(bike.getSalesmanId());
        return dto;
    }

    private Bike mapToEntity(BikeDTO dto) {
        final Bike bike = new Bike();
        bike.setName(dto.getName());
        bike.setCc(dto.getCc());
        bike.setMileage(dto.getMileage());
        bike.setPrice(dto.getPrice());
        bike.setStock(dto.getStock());
        bike.setSalesmanId(dto.getSalesmanId());
        return bike;
    }
}
