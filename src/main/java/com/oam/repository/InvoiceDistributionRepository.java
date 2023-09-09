package com.oam.repository;

import com.oam.model.InvoiceDistribution;
import com.oam.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InvoiceDistributionRepository extends JpaRepository<InvoiceDistribution, UUID> {

    @Query(
            """
            SELECT id FROM InvoiceDistribution id
            WHERE id.apartment IN (
                SELECT a FROM Apartment a
                JOIN a.association.associationMembers asm
                WHERE asm.role = com.oam.model.AssociationRole.ADMIN
            )
            OR id.apartment IN (
                SELECT a FROM Apartment a
                JOIN a.associationMembers apm
                WHERE apm.member.id = :userId
            )
            """)
    List<InvoiceDistribution> findAllByUserId(@Param("userId") UUID userId);
}
