package com.company.dto.response;

import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HistoryWithAtmResDto {
    private Long id;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Tashkent")
    private LocalDateTime date;
    private Float amount;
    private Float commission;


    private String senderCardHolder;


    private String senderCard;


    private String receiveCardHolder;


    private String receiveCard;
}


