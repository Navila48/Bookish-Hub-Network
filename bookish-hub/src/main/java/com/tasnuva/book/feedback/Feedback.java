package com.tasnuva.book.feedback;

import com.tasnuva.book.book.Book;
import com.tasnuva.book.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Feedback extends BaseEntity {

    private Double rating;
    private String comment;

    @ManyToOne()
    @JoinColumn(name = "book_id")
    private Book book;
}
