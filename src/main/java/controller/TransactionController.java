package com.aviva.enrichment.controller;

import com.aviva.enrichment.model.Transaction;
import com.aviva.enrichment.service.EnrichmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final EnrichmentService enrichmentService;

    public TransactionController(EnrichmentService enrichmentService) {
        this.enrichmentService = enrichmentService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransaction(@PathVariable UUID id) {
        return ResponseEntity.ok(enrichmentService.getTransaction(id));
    }

    @GetMapping("/high-risk")
    public ResponseEntity<List<Transaction>> getHighRisk(
            @RequestParam(defaultValue = "50") int minScore) {
        return ResponseEntity.ok(enrichmentService.getHighRiskTransactions(minScore));
    }
}