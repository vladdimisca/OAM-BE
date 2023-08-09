package com.oam.mapper;

import com.oam.dto.ApartmentResponseDto;
import com.oam.dto.CreateApartmentRequestDto;
import com.oam.dto.UpdateApartmentRequestDto;
import com.oam.dto.UserResponseDto;
import com.oam.model.Apartment;
import com.oam.model.AssociationMember;
import com.oam.model.AssociationRole;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(injectionStrategy = InjectionStrategy.CONSTRUCTOR, componentModel = "spring", uses = {UserMapper.class, AssociationMapper.class})
public abstract class ApartmentMapper {

    @Autowired
    protected UserMapper userMapper;

    @Mapping(target = "members", expression = "java(getMembers(apartment))")
    @Mapping(target = "admins", expression = "java(getAdmins(apartment))")
    public abstract ApartmentResponseDto mapToDto(Apartment apartment);

    @Mapping(source = "associationId", target = "association.id")
    public abstract Apartment mapToEntity(CreateApartmentRequestDto createApartmentRequestDto);


    public abstract Apartment mapToEntity(UpdateApartmentRequestDto updateApartmentRequestDto);

    public List<UserResponseDto> getMembers(Apartment apartment) {
        return apartment.getAssociationMembers().stream()
                .filter(associationMember -> associationMember.getRole() == AssociationRole.MEMBER)
                .map(AssociationMember::getMember)
                .map(userMapper::mapToDto)
                .toList();
    }

    public List<UserResponseDto> getAdmins(Apartment apartment) {
        return apartment.getAssociation().getAssociationMembers().stream()
                .filter(associationMember -> associationMember.getRole() == AssociationRole.ADMIN)
                .map(AssociationMember::getMember)
                .map(userMapper::mapToDto)
                .toList();
    }
}
