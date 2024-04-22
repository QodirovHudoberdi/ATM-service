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
    /**
     * Adds a new currency to the system.
     *
     * @param currencyReqDto        The request DTO containing the currency details.
     * @param httpServletRequest   The HTTP servlet request object.
     * @return                     The response DTO containing the newly added currency details.
     * @throws AlreadyExistException    If a currency with the same name already exists.
     */
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
    /**
     * Retrieves all currencies in the system.
     *
     * @param httpServletRequest   The HTTP servlet request object.
     * @return                     A list of response DTOs containing the details of all currencies.
     */
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
