package com.company.service.impl;

import com.company.dto.request.CardTypeReqDto;
import com.company.dto.response.CardTypeResDto;
import com.company.dto.response.CurrencyResDto;
import com.company.entity.CardType;
import com.company.entity.Currency;
import com.company.exps.CardTypeException;
import com.company.mapping.CardTypeMapper;
import com.company.repository.CardTypeRepository;
import com.company.repository.CurrencyRepository;
import com.company.service.CardtypeService;
import com.company.service.CheckService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CardTypeServiceImpl implements CardtypeService {


    private final CardTypeMapper cardTypeMapper;
    private final CardTypeRepository cardTypeRepository;
    private final CurrencyRepository currencyRepository;
    private final static Logger LOG = LoggerFactory.getLogger(CardType.class);
    private final CheckService checkService;

    /**
     * create card
     *
     * @param cardTypeReqDto     details of new card
     * @param httpServletRequest define to device
     * @return new card
     */
    @Override
    public CardTypeResDto createCardType(CardTypeReqDto cardTypeReqDto, HttpServletRequest httpServletRequest) {
        try {
            checkService.checkDevice(httpServletRequest);
            cardTypeReqDto.setName(cardTypeReqDto.getName().toUpperCase());
            Optional<CardType> cardTypeByName = cardTypeRepository.findByName(cardTypeReqDto.getName().toUpperCase());
            if (cardTypeByName.isPresent()) {
                throw new CardTypeException("This Card type Already Have Our Bank");
            }
            Optional<Currency> currencyById = currencyRepository.findById(cardTypeReqDto.getCurrencyId());
            if (currencyById.isEmpty()) {
                throw new CardTypeException("Currency is Not Found : Currency Id is Wrong");
            }
            Currency currency = currencyById.get();
            CurrencyResDto currencyResDto = new CurrencyResDto(currency.getId(), currency.getName());

            CardType cardtype = cardTypeMapper.toEntity(cardTypeReqDto);
            cardtype.setCurrency(currency);
            cardTypeRepository.save(cardtype);
            CardTypeResDto dto = cardTypeMapper.toDto(cardtype);
            dto.setCurrency(currencyResDto);
            LOG.info("Create Card Type  \t\t {}", cardTypeReqDto);
            return dto;
        } catch (Exception e) {
            throw new CardTypeException("Can not Create Card Type :  " + e.getMessage());
        }
    }

    /**
     * Retrieves all card types.
     *
     * @param httpServletRequest HttpServletRequest object for additional context
     * @return List of card type response DTOs
     */
    @Override

    public List<CardTypeResDto> getAllCardType(HttpServletRequest httpServletRequest) {
        try {
            checkService.checkDevice(httpServletRequest);
            List<CardType> all = cardTypeRepository.findAll();
            LOG.info("Get All CardType   \t\t {}", "");
            return cardTypeMapper.toDto(all);
        } catch (Exception e) {
            throw new CardTypeException("get all Card Type  :  " + e.getMessage());
        }
    }


    /**
     * Updates a card type by name.
     *
     * @param name               The name of the card type to update
     * @param cardTypeReqDto     Updated details of the card type
     * @param httpServletRequest HttpServletRequest object for additional context
     * @return The updated card type as a response DTO
     * @throws CardTypeException If the card type with the given name is not found
     */
    @Override
    public CardTypeResDto updateCardType(String name, CardTypeReqDto cardTypeReqDto, HttpServletRequest httpServletRequest) {
        try {
            checkService.checkDevice(httpServletRequest);
            Optional<CardType> cardTypeByName = cardTypeRepository.findByName(name);
            if (cardTypeByName.isPresent()) {
                CardType cardType1 = cardTypeByName.get();
                CardType cardType = cardTypeMapper.updateFromDto(cardTypeReqDto, cardType1);
                if (cardTypeReqDto.getCurrencyId() != null) {
                    Optional<Currency> currencyById = currencyRepository.findById(cardTypeReqDto.getCurrencyId());

                    if (currencyById.isEmpty()) {
                        throw new CardTypeException("Currency Not Found");
                    }

                    Currency currency = currencyById.get();

                    cardType.setCurrency(currency);
                }

                cardTypeRepository.save(cardType);
                LOG.info("update card Type \t\t {}", cardTypeReqDto);
                return cardTypeMapper.toDto(cardType);
            }

            throw new CardTypeException("This Card type not Found");
        } catch (Exception e) {
            throw new CardTypeException("Can not Create  :  " + e.getMessage());
        }
    }

    /**
     * Deletes a card type by name.
     *
     * @param name               The name of the card type to delete
     * @param httpServletRequest HttpServletRequest object for additional context
     * @throws CardTypeException If the card type with the given name is not found If the deletion is successful
     */
    @Override
    public void deleteCardType(String name, HttpServletRequest httpServletRequest) {
        checkService.checkDevice(httpServletRequest);
        try {
            Optional<CardType> cardTypeByName = cardTypeRepository.findByName(name);
            if (cardTypeByName.isPresent()) {
                CardType cardType = cardTypeByName.get();
                cardTypeRepository.delete(cardType);
                LOG.info("Delete Card type by name\t\t {}", name);
                throw new CardTypeException("Successfully deleted");
            }
            throw new CardTypeException("This Card type not Found");
        }catch (Exception e){
            throw new CardTypeException(e.getMessage());
        }
    }
}