package com.company.mapping;

import com.company.dto.request.CardHolderReqDto;
import com.company.dto.response.CardHolderResDto;
import com.company.entity.CardHolder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CardHolderMapper extends EntityMapping<CardHolder, CardHolderReqDto, CardHolderResDto> {
}
