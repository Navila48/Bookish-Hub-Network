package com.tasnuva.book.feedback;

import com.tasnuva.book.book.Book;
import com.tasnuva.book.book.BookRepository;
import com.tasnuva.book.book.OperationNoPermittedException;
import com.tasnuva.book.common.PageResponse;
import com.tasnuva.book.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final BookRepository bookRepository;
    private final FeedbackMapper feedbackMapper;
    private final FeedbackRepository feedbackRepository;

    public Integer saveFeedback(FeedbackRequest feedbackRequest, Authentication connectedUser) {
        Book book = bookRepository.findById(feedbackRequest.bookId()).orElseThrow(()-> new EntityNotFoundException("No book found with this id "+ feedbackRequest.bookId()));
        User user = (User) connectedUser.getPrincipal();
        if(Objects.equals(book.getOwner().getId(), user.getId())) {
            throw new OperationNoPermittedException("You can't post feedback at your own book");
        }

        if(book.isArchived() || !book.isSharable()){
            throw new OperationNoPermittedException("You can't post feedback of a book that is archived or not shareable");
        }
        Feedback feedback = feedbackMapper.convertToFeedback(feedbackRequest);
        return feedbackRepository.save(feedback).getId();
    }

    public PageResponse<FeedbackResponse> findAllFeedbackByBook(int page, int size, Integer bookId, Authentication connectedUser) {
        Pageable pageable = PageRequest.of(page, size);
        User user = (User) connectedUser.getPrincipal();
        Page<Feedback> feedbacks = feedbackRepository.findAllByBookId(bookId, pageable);
        List<FeedbackResponse> feedbackResponses = feedbacks.stream()
                .map( fb -> feedbackMapper.convertToFeedbackResponse(fb, user.getId()))
                .toList();
        return new PageResponse<>(feedbackResponses, feedbacks.getNumber(), feedbacks.getSize(),
                feedbacks.getTotalElements(), feedbacks.getTotalPages(), feedbacks.isFirst(),feedbacks.isLast());
    }
}
