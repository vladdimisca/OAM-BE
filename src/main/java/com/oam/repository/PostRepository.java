package com.oam.repository;

import com.oam.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {

    @Query(
            """
            SELECT p FROM Post p
            JOIN p.association.associationMembers am
            WHERE am.member.id = :userId
            ORDER BY p.createdAt
            """)
    List<Post> findAllByUserId(@Param("userId") UUID userId);
}
