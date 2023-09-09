package com.oam.repository;

import com.oam.model.Apartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ApartmentRepository extends JpaRepository<Apartment, UUID> {

    @Modifying
    @Query(
            """
            DELETE FROM AssociationMember am WHERE am.apartment IS NOT NULL AND am.apartment.id = :apartmentId
            """)
    void deleteApartmentMembersByApartmentId(@Param("apartmentId") UUID apartmentId);

    List<Apartment> findAllByAssociation_Id(UUID associationId);

    @Query(
            """
            SELECT ap FROM Apartment ap
            WHERE ap IN (
                SELECT a FROM Apartment a
                JOIN a.association.associationMembers asm
                WHERE asm.role = com.oam.model.AssociationRole.ADMIN
            )
            OR ap IN (
                SELECT a FROM Apartment a
                JOIN a.associationMembers apm
                WHERE apm.member.id = :userId
            )
            """)
    List<Apartment> findAllByUserId(@Param("userId") UUID userId);

    @Query(
            """
            SELECT ap FROM Apartment ap
            WHERE ap IN (
                SELECT a FROM Apartment a
                JOIN a.associationMembers apm
                WHERE apm.member.id = :userId
            )
            """)
    List<Apartment> findAllByUserIdAsMember(@Param("userId") UUID userId);
}
