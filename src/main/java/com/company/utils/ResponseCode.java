package com.company.utils;


import lombok.Getter;

@Getter
public enum ResponseCode {

    REQUIRED_DATA_MISSING(400900),
    ;

    private final int code;

    ResponseCode(int code) {
        this.code = code;
    }
}
