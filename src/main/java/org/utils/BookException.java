package org.utils;

public class BookException extends Exception {
    public BookException(String message) {
        super(message);
    }

    public String getMessage() {
        return String.format("Failed to write book: %s", super.getMessage());
    }
}
