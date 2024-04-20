package com.company.dto.response;

import com.company.dto.response.model.HistoryInPutResModel;
import lombok.Data;

import java.util.List;
@Data
public class InPutResDto {
    private CardResDto card;
    private List<HistoryInPutResModel> receives;
}
