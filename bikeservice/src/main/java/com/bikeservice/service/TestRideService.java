package com.bikeservice.service;

import com.bikeservice.dto.ResponseDTO;
import com.bikeservice.dto.TestRideDTO;
import com.bikeservice.exception.BadRequestServiceException;
import com.bikeservice.repository.TestRideRepository;
import com.bikeservice.util.Constant;
import com.common.entity.TestRide;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TestRideService {

    private final TestRideRepository testRideRepository;

    public TestRideService(final TestRideRepository testRideRepository) {
        this.testRideRepository = testRideRepository;
    }

    public ResponseDTO create(final TestRide testRide) {

         Object email = SecurityContextHolder.getContext().getAuthentication();
         System.out.println(email);
//        testRide.setCreatedBy(email);
        final TestRide saved = this.testRideRepository.save(testRide);
        return new ResponseDTO(HttpStatus.CREATED.value(), Constant.CREATE, testRide);
    }

    public ResponseDTO retrieve() {
        final List<TestRideDTO> rides = this.testRideRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return new ResponseDTO(HttpStatus.OK.value(), Constant.RETRIEVE, rides);
    }

    public ResponseDTO retrieveById(final String id) {
        final TestRide ride = this.testRideRepository.findById(id)
                .orElseThrow(() -> new BadRequestServiceException(Constant.ID_DOES_NOT_EXIST + id));
        return new ResponseDTO(HttpStatus.OK.value(), Constant.RETRIEVE, mapToDto(ride));
    }

    public ResponseDTO update(final String id, final TestRideDTO dto) {
        final TestRide existing = this.testRideRepository.findById(id)
                .orElseThrow(() -> new BadRequestServiceException(Constant.ID_DOES_NOT_EXIST + id));

        existing.setCustomerId(dto.getCustomerId());
        existing.setBikeId(dto.getBikeId());
        existing.setShowroomId(dto.getShowroomId());
        existing.setScheduledTime(dto.getScheduledTime());
        existing.setStatus(dto.getStatus());
        existing.setFeedback(dto.getFeedback());

        final TestRide updated = this.testRideRepository.save(existing);
        return new ResponseDTO(HttpStatus.OK.value(), Constant.UPDATE, mapToDto(updated));
    }

    public ResponseDTO delete(final String id) {
        if (!testRideRepository.existsById(id)) {
            throw new BadRequestServiceException(Constant.ID_DOES_NOT_EXIST + id);
        }
        this.testRideRepository.deleteById(id);
        return new ResponseDTO(HttpStatus.OK.value(), Constant.DELETE, null);
    }

    private TestRideDTO mapToDto(final TestRide tr) {
        final TestRideDTO dto = new TestRideDTO();
        dto.setId(tr.getId());
        dto.setCustomerId(tr.getCustomerId());
        dto.setBikeId(tr.getBikeId());
        dto.setShowroomId(tr.getShowroomId());
        dto.setScheduledTime(tr.getScheduledTime());
        dto.setStatus(tr.getStatus());
        dto.setFeedback(tr.getFeedback());
        return dto;
    }
}
