package com.company.dto.request;

import lombok.Data;

@Data
public class CardHolderReqDto {
    private String firstName;
    private String lastName;
    private String middleName;
    private String passport;
    private String number;
    private String pinfl;
    private String birthDay;
}
