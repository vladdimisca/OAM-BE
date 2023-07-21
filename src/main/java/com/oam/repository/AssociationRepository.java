package com.oam.repository;

import com.oam.model.Apartment;
import com.oam.model.Association;
import com.oam.model.AssociationRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AssociationRepository extends JpaRepository<Association, UUID> {

    @Query(
        """
        SELECT association FROM Association association
        JOIN association.apartments apartment
        WHERE apartment.code = :code
        """)
    Optional<Association> findAssociationByApartmentCode(@Param("code") String code);

    @Query(
        """
        SELECT a FROM Association a
        JOIN AssociationMember am ON am.association = a
        WHERE am.member.id = :userId AND (:role IS NULL OR am.role = :role)
        """)
    List<Association> findAllByUserIdAndRole(@Param("userId") UUID userId, @Param("role") AssociationRole role);

    @Modifying
    @Query(
        """
        DELETE FROM AssociationMember am WHERE am.association.id = :associationId
        """)
    void deleteAssociationMembersByFamilyId(@Param("associationId") UUID associationId);
}
