package com.tasnuva.book.book;

public class OperationNoPermittedException extends RuntimeException {
    public OperationNoPermittedException(String msg) {
        super(msg);
    }
}
