package com.userService.controller;

import com.userService.dto.FeedbackDTO;
import com.userService.dto.ResponseDTO;
import com.userService.service.FeedbackService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/feedbacks")
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping("/create")
    public ResponseDTO create(@RequestBody FeedbackDTO feedbackDTO) {
        return this.feedbackService.create(feedbackDTO);
    }

    @GetMapping("/retrieve")
    public ResponseDTO retrieve() {
        return this.feedbackService.retrieveAll();
    }

    @GetMapping("/retrieve/{id}")
    public ResponseDTO retrieveById(@PathVariable("id") String id) {
        return this.feedbackService.retrieveById(id);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseDTO delete(@PathVariable("id") String id) {
        return this.feedbackService.delete(id);
    }
}
