package com.company.service.impl;

import com.company.dto.request.CardHolderReqDto;
import com.company.dto.response.CardHolderResDto;
import com.company.entity.CardHolder;
import com.company.exps.CardHolderException;
import com.company.mapping.CardHolderMapper;
import com.company.repository.CardHolderRepository;
import com.company.service.CardHolderService;
import com.company.service.CheckService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;


@RequiredArgsConstructor
@Service
public class CardHolderServiceImpl implements CardHolderService {
    private final static Logger LOG = LoggerFactory.getLogger(CardHolder.class);
    private final CardHolderMapper cardHolderMapper;
    private final CardHolderRepository cardHolderRepository;
    private final CheckService checkService;

    /**
     * Creates a new cardHolder with the specified details.
     *
     * @param cardHolderReqDto   The request DTO containing the CardHolder details.
     * @param httpServletRequest The HTTP servlet request object.
     * @return The response DTO containing the newly created CardHolder details.
     * @throws CardHolderException If a CardHolder with the same series number already exists.
     */
    @Override
    public CardHolderResDto createCardHolder(CardHolderReqDto cardHolderReqDto,
                                             HttpServletRequest httpServletRequest) {
        try {
            checkService.checkDevice(httpServletRequest);
            LOG.info("Create Card Holder  \t\t {}", cardHolderReqDto);
            String seriesNum = cardHolderReqDto.getPassport().toUpperCase() + cardHolderReqDto.getNumber();

            Optional<CardHolder> cardHolderBySeriesNum = cardHolderRepository.findBySeriesNum(seriesNum);
            if (cardHolderBySeriesNum.isPresent()) {
                throw new CardHolderException("This Client already with us");
            }
            CardHolder holder = cardHolderMapper.toEntity(cardHolderReqDto);
            holder.setSeriesNum(seriesNum);
            cardHolderRepository.save(holder);
            LOG.info("Create Card Holder  \t\t {}", cardHolderReqDto);
            return cardHolderMapper.toDto(holder);
        } catch (Exception e) {
            throw new CardHolderException("Can not Create card Holder : " + e.getMessage());
        }
    }
}
