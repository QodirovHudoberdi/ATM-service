package com.company.service.impl;

import com.company.dto.request.*;
import com.company.dto.response.*;
import com.company.dto.response.model.HistoryInPutResModel;
import com.company.dto.response.model.HistoryOutputModel;
import com.company.entity.*;
import com.company.exps.NotAllowedExceptions;
import com.company.exps.NotFoundException;
import com.company.mapping.CardHolderMapper;
import com.company.mapping.CardMapper;
import com.company.mapping.CardTypeMapper;
import com.company.repository.*;
import com.company.service.CardService;
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
    private final static Logger LOG = LoggerFactory.getLogger(CardHolder.class);
    private final NetworkDataService networkDataService;
    /**
     * Creates a new card for a cardHolder.
     *
     * @param cardReqDto           The request DTO containing the card details.
     * @param httpServletRequest   The HTTP servlet request object.
     * @return                     The response DTO containing the created card details.
     * @throws NotFoundException       If the cardHolder or card type is not found.
     * @throws NotAllowedExceptions    If the cardHolder is blocked and cannot create a new card.
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

        return cardResDto;
    }
    /**
     * Retrieves a list of cards based on the provided PINFL (Personal Identification Number of a CardHolder).
     *
     * @param pinfl                 The request DTO containing the PINFL.
     * @param httpServletRequest   The HTTP servlet request object.
     * @return                      A list of response DTOs containing the card details.
     */
    @Override
    public List<CardResDto> getByPinfl(PinflReqDto pinfl, HttpServletRequest httpServletRequest) {
        List<Card> allByCardHolderPinfl = cardRepository.getAllByCardHolder_Pinfl(pinfl.getPinfl());
        return cardMapping.toDto(allByCardHolderPinfl);
    }
    /**
     * Fills a card with a specified amount and performs necessary validations.
     *
     * @param fillCardReqDto        The request DTO containing the card filling details.
     * @param httpServletRequest    The HTTP servlet request object.
     * @return                      The response DTO containing the card filling history details.
     * @throws NotAllowedExceptions If the currency is invalid, pin code is incorrect, or the card is blocked.
     */
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
        byCardNumber.setBalance(byCardNumber.getBalance() +99*fillCardReqDto.getAmount() / 100);
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
        dto.setCard(cardResDto);
        dto.setDate(historyCard.getLocalDateTime());
        dto.setId(historyCard.getId());

        return dto;
    }
    /**
     * Blocks or unblocks a card based on the provided card number.
     *
     * @param cardReqDto            The request DTO containing the card details.
     * @param httpServletRequest    The HTTP servlet request object.
     * @return                      The response DTO containing the updated card details.
     */
    @Override
    public CardResDto blockCard(CardReqDto cardReqDto, HttpServletRequest httpServletRequest) {
        Card byCardNumber = cardRepository.findByCardNumber(cardReqDto.getCardNumber());
        byCardNumber.setIsActive(!byCardNumber.getIsActive());
        cardRepository.save(byCardNumber);
        return cardMapping.toDto(byCardNumber);
    }

    /***
     * Update Card PinCode
     * @param cardReqDto New Pincode of Card
     * @param httpServletRequest define to device
     * @return old card with new pinCode
     */
    @Override
    public CardResDto updateCardPin(CardReqDto cardReqDto, HttpServletRequest httpServletRequest) {
        Card byCardNumber = cardRepository.findByCardNumber(cardReqDto.getCardNumber());

        cardMapping.updateFromDto(cardReqDto, byCardNumber);
        cardRepository.save(byCardNumber);
        return cardMapping.toDto(byCardNumber);
    }
    /**
     * Transfers an amount from one card to another.
     *
     * @param transferReqDto        The request DTO containing the transfer details.
     * @param httpServletRequest    The HTTP servlet request object.
     * @return                      The response DTO containing the transfer history details.
     * @throws NotFoundException       If the sender card or receiver card is not found.
     * @throws NotAllowedExceptions    If the currency is invalid, pin code is incorrect, or there are insufficient funds.
     */
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
        transferResDto.setTransferId(historyCard.getId());
        return transferResDto;
    }
    /**
     * Retrieves the transfer history of a specific card as the sender.
     *
     * @param historyReqDto         The request DTO containing the history details.
     * @param httpServletRequest    The HTTP servlet request object.
     * @return                      The response DTO containing the transfer history details.
     * @throws NotFoundException       If the sender card is not found or no transfers have been made from the card yet.
     * @throws NotAllowedExceptions    If the pin code is incorrect or the card is blocked.
     */
    @Override
    public OutPutResDto getSendHistory(HistoryReqDto historyReqDto, HttpServletRequest httpServletRequest) {

        List<HistoryCard> sendHistory = historyCardRepository.findAllByFromCard_CardNumber(historyReqDto.getCardNumber());
        OutPutResDto outPutResDto = new OutPutResDto();
        Card card = cardRepository.findByCardNumber(historyReqDto.getCardNumber());

        if (card==null){
            throw new NotFoundException("Sender Card is Not Found");
        }

        if (!card.getPinCode().equals(historyReqDto.getPinCode())) {
            if (validatePinCode(card)) {
                throw new NotAllowedExceptions("Pin Code is Wrong");
            } else throw new NotAllowedExceptions("Card is Blocked");
        }

        outPutResDto.setCard(cardMapping.toDto(card));
        List<HistoryOutputModel> outputModels=new ArrayList<>();
        for (HistoryCard historyCard : sendHistory) {
            if (historyCard.getToCard()!=null){
            HistoryOutputModel outputModel=new HistoryOutputModel();
            outputModel.setAmount(historyCard.getAmount());
            outputModel.setToCard(cardMapping.toDto(historyCard.getToCard()));
            outputModel.setCommission(historyCard.getCommission());
            outputModel.setDate(historyCard.getLocalDateTime());
            outputModel.setTransferId(historyCard.getId());
            outputModels.add(outputModel);
            }
        }
        if (outputModels.isEmpty()){
            throw new NotFoundException("No transfers have been made from this card yet");
        }
        outPutResDto.setTransfers(outputModels);
        return outPutResDto;
    }
    /**
     * Retrieves the transfer history of a specific card as the receiver.
     *
     * @param historyReqDto         The request DTO containing the history details.
     * @param httpServletRequest    The HTTP servlet request object.
     * @return                      The response DTO containing the transfer history details.
     * @throws NotFoundException       If the sender card is not found or no transfers have been made to the card yet.
     * @throws NotAllowedExceptions    If the pin code is incorrect or the card is blocked.
     */
    @Override
    public InPutResDto getReceiveHistory(HistoryReqDto historyReqDto, HttpServletRequest httpServletRequest) {

        List<HistoryCard> receiveHistory = historyCardRepository.findAllByToCard_CardNumber(historyReqDto.getCardNumber());
        InPutResDto inPutResDto = new InPutResDto();
        Card card = cardRepository.findByCardNumber(historyReqDto.getCardNumber());

        if (card==null){
            throw new NotFoundException("Sender Card is Not Found");
        }

        if (!card.getPinCode().equals(historyReqDto.getPinCode())) {
            if (validatePinCode(card)) {
                throw new NotAllowedExceptions("Pin Code is Wrong");
            } else throw new NotAllowedExceptions("Card is Blocked");
        }

        inPutResDto.setCard(cardMapping.toDto(card));
        List<HistoryInPutResModel> inputModels=new ArrayList<>();
        for (HistoryCard historyCard : receiveHistory) {
            if (historyCard.getFromCard()!=null){
            HistoryInPutResModel inputModel=new HistoryInPutResModel();
            inputModel.setAmount(historyCard.getAmount());
            inputModel.setFromCard(cardMapping.toDto(historyCard.getToCard()));
            inputModel.setCommission(historyCard.getCommission());
            inputModel.setDate(historyCard.getLocalDateTime());
            inputModel.setTransferId(historyCard.getId());
            inputModels.add(inputModel);
            }
        }
        if (inputModels.isEmpty()){
            throw new NotFoundException("No Receive have been made from this card yet");
        }
        inPutResDto.setReceives(inputModels);
        return inPutResDto;
    }
    /**
     * Performs a cash withdrawal from an ATM using the specified card.
     *
     * @param cashingReqDto         The request DTO containing the cashing details.
     * @param httpServletRequest    The HTTP servlet request object.
     * @return                      The response DTO containing the cash withdrawal details.
     * @throws NotFoundException       If the card number is not found or the entered amount is incorrect.
     * @throws NotAllowedExceptions    If the card does not have sufficient funds, is blocked, or the pin code is incorrect.
     */
    @Override
    public CashingResDto cashingFromAtm(CashingReqDto cashingReqDto,HttpServletRequest httpServletRequest){
        Card byCardNumber = cardRepository.findByCardNumber(cashingReqDto.getCardNumber());
        if (validate(byCardNumber.getCardType().getCurrency().getId(),byCardNumber)) {
            throw new NotFoundException("Not Found Card Number");
        }
        if (byCardNumber.getBalance()< cashingReqDto.getAmount()*1.01f){
            throw new NotAllowedExceptions("card is not have enough Money");
        }
        if (!byCardNumber.getIsActive()){
            throw new NotAllowedExceptions("Card is Blocked");
        }
        if (!byCardNumber.getPinCode().equals(cashingReqDto.getPinCode())) {
            if (validatePinCode(byCardNumber)) {
                throw new NotAllowedExceptions("Pin Code is Wrong");
            } else throw new NotAllowedExceptions("Card is Blocked");
        }
        List<BankNote> allBankNotes;
        if (cashingReqDto.getTypeId()==1) {
            allBankNotes = bankNoteRepository.findAllByCurrency_IdOrderByAmount(
                    byCardNumber.getCardType().getCurrency().getId()
            );
        }else{
            allBankNotes=bankNoteRepository.findAllByType_IdOrderByAmount(cashingReqDto.getTypeId());
        }
        Float amount = cashingReqDto.getAmount();
        if((amount % allBankNotes.get(0).getAmount()) != 0){
            throw new NotAllowedExceptions("Entered Wrong Amount");
        }
        List<BankNote> cashing = getCashing(allBankNotes, amount);
        bankNoteRepository.saveAll( cashing);

        byCardNumber.setBalance(byCardNumber.getBalance()- cashingReqDto.getAmount());
        cardRepository.save(byCardNumber);

        HistoryCard historyCard=new HistoryCard();
        historyCard.setToCard(byCardNumber);
        historyCard.setAmount(amount);
        historyCard.setCommission(amount/100);
        historyCardRepository.save(historyCard);

        CashingResDto cashingResDto = new CashingResDto();
        cashingResDto.setCard(cardMapping.toDto(byCardNumber));
        cashingResDto.setAmount(cashingReqDto.getAmount());
        cashingResDto.setCommission(cashingReqDto.getAmount()/100);

        return  cashingResDto;
    }
    /**
     * Validates if the card's currency matches the specified currency ID.
     *
     * @param currencyId     The ID of the currency to validate against.
     * @param byCardNumber   The card to validate.
     * @return              {@code true} if the card's currency does not match the specified currency ID, {@code false} otherwise.
     * @throws NotFoundException   If the card number is incorrect.
     */
    Boolean validate(Long currencyId, Card byCardNumber) {
        if (byCardNumber == null) {
            throw new NotFoundException("Card Number is Wrong");
        }
        return !byCardNumber.getCardType().getCurrency().getId().equals(currencyId);
    }
    /**
     * Validates the PIN code of a card and updates the card's count and status accordingly.
     *
     * @param card  The card to validate the PIN code for.
     * @return      {@code true} if the PIN code is correct and the card's count is decremented, {@code false} otherwise.
     */
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
    /**
     * Performs the cashing operation by selecting the appropriate banknotes from the available banknotes.
     *
     * @param allBankNotes  The list of all available banknotes.
     * @param amount        The amount to be withdrawn.
     * @return              The list of banknotes to be dispensed for the cash withdrawal.
     * @throws NotAllowedExceptions    If the ATM does not have the requested amount of money.
     */
    List<BankNote>getCashing(List<BankNote> allBankNotes,Float amount){
        for (int i = allBankNotes.size()-1; i >=0; i--) {
            Integer amount1 = allBankNotes.get(i).getAmount();
            int count=(int) (amount/amount1);
            if (count>0&&count<allBankNotes.get(i).getCount()){
                amount-=count*amount1;
            }
            allBankNotes.get(i).setCount(allBankNotes.get(i).getCount()-count);
        }
        if (amount!=0){
            throw new NotAllowedExceptions("Atm Do not Have this amount of money");
        }
        return allBankNotes;
    }
}