package com.bikeservice.service;

import com.bikeservice.dto.BikeDTO;
import com.bikeservice.dto.ResponseDTO;
import com.bikeservice.exception.BadRequestServiceException;
import com.bikeservice.repository.BikeRepository;
import com.bikeservice.util.Constant;
import com.common.entity.Bike;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BikeService {

    private final BikeRepository bikeRepository;

    public BikeService(final BikeRepository bikeRepository) {
        this.bikeRepository = bikeRepository;
    }

    public ResponseDTO create(final Bike bike) {

        final Object email = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(email);
//        bike.setCreatedBy(email);
        final Bike savedBike = this.bikeRepository.save(bike);
        return new ResponseDTO(HttpStatus.CREATED.value(), Constant.CREATE, bike);
    }

    public ResponseDTO retrieve() {
        final List<BikeDTO> bikes = this.bikeRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return new ResponseDTO(HttpStatus.OK.value(), Constant.RETRIEVE, bikes);
    }

    public ResponseDTO retrieveById(final String id) {
        final Bike bike = this.bikeRepository.findById(id)
                .orElseThrow(() -> new BadRequestServiceException(Constant.ID_DOES_NOT_EXIST + id));
        return new ResponseDTO(HttpStatus.OK.value(), Constant.RETRIEVE, mapToDto(bike));
    }

    public ResponseDTO update(final String id, final BikeDTO updatedDto) {
        final Bike existingBike = this.bikeRepository.findById(id)
                .orElseThrow(() -> new BadRequestServiceException(Constant.ID_DOES_NOT_EXIST + id));

        existingBike.setName(updatedDto.getName());
        existingBike.setCc(updatedDto.getCc());
        existingBike.setMileage(updatedDto.getMileage());
        existingBike.setPrice(updatedDto.getPrice());
        existingBike.setStock(updatedDto.getStock());
        existingBike.setSalesmanId(updatedDto.getSalesmanId());

        final Bike updatedBike = this.bikeRepository.save(existingBike);
        return new ResponseDTO(HttpStatus.OK.value(), Constant.UPDATE, mapToDto(updatedBike));
    }

    public ResponseDTO delete(final String id) {
        if (!bikeRepository.existsById(id)) {
            throw new BadRequestServiceException(Constant.ID_DOES_NOT_EXIST + id);
        }
        this.bikeRepository.deleteById(id);
        return new ResponseDTO(HttpStatus.OK.value(), Constant.DELETE, null);
    }

    private BikeDTO mapToDto(final Bike bike) {
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
}
