package com.company.controller;

import com.company.dto.request.BankNoteReqDto;
import com.company.dto.request.CashingReqDto;
import com.company.service.BankNoteService;
import com.company.service.CashingService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("cashing")
    public class CashingController {
    private final CashingService cashingService;

    @PostMapping("cashing")
    public  ResponseEntity<?> cashingFromAtm(@RequestBody CashingReqDto cashingReqDto , HttpServletRequest httpServletRequest){
        return ResponseEntity.ok(cashingService.cashingFromAtm(cashingReqDto,httpServletRequest));
    }
}
