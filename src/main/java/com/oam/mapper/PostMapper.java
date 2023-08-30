package com.oam.mapper;

import com.oam.dto.CreatePostRequestDto;
import com.oam.dto.PostResponseDto;
import com.oam.dto.UpdatePostRequestDto;
import com.oam.model.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { AssociationMapper.class, UserMapper.class })
public interface PostMapper {

    PostResponseDto mapToDto(Post post);

    @Mapping(source = "associationId", target = "association.id")
    Post mapToEntity(CreatePostRequestDto createPostRequestDto);

    Post mapToEntity(UpdatePostRequestDto updatePostRequestDto);
}
