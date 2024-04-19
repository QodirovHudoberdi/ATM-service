package com.company.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class HistoryWithAtmResDto {
private Long id;
private CardResDto card;
@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone ="Asia/Tashkent")
private LocalDateTime date;
private Float amount;
private Float commission;
}
