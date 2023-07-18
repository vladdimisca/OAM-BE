package com.oam.repository;

import com.oam.model.Association;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AssociationRepository extends JpaRepository<Association, UUID> {
}
