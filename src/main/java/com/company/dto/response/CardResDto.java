package com.company.dto.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CardResDto {
    private Long id;
    private CardHolderResDto cardHolder;
    private LocalDate expiredDate;
    private Float balance;
    private Boolean isActive;
    private String cardNumber;
    private CardTypeResDto cardType;
    private String pinCode;
}
