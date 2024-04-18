package com.company.service;

import com.company.dto.request.CardReqDto;
import com.company.dto.request.PinflReqDto;
import com.company.dto.response.CardResDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CardService {
    CardResDto createCard(CardReqDto cardReqDto, HttpServletRequest httpServletRequest);

    List<CardResDto> getByPinfl(PinflReqDto pinfl);
}
