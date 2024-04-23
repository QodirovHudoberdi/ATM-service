package com.company.controller;

import com.company.dto.request.BankNoteReqDto;
import com.company.dto.request.CardReqDto;
import com.company.service.BankNoteService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("banknote")
    public class BankNoteController {
    private final BankNoteService bankNoteService;
    @PostMapping("/createBankNote")
    public ResponseEntity<?> createBankNote(@RequestBody BankNoteReqDto bankNoteReqDto, HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(bankNoteService.createBankNote(bankNoteReqDto,httpServletRequest));
    }
    @DeleteMapping("/deleteBankNote")
    public ResponseEntity<?> deleteBankNote(@RequestBody BankNoteReqDto bankNoteReqDto, HttpServletRequest httpServletRequest) {
        bankNoteService.deleteBankNote(bankNoteReqDto.getAmount(),httpServletRequest);
        return ResponseEntity.ok().build();
    }
}
