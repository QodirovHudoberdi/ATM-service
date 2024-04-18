package com.company.service;

import com.company.dto.request.CardTypeReqDto;
import com.company.dto.response.CardTypeResDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public interface CardtypeService {
    CardTypeResDto createCardType(CardTypeReqDto cardTypeReqDto, HttpServletRequest httpServletRequest);
}
