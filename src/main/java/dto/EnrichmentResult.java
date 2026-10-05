package com.aviva.enrichment.dto;

import com.aviva.enrichment.model.DecisionOutcome;
import java.util.List;
import java.util.UUID;

public record EnrichmentResult(
        UUID transactionId,
        DecisionOutcome decision,
        int riskScore,
        List<com.aviva.enrichment.dto.RiskSignalDto> signals,
        long processingTimeMs
) {}