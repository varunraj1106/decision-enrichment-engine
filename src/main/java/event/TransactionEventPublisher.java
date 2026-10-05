package com.aviva.enrichment.event;

import com.aviva.enrichment.dto.EnrichmentResult;
import com.aviva.enrichment.event.TransactionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import java.time.Instant;

@Component
public class TransactionEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(TransactionEventPublisher.class);

    @Async
    public void publish(EnrichmentResult result) {
        TransactionEvent event = new TransactionEvent(
                "TRANSACTION_DECIDED",
                result,
                Instant.now()
        );
        // In production this would publish to Kafka/RabbitMQ
        log.info("Published event: type={}, transactionId={}, decision={}, riskScore={}",
                event.eventType(),
                event.result().transactionId(),
                event.result().decision(),
                event.result().riskScore());
    }
}