package com.company.mapping;

import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Set;

public interface EntityMapping<E, RQ, RS> {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    E toEntity(RQ req);
    E toEntity1(RS req);

    RS toDto(E entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    E updateFromDto(RQ req, @MappingTarget E entity);
    E updateFromEntity(E ent, @MappingTarget E entity);

    List<E> toEntity(List<RQ> dtoList);

  List<RS> toDto(List<E> entityList);
    List<RS> toDto(Page<E> entityList);

    Set<E> toEntity(Set<RQ> rqSet);

    Set<RS> toDto(Set<E> eSet);
}
