package com.company.dto.request;

import lombok.Data;

@Data
public class CashingReqDto {
    private String cardNumber;
    private String pinCode;
    private Float amount;
    private Long typeId ;
}
