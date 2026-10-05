package com.aviva.enrichment.event;

import com.aviva.enrichment.dto.EnrichmentResult;
import java.time.Instant;

public record TransactionEvent(
        String eventType,
        EnrichmentResult result,
        Instant timestamp
) {}