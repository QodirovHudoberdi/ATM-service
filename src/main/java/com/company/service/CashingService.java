package com.company.service;

import com.company.dto.request.CashingReqDto;
import com.company.dto.response.CashingResDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public interface CashingService {

    CashingResDto cashingFromAtm(CashingReqDto cashingReqDto, HttpServletRequest httpServletRequest);
}
