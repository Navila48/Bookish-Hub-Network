package com.tasnuva.book.feedback;

import com.tasnuva.book.book.Book;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class FeedbackMapper {

    public Feedback convertToFeedback(FeedbackRequest feedbackRequest) {
        return Feedback.builder()
                .rating(feedbackRequest.rating())
                .comment(feedbackRequest.comment())
                .book(Book.builder()
                        .id(feedbackRequest.bookId())
                        .build())
                .build();
    }

    public FeedbackResponse convertToFeedbackResponse(Feedback feedback, Integer userId) {
        return FeedbackResponse.builder()
                .rating(feedback.getRating())
                .comment(feedback.getComment())
                .ownFeedbacks(Objects.equals(feedback.getCreatedBy(), userId))
                .build();
    }
}
