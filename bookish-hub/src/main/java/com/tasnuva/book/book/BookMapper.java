package com.tasnuva.book.book;

import com.tasnuva.book.bookTransactionHistory.BookTransactionHistory;
import com.tasnuva.book.file.FileUtils;
import org.springframework.stereotype.Service;

@Service
public class BookMapper {
    public Book convertToBook(BookRequest bookRequest) {

        return Book.builder()
                .id(bookRequest.id())
                .title(bookRequest.title())
                .author(bookRequest.authorName())
                .isbn(bookRequest.isbn())
                .synopsis(bookRequest.synopsis())
                .archived(false)
                .sharable(bookRequest.shareable())
                .build();
    }

    public BookResponse convertToBookResponse(Book book) {
        return BookResponse.builder()
                .bookId(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .isbn(book.getIsbn())
                .synopsis(book.getSynopsis())
                .owner(book.getOwner().fullName())
                .bookCover(FileUtils.readFileFromLocation(book.getBookCover()))
                .rate(book.getRate())
                .archived(book.isArchived())
                .sharable(book.isSharable())
                .build();
    }

    public BookTransactionResponse convertToBorrowedBookResponse(BookTransactionHistory transactionHistory) {
        return BookTransactionResponse.builder()
                .bookId(transactionHistory.getBook().getId())
                .title(transactionHistory.getBook().getTitle())
                .author(transactionHistory.getBook().getAuthor())
                .isbn(transactionHistory.getBook().getIsbn())
                .returned(transactionHistory.isReturned())
                .returnApproved(transactionHistory.isReturnApproved())
                .rate(transactionHistory.getBook().getRate())
                .build();
    }
}
