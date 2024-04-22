package com.company.dto.response;

import lombok.Data;

@Data
public class BankNoteResDto {
    private Long id;
    private String name;
    private Integer amount;
    private CurrencyResDto currency;
    private Integer count;
    private BankNoteTypeResDto type;
}
