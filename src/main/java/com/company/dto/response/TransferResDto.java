package com.company.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransferResDto {
    private Long transferId;
    private Float amount;
    private Float commission;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Tashkent")
    private LocalDateTime date;
    private CardResDto fromCard;
    private CardResDto toCard;
}
