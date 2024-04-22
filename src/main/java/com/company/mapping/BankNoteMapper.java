package com.company.mapping;

import com.company.dto.request.BankNoteReqDto;
import com.company.dto.request.CardReqDto;
import com.company.dto.response.BankNoteResDto;
import com.company.dto.response.CardResDto;
import com.company.entity.BankNote;
import com.company.entity.Card;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BankNoteMapper extends EntityMapping<BankNote, BankNoteReqDto, BankNoteResDto> {
}
