package com.userService.service;

import com.common.entity.Feedback;
import com.userService.dto.FeedbackDTO;
import com.userService.dto.ResponseDTO;
import com.userService.repository.FeedbackRepository;
import com.userService.util.Constant;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;

    public FeedbackService(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    public ResponseDTO create(FeedbackDTO dto) {
        final Feedback feedback = mapToEntity(dto);
        feedback.setSubmittedAt(LocalDateTime.now());
        final Feedback saved = this.feedbackRepository.save(feedback);
        return new ResponseDTO(HttpStatus.CREATED.value(), Constant.CREATE, mapToDto(saved));
    }

    public ResponseDTO retrieveAll() {
        final List<FeedbackDTO> feedbackList = this.feedbackRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return new ResponseDTO(HttpStatus.OK.value(), Constant.RETRIEVE, feedbackList);
    }

    public ResponseDTO retrieveById(String id) {
        final Feedback feedback = this.feedbackRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Feedback not found with id: " + id));
        return new ResponseDTO(HttpStatus.OK.value(), Constant.RETRIEVE, mapToDto(feedback));
    }

    public ResponseDTO delete(String id) {
        if (!feedbackRepository.existsById(id)) {
            throw new RuntimeException("Feedback not found with id: " + id);
        }
        this.feedbackRepository.deleteById(id);
        return new ResponseDTO(HttpStatus.OK.value(), Constant.DELETE, null);
    }

    private Feedback mapToEntity(FeedbackDTO dto) {
        final Feedback feedback = new Feedback();
        feedback.setId(dto.getId());
        feedback.setCustomerId(dto.getCustomerId());
        feedback.setMessage(dto.getMessage());
        feedback.setRating(dto.getRating());
        feedback.setSubmittedAt(dto.getSubmittedAt());
        return feedback;
    }

    private FeedbackDTO mapToDto(Feedback feedback) {
        final FeedbackDTO dto = new FeedbackDTO();
        dto.setId(feedback.getId());
        dto.setCustomerId(feedback.getCustomerId());
        dto.setMessage(feedback.getMessage());
        dto.setRating(feedback.getRating());
        dto.setSubmittedAt(feedback.getSubmittedAt());
        return dto;
    }
}
