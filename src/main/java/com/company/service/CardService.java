package com.company.service;

import com.company.dto.request.*;
import com.company.dto.response.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CardService {
    CardResDto createCard(CardReqDto cardReqDto, HttpServletRequest httpServletRequest);

    List<CardResDto> getByPinfl(PinflReqDto pinfl, HttpServletRequest httpServletRequest);

    HistoryWithAtmResDto fillCard(FillCardReqDto cardReqDto, HttpServletRequest httpServletRequest);

    CardResDto blockCard(CardReqDto cardReqDto, HttpServletRequest httpServletRequest);

    CardResDto updateCardPin(CardReqDto cardReqDto, HttpServletRequest httpServletRequest);

    TransferResDto transferToCard(TransferReqDto transferReqDto, HttpServletRequest httpServletRequest);

    OutPutResDto getSendHistory(HistoryReqDto historyReqDto, HttpServletRequest httpServletRequest);

    InPutResDto getReceiveHistory(HistoryReqDto historyReqDto, HttpServletRequest httpServletRequest);
}
