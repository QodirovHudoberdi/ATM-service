package com.company.dto.response;

import com.company.exps.ErrorResponse;
import com.company.utils.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseMessage {
    private int code;
    private String message;

    public static ErrorResponse of(ResponseCode responseCode, String message) {
        return new ErrorResponse(responseCode.getCode(), message);
    }
}
