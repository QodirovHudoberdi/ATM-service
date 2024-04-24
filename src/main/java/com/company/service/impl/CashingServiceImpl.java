package com.company.service.impl;

import com.company.dto.request.CashingReqDto;
import com.company.dto.response.CashingResDto;
import com.company.entity.BankNote;
import com.company.entity.Card;
import com.company.entity.HistoryCard;
import com.company.exps.CardException;
import com.company.mapping.CardMapper;
import com.company.repository.BankNoteRepository;
import com.company.repository.CardRepository;
import com.company.repository.HistoryCardRepository;
import com.company.service.CashingService;
import com.company.service.CheckService;
import com.company.service.ValidateService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CashingServiceImpl implements CashingService {
    private final CardRepository cardRepository;
    private final CardMapper cardMapping;
    private final HistoryCardRepository historyCardRepository;
    private final BankNoteRepository bankNoteRepository;
    private final static Logger LOG = LoggerFactory.getLogger(Card.class);
    private final ValidateService validateService;
    private final CheckService checkService;

    /**
     * Performs a cash withdrawal from an ATM using the provided request data and HTTP servlet request.
     *
     * @param cashingReqDto      The cash withdrawal request data.
     * @param httpServletRequest The HTTP servlet request associated with the transaction.
     * @return The response data containing the details of the cash withdrawal.
     * @throws CardException If an error occurs during the cash withdrawal process.
     */
    @Override
    public CashingResDto cashingFromAtm(CashingReqDto cashingReqDto, HttpServletRequest httpServletRequest) {
        try {
            checkService.checkDevice(httpServletRequest);
            Card byCardNumber = cardRepository.findByCardNumber(cashingReqDto.getCardNumber());
            validateService.validation(byCardNumber);

            if (byCardNumber.getBalance() < cashingReqDto.getAmount() * 1.01f) {
                throw new CardException("card is not have enough Money");
            }
            if (!byCardNumber.getIsActive()) {
                throw new CardException("Card is Blocked");
            }
            if (!byCardNumber.getPinCode().equals(cashingReqDto.getPinCode())) {
                if (validateService.validatePinCode(byCardNumber)) {
                    throw new CardException("Pin Code is Wrong");
                } else throw new CardException("Card is Blocked");
            }
            byCardNumber.setCount(3);

            List<BankNote> allBankNotes;
            if (cashingReqDto.getTypeId() == 1) {
                allBankNotes = bankNoteRepository.findAllByCurrency_IdOrderByAmount(
                        byCardNumber.getCardType().getCurrency().getId()
                );
            } else {
                allBankNotes = bankNoteRepository.findAllByType_IdOrderByAmount(cashingReqDto.getTypeId());
            }
            Float amount = cashingReqDto.getAmount();
            if ((amount % allBankNotes.get(0).getAmount()) != 0) {
                throw new CardException("Entered Wrong Amount");
            }
            List<BankNote> cashing = getCashing(allBankNotes, amount);
            bankNoteRepository.saveAll(cashing);

            byCardNumber.setBalance(byCardNumber.getBalance() - cashingReqDto.getAmount());
            cardRepository.save(byCardNumber);

            HistoryCard historyCard = new HistoryCard();
            historyCard.setToCard(byCardNumber);
            historyCard.setAmount(amount);
            historyCard.setCommission(amount / 100);
            historyCardRepository.save(historyCard);

            CashingResDto cashingResDto = new CashingResDto();
            cashingResDto.setCard(cardMapping.toDto(byCardNumber));
            cashingResDto.setAmount(cashingReqDto.getAmount());
            cashingResDto.setCommission(cashingReqDto.getAmount() / 100);
            LOG.info("Cashing from Atm \t\t {}", cashingResDto);
            return cashingResDto;


        } catch (Exception e) {
            throw new CardException("Can not Cashing : " + e.getMessage());
        }
    }

    /**
     * Performs the cashing operation by selecting the appropriate banknotes from the available banknotes.
     *
     * @param allBankNotes The list of all available banknotes.
     * @param amount       The amount to be withdrawn.
     * @return The list of banknotes to be dispensed for the cash withdrawal.
     * @throws CardException If the ATM does not have the requested amount of money.
     */
    List<BankNote> getCashing(List<BankNote> allBankNotes, Float amount) {
        try {
            for (int i = allBankNotes.size() - 1; i >= 0; i--) {
                Integer amount1 = allBankNotes.get(i).getAmount();
                int count = (int) (amount / amount1);
                if (count > 0 && count < allBankNotes.get(i).getCount()) {
                    amount -= count * amount1;
                }
                allBankNotes.get(i).setCount(allBankNotes.get(i).getCount() - count);
            }
            if (amount != 0) {
                throw new CardException("Atm Do not Have this amount of money");
            }
            return allBankNotes;
        } catch (Exception e) {
            throw new CardException("Can not get Cashing :  " + e.getMessage());
        }
    }
}
