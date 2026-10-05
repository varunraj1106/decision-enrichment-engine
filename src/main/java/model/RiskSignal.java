package com.aviva.enrichment.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "risk_signals")
public class RiskSignal {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String signalType;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private int weight;

    @Column(nullable = false)
    private boolean triggered;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;

    public RiskSignal() {}

    public RiskSignal(String signalType, String description, int weight, boolean triggered) {
        this.signalType = signalType;
        this.description = description;
        this.weight = weight;
        this.triggered = triggered;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getSignalType() { return signalType; }
    public void setSignalType(String signalType) { this.signalType = signalType; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public int getWeight() { return weight; }
    public void setWeight(int weight) { this.weight = weight; }
    public boolean isTriggered() { return triggered; }
    public void setTriggered(boolean triggered) { this.triggered = triggered; }
    public Transaction getTransaction() { return transaction; }
    public void setTransaction(Transaction transaction) { this.transaction = transaction; }
}