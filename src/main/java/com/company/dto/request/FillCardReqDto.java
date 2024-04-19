package com.company.dto.request;

import lombok.Data;

@Data
public class FillCardReqDto {
    private String cardNumber;
    private Float Amount;
    private String pinCode;
    private Long currencyId;
}
