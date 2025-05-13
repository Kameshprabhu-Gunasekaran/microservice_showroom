package com.bikeservice.service;

import com.bikeservice.dto.ShowroomDTO;
import com.bikeservice.repository.ShowroomRepository;
import com.common.entity.Showroom;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShowroomService {
    private final ShowroomRepository showroomRepository;

    public ShowroomService(ShowroomRepository showroomRepository) {
        this.showroomRepository = showroomRepository;
    }

    public ShowroomDTO createShowroom(ShowroomDTO dto) {
        Showroom showroom = mapToEntity(dto);
        Showroom saved = showroomRepository.save(showroom);
        return mapToDto(saved);
    }

    public List<ShowroomDTO> getAllShowrooms() {
        return showroomRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public ShowroomDTO getShowroomById(String id) {
        Showroom showroom = showroomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Showroom not found with id: " + id));
        return mapToDto(showroom);
    }

    public ShowroomDTO updateShowroom(String id, ShowroomDTO updatedDto) {
        Showroom existing = showroomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Showroom not found with id: " + id));

        existing.setName(updatedDto.getName());
        existing.setBrand(updatedDto.getBrand());
        existing.setAddress(updatedDto.getAddress());
        existing.setContactNumber(updatedDto.getContactNumber());

        Showroom updated = showroomRepository.save(existing);
        return mapToDto(updated);
    }

    public void deleteShowroom(String id) {
        if (!showroomRepository.existsById(id)) {
            throw new RuntimeException("Showroom not found with id: " + id);
        }
        showroomRepository.deleteById(id);
    }

    private ShowroomDTO mapToDto(Showroom showroom) {
        ShowroomDTO dto = new ShowroomDTO();
        dto.setId(showroom.getId());
        dto.setName(showroom.getName());
        dto.setBrand(showroom.getBrand());
        dto.setAddress(showroom.getAddress());
        dto.setContactNumber(showroom.getContactNumber());
        return dto;
    }

    private Showroom mapToEntity(ShowroomDTO dto) {
        Showroom showroom = new Showroom();
        showroom.setId(dto.getId());
        showroom.setName(dto.getName());
        showroom.setBrand(dto.getBrand());
        showroom.setAddress(dto.getAddress());
        showroom.setContactNumber(dto.getContactNumber());
        return showroom;
    }
}
