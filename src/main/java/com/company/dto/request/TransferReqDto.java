package com.company.dto.request;

import lombok.Data;

@Data
public class TransferReqDto {
    private String fromCard;
    private Float amount;
    private String pinCode;
    private Long currencyId;
    private String toCard;
}
