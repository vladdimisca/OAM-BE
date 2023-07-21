package com.oam.repository;

import com.oam.model.InvoiceDistribution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface InvoiceDistributionRepository extends JpaRepository<InvoiceDistribution, UUID> {
}
