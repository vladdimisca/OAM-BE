package com.oam.mapper;

import com.oam.dto.*;
import com.oam.model.Comment;
import com.oam.model.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { UserMapper.class, PostMapper.class })
public interface CommentMapper {

    CommentResponseDto mapToDto(Comment comment);

    @Mapping(source = "postId", target = "post.id")
    Comment mapToEntity(CreateCommentRequestDto createCommentRequestDto);

    Comment mapToEntity(UpdateCommentRequestDto updateCommentRequestDto);
}
