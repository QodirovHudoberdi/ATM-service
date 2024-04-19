package com.company.dto.response;

import lombok.Data;

@Data
public class CardTypeResDto {
    private Long id;
    private String name;
    private String beginCardNumber;
    private Boolean isActive;
    private Integer expiredYears;
    private CurrencyResDto currency;
}