package com.bikeservice.service;

import com.bikeservice.dto.ResponseDTO;
import com.bikeservice.dto.ShowroomDTO;
import com.bikeservice.exception.BadRequestServiceException;
import com.bikeservice.repository.ShowroomRepository;
import com.bikeservice.util.Constant;
import com.common.entity.Showroom;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShowroomService {

    private final ShowroomRepository showroomRepository;

    public ShowroomService(final ShowroomRepository showroomRepository) {
        this.showroomRepository = showroomRepository;
    }

    public ResponseDTO create(final Showroom showroom) {

        final String email = SecurityContextHolder.getContext().getAuthentication().getName();
        showroom.setCreatedBy(email);        final Showroom saved = this.showroomRepository.save(showroom);
        return new ResponseDTO(HttpStatus.CREATED.value(), Constant.CREATE, showroom);
    }

    public ResponseDTO retrieve() {
        final List<ShowroomDTO> showrooms = this.showroomRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return new ResponseDTO(HttpStatus.OK.value(), Constant.RETRIEVE, showrooms);
    }

    public ResponseDTO retrieveById(final String id) {
        final Showroom showroom = this.showroomRepository.findById(id)
                .orElseThrow(() -> new BadRequestServiceException(Constant.ID_DOES_NOT_EXIST + id));
        return new ResponseDTO(HttpStatus.OK.value(), Constant.RETRIEVE, showroom);
    }

    public ResponseDTO update(final String id, final ShowroomDTO updatedDto) {
        final Showroom existing = this.showroomRepository.findById(id)
                .orElseThrow(() -> new BadRequestServiceException(Constant.ID_DOES_NOT_EXIST + id));

        existing.setName(updatedDto.getName());
        existing.setBrand(updatedDto.getBrand());
        existing.setAddress(updatedDto.getAddress());
        existing.setContactNumber(updatedDto.getContactNumber());

        final Showroom updated = this.showroomRepository.save(existing);
        return new ResponseDTO(HttpStatus.OK.value(), Constant.UPDATE, mapToDto(updated));
    }

    public ResponseDTO delete(final String id) {
        if (!showroomRepository.existsById(id)) {
            throw new BadRequestServiceException(Constant.ID_DOES_NOT_EXIST + id);
        }
        this.showroomRepository.deleteById(id);
        return new ResponseDTO(HttpStatus.OK.value(), Constant.DELETE, null);
    }

    private ShowroomDTO mapToDto(final Showroom showroom) {
        final ShowroomDTO dto = new ShowroomDTO();
        dto.setId(showroom.getId());
        dto.setName(showroom.getName());
        dto.setBrand(showroom.getBrand());
        dto.setAddress(showroom.getAddress());
        dto.setContactNumber(showroom.getContactNumber());
        return dto;
    }
}
