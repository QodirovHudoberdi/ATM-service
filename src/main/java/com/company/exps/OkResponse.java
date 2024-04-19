package com.company.exps;

public class OkResponse extends RuntimeException {
    public OkResponse(String message) {
        super(message);
    }
}