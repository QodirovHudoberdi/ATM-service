package com.company.service;

import com.company.dto.request.CardTypeReqDto;
import com.company.dto.response.CardTypeResDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CardtypeService {
    CardTypeResDto createCardType(CardTypeReqDto cardTypeReqDto, HttpServletRequest httpServletRequest);

    List<CardTypeResDto> getAllCardType(HttpServletRequest httpServletRequest);

    CardTypeResDto updateCardType(String name, CardTypeReqDto cardTypeReqDto, HttpServletRequest httpServletRequest);


    void deleteCardType(String name, HttpServletRequest httpServletRequest);
}
