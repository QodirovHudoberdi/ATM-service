package com.company.service.impl;

import com.company.dto.request.CurrencyReqDto;
import com.company.dto.response.CurrencyResDto;
import com.company.entity.Currency;
import com.company.exps.AlreadyExistException;
import com.company.repository.CurrencyRepository;
import com.company.service.CurrencyService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CurrencyServiceImpl implements CurrencyService {
    private final CurrencyRepository currencyRepository;
    @Override
    public CurrencyResDto addCurrency(CurrencyReqDto currencyReqDto, HttpServletRequest httpServletRequest) {
        Currency currency = currencyRepository.searchByName(currencyReqDto.getName());
        if (currency!=null){
            throw new AlreadyExistException("This Currency is have already");
        }
        Currency currency1=new Currency();
        currency1.setName(currencyReqDto.getName());
        currencyRepository.save(currency1);
        return  new CurrencyResDto(currency1.getId(), currency1.getName());
    }
    @Override
    public List<CurrencyResDto> getAllCurrencies(HttpServletRequest httpServletRequest){
        List<Currency> all = currencyRepository.findAll();
        List<CurrencyResDto> allCurrencies=new ArrayList<>();
        for (Currency currency : all) {
            CurrencyResDto resDto=new CurrencyResDto(currency.getId(),currency.getName());
            allCurrencies.add(resDto);
        }
        return allCurrencies;
    }
}
