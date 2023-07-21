package com.oam.mapper;

import com.oam.dto.AssociationResponseDto;
import com.oam.dto.CreateAssociationRequestDto;
import com.oam.dto.UpdateAssociationRequestDto;
import com.oam.model.Association;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AssociationMapper {

    AssociationResponseDto mapToDto(Association association);

    Association mapToEntity(CreateAssociationRequestDto createAssociationRequestDto);

    Association mapToEntity(UpdateAssociationRequestDto updateAssociationRequestDto);
}
