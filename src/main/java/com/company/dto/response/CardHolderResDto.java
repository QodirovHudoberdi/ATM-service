package com.company.dto.response;

import lombok.Data;

@Data
public class CardHolderResDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String middleName;
    private String seriesNum;
    private String pinfl;
    private Boolean isActive;
    private String birthDay;
}
