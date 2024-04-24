package com.company.dto.request;

import lombok.Data;

@Data
public class HistoryReqDto {
    private String cardNumber;
    private String pinCode;
    private String startDate;
    private String endDate;
}
