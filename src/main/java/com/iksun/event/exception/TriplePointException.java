package com.iksun.event.exception;

public class TriplePointException extends RuntimeException {
    String msg;

    public TriplePointException(String msg) {
        super(msg);
    }

}
