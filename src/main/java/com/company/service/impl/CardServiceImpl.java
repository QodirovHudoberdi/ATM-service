package com.company.service.impl;

import com.company.dto.request.CardReqDto;
import com.company.dto.request.FillCardReqDto;
import com.company.dto.request.PinflReqDto;
import com.company.dto.response.CardHolderResDto;
import com.company.dto.response.CardResDto;
import com.company.dto.response.HistoryWithAtmResDto;
import com.company.entity.Card;
import com.company.entity.CardHolder;
import com.company.entity.CardType;
import com.company.entity.HistoryCard;
import com.company.exps.NotAllowedExceptions;
import com.company.exps.NotFoundException;
import com.company.mapping.CardHolderMapper;
import com.company.mapping.CardMapper;
import com.company.mapping.CardTypeMapper;
import com.company.repository.CardHolderRepository;
import com.company.repository.CardRepository;
import com.company.repository.CardTypeRepository;
import com.company.repository.HistoryCardRepository;
import com.company.service.CardService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.hibernate.engine.jndi.JndiNameException;
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
    private final HistoryCardRepository historyCardRepository;

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
        cardResDto.setCardNumber(cardType.getBeginCardNumber() +
                ThreadLocalRandom.current().nextInt(10000000, 100000000));
        cardResDto.setCardHolder(dto);

        Card card = new Card();
        card.setCardType(cardType);
        card.setCardHolder(holder);
        card.setCardNumber(cardResDto.getCardNumber());
        card.setExpiredDate(LocalDate.now().plusYears(cardType.getExpiredYears()));
        card.setPinCode(cardReqDto.getPinCode());
        card.setIsActive(true);
        cardRepository.save(card);

        cardResDto.setBalance(card.getBalance());
        cardResDto.setExpiredDate(card.getExpiredDate());
        cardResDto.setId(card.getId());
        cardResDto.setCardType(cardTypeMapper.toDto(cardType));
        cardResDto.setIsActive(card.getIsActive());
        cardResDto.setPinCode(card.getPinCode());

        return cardResDto;
    }
    @Override
    public List<CardResDto> getByPinfl(PinflReqDto pinfl){
        List<Card> allByCardHolderPinfl = cardRepository.getAllByCardHolder_Pinfl(pinfl.getPinfl());
        return  cardMapping.toDto(allByCardHolderPinfl);
    }
    @Override
    public HistoryWithAtmResDto fillCard(FillCardReqDto fillCardReqDto, HttpServletRequest httpServletRequest) {
        Card byCardNumber = cardRepository.findByCardNumber(fillCardReqDto.getCardNumber());
        if (validate(fillCardReqDto.getCurrencyId(),byCardNumber)) {
            throw new NotAllowedExceptions("Currency is WRONG");
        }
        if (!byCardNumber.getPinCode().equals(fillCardReqDto.getPinCode())) {
            throw new NotAllowedExceptions("Pin Code is Wrong");
        }
        byCardNumber.setBalance(byCardNumber.getBalance()+99*fillCardReqDto.getAmount()/100);
        cardRepository.save(byCardNumber);

        CardResDto cardResDto=cardMapping.toDto(byCardNumber);

        HistoryCard historyCard=new HistoryCard();
        historyCard.setFromCard(byCardNumber);
        historyCard.setAmount(fillCardReqDto.getAmount());
        historyCard.setCommission(99*fillCardReqDto.getAmount()/100);
        historyCardRepository.save(historyCard);

        HistoryWithAtmResDto dto=new HistoryWithAtmResDto();
        dto.setCommission(fillCardReqDto.getAmount()/100);
        dto.setAmount(fillCardReqDto.getAmount());
        dto.setCard(cardResDto);
        dto.setDate(historyCard.getLocalDateTime());
        dto.setId(historyCard.getId());

        return dto;
    }
    @Override
    public CardResDto blockCard(CardReqDto cardReqDto, HttpServletRequest httpServletRequest) {
        Card byCardNumber = cardRepository.findByCardNumber(cardReqDto.getCardNumber());
            byCardNumber.setIsActive(!byCardNumber.getIsActive());
            cardRepository.save(byCardNumber);
            return cardMapping.toDto(byCardNumber);
    }

    Boolean validate(Long currencyId , Card byCardNumber){
        if (byCardNumber==null) {
            throw new NotFoundException("Card Number is Wrong");
        }
        return !byCardNumber.getCardType().getCurrency().getId().equals(currencyId);
    }
}