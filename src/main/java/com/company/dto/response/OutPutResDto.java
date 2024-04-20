package com.company.dto.response;

import com.company.dto.response.model.HistoryOutputModel;
import lombok.Data;

import java.util.List;
@Data
public class OutPutResDto {
    private CardResDto card;
    private List<HistoryOutputModel> transfers;
}
