package com.company.mapping;

import com.company.dto.CardHolderReqDto;
import com.company.dto.CardHolderResDto;
import com.company.entity.CardHolder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CardHolderMapper extends EntityMapping<CardHolder, CardHolderReqDto, CardHolderResDto> {
}
