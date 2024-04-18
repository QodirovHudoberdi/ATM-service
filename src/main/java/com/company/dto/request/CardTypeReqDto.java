package com.company.dto.request;

import lombok.Data;

@Data
public class CardTypeReqDto {
    private String name;
    private String beginCardNumber;
    private Boolean isActive;
    private Integer expiredYears;
}
