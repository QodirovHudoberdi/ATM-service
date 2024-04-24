package com.company.service.impl;

import com.company.dto.request.CurrencyReqDto;
import com.company.dto.response.CurrencyResDto;
import com.company.entity.Card;
import com.company.entity.Currency;
import com.company.exps.CurrencyException;
import com.company.repository.CurrencyRepository;
import com.company.service.CheckService;
import com.company.service.CurrencyService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CurrencyServiceImpl implements CurrencyService {
    private final CurrencyRepository currencyRepository;
    private final CheckService checkService;
    private final static Logger LOG = LoggerFactory.getLogger(Card.class);

    /**
     * Adds a new currency to the system.
     *
     * @param currencyReqDto     The request DTO containing the currency details.
     * @param httpServletRequest The HTTP servlet request object.
     * @return The response DTO containing the newly added currency details.
     * @throws CurrencyException If a currency with the same name already exists.
     */
    @Override
    public CurrencyResDto addCurrency(CurrencyReqDto currencyReqDto, HttpServletRequest httpServletRequest) {
        try {
            checkService.checkDevice(httpServletRequest);
            Currency currency = currencyRepository.searchByName(currencyReqDto.getName());
            if (currency != null) {
                throw new CurrencyException("This Currency is have already");
            }
            Currency currency1 = new Currency();
            currency1.setName(currencyReqDto.getName());
            LOG.info("Add Currency \t\t {}  ", currencyReqDto);
            currencyRepository.save(currency1);
            return new CurrencyResDto(currency1.getId(), currency1.getName());
        } catch (Exception e) {
            throw new CurrencyException("Can not add Currency  :  " + e.getMessage());
        }
    }

    /**
     * Retrieves all currencies in the system.
     *
     * @param httpServletRequest The HTTP servlet request object.
     * @return A list of response DTOs containing the details of all currencies.
     */
    @Override
    public List<CurrencyResDto> getAllCurrencies(HttpServletRequest httpServletRequest) {
        try {
            List<Currency> all = currencyRepository.findAll();
            List<CurrencyResDto> allCurrencies = new ArrayList<>();
            for (Currency currency : all) {
                CurrencyResDto resDto = new CurrencyResDto(currency.getId(), currency.getName());
                allCurrencies.add(resDto);
            }
            LOG.info("Get All  Currency \t\t {}  ", "");

            return allCurrencies;
        } catch (Exception e) {
            throw new CurrencyException("Can Not get All Currency :  " + e.getMessage());
        }
    }
}
