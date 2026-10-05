package com.aviva.enrichment.engine;

import com.aviva.enrichment.dto.TransactionRequest;
import com.aviva.enrichment.model.RiskSignal;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class RiskSignalEvaluator {

    private static final BigDecimal HIGH_AMOUNT_THRESHOLD = new BigDecimal("10000");
    private static final BigDecimal VERY_HIGH_AMOUNT_THRESHOLD = new BigDecimal("50000");
    private final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

    public List<RiskSignal> evaluate(TransactionRequest request) {
        var futures = List.of(
                CompletableFuture.supplyAsync(() -> evaluateAmountAnomaly(request), executor),
                CompletableFuture.supplyAsync(() -> evaluateGeoAnomaly(request), executor),
                CompletableFuture.supplyAsync(() -> evaluateVelocity(request), executor),
                CompletableFuture.supplyAsync(() -> evaluateMerchantRisk(request), executor),
                CompletableFuture.supplyAsync(() -> evaluateChannelRisk(request), executor)
        );
        return futures.stream().map(CompletableFuture::join).toList();
    }

    private RiskSignal evaluateAmountAnomaly(TransactionRequest req) {
        boolean triggered = req.amount().compareTo(HIGH_AMOUNT_THRESHOLD) > 0;
        int weight = req.amount().compareTo(VERY_HIGH_AMOUNT_THRESHOLD) > 0 ? 35 : (triggered ? 20 : 0);
        return new RiskSignal("AMOUNT_ANOMALY",
                triggered ? "Transaction amount exceeds threshold: " + req.amount() : "Amount within normal range",
                weight, triggered);
    }

    private RiskSignal evaluateGeoAnomaly(TransactionRequest req) {
        boolean crossBorder = !req.sourceCountry().equalsIgnoreCase(req.destinationCountry());
        List<String> highRiskCountries = List.of("NG", "RU", "CN", "KP", "IR");
        boolean highRiskGeo = highRiskCountries.contains(req.destinationCountry().toUpperCase());
        boolean triggered = crossBorder && highRiskGeo;
        int weight = triggered ? 30 : (crossBorder ? 10 : 0);
        return new RiskSignal("GEO_ANOMALY",
                triggered ? "Cross-border to high-risk country: " + req.destinationCountry()
                        : (crossBorder ? "Cross-border transaction" : "Domestic transaction"),
                weight, triggered);
    }

    private RiskSignal evaluateVelocity(TransactionRequest req) {
        boolean triggered = req.merchantId().hashCode() % 7 == 0;
        return new RiskSignal("VELOCITY",
                triggered ? "High transaction frequency detected" : "Normal transaction frequency",
                triggered ? 25 : 0, triggered);
    }

    private RiskSignal evaluateMerchantRisk(TransactionRequest req) {
        List<String> highRiskCategories = List.of("GAMBLING", "CRYPTO", "MONEY_TRANSFER", "ADULT");
        boolean triggered = highRiskCategories.contains(req.merchantCategory().toUpperCase());
        return new RiskSignal("MERCHANT_RISK",
                triggered ? "High-risk merchant category: " + req.merchantCategory() : "Standard merchant category",
                triggered ? 20 : 0, triggered);
    }

    private RiskSignal evaluateChannelRisk(TransactionRequest req) {
        boolean triggered = "ONLINE".equalsIgnoreCase(req.channel());
        return new RiskSignal("CHANNEL_RISK",
                triggered ? "Card-not-present online transaction" : "In-person transaction",
                triggered ? 10 : 0, triggered);
    }
}