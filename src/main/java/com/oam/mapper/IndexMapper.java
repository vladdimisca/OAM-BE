package com.oam.mapper;

import com.oam.dto.CreateIndexRequestDto;
import com.oam.dto.IndexResponseDto;
import com.oam.model.Index;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = ApartmentMapper.class)
public interface IndexMapper {

    IndexResponseDto mapToDto(Index index);

    @Mapping(source = "apartmentId", target = "apartment.id")
    Index mapToEntity(CreateIndexRequestDto createIndexRequestDto);
}
