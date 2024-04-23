package com.company.service.impl;

import com.company.dto.request.HistoryReqDto;
import com.company.dto.response.HistoryWithAtmResDto;
import com.company.dto.response.InPutResDto;
import com.company.dto.response.OutPutResDto;
import com.company.dto.response.model.HistoryInPutResModel;
import com.company.dto.response.model.HistoryOutputModel;
import com.company.entity.Card;
import com.company.entity.HistoryCard;
import com.company.exps.CardException;
import com.company.exps.NotAllowedExceptions;
import com.company.exps.NotFoundException;
import com.company.mapping.CardMapper;
import com.company.repository.CardRepository;
import com.company.repository.HistoryCardRepository;
import com.company.service.HistoryService;
import com.company.service.ValidateService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService {

    private final CardRepository cardRepository;
    private final CardMapper cardMapping;
    private final HistoryCardRepository historyCardRepository;
    private final static Logger LOG = LoggerFactory.getLogger(Card.class);
    private final NetworkDataService networkDataService;
    private final ValidateService validateService;


    /**
     * Retrieves the transfer history of a specific card as the sender.
     *
     * @param historyReqDto      The request DTO containing the history details.
     * @param httpServletRequest The HTTP servlet request object.
     * @return The response DTO containing the transfer history details.
     * @throws NotFoundException    If the sender card is not found or no transfers have been made from the card yet.
     * @throws NotAllowedExceptions If the pin code is incorrect or the card is blocked.
     */
    @Override
    public OutPutResDto getSendHistory(HistoryReqDto historyReqDto, HttpServletRequest httpServletRequest) {
        try {
            String ClientInfo = networkDataService.getClientIPv4Address(httpServletRequest);
            String ClientIP = networkDataService.getRemoteUserInfo(httpServletRequest);
            List<HistoryCard> sendHistory = historyCardRepository.findAllByFromCard_CardNumber(historyReqDto.getCardNumber());
            OutPutResDto outPutResDto = new OutPutResDto();
            Card card = cardRepository.findByCardNumber(historyReqDto.getCardNumber());

            if (card == null) {
                throw new NotFoundException("Sender Card is Not Found");
            }

            if (!card.getPinCode().equals(historyReqDto.getPinCode())) {
                if (validateService.validatePinCode(card)) {
                    throw new NotAllowedExceptions("Pin Code is Wrong");
                } else throw new NotAllowedExceptions("Card is Blocked");
            }

            reType(sendHistory, outPutResDto, card, cardMapping);
            LOG.info("Get transfer's  Histories\t\t {}", historyReqDto);
            LOG.info("Client host : \t\t {}", ClientInfo);
            LOG.info("Client IP :  \t\t {}", ClientIP);
            return outPutResDto;
        } catch (Exception e) {
            throw new CardException("Can not get Send history :  " + e.getMessage());
        }
    }

    static void reType(List<HistoryCard> sendHistory, OutPutResDto outPutResDto, Card card, CardMapper cardMapping) {
        outPutResDto.setCard(cardMapping.toDto(card));
        List<HistoryOutputModel> outputModels = new ArrayList<>();
        for (HistoryCard historyCard : sendHistory) {
            if (historyCard.getToCard() != null) {
                HistoryOutputModel outputModel = new HistoryOutputModel();
                outputModel.setAmount(historyCard.getAmount());
                outputModel.setToCard(cardMapping.toDto(historyCard.getToCard()));
                outputModel.setCommission(historyCard.getCommission());
                outputModel.setDate(historyCard.getLocalDateTime());
                outputModel.setTransferId(historyCard.getId());
                outputModels.add(outputModel);
            }
        }
        if (outputModels.isEmpty()) {
            throw new NotFoundException("No transfers have been made from this card yet");
        }
        outPutResDto.setTransfers(outputModels);
    }

    /**
     * Retrieves the transfer history of a specific card as the receiver.
     *
     * @param historyReqDto      The request DTO containing the history details.
     * @param httpServletRequest The HTTP servlet request object.
     * @return The response DTO containing the transfer history details.
     * @throws NotFoundException    If the sender card is not found or no transfers have been made to the card yet.
     * @throws NotAllowedExceptions If the pin code is incorrect or the card is blocked.
     */
    @Override
    public InPutResDto getReceiveHistory(HistoryReqDto historyReqDto, HttpServletRequest httpServletRequest) {
        try {
            String ClientInfo = networkDataService.getClientIPv4Address(httpServletRequest);
            String ClientIP = networkDataService.getRemoteUserInfo(httpServletRequest);
            List<HistoryCard> receiveHistory = historyCardRepository.findAllByToCard_CardNumber(historyReqDto.getCardNumber());
            InPutResDto inPutResDto = new InPutResDto();
            Card card = cardRepository.findByCardNumber(historyReqDto.getCardNumber());

            if (card == null) {
                throw new NotFoundException("Sender Card is Not Found");
            }

            if (!card.getPinCode().equals(historyReqDto.getPinCode())) {
                if (validateService.validatePinCode(card)) {
                    throw new NotAllowedExceptions("Pin Code is Wrong");
                } else throw new NotAllowedExceptions("Card is Blocked");
            }
            setCount(receiveHistory, inPutResDto, card, cardRepository, cardMapping);
            LOG.info("Get transfer's  Histories\t\t {}", historyReqDto);
            LOG.info("Client host : \t\t {}", ClientInfo);
            LOG.info("Client IP :  \t\t {}", ClientIP);
            return inPutResDto;
        } catch (Exception e) {
            throw new CardException("Cannot get Receive History : " + e.getMessage());
        }
    }

    static void setCount(List<HistoryCard> receiveHistory, InPutResDto inPutResDto, Card card, CardRepository cardRepository, CardMapper cardMapping) {
        card.setCount(3);
        cardRepository.save(card);

        inPutResDto.setCard(cardMapping.toDto(card));
        List<HistoryInPutResModel> inputModels = new ArrayList<>();
        for (HistoryCard historyCard : receiveHistory) {
            if (historyCard.getFromCard() != null) {
                HistoryInPutResModel inputModel = new HistoryInPutResModel();
                inputModel.setAmount(historyCard.getAmount());
                inputModel.setFromCard(cardMapping.toDto(historyCard.getToCard()));
                inputModel.setCommission(historyCard.getCommission());
                inputModel.setDate(historyCard.getLocalDateTime());
                inputModel.setTransferId(historyCard.getId());
                inputModels.add(inputModel);
            }
        }
        if (inputModels.isEmpty()) {
            throw new NotFoundException("No Receive have been made from this card yet");
        }
        inPutResDto.setReceives(inputModels);
    }

    @Override
    public List<HistoryWithAtmResDto> getAtmHistory(HistoryReqDto historyReqDto, HttpServletRequest httpServletRequest) {
        List<HistoryCard> allHistory = historyCardRepository.findAllByFromCard_CardNumberOrToCard_CardNumber(
                historyReqDto.getCardNumber(), historyReqDto.getCardNumber()
        );
        List<HistoryWithAtmResDto> response = new ArrayList<>();
        for (HistoryCard historyCard : allHistory) {
            HistoryWithAtmResDto atm = new HistoryWithAtmResDto();
            atm.setId(historyCard.getId());
            atm.setDate(historyCard.getLocalDateTime());
            atm.setCommission(historyCard.getCommission());
            atm.setAmount(historyCard.getAmount());
            if (historyCard.getToCard()!=null) {
                atm.setReceiveCard(historyCard.getToCard().getCardNumber());
                atm.setReceiveCardHolder(historyCard.getToCard().getCardHolder().getFirstName() + " " +
                        historyCard.getToCard().getCardHolder().getLastName());
            }
            if (historyCard.getFromCard()!=null) {
                atm.setSenderCard(historyCard.getFromCard().getCardNumber());
                atm.setSenderCardHolder(historyCard.getFromCard().getCardHolder().getFirstName() + " " +
                        historyCard.getFromCard().getCardHolder().getLastName());
            }
            response.add(atm);
        }

        return response;
    }

}
