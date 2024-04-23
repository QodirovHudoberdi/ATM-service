package com.company.service;

import com.company.dto.request.BankNoteReqDto;
import com.company.dto.response.BankNoteResDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public interface BankNoteService {
    BankNoteResDto createBankNote(BankNoteReqDto bankNoteReqDto, HttpServletRequest httpServletRequest);

    void deleteBankNote(Integer amount, HttpServletRequest httpServletRequest);
}
