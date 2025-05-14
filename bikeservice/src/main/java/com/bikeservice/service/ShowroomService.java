package com.bikeservice.service;

import com.bikeservice.dto.ResponseDTO;
import com.bikeservice.dto.ShowroomDTO;
import com.bikeservice.repository.ShowroomRepository;
import com.bikeservice.util.Constant;
import com.common.entity.Showroom;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShowroomService {

    private final ShowroomRepository showroomRepository;

    public ShowroomService(ShowroomRepository showroomRepository) {
        this.showroomRepository = showroomRepository;
    }

    public ResponseDTO create(ShowroomDTO dto) {
        final Showroom showroom = mapToEntity(dto);
        final Showroom saved = this.showroomRepository.save(showroom);
        return new ResponseDTO(HttpStatus.CREATED.value(), Constant.CREATE, mapToDto(saved));
    }

    public ResponseDTO retrieve() {
        final List<ShowroomDTO> showrooms = this.showroomRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return new ResponseDTO(HttpStatus.OK.value(), Constant.RETRIEVE, showrooms);
    }

    public ResponseDTO retrieveById(String id) {
        final Showroom showroom = this.showroomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Showroom not found with id: " + id));
        return new ResponseDTO(HttpStatus.OK.value(), Constant.RETRIEVE, mapToDto(showroom));
    }

    public ResponseDTO update(String id, ShowroomDTO updatedDto) {
        final Showroom existing = this.showroomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Showroom not found with id: " + id));

        existing.setName(updatedDto.getName());
        existing.setBrand(updatedDto.getBrand());
        existing.setAddress(updatedDto.getAddress());
        existing.setContactNumber(updatedDto.getContactNumber());

        final Showroom updated = this.showroomRepository.save(existing);
        return new ResponseDTO(HttpStatus.OK.value(), Constant.UPDATE, mapToDto(updated));
    }

    public ResponseDTO delete(String id) {
        if (!showroomRepository.existsById(id)) {
            throw new RuntimeException("Showroom not found with id: " + id);
        }
        this.showroomRepository.deleteById(id);
        return new ResponseDTO(HttpStatus.OK.value(), Constant.DELETE, null);
    }

    private ShowroomDTO mapToDto(Showroom showroom) {
        final ShowroomDTO dto = new ShowroomDTO();
        dto.setId(showroom.getId());
        dto.setName(showroom.getName());
        dto.setBrand(showroom.getBrand());
        dto.setAddress(showroom.getAddress());
        dto.setContactNumber(showroom.getContactNumber());
        return dto;
    }

    private Showroom mapToEntity(ShowroomDTO dto) {
        final Showroom showroom = new Showroom();
        showroom.setId(dto.getId());
        showroom.setName(dto.getName());
        showroom.setBrand(dto.getBrand());
        showroom.setAddress(dto.getAddress());
        showroom.setContactNumber(dto.getContactNumber());
        return showroom;
    }
}
