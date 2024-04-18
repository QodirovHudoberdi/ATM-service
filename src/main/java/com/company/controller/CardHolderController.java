package com.company.controller;

import com.company.dto.CardHolderReqDto;
import com.company.service.CardHolderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("cardholder")
public class CardHolderController {

    private final CardHolderService cardHolderService;

    @PostMapping("/createHolder")
    public ResponseEntity<?> createCardHolder(@RequestBody CardHolderReqDto cardHolderReqDto, HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(cardHolderService.createCardHolder(cardHolderReqDto,httpServletRequest));
    }
}
