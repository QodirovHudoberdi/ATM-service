package com.company.controller;

import com.company.dto.request.*;
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
    public ResponseEntity<?> createCard(@RequestBody CardReqDto cardReqDto, HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(cardService.createCard(cardReqDto,httpServletRequest));
    }
    @GetMapping("/byPinfl")
    public ResponseEntity<?> getByPinfl(@RequestBody PinflReqDto reqDto, HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(cardService.getByPinfl(reqDto,httpServletRequest));
    }
   @PutMapping("/fill")
    public ResponseEntity<?> fillCard(@RequestBody FillCardReqDto cardReqDto, HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(cardService.fillCard(cardReqDto,httpServletRequest));
    }
    @PutMapping("block")
    public ResponseEntity<?> blockCard(@RequestBody CardReqDto cardReqDto,HttpServletRequest httpServletRequest){
        return ResponseEntity.ok(cardService.blockCard(cardReqDto,httpServletRequest));
    }
    @PutMapping("updatePIn")
    public ResponseEntity<?> updateCardPIn(@RequestBody CardReqDto cardReqDto,HttpServletRequest httpServletRequest){
        return ResponseEntity.ok(cardService.updateCardPin(cardReqDto,httpServletRequest));
    }
    @PutMapping("transfer")
    public ResponseEntity<?> transfer(@RequestBody TransferReqDto transferReqDto, HttpServletRequest httpServletRequest){
        return ResponseEntity.ok(cardService.transferToCard(transferReqDto,httpServletRequest));
    }
    @GetMapping("sendHistory")
    public ResponseEntity<?> historySend(@RequestBody HistoryReqDto historyReqDto,HttpServletRequest httpServletRequest){
        return ResponseEntity.ok(cardService.getSendHistory(historyReqDto,httpServletRequest));
    }
    @GetMapping("receiveHistory")
    public ResponseEntity<?> historyReceive(@RequestBody HistoryReqDto historyReqDto,HttpServletRequest httpServletRequest){
        return ResponseEntity.ok(cardService.getReceiveHistory(historyReqDto,httpServletRequest));
    }
    @PostMapping("cashing")
    public  ResponseEntity<?> cashingFromAtm(@RequestBody CashingReqDto cashingReqDto , HttpServletRequest httpServletRequest){
        return ResponseEntity.ok(cardService.cashingFromAtm(cashingReqDto,httpServletRequest));
    }
}
