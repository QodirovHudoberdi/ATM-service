package com.company.controller;

import com.company.dto.request.CardTypeReqDto;
import com.company.service.CardtypeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("cardtype")
public class CardTypeController {
private final CardtypeService cardtypeService;
    @PostMapping("/createCardType")
    public ResponseEntity<?> createCardType(@RequestBody CardTypeReqDto cardTypeReqDto, HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(cardtypeService.createCardType(cardTypeReqDto,httpServletRequest));
    }
}
