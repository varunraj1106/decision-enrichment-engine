package com.aviva.enrichment.repository;

import com.aviva.enrichment.model.DecisionOutcome;
import com.aviva.enrichment.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    List<Transaction> findByCustomerId(String customerId);

    Page<Transaction> findByDecision(DecisionOutcome decision, Pageable pageable);

    @Query("SELECT t FROM Transaction t WHERE t.riskScore >= :minScore ORDER BY t.createdAt DESC")
    List<Transaction> findHighRiskTransactions(@Param("minScore") int minScore);

    @Query("SELECT t FROM Transaction t WHERE t.createdAt BETWEEN :start AND :end")
    List<Transaction> findByDateRange(@Param("start") Instant start, @Param("end") Instant end);

    long countByDecision(DecisionOutcome decision);
}