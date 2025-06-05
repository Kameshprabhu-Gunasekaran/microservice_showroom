package com.userService.service;

import com.common.entity.Feedback;
import com.userService.dto.FeedbackDTO;
import com.userService.dto.ResponseDTO;
import com.userService.exception.BadRequestServiceException;
import com.userService.repository.FeedbackRepository;
import com.userService.util.Constant;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;

    public FeedbackService(final FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    public ResponseDTO create(final Feedback feedback) {

        final String email = SecurityContextHolder.getContext().getAuthentication().getName();
        feedback.setCreatedBy(email);
        feedback.setSubmittedAt(LocalDateTime.now());
        final Feedback saved = this.feedbackRepository.save(feedback);
        return new ResponseDTO(HttpStatus.CREATED.value(), Constant.CREATE, feedback);
    }

    public ResponseDTO retrieveAll() {
        final List<FeedbackDTO> feedbackList = this.feedbackRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return new ResponseDTO(HttpStatus.OK.value(), Constant.RETRIEVE, feedbackList);
    }

    public ResponseDTO retrieveById(final String id) {
        final Feedback feedback = this.feedbackRepository.findById(id)
                .orElseThrow(() -> new BadRequestServiceException(Constant.ID_DOES_NOT_EXIST + id));
        return new ResponseDTO(HttpStatus.OK.value(), Constant.RETRIEVE, mapToDto(feedback));
    }

    public ResponseDTO delete(final String id) {
        if (!feedbackRepository.existsById(id)) {
            throw new BadRequestServiceException(Constant.ID_DOES_NOT_EXIST + id);
        }
        this.feedbackRepository.deleteById(id);
        return new ResponseDTO(HttpStatus.OK.value(), Constant.DELETE, null);
    }

    private FeedbackDTO mapToDto(final Feedback feedback) {
        final FeedbackDTO dto = new FeedbackDTO();
        dto.setId(feedback.getId());
        dto.setCustomerId(feedback.getCustomerId());
        dto.setMessage(feedback.getMessage());
        dto.setRating(feedback.getRating());
        dto.setSubmittedAt(feedback.getSubmittedAt());
        return dto;
    }
}
