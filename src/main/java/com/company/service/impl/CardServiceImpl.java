package com.company.service.impl;

import com.company.dto.request.CardReqDto;
import com.company.dto.request.PinflReqDto;
import com.company.dto.response.CardHolderResDto;
import com.company.dto.response.CardResDto;
import com.company.entity.Card;
import com.company.entity.CardHolder;
import com.company.entity.CardType;
import com.company.exps.NotAllowedExceptions;
import com.company.exps.NotFoundException;
import com.company.mapping.CardHolderMapper;
import com.company.mapping.CardMapper;
import com.company.mapping.CardTypeMapper;
import com.company.repository.CardHolderRepository;
import com.company.repository.CardRepository;
import com.company.repository.CardTypeRepository;
import com.company.service.CardService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@RequiredArgsConstructor
@Service
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final CardTypeRepository cardTypeRepository;
    private final CardHolderRepository cardHolderRepository;
    private final CardHolderMapper cardholderMapping;
    private final CardMapper cardMapping;
    private final CardTypeMapper cardTypeMapper;

    @Override
    public CardResDto createCard(CardReqDto cardReqDto, HttpServletRequest httpServletRequest) {
        Optional<CardHolder> cardHolderById = cardHolderRepository.findById(cardReqDto.getCardholderId());
        if (cardHolderById.isEmpty()) {
            throw new NotFoundException("This Card Holder Not Found ");
        }
        CardHolder holder = cardHolderById.get();
        if (!holder.getIsActive()) {
            throw new NotAllowedExceptions("This User Not Create New Card because of Blocked");
        }
        CardResDto cardResDto = new CardResDto();
        CardHolderResDto dto = cardholderMapping.toDto(holder);

        Optional<CardType> cardTypeById = cardTypeRepository.findById(cardReqDto.getCardTypeId());
        if (cardTypeById.isEmpty()) {
            throw new NotFoundException("This Card Type Not Found");
        }
        CardType cardType = cardTypeById.get();
        cardResDto.setCardNumber(cardType.getBeginCardNumber() + ThreadLocalRandom.current().nextInt(10000000, 100000000));
        cardResDto.setCardHolder(dto);

        Card card = new Card();
        card.setCardType(cardType);
        card.setCardHolder(holder);
        card.setCardNumber(cardResDto.getCardNumber());
        card.setExpiredDate(LocalDate.now().plusYears(cardType.getExpiredYears()));
        cardRepository.save(card);
        cardResDto.setBalance(card.getBalance());
        cardResDto.setExpiredDate(card.getExpiredDate());
        cardResDto.setId(card.getId());
        cardResDto.setCardType(cardTypeMapper.toDto(cardType));
        cardResDto.setIsActive(card.getIsActive());

        return cardResDto;
    }
    @Override
    public List<CardResDto> getByPinfl(PinflReqDto pinfl){
        List<Card> allByCardHolderPinfl = cardRepository.getAllByCardHolder_Pinfl(pinfl.getPinfl());
        return  cardMapping.toDto(allByCardHolderPinfl);
    }
}
