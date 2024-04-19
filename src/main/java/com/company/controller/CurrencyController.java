package com.company.controller;

import com.company.dto.request.CurrencyReqDto;
import com.company.service.CurrencyService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("currency")
public class CurrencyController {
    private final CurrencyService currencyService;
    @PostMapping("/createCurr")
    public ResponseEntity<?> addCurrency(@RequestBody CurrencyReqDto currencyReqDto, HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(currencyService.addCurrency(currencyReqDto, httpServletRequest));
    }
    @GetMapping("/getAllCurrencies")
    public ResponseEntity<?> getAllCurrencies(HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(currencyService.getAllCurrencies(httpServletRequest));
    }
}
