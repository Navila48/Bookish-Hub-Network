package com.tasnuva.book.feedback;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FeedbackResponse {
    Double rating;
    String comment;
    boolean ownFeedbacks;
}
