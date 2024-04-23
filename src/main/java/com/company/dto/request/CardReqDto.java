package com.company.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
public class CardReqDto {

    private Long cardholderId;
    private Long cardTypeId;

    @Size(min = 4, max = 4, message = "pin code must be 4 number")
    private String pinCode;
    private String cardNumber;

}
