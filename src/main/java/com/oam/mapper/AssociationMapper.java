package com.oam.mapper;

import com.oam.dto.*;
import com.oam.model.Association;
import com.oam.model.AssociationMember;
import com.oam.model.AssociationRole;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(injectionStrategy = InjectionStrategy.CONSTRUCTOR, componentModel = "spring", uses = UserMapper.class)
public abstract class AssociationMapper {

    @Autowired
    protected UserMapper userMapper;

    @Mapping(target = "members", expression = "java(getMembers(association))")
    @Mapping(target = "admins", expression = "java(getAdmins(association))")
    @Mapping(target = "apartments", expression = "java(getSimplifiedApartmentsResponse(association))")
    public abstract AssociationResponseDto mapToDto(Association association);

    public abstract Association mapToEntity(CreateAssociationRequestDto createAssociationRequestDto);

    public abstract Association mapToEntity(UpdateAssociationRequestDto updateAssociationRequestDto);

    public List<UserResponseDto> getMembers(Association association) {
        return association.getAssociationMembers().stream()
                .filter(associationMember -> associationMember.getRole() == AssociationRole.MEMBER)
                .map(AssociationMember::getMember)
                .map(userMapper::mapToDto)
                .toList();
    }

    public List<UserResponseDto> getAdmins(Association association) {
        return association.getAssociationMembers().stream()
                .filter(associationMember -> associationMember.getRole() == AssociationRole.ADMIN)
                .map(AssociationMember::getMember)
                .map(userMapper::mapToDto)
                .toList();
    }

    public List<SimplifiedApartmentResponseDto> getSimplifiedApartmentsResponse(Association association) {
        List<UserResponseDto> members = association.getApartments().stream()
                .flatMap(apartment -> apartment.getAssociationMembers().stream())
                .map(AssociationMember::getMember)
                .map(userMapper::mapToDto)
                .toList();
        return association.getApartments().stream()
                .map(apartment -> new SimplifiedApartmentResponseDto(apartment.getId(), apartment.getNumber(), members))
                .toList();
    }
}
