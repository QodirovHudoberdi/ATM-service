package com.company.dto.request;

import lombok.Data;

@Data
public class BankNoteReqDto {
    private String name;
    private Integer amount;
    private Long currencyId;
    private Integer count;
    private Long typeId;
}
