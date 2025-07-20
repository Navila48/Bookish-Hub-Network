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
        WHERE history.user.id = :userId
    """)
    Page<BookTransactionHistory> findAllBorrowedBooks(Pageable pageable, Integer userId);

    @Query("""
        SELECT history
        FROM BookTransactionHistory history
        WHERE history.book.owner.id = :userId
    """)
    Page<BookTransactionHistory> findAllReturnedBooks(Pageable pageable, Integer userId);

    @Query("""
        SELECT (count(*)>0) AS isBorrowed
        FROM BookTransactionHistory transactionHistory
        WHERE transactionHistory.book.id = :bookId
        AND transactionHistory.returned = false
        OR transactionHistory.returnApproved = false
    """)
    boolean isAlreadyBorrowed(@Valid Integer bookId, Integer userId);

    @Query("""
        SELECT count(*)>0
        FROM BookTransactionHistory transactionHistory
        WHERE transactionHistory.book.id = :bookId
        AND transactionHistory.user.id = :userId
        AND transactionHistory.returned = false
        OR transactionHistory.returnApproved = false
    """)
    boolean isAlreadyBorrowedByUser(@Valid Integer bookId, Integer id);

    @Query("""
        SELECT transactionHistory
        FROM BookTransactionHistory transactionHistory
        WHERE transactionHistory.user.id = :userId
        AND transactionHistory.book.id = :bookId
        AND transactionHistory.returned = false
        AND transactionHistory.returnApproved = false
    """)
    Optional<BookTransactionHistory> findByBookIdAndUserId(@Valid Integer bookId, Integer id);

    @Query("""
        SELECT transactionHistory
        FROM BookTransactionHistory transactionHistory
        WHERE transactionHistory.book.owner.id = :userId
        AND transactionHistory.book.id = :bookId
        AND transactionHistory.returned = true
        AND transactionHistory.returnApproved = false
    """)
    Optional<BookTransactionHistory> findByBookIdAndOwnerId(Integer bookId, Integer userId);
}
