package com.aviva.enrichment.controller;

import com.aviva.enrichment.dto.EnrichmentResult;
import com.aviva.enrichment.dto.TransactionRequest;
import com.aviva.enrichment.service.EnrichmentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/enrichment")
public class EnrichmentController {

    private final EnrichmentService enrichmentService;

    public EnrichmentController(EnrichmentService enrichmentService) {
        this.enrichmentService = enrichmentService;
    }

    @PostMapping("/decide")
    public ResponseEntity<EnrichmentResult> decide(@Valid @RequestBody TransactionRequest request) {
        EnrichmentResult result = enrichmentService.enrichAndDecide(request);
        return ResponseEntity.ok(result);
    }
}