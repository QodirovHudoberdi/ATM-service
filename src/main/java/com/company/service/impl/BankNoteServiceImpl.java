package com.company.service.impl;

import com.company.dto.request.BankNoteReqDto;
import com.company.dto.response.BankNoteResDto;
import com.company.dto.response.BankNoteTypeResDto;
import com.company.dto.response.CurrencyResDto;
import com.company.entity.BankNote;
import com.company.entity.BankNoteType;
import com.company.entity.CardHolder;
import com.company.entity.Currency;
import com.company.exps.BankNoteException;
import com.company.exps.OkResponse;
import com.company.mapping.BankNoteMapper;
import com.company.repository.BankNoteRepository;
import com.company.repository.BanknoteTypeRepository;
import com.company.repository.CurrencyRepository;
import com.company.service.BankNoteService;
import com.company.service.CheckService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BankNoteServiceImpl implements BankNoteService {
    private final BankNoteMapper bankNoteMapper;
    private final BankNoteRepository bankNoteRepository;
    private final CurrencyRepository currencyRepository;
    private final BanknoteTypeRepository banknoteTypeRepository;
    private final static Logger LOG = LoggerFactory.getLogger(CardHolder.class);
    private final CheckService checkService;

    /**
     * Creates a new banknote.
     *
     * @param bankNoteReqDto     Details of the new banknote
     * @param httpServletRequest HttpServletRequest object for additional context
     * @return The newly created banknote as a response DTO
     * @throws BankNoteException If a banknote with the same amount already exists
     * @throws BankNoteException     If the currency or banknote type is not found
     */
    @Override
    public BankNoteResDto createBankNote(BankNoteReqDto bankNoteReqDto, HttpServletRequest httpServletRequest) {
        try {
            checkService.checkDevice(httpServletRequest);

            BankNote byName = bankNoteRepository.findByAmount(bankNoteReqDto.getAmount());
            if (byName != null) {

                throw new BankNoteException("This Banknote already have");
            }
            BankNote bankNote = bankNoteMapper.toEntity(bankNoteReqDto);
            Optional<Currency> currencyById = currencyRepository.findById(bankNoteReqDto.getCurrencyId());
            if (currencyById.isEmpty()) {

                throw new BankNoteException("Currency Not Found ");
            }
            Optional<BankNoteType> byId = banknoteTypeRepository.findById(bankNoteReqDto.getTypeId());
            if (byId.isEmpty()) {

                throw new BankNoteException("Banknote type Not found");
            }
            Currency currency = currencyById.get();
            bankNote.setCurrency(currency);
            BankNoteType bankNoteType = new BankNoteType(byId.get().getId(), byId.get().getName());
            bankNote.setType(bankNoteType);
            bankNoteRepository.save(bankNote);
            BankNoteResDto dto = bankNoteMapper.toDto(bankNote);

            CurrencyResDto currencyResDto = new CurrencyResDto();
            currencyResDto.setId(currency.getId());
            currencyResDto.setName(currency.getName());

            dto.setType(new BankNoteTypeResDto(bankNoteType.getId(), bankNoteType.getName()));
            dto.setCurrency(currencyResDto);

            LOG.info("Create Banknote   \t\t {}", bankNoteReqDto);
            return dto;
        } catch (Exception e) {
            throw new BankNoteException("Can not Create Bank Note  : " + e.getMessage());
        }
    }

    @Override
    public void deleteBankNote(Integer amount, HttpServletRequest httpServletRequest) {
        try {
            checkService.checkDevice(httpServletRequest);

            BankNote byName = bankNoteRepository.findByAmount(amount);
            if (byName == null) {
                throw new BankNoteException("This Banknote Not have");
            }
            bankNoteRepository.delete(byName);
            throw new OkResponse("Successfully Deleted banknote");
        } catch (Exception e) {
            throw new BankNoteException("Can not Delete Banknote : " + e.getMessage());
        }
    }
}
