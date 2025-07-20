package com.tasnuva.book.feedback;

import com.tasnuva.book.common.PageResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("feedbacks")
@Tag(name = "Feedbacks")
@RequiredArgsConstructor
public class FeedBackController {

    private final FeedbackService feedbackService;

    @PostMapping
    public ResponseEntity<Integer> saveFeedback(@RequestBody FeedbackRequest feedbackRequest, Authentication connectedUser){
        return ResponseEntity.ok(feedbackService.saveFeedback(feedbackRequest, connectedUser));
    }

    @GetMapping("/book/{book-id}")
    public ResponseEntity<PageResponse<FeedbackResponse>> findAllFeedbackByBook(
            @PathVariable("book-id") Integer bookId,
            @RequestParam( name = "page", defaultValue = "0", required = false) int page,
            @RequestParam( name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser
            ){
        return ResponseEntity.ok(feedbackService.findAllFeedbackByBook(page, size, bookId, connectedUser));
    }
}
