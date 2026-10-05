package com.aviva.enrichment.engine;

import com.aviva.enrichment.model.DecisionOutcome;
import com.aviva.enrichment.model.RiskSignal;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class DecisionEngine {

    private static final int FLAG_THRESHOLD = 25;
    private static final int REJECT_THRESHOLD = 60;

    public DecisionOutcome decide(List<RiskSignal> signals) {
        int totalScore = signals.stream()
                .filter(RiskSignal::isTriggered)
                .mapToInt(RiskSignal::getWeight)
                .sum();

        if (totalScore >= REJECT_THRESHOLD) {
            return DecisionOutcome.REJECTED;
        } else if (totalScore >= FLAG_THRESHOLD) {
            return DecisionOutcome.FLAGGED;
        }
        return DecisionOutcome.APPROVED;
    }

    public int computeRiskScore(List<RiskSignal> signals) {
        return Math.min(100, signals.stream()
                .filter(RiskSignal::isTriggered)
                .mapToInt(RiskSignal::getWeight)
                .sum());
    }
}