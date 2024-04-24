package com.company.service.impl;

import com.company.dto.request.HistoryReqDto;
import com.company.dto.response.HistoryWithAtmResDto;
import com.company.dto.response.InPutResDto;
import com.company.dto.response.OutPutResDto;
import com.company.dto.response.model.HistoryInPutResModel;
import com.company.dto.response.model.HistoryOutputModel;
import com.company.entity.Card;
import com.company.entity.HistoryCard;
import com.company.exps.HistoryException;
import com.company.mapping.CardMapper;
import com.company.repository.CardRepository;
import com.company.repository.HistoryCardRepository;
import com.company.service.CheckService;
import com.company.service.HistoryService;
import com.company.service.ValidateService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService {

    private final CardRepository cardRepository;
    private final CardMapper cardMapping;
    private final HistoryCardRepository historyCardRepository;
    private final static Logger LOG = LoggerFactory.getLogger(Card.class);
    private final ValidateService validateService;
    private final CheckService checkService;


    /**
     * Retrieves the transfer history of a specific card as the sender.
     *
     * @param historyReqDto      The request DTO containing the history details.
     * @param httpServletRequest The HTTP servlet request object.
     * @return The response DTO containing the transfer history details.
     * @throws HistoryException If the sender card is not found or no transfers have been made from the card yet And
     *                          If the pin code is incorrect or the card is blocked.
     */
    @Override
    public OutPutResDto getSendHistory(HistoryReqDto historyReqDto, HttpServletRequest httpServletRequest) {
        try {
            checkService.checkDevice(httpServletRequest);
            List<HistoryCard> sendHistory = historyCardRepository.findAllByFromCard_CardNumber(historyReqDto.getCardNumber());
            OutPutResDto outPutResDto = new OutPutResDto();
            Card card = cardRepository.findByCardNumber(historyReqDto.getCardNumber());

            if (card == null) {
                throw new HistoryException("Sender Card is Not Found");
            }

            if (!card.getPinCode().equals(historyReqDto.getPinCode())) {
                if (validateService.validatePinCode(card)) {
                    throw new HistoryException("Pin Code is Wrong");
                } else throw new HistoryException("Card is Blocked");
            }

            reType(sendHistory, outPutResDto, card, cardMapping);
            LOG.info("Get transfer's  Histories\t\t {}", historyReqDto);
            return outPutResDto;
        } catch (Exception e) {
            throw new HistoryException("Can not get Send history :  " + e.getMessage());
        }
    }

    /**
     * Retrieves the transfer history of a specific card as the receiver.
     *
     * @param historyReqDto      The request DTO containing the history details.
     * @param httpServletRequest The HTTP servlet request object.
     * @return The response DTO containing the transfer history details.
     * @throws HistoryException If the sender card is not found or no transfers have been made to the card yet And
     *                          If the pin code is incorrect or the card is blocked.
     */
    @Override
    public InPutResDto getReceiveHistory(HistoryReqDto historyReqDto, HttpServletRequest httpServletRequest) {
        try {
            checkService.checkDevice(httpServletRequest);
            List<HistoryCard> receiveHistory = historyCardRepository.findAllByToCard_CardNumber(historyReqDto.getCardNumber());
            InPutResDto inPutResDto = new InPutResDto();
            Card card = cardRepository.findByCardNumber(historyReqDto.getCardNumber());

            if (card == null) {
                throw new HistoryException("Sender Card is Not Found");
            }

            if (!card.getPinCode().equals(historyReqDto.getPinCode())) {
                if (validateService.validatePinCode(card)) {
                    throw new HistoryException("Pin Code is Wrong");
                } else throw new HistoryException("Card is Blocked");
            }
            setCount(receiveHistory, inPutResDto, card, cardRepository, cardMapping);
            LOG.info("Get transfer's  Histories\t\t {}", historyReqDto);
            return inPutResDto;
        } catch (Exception e) {
            throw new HistoryException("Cannot get Receive History : " + e.getMessage());
        }
    }

    /**
     * All the transaction history for a specified card.
     *
     * @param historyReqDto      The request data containing the card number.
     * @param httpServletRequest The HTTP servlet request associated with the transaction.
     * @return The list of transaction history with ATM details.
     * @throws HistoryException If an error occurs while retrieving the transaction history.
     */
    @Override
    public List<HistoryWithAtmResDto> getAllHistory(HistoryReqDto historyReqDto, HttpServletRequest httpServletRequest) {

        try {
            checkService.checkDevice(httpServletRequest);
            Card card = cardRepository.findByCardNumber(historyReqDto.getCardNumber());
            validateService.validate1(card);
            validateService.validatePinCode(card);
            List<HistoryCard> allHistory = historyCardRepository.findAllByFromCard_CardNumberOrToCard_CardNumber(
                    historyReqDto.getCardNumber(), historyReqDto.getCardNumber()
            );
            List<HistoryWithAtmResDto> response = new ArrayList<>();
            for (HistoryCard historyCard : allHistory) {
                HistoryWithAtmResDto atm = getHistoryWithAtmResDto(historyCard);
                response.add(atm);
            }
            LOG.info("Get All History of Card \t\t {} ", historyReqDto);
            return response;
        } catch (Exception e) {
            throw new HistoryException("Can not get All History of the Card  :  " + e.getMessage());
        }
    }




    @Override
    public List<HistoryWithAtmResDto> getAllHistoryWithDate(HistoryReqDto historyReqDto, HttpServletRequest httpServletRequest) {
        String startDate = historyReqDto.getStartDate()+"T00:00:00.00";
        String endDate = historyReqDto.getEndDate()+"T23:59:59.00";

        LocalDateTime start = LocalDateTime.parse(startDate);
        LocalDateTime end = LocalDateTime.parse(endDate);
        System.out.println(start+"    " +end);
        try {
            checkService.checkDevice(httpServletRequest);
            Card card = cardRepository.findByCardNumber(historyReqDto.getCardNumber());
            validateService.validate1(card);
            validateService.validatePinCode(card);
            List<HistoryCard> allHistory =
                    historyCardRepository.findAllByToCard_CardNumberAndFromCard_CardNumberOrLocalDateTimeBetween(
                    historyReqDto.getCardNumber(), historyReqDto.getCardNumber(), start,end
            );
            List<HistoryWithAtmResDto> response = new ArrayList<>();
            for (HistoryCard historyCard : allHistory) {
                HistoryWithAtmResDto atm = getHistoryWithAtmResDto(historyCard);
                response.add(atm);
            }
            LOG.info("Get All History of Card \t\t {} ", historyReqDto);
            return response;
        } catch (Exception e) {
            throw new HistoryException("Can not get All History of the Card  :  " + e.getMessage());
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
            throw new HistoryException("No Receive have been made from this card yet");
        }
        inPutResDto.setReceives(inputModels);
    }


    private static HistoryWithAtmResDto getHistoryWithAtmResDto(HistoryCard historyCard) {
        HistoryWithAtmResDto atm = new HistoryWithAtmResDto();
        atm.setId(historyCard.getId());
        atm.setDate(historyCard.getLocalDateTime());
        atm.setCommission(historyCard.getCommission());
        atm.setAmount(historyCard.getAmount());
        if (historyCard.getToCard() != null) {
            atm.setReceiveCard(historyCard.getToCard().getCardNumber());
            atm.setReceiveCardHolder(historyCard.getToCard().getCardHolder().getFirstName() + " " +
                    historyCard.getToCard().getCardHolder().getLastName());
        }
        if (historyCard.getFromCard() != null) {
            atm.setSenderCard(historyCard.getFromCard().getCardNumber());
            atm.setSenderCardHolder(historyCard.getFromCard().getCardHolder().getFirstName() + " " +
                    historyCard.getFromCard().getCardHolder().getLastName());
        }
        return atm;
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
            throw new HistoryException("No transfers have been made from this card yet");
        }
        outPutResDto.setTransfers(outputModels);
    }

}
