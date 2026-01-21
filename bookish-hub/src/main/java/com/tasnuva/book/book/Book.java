package com.tasnuva.book.book;

import com.tasnuva.book.bookTransactionHistory.BookTransactionHistory;
import com.tasnuva.book.common.BaseEntity;
import com.tasnuva.book.feedback.Feedback;
import com.tasnuva.book.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Book extends BaseEntity {

    private String title;
    private String author;
    private String isbn;
    private String synopsis;
    private String bookCover;

    private boolean sharable;
    private boolean archived;

    //Relationship
//    @ManyToOne
//    @JoinColumn(name = "owner_id")
//    private User owner;

    @OneToMany(mappedBy = "book")
    List<Feedback> feedbacks;

    @OneToMany(mappedBy = "book")
    List<BookTransactionHistory> bookTransactionHistories;

    @Transient
    public double getRate(){
        if(this.feedbacks == null || feedbacks.isEmpty()){
            return 0.0;
        }
        var rate = this.feedbacks.stream()
                .mapToDouble(Feedback :: getRating)
                .average()
                .orElse(0.0);
        double roundedRate = Math.round(rate * 10.0) / 10.0;
        return roundedRate;
    }

}
