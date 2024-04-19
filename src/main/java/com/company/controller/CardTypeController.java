package com.company.controller;

import com.company.dto.request.CardTypeReqDto;
import com.company.service.CardtypeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("cardtype")
public class CardTypeController {
    private final CardtypeService cardtypeService;

    @PostMapping("/createCardType")
    public ResponseEntity<?> createCardType(@RequestBody CardTypeReqDto cardTypeReqDto, HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(cardtypeService.createCardType(cardTypeReqDto, httpServletRequest));
    }

    @GetMapping("/getAllCardType")
    public ResponseEntity<?> getAllCardtype(HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(cardtypeService.getAllCardType(httpServletRequest));
    }
    @PutMapping("/updateCardType")
    public ResponseEntity<?> updateCardType(@RequestParam ("name") String name ,
                                            @RequestBody CardTypeReqDto cardTypeReqDto ,
                                            HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(cardtypeService.updateCardType(name,cardTypeReqDto , httpServletRequest));
    }
    @DeleteMapping("/updateCardType")
    public ResponseEntity<?> deleteCardType(@RequestParam ("name") String name ,
                                            HttpServletRequest httpServletRequest) {
        cardtypeService.deleteCardType(name, httpServletRequest);
        return ResponseEntity.ok().build();
    }
}