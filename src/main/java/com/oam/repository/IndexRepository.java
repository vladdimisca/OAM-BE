package com.oam.repository;

import com.oam.model.Index;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IndexRepository extends JpaRepository<Index, UUID> {

    @Query(
        """
        SELECT ind FROM Index ind
        WHERE ind IN (
            SELECT i FROM Index i
            JOIN i.apartment.association.associationMembers asm
            WHERE asm.role = com.oam.model.AssociationRole.ADMIN
        )
        OR ind IN (
            SELECT i FROM Index i
            JOIN i.apartment.associationMembers apm
            WHERE apm.member.id = :userId
        )
        """)
    List<Index> findAllByUserId(@Param("userId") UUID userId);
}
