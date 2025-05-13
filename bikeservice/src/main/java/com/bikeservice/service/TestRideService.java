package com.bikeservice.service;

import com.bikeservice.dto.TestRideDTO;
import com.bikeservice.repository.TestRideRepository;
import com.common.entity.TestRide;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TestRideService {
    private final TestRideRepository testRideRepository;

    public TestRideService(TestRideRepository testRideRepository) {
        this.testRideRepository = testRideRepository;
    }

    public TestRideDTO createTestRide(TestRideDTO dto) {
        TestRide testRide = mapToEntity(dto);
        TestRide saved = testRideRepository.save(testRide);
        return mapToDto(saved);
    }

    public List<TestRideDTO> getAllTestRides() {
        return testRideRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public TestRideDTO getTestRideById(String id) {
        TestRide testRide = testRideRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TestRide not found with id: " + id));
        return mapToDto(testRide);
    }

    public TestRideDTO updateTestRide(String id, TestRideDTO dto) {
        TestRide existing = testRideRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TestRide not found with id: " + id));

        existing.setCustomerId(dto.getCustomerId());
        existing.setBikeId(dto.getBikeId());
        existing.setShowroomId(dto.getShowroomId());
        existing.setScheduledTime(dto.getScheduledTime());
        existing.setStatus(dto.getStatus());
        existing.setFeedback(dto.getFeedback());

        TestRide updated = testRideRepository.save(existing);
        return mapToDto(updated);
    }

    public void deleteTestRide(String id) {
        if (!testRideRepository.existsById(id)) {
            throw new RuntimeException("TestRide not found with id: " + id);
        }
        testRideRepository.deleteById(id);
    }

    private TestRideDTO mapToDto(TestRide tr) {
        TestRideDTO dto = new TestRideDTO();
        dto.setId(tr.getId());
        dto.setCustomerId(tr.getCustomerId());
        dto.setBikeId(tr.getBikeId());
        dto.setShowroomId(tr.getShowroomId());
        dto.setScheduledTime(tr.getScheduledTime());
        dto.setStatus(tr.getStatus());
        dto.setFeedback(tr.getFeedback());
        return dto;
    }

    private TestRide mapToEntity(TestRideDTO dto) {
        TestRide testRide = new TestRide();
        testRide.setId(dto.getId());
        testRide.setCustomerId(dto.getCustomerId());
        testRide.setBikeId(dto.getBikeId());
        testRide.setShowroomId(dto.getShowroomId());
        testRide.setScheduledTime(dto.getScheduledTime());
        testRide.setStatus(dto.getStatus());
        testRide.setFeedback(dto.getFeedback());
        return testRide;
    }
}
