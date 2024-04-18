package com.company.service.impl;

import com.company.dto.request.CardTypeReqDto;
import com.company.dto.response.CardTypeResDto;
import com.company.entity.CardType;
import com.company.exps.AlreadyExistException;
import com.company.mapping.CardTypeMapper;
import com.company.repository.CardTypeRepository;
import com.company.service.CardtypeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CardTypeServiceImpl implements CardtypeService {


    private final CardTypeMapper cardTypeMapper;
    private final CardTypeRepository cardTypeRepository;

    @Override
    public CardTypeResDto createCardType(CardTypeReqDto cardTypeReqDto, HttpServletRequest httpServletRequest) {
        cardTypeReqDto.setName(cardTypeReqDto.getName().toUpperCase());
        Optional<CardType> cardTypeByName = cardTypeRepository.findByName(cardTypeReqDto.getName().toUpperCase());
        if (cardTypeByName.isPresent()) {
            throw new AlreadyExistException("This Card type Already Have Our Bank");
        }
        CardType cardtype = cardTypeMapper.toEntity(cardTypeReqDto);
        cardTypeRepository.save(cardtype);
       return cardTypeMapper.toDto(cardtype);
    }

}
