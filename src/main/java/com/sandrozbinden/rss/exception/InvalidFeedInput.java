package com.sandrozbinden.rss.exception;

public class InvalidFeedInput extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidFeedInput(String message) {
        super(message);
    }

}
