package com.oam.mapper;

import com.oam.dto.CreateUserRequestDto;
import com.oam.dto.UpdateUserRequestDto;
import com.oam.dto.UserResponseDto;
import com.oam.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "userDetails.callingCode", target = "callingCode")
    @Mapping(source = "userDetails.phoneNumber", target = "phoneNumber")
    @Mapping(source = "userDetails.firstName", target = "firstName")
    @Mapping(source = "userDetails.lastName", target = "lastName")
    @Mapping(source = "userDetails.description", target = "description")
    @Mapping(source = "userDetails.profilePictureURL", target = "profilePictureURL")
    UserResponseDto mapToDto(User user);

    @Mapping(source = "callingCode", target = "userDetails.callingCode")
    @Mapping(source = "phoneNumber", target = "userDetails.phoneNumber")
    @Mapping(source = "firstName", target = "userDetails.firstName")
    @Mapping(source = "lastName", target = "userDetails.lastName")
    User mapToEntity(CreateUserRequestDto createUserRequestDto);

    @Mapping(source = "callingCode", target = "userDetails.callingCode")
    @Mapping(source = "phoneNumber", target = "userDetails.phoneNumber")
    @Mapping(source = "firstName", target = "userDetails.firstName")
    @Mapping(source = "lastName", target = "userDetails.lastName")
    @Mapping(source = "description", target = "userDetails.description")
    User mapToEntity(UpdateUserRequestDto updateUserRequestDto);
}
