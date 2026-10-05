package com.aviva.enrichment.service;

import com.aviva.enrichment.dto.*;
import com.aviva.enrichment.engine.DecisionEngine;
import com.aviva.enrichment.engine.RiskSignalEvaluator;
import com.aviva.enrichment.event.TransactionEventPublisher;
import com.aviva.enrichment.model.*;
import com.aviva.enrichment.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class EnrichmentService {

    private static final Logger log = LoggerFactory.getLogger(EnrichmentService.class);

    private final RiskSignalEvaluator signalEvaluator;
    private final DecisionEngine decisionEngine;
    private final TransactionRepository transactionRepository;
    private final TransactionEventPublisher eventPublisher;

    public EnrichmentService(RiskSignalEvaluator signalEvaluator,
                             DecisionEngine decisionEngine,
                             TransactionRepository transactionRepository,
                             TransactionEventPublisher eventPublisher) {
        this.signalEvaluator = signalEvaluator;
        this.decisionEngine = decisionEngine;
        this.transactionRepository = transactionRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public EnrichmentResult enrichAndDecide(TransactionRequest request) {
        long start = System.currentTimeMillis();

        List<RiskSignal> signals = signalEvaluator.evaluate(request);
        DecisionOutcome decision = decisionEngine.decide(signals);
        int riskScore = decisionEngine.computeRiskScore(signals);

        Transaction transaction = new Transaction();
        transaction.setMerchantId(request.merchantId());
        transaction.setCustomerId(request.customerId());
        transaction.setAmount(request.amount());
        transaction.setCurrency(request.currency());
        transaction.setMerchantCategory(request.merchantCategory());
        transaction.setSourceCountry(request.sourceCountry());
        transaction.setDestinationCountry(request.destinationCountry());
        transaction.setChannel(request.channel());
        transaction.setDecision(decision);
        transaction.setRiskScore(riskScore);
        transaction.setProcessedAt(Instant.now());

        Transaction saved = transactionRepository.save(transaction);

        List<RiskSignalDto> signalDtos = signals.stream()
                .map(s -> new RiskSignalDto(s.getSignalType(), s.getDescription(), s.getWeight(), s.isTriggered()))
                .toList();

        long processingTime = System.currentTimeMillis() - start;

        EnrichmentResult result = new EnrichmentResult(
                saved.getId(), decision, riskScore, signalDtos, processingTime
        );

        eventPublisher.publish(result);

        log.info("Transaction {} decided: {} (score={}, time={}ms)",
                saved.getId(), decision, riskScore, processingTime);

        return result;
    }

    public List<Transaction> getHighRiskTransactions(int minScore) {
        return transactionRepository.findHighRiskTransactions(minScore);
    }

    public Transaction getTransaction(UUID id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found: " + id));
    }
}