package com.company.service.impl;

import com.company.dto.request.BankNoteReqDto;
import com.company.dto.response.BankNoteResDto;
import com.company.dto.response.BankNoteTypeResDto;
import com.company.dto.response.CurrencyResDto;
import com.company.entity.BankNote;
import com.company.entity.BankNoteType;
import com.company.entity.CardHolder;
import com.company.entity.Currency;
import com.company.exps.AlreadyExistException;
import com.company.exps.NotFoundException;
import com.company.mapping.BankNoteMapper;
import com.company.repository.BankNoteRepository;
import com.company.repository.BanknoteTypeRepository;
import com.company.repository.CurrencyRepository;
import com.company.service.BankNoteService;
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
    private final NetworkDataService networkDataService;
    /**
     * Creates a new bank note.
     *
     * @param bankNoteReqDto        Details of the new bank note
     * @param httpServletRequest    HttpServletRequest object for additional context
     * @return                      The newly created bank note as a response DTO
     * @throws AlreadyExistException    If a bank note with the same amount already exists
     * @throws NotFoundException       If the currency or bank note type is not found
     */
    @Override
    public BankNoteResDto createBankNote(BankNoteReqDto bankNoteReqDto, HttpServletRequest httpServletRequest) {
        String ClientInfo = networkDataService.getClientIPv4Address(httpServletRequest);
        String ClientIP = networkDataService.getRemoteUserInfo(httpServletRequest);

        BankNote byName = bankNoteRepository.findByAmount(bankNoteReqDto.getAmount());
        if (byName != null) {
            LOG.info("This Banknote already have  \t\t {}", bankNoteReqDto);
            LOG.info("Client host : \t\t {}", ClientInfo);
            LOG.info("Client IP :  \t\t {}", ClientIP);
            throw new AlreadyExistException("This Banknote already have");
        }
        BankNote bankNote = bankNoteMapper.toEntity(bankNoteReqDto);
        Optional<Currency> currencyById = currencyRepository.findById(bankNoteReqDto.getCurrencyId());
        if (currencyById.isEmpty()) {
            LOG.info("Currency Not Found \t\t {}", bankNoteReqDto);
            LOG.info("Client host : \t\t {}", ClientInfo);
            LOG.info("Client IP :  \t\t {}", ClientIP);
            throw new NotFoundException("Currency Not Found ");
        }
        Optional<BankNoteType> byId = banknoteTypeRepository.findById(bankNoteReqDto.getTypeId());
        if (byId.isEmpty()) {
            LOG.info("Banknote type Not found  \t\t {}", bankNoteReqDto);
            LOG.info("Client host : \t\t {}", ClientInfo);
            LOG.info("Client IP :  \t\t {}", ClientIP);
            throw new NotFoundException("Banknote type Not found");
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
        LOG.info("Create BankNote  \t\t {}", bankNoteReqDto);
        LOG.info("Client host : \t\t {}", ClientInfo);
        LOG.info("Client IP :  \t\t {}", ClientIP);

        return dto;
    }
}
