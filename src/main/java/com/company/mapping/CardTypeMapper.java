package com.company.mapping;

import com.company.dto.request.CardTypeReqDto;
import com.company.dto.response.CardTypeResDto;
import com.company.entity.CardType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CardTypeMapper extends EntityMapping<CardType, CardTypeReqDto, CardTypeResDto> {
}
