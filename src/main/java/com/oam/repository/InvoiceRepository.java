package com.oam.repository;

import com.oam.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {

    @Query(
            """
            SELECT DISTINCT(i.documentUrl) FROM Invoice i WHERE i.number = :number
            """)
    List<String> findInvoiceUrlByNumber(@Param("number") String number);

    @Query(
           """
           SELECT i FROM Invoice i
           JOIN i.association.associationMembers am
           WHERE am.member.id = :userId
           """)
    List<Invoice> findAllByUserId(@Param("userId") UUID userId);
}
