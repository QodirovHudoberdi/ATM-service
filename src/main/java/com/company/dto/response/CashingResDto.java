package com.company.dto.response;

import lombok.Data;

@Data
public class CashingResDto {
    private CardResDto card;
    private Float amount ;
    private Float commission;
}
