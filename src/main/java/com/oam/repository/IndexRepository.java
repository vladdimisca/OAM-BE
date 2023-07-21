package com.oam.repository;

import com.oam.model.Index;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IndexRepository extends JpaRepository<Index, UUID> {
}
