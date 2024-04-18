package com.company.service.impl;

import com.company.dto.CardHolderReqDto;
import com.company.dto.CardHolderResDto;
import com.company.entity.CardHolder;
import com.company.exps.AlreadyExistException;
import com.company.mapping.CardHolderMapper;
import com.company.repository.CardHolderRepository;
import com.company.service.CardHolderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;


@RequiredArgsConstructor
@Service
public class CardHolderServiceImpl implements CardHolderService {
    private final static Logger LOG = LoggerFactory.getLogger(CardHolder.class);
    private final NetworkDataService networkDataService;
    private final CardHolderMapper cardHolderMapper;
    private final CardHolderRepository cardHolderRepository;
    @Override
    public CardHolderResDto createCardHolder(CardHolderReqDto cardHolderReqDto, HttpServletRequest httpServletRequest) {
        String ClientInfo = networkDataService.getClientIPv4Address(httpServletRequest);
        String ClientIP = networkDataService.getRemoteUserInfo(httpServletRequest);
        LOG.info("Create Card Holder  \t\t {}", cardHolderReqDto);
        LOG.info("Client host : \t\t {}", ClientInfo);
        LOG.info("Client IP :  \t\t {}", ClientIP);

        String seriesNum=cardHolderReqDto.getPassport().toUpperCase()+cardHolderReqDto.getNumber();

        Optional<CardHolder> cardHolderBySeriesNum = cardHolderRepository.findBySeriesNum(seriesNum);
        if (cardHolderBySeriesNum.isPresent()) {
            LOG.info("This Client already with us  \t\t {}", cardHolderReqDto);
            LOG.info("Client host : \t\t {}", ClientInfo);
            LOG.info("Client IP :  \t\t {}", ClientIP);
            throw new AlreadyExistException("This Client already with us");
        }
        CardHolder holder = cardHolderMapper.toEntity(cardHolderReqDto);
        holder.setSeriesNum(seriesNum);
        cardHolderRepository.save(holder);
        return  cardHolderMapper.toDto(holder);
    }
}
