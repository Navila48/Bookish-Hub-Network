package com.tasnuva.book.book;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookResponse {

    private Integer bookId;
    private String title;
    private String author;
    private String isbn;
    private String synopsis;
    private byte[] bookCover;
    private String owner;
    private double rate;
    private boolean sharable;
    private boolean archived;
}
