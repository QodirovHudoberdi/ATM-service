package com.company.controller;

import com.company.dto.request.HistoryReqDto;
import com.company.service.HistoryService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("history")
public class HistoryController {
    private final HistoryService historyService;
    @GetMapping("sendHistory")
    public ResponseEntity<?> historySend(@RequestBody HistoryReqDto historyReqDto, HttpServletRequest httpServletRequest){
        return ResponseEntity.ok(historyService.getSendHistory(historyReqDto,httpServletRequest));
    }
    @GetMapping("receiveHistory")
    public ResponseEntity<?> historyReceive(@RequestBody HistoryReqDto historyReqDto,HttpServletRequest httpServletRequest){
        return ResponseEntity.ok(historyService.getReceiveHistory(historyReqDto,httpServletRequest));
    }
    @GetMapping("HistoryCashing")
    public ResponseEntity<?> historyCard(@RequestBody HistoryReqDto historyReqDto,HttpServletRequest httpServletRequest){
        return ResponseEntity.ok(historyService.getAllHistory(historyReqDto,httpServletRequest));
    }
    @GetMapping("historyWithDate")
    public ResponseEntity<?> historyCardWithDate(@RequestBody HistoryReqDto historyReqDto,HttpServletRequest httpServletRequest){
        return ResponseEntity.ok(historyService.getAllHistoryWithDate(historyReqDto,httpServletRequest));
    }
}
