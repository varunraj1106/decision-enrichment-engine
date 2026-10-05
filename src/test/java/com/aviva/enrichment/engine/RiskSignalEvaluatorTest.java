package com.aviva.enrichment.engine;

import com.aviva.enrichment.dto.TransactionRequest;
import com.aviva.enrichment.model.RiskSignal;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RiskSignalEvaluatorTest {

    private final RiskSignalEvaluator evaluator = new RiskSignalEvaluator();

    @Test
    void shouldReturnFiveSignals() {
        TransactionRequest request = new TransactionRequest(
                "MERCH-001", "CUST-001", new BigDecimal("100"),
                "GBP", "GROCERY", "GB", "GB", "POS"
        );

        List<RiskSignal> signals = evaluator.evaluate(request);
        assertEquals(5, signals.size());
    }

    @Test
    void shouldTriggerAmountAnomalyForHighAmount() {
        TransactionRequest request = new TransactionRequest(
                "MERCH-001", "CUST-001", new BigDecimal("55000"),
                "USD", "RETAIL", "GB", "GB", "POS"
        );

        List<RiskSignal> signals = evaluator.evaluate(request);
        RiskSignal amountSignal = signals.stream()
                .filter(s -> s.getSignalType().equals("AMOUNT_ANOMALY"))
                .findFirst().orElseThrow();

        assertTrue(amountSignal.isTriggered());
        assertEquals(35, amountSignal.getWeight());
    }

    @Test
    void shouldTriggerGeoAnomalyForHighRiskCountry() {
        TransactionRequest request = new TransactionRequest(
                "MERCH-001", "CUST-001", new BigDecimal("100"),
                "USD", "RETAIL", "GB", "KP", "POS"
        );

        List<RiskSignal> signals = evaluator.evaluate(request);
        RiskSignal geoSignal = signals.stream()
                .filter(s -> s.getSignalType().equals("GEO_ANOMALY"))
                .findFirst().orElseThrow();

        assertTrue(geoSignal.isTriggered());
        assertEquals(30, geoSignal.getWeight());
    }

    @Test
    void shouldTriggerMerchantRiskForGambling() {
        TransactionRequest request = new TransactionRequest(
                "MERCH-001", "CUST-001", new BigDecimal("100"),
                "GBP", "GAMBLING", "GB", "GB", "POS"
        );

        List<RiskSignal> signals = evaluator.evaluate(request);
        RiskSignal merchantSignal = signals.stream()
                .filter(s -> s.getSignalType().equals("MERCHANT_RISK"))
                .findFirst().orElseThrow();

        assertTrue(merchantSignal.isTriggered());
        assertEquals(20, merchantSignal.getWeight());
    }

    @Test
    void shouldTriggerChannelRiskForOnline() {
        TransactionRequest request = new TransactionRequest(
                "MERCH-001", "CUST-001", new BigDecimal("100"),
                "GBP", "RETAIL", "GB", "GB", "ONLINE"
        );

        List<RiskSignal> signals = evaluator.evaluate(request);
        RiskSignal channelSignal = signals.stream()
                .filter(s -> s.getSignalType().equals("CHANNEL_RISK"))
                .findFirst().orElseThrow();

        assertTrue(channelSignal.isTriggered());
        assertEquals(10, channelSignal.getWeight());
    }
}