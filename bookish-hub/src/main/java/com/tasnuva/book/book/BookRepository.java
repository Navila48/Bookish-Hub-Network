package com.tasnuva.book.book;

import com.tasnuva.book.common.PageResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> , JpaSpecificationExecutor {

    @Query("""
        SELECT book
        FROM Book book
        WHERE book.archived = false
        AND book.sharable = true
        AND book.owner.id != :userId
    """)
    Page<Book> findAllDisplayableBooks(Pageable pageable, Integer userId);

}
