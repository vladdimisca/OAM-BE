package com.oam.repository;

import com.oam.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    @Query(
            """
            SELECT p FROM Payment p
            JOIN p.invoiceDistributions id
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
    List<Payment> findAllByUserId(@Param("userId") UUID userId);
}
