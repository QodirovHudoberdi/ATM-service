package com.company.service;

import com.company.dto.request.CardHolderReqDto;
import com.company.dto.response.CardHolderResDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public interface CardHolderService {

    CardHolderResDto createCardHolder(CardHolderReqDto cardHolderReqDto, HttpServletRequest httpServletRequest);

}
