package com.company.dto.request;

import lombok.Data;

@Data
public class FillCardReqDto {
    private String cardNumber;
    private Float amount;
    private String pinCode;
    private Long currencyId;
}
