package com.tasnuva.book.book;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookTransactionResponse {

    private Integer bookId;
    private String title;
    private String author;
    private String isbn;

    private boolean returned;
    private boolean returnApproved;
}
