package com.company.mapping;

import com.company.dto.request.CardReqDto;
import com.company.dto.response.CardResDto;
import com.company.entity.Card;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CardMapper extends EntityMapping<Card, CardReqDto, CardResDto> {
}
