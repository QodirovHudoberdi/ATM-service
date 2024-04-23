package com.company.service.impl;

import com.company.dto.request.*;
import com.company.dto.response.*;
import com.company.dto.response.model.HistoryInPutResModel;
import com.company.dto.response.model.HistoryOutputModel;
import com.company.entity.*;
import com.company.exps.CardException;
import com.company.exps.NotAllowedExceptions;
import com.company.exps.NotFoundException;
import com.company.mapping.CardHolderMapper;
import com.company.mapping.CardMapper;
import com.company.mapping.CardTypeMapper;
import com.company.repository.*;
import com.company.service.CardService;
import com.company.service.ValidateService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private final BankNoteRepository bankNoteRepository;
    private final static Logger LOG = LoggerFactory.getLogger(Card.class);
    private final NetworkDataService networkDataService;
    private final ValidateService validateService;

    /**
     * Creates a new card for a cardHolder.
     *
     * @param cardReqDto         The request DTO containing the card details.
     * @param httpServletRequest The HTTP servlet request object.
     * @return The response DTO containing the created card details.
     * @throws NotFoundException    If the cardHolder or card type is not found.
     * @throws NotAllowedExceptions If the cardHolder is blocked and cannot create a new card.
     */
    @Override
    public CardResDto createCard(CardReqDto cardReqDto, HttpServletRequest httpServletRequest) {
        String ClientInfo = networkDataService.getClientIPv4Address(httpServletRequest);
        String ClientIP = networkDataService.getRemoteUserInfo(httpServletRequest);
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
        LOG.info("Create Card   \t\t {}", cardReqDto);
        LOG.info("Client host : \t\t {}", ClientInfo);
        LOG.info("Client IP :  \t\t {}", ClientIP);
        return cardResDto;
    }

    /**
     * Retrieves a list of cards based on the provided PINFL (Personal Identification Number of a CardHolder).
     *
     * @param pinfl              The request DTO containing the PINFL.
     * @param httpServletRequest The HTTP servlet request object.
     * @return A list of response DTOs containing the card details.
     */
    @Override
    public List<CardResDto> getByPinfl(PinflReqDto pinfl, HttpServletRequest httpServletRequest) {
        String ClientInfo = networkDataService.getClientIPv4Address(httpServletRequest);
        String ClientIP = networkDataService.getRemoteUserInfo(httpServletRequest);
        LOG.info("Get cards byPinfl  \t\t {}", pinfl);
        LOG.info("Client host : \t\t {}", ClientInfo);
        LOG.info("Client IP :  \t\t {}", ClientIP);
        List<Card> allByCardHolderPinfl = cardRepository.getAllByCardHolder_Pinfl(pinfl.getPinfl());
        return cardMapping.toDto(allByCardHolderPinfl);
    }

    /**
     * Fills a card with a specified amount and performs necessary validations.
     *
     * @param fillCardReqDto     The request DTO containing the card filling details.
     * @param httpServletRequest The HTTP servlet request object.
     * @return The response DTO containing the card filling history details.
     * @throws NotAllowedExceptions If the currency is invalid, pin code is incorrect, or the card is blocked.
     */
    @Override
    public HistoryWithAtmResDto fillCardBalance(FillCardReqDto fillCardReqDto, HttpServletRequest httpServletRequest) {
        String ClientInfo = networkDataService.getClientIPv4Address(httpServletRequest);
        String ClientIP = networkDataService.getRemoteUserInfo(httpServletRequest);
        Card byCardNumber = cardRepository.findByCardNumber(fillCardReqDto.getCardNumber());
        if (validateService.validate(byCardNumber, fillCardReqDto.getCurrencyId())) {
            throw new NotAllowedExceptions("Currency is WRONG");
        }
        if (!byCardNumber.getPinCode().equals(fillCardReqDto.getPinCode())) {
            if (validateService.validatePinCode(byCardNumber)) {
                throw new NotAllowedExceptions("Pin Code is Wrong");
            } else throw new NotAllowedExceptions("Card is Blocked");
        }
        byCardNumber.setBalance(byCardNumber.getBalance() + 99 * fillCardReqDto.getAmount() / 100);
        cardRepository.save(byCardNumber);

        CardResDto cardResDto = cardMapping.toDto(byCardNumber);

        HistoryCard historyCard = new HistoryCard();
        historyCard.setFromCard(byCardNumber);
        historyCard.setAmount(fillCardReqDto.getAmount());
        historyCard.setCommission(fillCardReqDto.getAmount() / 100);
        historyCardRepository.save(historyCard);

        HistoryWithAtmResDto dto = new HistoryWithAtmResDto();
        dto.setCommission(fillCardReqDto.getAmount() / 100);
        dto.setAmount(fillCardReqDto.getAmount());
        dto.setReceiveCard(cardResDto.getCardNumber());
        dto.setDate(historyCard.getLocalDateTime());
        dto.setId(historyCard.getId());
        LOG.info("fill card from ATM  \t\t {}", fillCardReqDto);
        LOG.info("Client host : \t\t {}", ClientInfo);
        LOG.info("Client IP :  \t\t {}", ClientIP);
        return dto;
    }

    /**
     * Blocks or unblocks a card based on the provided card number.
     *
     * @param cardReqDto         The request DTO containing the card details.
     * @param httpServletRequest The HTTP servlet request object.
     * @return The response DTO containing the updated card details.
     */
    @Override
    public CardResDto blockCard(CardReqDto cardReqDto, HttpServletRequest httpServletRequest) {
        try {
            String ClientInfo = networkDataService.getClientIPv4Address(httpServletRequest);
            String ClientIP = networkDataService.getRemoteUserInfo(httpServletRequest);
            Card byCardNumber = cardRepository.findByCardNumber(cardReqDto.getCardNumber());
            byCardNumber.setIsActive(!byCardNumber.getIsActive());
            cardRepository.save(byCardNumber);
            LOG.info("Blocked or Unblocked card \t\t {}", cardReqDto);
            LOG.info("Client host : \t\t {}", ClientInfo);
            LOG.info("Client IP :  \t\t {}", ClientIP);
            return cardMapping.toDto(byCardNumber);
        } catch (Exception e) {
            throw new CardException("Cannot  blocked the card : " + e.getMessage());
        }
    }

    /***
     * Update Card PinCode
     * @param cardReqDto New Pincode of Card
     * @param httpServletRequest define to device
     * @return old card with new pinCode
     */
    @Override
    public CardResDto updateCardPin(CardReqDto cardReqDto, HttpServletRequest httpServletRequest) {
        try {
            String ClientInfo = networkDataService.getClientIPv4Address(httpServletRequest);
            String ClientIP = networkDataService.getRemoteUserInfo(httpServletRequest);
            Card byCardNumber = cardRepository.findByCardNumber(cardReqDto.getCardNumber());

            cardMapping.updateFromDto(cardReqDto, byCardNumber);
            cardRepository.save(byCardNumber);
            LOG.info("Update Card Pin  \t\t {}", cardReqDto);
            LOG.info("Client host : \t\t {}", ClientInfo);
            LOG.info("Client IP :  \t\t {}", ClientIP);
            return cardMapping.toDto(byCardNumber);
        } catch (Exception e) {
            throw new CardException("Cannot Update Card Pin : " + e.getMessage());
        }
    }

    /**
     * Transfers an amount from one card to another.
     *
     * @param transferReqDto     The request DTO containing the transfer details.
     * @param httpServletRequest The HTTP servlet request object.
     * @return The response DTO containing the transfer history details.
     * @throws NotFoundException    If the sender card or receiver card is not found.
     * @throws NotAllowedExceptions If the currency is invalid, pin code is incorrect, or there are insufficient funds.
     */
    @Override
    public TransferResDto transferToCard(TransferReqDto transferReqDto, HttpServletRequest httpServletRequest) {
        try {
            String ClientInfo = networkDataService.getClientIPv4Address(httpServletRequest);
            String ClientIP = networkDataService.getRemoteUserInfo(httpServletRequest);
            Card fromCard = cardRepository.findByCardNumber(transferReqDto.getFromCard());
            Card toCard = cardRepository.findByCardNumber(transferReqDto.getToCard());
            if (fromCard == null) {
                throw new NotFoundException("Sender Card is Not Found");
            }
            if (validateService.validate(fromCard, transferReqDto.getCurrencyId())) {
                throw new NotAllowedExceptions("Currency is WRONG on Sender Card");
            }
            if (validateService.validate(toCard, transferReqDto.getCurrencyId())) {
                throw new NotAllowedExceptions("Currency is WRONG on Receive Card");
            }
            if (!fromCard.getPinCode().equals(transferReqDto.getPinCode())) {
                if (validateService.validatePinCode(fromCard)) {
                    throw new NotAllowedExceptions("Pin Code is Wrong");
                } else throw new NotAllowedExceptions("Card is Blocked");
            }
            if (toCard == null) {
                throw new NotFoundException("Receive card is not Found ");
            }
            if (fromCard.getBalance() * 1.01 < transferReqDto.getAmount()) {
                throw new NotAllowedExceptions("Not enough money on sender card");
            }
            fromCard.setBalance(fromCard.getBalance() - transferReqDto.getAmount() * 1.01f);
            cardRepository.save(fromCard);

            toCard.setBalance(toCard.getBalance() + transferReqDto.getAmount());
            cardRepository.save(toCard);

            HistoryCard historyCard = new HistoryCard();
            historyCard.setToCard(toCard);
            historyCard.setFromCard(fromCard);
            historyCard.setAmount(transferReqDto.getAmount());
            historyCard.setCommission(transferReqDto.getAmount() / 100);
            historyCard.setLocalDateTime(LocalDateTime.now());
            historyCardRepository.save(historyCard);

            TransferResDto transferResDto = new TransferResDto();
            transferResDto.setAmount(transferReqDto.getAmount());
            transferResDto.setDate(LocalDateTime.now());
            transferResDto.setCommission(transferReqDto.getAmount() / 100);
            transferResDto.setToCard(cardMapping.toDto(toCard));
            transferResDto.setFromCard(cardMapping.toDto(fromCard));
            transferResDto.setTransferId(historyCard.getId());
            LOG.info("Transfer  from card to card  \t\t {}", transferReqDto);
            LOG.info("Client host : \t\t {}", ClientInfo);
            LOG.info("Client IP :  \t\t {}", ClientIP);
            return transferResDto;
        } catch (Exception e) {
            throw new CardException(" Cannot Transfer to card  : " + e.getMessage());
        }
    }

}