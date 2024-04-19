package com.company.service;

import com.company.dto.request.CurrencyReqDto;
import com.company.dto.response.CurrencyResDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CurrencyService {
    CurrencyResDto addCurrency(CurrencyReqDto currencyReqDto, HttpServletRequest httpServletRequest);

    List<CurrencyResDto> getAllCurrencies(HttpServletRequest httpServletRequest);
}
