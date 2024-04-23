package com.company.service;

import com.company.dto.request.HistoryReqDto;
import com.company.dto.response.HistoryWithAtmResDto;
import com.company.dto.response.InPutResDto;
import com.company.dto.response.OutPutResDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface HistoryService {

    OutPutResDto getSendHistory(HistoryReqDto historyReqDto, HttpServletRequest httpServletRequest);

    InPutResDto getReceiveHistory(HistoryReqDto historyReqDto, HttpServletRequest httpServletRequest);

    List<HistoryWithAtmResDto> getAtmHistory(HistoryReqDto historyReqDto, HttpServletRequest httpServletRequest);
}