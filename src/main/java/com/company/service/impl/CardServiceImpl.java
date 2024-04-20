package com.company.service.impl;

import com.company.dto.request.TransferReqDto;
import com.company.dto.response.TransferResDto;
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
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
        card.setCount(3);
        card.setIsActive(true);
        cardRepository.save(card);

        cardResDto.setBalance(card.getBalance());
        cardResDto.setExpiredDate(card.getExpiredDate());
        cardResDto.setId(card.getId());
        cardResDto.setCardType(cardTypeMapper.toDto(cardType));
        cardResDto.setIsActive(card.getIsActive());
        cardResDto.setPinCode(card.getPinCode());
        cardResDto.setCount(card.getCount());
        return cardResDto;
    }

    @Override
    public List<CardResDto> getByPinfl(PinflReqDto pinfl) {
        List<Card> allByCardHolderPinfl = cardRepository.getAllByCardHolder_Pinfl(pinfl.getPinfl());
        return cardMapping.toDto(allByCardHolderPinfl);
    }

    @Override
    public HistoryWithAtmResDto fillCard(FillCardReqDto fillCardReqDto, HttpServletRequest httpServletRequest) {
        Card byCardNumber = cardRepository.findByCardNumber(fillCardReqDto.getCardNumber());
        if (validate(fillCardReqDto.getCurrencyId(), byCardNumber)) {
            throw new NotAllowedExceptions("Currency is WRONG");
        }
        if (!byCardNumber.getPinCode().equals(fillCardReqDto.getPinCode())) {
            if (validatePinCode(byCardNumber)) {
                throw new NotAllowedExceptions("Pin Code is Wrong");
            } else throw new NotAllowedExceptions("Card is Blocked");
        }
        byCardNumber.setBalance(byCardNumber.getBalance() + 99 * fillCardReqDto.getAmount() / 100);
        cardRepository.save(byCardNumber);

        CardResDto cardResDto = cardMapping.toDto(byCardNumber);

        HistoryCard historyCard = new HistoryCard();
        historyCard.setFromCard(byCardNumber);
        historyCard.setAmount(fillCardReqDto.getAmount());
        historyCard.setCommission(99 * fillCardReqDto.getAmount() / 100);
        historyCardRepository.save(historyCard);

        HistoryWithAtmResDto dto = new HistoryWithAtmResDto();
        dto.setCommission(fillCardReqDto.getAmount() / 100);
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

    @Override
    public CardResDto updateCardPin(CardReqDto cardReqDto, HttpServletRequest httpServletRequest) {
        Card byCardNumber = cardRepository.findByCardNumber(cardReqDto.getCardNumber());

        cardMapping.updateFromDto(cardReqDto, byCardNumber);
        cardRepository.save(byCardNumber);
        return cardMapping.toDto(byCardNumber);
    }
    @Override
    public TransferResDto transferToCard(TransferReqDto transferReqDto, HttpServletRequest httpServletRequest){
        Card fromCard = cardRepository.findByCardNumber(transferReqDto.getFromCard());
        Card toCard = cardRepository.findByCardNumber(transferReqDto.getToCard());
        if (fromCard==null){
            throw new NotFoundException("Sender Card is Not Found");
        }
        if (validate(transferReqDto.getCurrencyId(), fromCard)){
            throw new NotAllowedExceptions("Currency is WRONG on Sender Card");
        }
        if (validate(transferReqDto.getCurrencyId(), toCard)){
            throw new NotAllowedExceptions("Currency is WRONG on Receive Card");
        }
        if (!fromCard.getPinCode().equals(transferReqDto.getPinCode())) {
            if (validatePinCode(fromCard)) {
                throw new NotAllowedExceptions("Pin Code is Wrong");
            } else throw new NotAllowedExceptions("Card is Blocked");
        }
        if (toCard==null){
            throw new NotFoundException("Receive card is not Found ");
        }
        if (fromCard.getBalance()*1.01<transferReqDto.getAmount()){
            throw new NotAllowedExceptions("Not enough money on sender card");
        }
        fromCard.setBalance(fromCard.getBalance()-transferReqDto.getAmount()*1.01f);
        cardRepository.save(fromCard);

        toCard.setBalance(toCard.getBalance()+transferReqDto.getAmount());
        cardRepository.save(toCard);

        HistoryCard historyCard=new HistoryCard();
        historyCard.setToCard(toCard);
        historyCard.setFromCard(fromCard);
        historyCard.setAmount(transferReqDto.getAmount());
        historyCard.setCommission(transferReqDto.getAmount()/100);
        historyCard.setLocalDateTime(LocalDateTime.now());
        historyCardRepository.save(historyCard);

        TransferResDto transferResDto=new TransferResDto();
        transferResDto.setAmount(transferReqDto.getAmount());
        transferResDto.setDate(LocalDateTime.now());
        transferResDto.setCommission(transferReqDto.getAmount()/100);
        transferResDto.setToCard(cardMapping.toDto(toCard));
        transferResDto.setFromCard(cardMapping.toDto(fromCard));
        transferResDto.setId(historyCard.getId());
        return transferResDto;
    }

    /**
     *TO BE Continued
     */
    Boolean validate(Long currencyId, Card byCardNumber) {
        if (byCardNumber == null) {
            throw new NotFoundException("Card Number is Wrong");
        }
        return !byCardNumber.getCardType().getCurrency().getId().equals(currencyId);
    }

    Boolean validatePinCode(Card card) {
        if (card.getCount() > 0) {
            card.setCount(card.getCount() - 1);
            cardRepository.save(card);
            return true;
        } else {
            card.setIsActive(false);
            cardRepository.save(card);
            return false;
        }
    }
}