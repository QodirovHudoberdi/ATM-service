package com.company.service.impl;

import com.company.dto.request.CardTypeReqDto;
import com.company.dto.response.CardTypeResDto;
import com.company.dto.response.CurrencyResDto;
import com.company.entity.CardType;
import com.company.entity.Currency;
import com.company.exps.AlreadyExistException;
import com.company.exps.NotFoundException;
import com.company.exps.OkResponse;
import com.company.mapping.CardTypeMapper;
import com.company.repository.CardTypeRepository;
import com.company.repository.CurrencyRepository;
import com.company.service.CardtypeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CardTypeServiceImpl implements CardtypeService {


    private final CardTypeMapper cardTypeMapper;
    private final CardTypeRepository cardTypeRepository;
    private final CurrencyRepository currencyRepository;

    /**
     * create card
     * @param cardTypeReqDto details of new card
     * @param httpServletRequest define to device
     * @return new card
     */
    @Override
    public CardTypeResDto createCardType(CardTypeReqDto cardTypeReqDto, HttpServletRequest httpServletRequest) {
        cardTypeReqDto.setName(cardTypeReqDto.getName().toUpperCase());
        Optional<CardType> cardTypeByName = cardTypeRepository.findByName(cardTypeReqDto.getName().toUpperCase());
        if (cardTypeByName.isPresent()) {
            throw new AlreadyExistException("This Card type Already Have Our Bank");
        }
        Optional<Currency> currencyById = currencyRepository.findById(cardTypeReqDto.getCurrencyId());
        if (currencyById.isEmpty()) {
            throw new NotFoundException("Currency is Not Found : Currency Id is Wrong");
        }
        Currency currency = currencyById.get();
        CurrencyResDto currencyResDto = new CurrencyResDto(currency.getId(), currency.getName());

        CardType cardtype = cardTypeMapper.toEntity(cardTypeReqDto);
        cardtype.setCurrency(currency);
        cardTypeRepository.save(cardtype);
        CardTypeResDto dto = cardTypeMapper.toDto(cardtype);
        dto.setCurrency(currencyResDto);
        return dto;
    }
    /**
     * Retrieves all card types.
     *
     * @param httpServletRequest    HttpServletRequest object for additional context
     * @return                      List of card type response DTOs
     */
    @Override

    public List<CardTypeResDto> getAllCardType(HttpServletRequest httpServletRequest) {
        // Retrieve all card types from the repository
        List<CardType> all = cardTypeRepository.findAll();

        // Map the card types to response DTOs using the mapper
        return cardTypeMapper.toDto(all);
    }


    /**
     * Updates a card type by name.
     *
     * @param name                  The name of the card type to update
     * @param cardTypeReqDto        Updated details of the card type
     * @param httpServletRequest    HttpServletRequest object for additional context
     * @return                      The updated card type as a response DTO
     * @throws NotFoundException   If the card type with the given name is not found
     */
    @Override
    public CardTypeResDto updateCardType(String name, CardTypeReqDto cardTypeReqDto, HttpServletRequest httpServletRequest) {
        Optional<CardType> cardTypeByName = cardTypeRepository.findByName(name);

        if (cardTypeByName.isPresent()) {
            CardType cardType1 = cardTypeByName.get();
            CardType cardType = cardTypeMapper.updateFromDto(cardTypeReqDto, cardType1);
            if (cardTypeReqDto.getCurrencyId() != null) {
                Optional<Currency> currencyById = currencyRepository.findById(cardTypeReqDto.getCurrencyId());

                if (currencyById.isEmpty()) {
                    throw new NotFoundException("Currency Not Found");
                }

                Currency currency = currencyById.get();

                cardType.setCurrency(currency);
            }

            cardTypeRepository.save(cardType);

            return cardTypeMapper.toDto(cardType);
        }

        throw new NotFoundException("This Card type not Found");
    }
    /**
     * Deletes a card type by name.
     *
     * @param name                  The name of the card type to delete
     * @param httpServletRequest    HttpServletRequest object for additional context
     * @throws NotFoundException   If the card type with the given name is not found
     * @throws OkResponse           If the deletion is successful
     */
    @Override
    public void deleteCardType(String name, HttpServletRequest httpServletRequest) {
        Optional<CardType> cardTypeByName = cardTypeRepository.findByName(name);
        if (cardTypeByName.isPresent()) {
            CardType cardType = cardTypeByName.get();
            cardTypeRepository.delete(cardType);
            throw new OkResponse("Successfully deleted");
        }
        throw new NotFoundException("This Card type not Found");
    }
}