package com.oam.mapper;

import com.oam.dto.ApartmentResponseDto;
import com.oam.dto.CreateApartmentRequestDto;
import com.oam.model.Apartment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ApartmentMapper {

    @Mapping(source = "association.id", target = "associationId")
    ApartmentResponseDto mapToDto(Apartment apartment);

    @Mapping(source = "associationId", target = "association.id")
    Apartment mapToEntity(CreateApartmentRequestDto createApartmentRequestDto);
}
