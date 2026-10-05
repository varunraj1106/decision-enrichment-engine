package com.aviva.enrichment.engine;

import com.aviva.enrichment.model.DecisionOutcome;
import com.aviva.enrichment.model.RiskSignal;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DecisionEngineTest {

    private final DecisionEngine engine = new DecisionEngine();

    @Test
    void shouldApproveWhenNoSignalsTriggered() {
        List<RiskSignal> signals = List.of(
                new RiskSignal("AMOUNT_ANOMALY", "Normal", 0, false),
                new RiskSignal("GEO_ANOMALY", "Domestic", 0, false)
        );

        assertEquals(DecisionOutcome.APPROVED, engine.decide(signals));
        assertEquals(0, engine.computeRiskScore(signals));
    }

    @Test
    void shouldFlagWhenScoreExceeds25() {
        List<RiskSignal> signals = List.of(
                new RiskSignal("MERCHANT_RISK", "Gambling", 20, true),
                new RiskSignal("CHANNEL_RISK", "Online", 10, true)
        );

        assertEquals(DecisionOutcome.FLAGGED, engine.decide(signals));
        assertEquals(30, engine.computeRiskScore(signals));
    }

    @Test
    void shouldRejectWhenScoreExceeds60() {
        List<RiskSignal> signals = List.of(
                new RiskSignal("AMOUNT_ANOMALY", "High", 35, true),
                new RiskSignal("GEO_ANOMALY", "High risk country", 30, true)
        );

        assertEquals(DecisionOutcome.REJECTED, engine.decide(signals));
        assertEquals(65, engine.computeRiskScore(signals));
    }

    @Test
    void shouldCapRiskScoreAt100() {
        List<RiskSignal> signals = List.of(
                new RiskSignal("AMOUNT_ANOMALY", "High", 35, true),
                new RiskSignal("GEO_ANOMALY", "High risk", 30, true),
                new RiskSignal("VELOCITY", "High freq", 25, true),
                new RiskSignal("MERCHANT_RISK", "Crypto", 20, true)
        );

        assertEquals(100, engine.computeRiskScore(signals));
    }
}