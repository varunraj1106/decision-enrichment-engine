package com.aviva.enrichment.dto;

public record RiskSignalDto(
        String signalType,
        String description,
        int weight,
        boolean triggered
) {}