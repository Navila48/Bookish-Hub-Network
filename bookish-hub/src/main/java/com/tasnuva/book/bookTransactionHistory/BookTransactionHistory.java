package com.tasnuva.book.bookTransactionHistory;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.tasnuva.book.book.Book;
import com.tasnuva.book.common.BaseEntity;
import com.tasnuva.book.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class BookTransactionHistory extends BaseEntity {

    private boolean returned;
    private boolean returnApproved;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @Column(name= "user_id")
    private String userId;

//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private User user;

}
