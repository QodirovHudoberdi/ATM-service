package com.company.service;

import com.company.dto.request.CardReqDto;
import com.company.dto.request.FillCardReqDto;
import com.company.dto.request.PinflReqDto;
import com.company.dto.request.TransferReqDto;
import com.company.dto.response.CardResDto;
import com.company.dto.response.HistoryWithAtmResDto;
import com.company.dto.response.TransferResDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CardService {
    CardResDto createCard(CardReqDto cardReqDto, HttpServletRequest httpServletRequest);

    List<CardResDto> getByPinfl(PinflReqDto pinfl);

    HistoryWithAtmResDto fillCard(FillCardReqDto cardReqDto, HttpServletRequest httpServletRequest);

    CardResDto blockCard(CardReqDto cardReqDto, HttpServletRequest httpServletRequest);

    CardResDto updateCardPin(CardReqDto cardReqDto, HttpServletRequest httpServletRequest);

    TransferResDto transferToCard(TransferReqDto transferReqDto, HttpServletRequest httpServletRequest);
}
