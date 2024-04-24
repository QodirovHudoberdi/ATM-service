package com.company.service;

import com.company.entity.Card;
import com.company.exps.NotFoundException;
import com.company.mapping.CardHolderMapper;
import com.company.mapping.CardMapper;
import com.company.mapping.CardTypeMapper;
import com.company.repository.*;
import com.company.service.impl.NetworkDataService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidateService {
    private final CardRepository cardRepository;
    private final CardTypeRepository cardTypeRepository;
    private final CardHolderRepository cardHolderRepository;
    private final CardHolderMapper cardholderMapping;
    private final CardMapper cardMapping;
    private final CardTypeMapper cardTypeMapper;
    private final HistoryCardRepository historyCardRepository;
    private final BankNoteRepository bankNoteRepository;
    private final static Logger LOG = LoggerFactory.getLogger(Card.class);
    private final NetworkDataService networkDataService;


    /**
     * Validates if the card's currency matches the specified currency ID.
     *
     * @param currencyId   The ID of the currency to validate against.
     * @param byCardNumber The card to validate.
     * @return {@code true} if the card's currency does not match the specified currency ID, {@code false} otherwise.
     * @throws NotFoundException If the card number is incorrect.
     */
   public Boolean validate(Card byCardNumber,Long currencyId) {
        if (byCardNumber == null) {
            throw new NotFoundException("Card Number is Wrong");
        }
        return !currencyId.equals(byCardNumber.getCardType().getCurrency().getId());
    }
    public void validate1(Card byCardNumber) {
        if (byCardNumber == null) {
            throw new NotFoundException("Card Number is Wrong");
        }
    }

    public Boolean validation(Card card) {
        if (card == null) {
            throw new NotFoundException("Card Number is Wrong");
        }
        if (!card.getCardType().getCurrency().getId().equals(card.getCardType().getCurrency().getId())) {
            throw new NotFoundException("Currency not found");
        } else return true;

    }

    /**
     * Validates the PIN code of a card and updates the card's count and status accordingly.
     *
     * @param card The card to validate the PIN code for.
     * @return {@code true} if the PIN code is correct and the card's count is decremented, {@code false} otherwise.
     */
    public Boolean validatePinCode(Card card) {
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
