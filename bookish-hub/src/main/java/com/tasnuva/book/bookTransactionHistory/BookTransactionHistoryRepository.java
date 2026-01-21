package com.tasnuva.book.bookTransactionHistory;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BookTransactionHistoryRepository extends JpaRepository<BookTransactionHistory, Integer> {

    @Query("""
        SELECT history
        FROM BookTransactionHistory history
        WHERE history.userId = :userId
    """)
    Page<BookTransactionHistory> findAllBorrowedBooks(Pageable pageable, String userId);

    @Query("""
        SELECT history
        FROM BookTransactionHistory history
        WHERE history.book.createdBy = :userName 
    """)
    Page<BookTransactionHistory> findAllReturnedBooks(Pageable pageable, String userName);

    @Query("""
        SELECT (count(*)>0) AS isBorrowed
        FROM BookTransactionHistory transactionHistory
        WHERE transactionHistory.book.id = :bookId
        AND transactionHistory.returnApproved = false
    """)
    boolean isAlreadyBorrowed(@Valid Integer bookId, String userId);

    @Query("""
        SELECT count(*)>0
        FROM BookTransactionHistory transactionHistory
        WHERE transactionHistory.book.id = :bookId
        AND transactionHistory.userId = :userId
        AND transactionHistory.returnApproved = false
    """)
    boolean isAlreadyBorrowedByUser(@Valid Integer bookId, String userId);

    @Query("""
        SELECT transactionHistory
        FROM BookTransactionHistory transactionHistory
        WHERE transactionHistory.userId = :userId
        AND transactionHistory.book.id = :bookId
        AND transactionHistory.returned = false
        AND transactionHistory.returnApproved = false
    """)
    Optional<BookTransactionHistory> findByBookIdAndUserId(@Valid Integer bookId, String userId);

    @Query("""
        SELECT transactionHistory
        FROM BookTransactionHistory transactionHistory
        WHERE transactionHistory.book.createdBy = :userId
        AND transactionHistory.book.id = :bookId
        AND transactionHistory.returned = true
        AND transactionHistory.returnApproved = false
    """)
    Optional<BookTransactionHistory> findByBookIdAndOwnerId(Integer bookId, String userId);
}
