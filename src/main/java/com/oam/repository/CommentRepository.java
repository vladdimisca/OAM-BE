package com.oam.repository;

import com.oam.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID> {

    @Query(
            """
            SELECT c FROM Comment c
            WHERE :postId IS NULL OR c.post.id = :postId
            ORDER BY c.createdAt
            """)
    List<Comment> findAllByPostId(@Param("postId") UUID postId);
}
