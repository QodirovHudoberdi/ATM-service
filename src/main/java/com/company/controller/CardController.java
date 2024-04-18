package com.company.controller;

import com.company.dto.request.CardReqDto;
import com.company.dto.request.PinflReqDto;
import com.company.service.CardService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("card")
public class CardController {
private final CardService cardService;
    @PostMapping("/createCard")
    public ResponseEntity<?> createCardHolder(@RequestBody CardReqDto cardReqDto, HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(cardService.createCard(cardReqDto,httpServletRequest));
    }
    @GetMapping("/byPinfl")
    public ResponseEntity<?> getByPinfl(@RequestBody PinflReqDto reqDto, HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(cardService.getByPinfl(reqDto));
    }

}
