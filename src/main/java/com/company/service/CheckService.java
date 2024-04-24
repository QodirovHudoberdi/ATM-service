package com.company.service;

import com.company.exps.advice.ControllerAdvice;
import com.company.service.impl.NetworkDataService;
import jakarta.servlet.http.HttpServletRequest;


import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class CheckService {
    private final static Logger LOG = LoggerFactory.getLogger(ControllerAdvice.class);
    private  final NetworkDataService networkDataService;

    public void checkDevice(HttpServletRequest httpServletRequest) {
        String ClientInfo = networkDataService.getClientIPv4Address(httpServletRequest);
        String ClientIP = networkDataService.getRemoteUserInfo(httpServletRequest);
        LOG.info("Client host : \t\t {}", ClientInfo);
        LOG.info("Client IP :  \t\t {}", ClientIP);
    }


}
