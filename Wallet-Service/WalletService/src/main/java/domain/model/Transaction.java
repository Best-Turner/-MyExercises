package domain.model;

import java.util.Objects;

public class Transaction {
    private final String transactionId;
    private final String playerId;
    private final double amount;
    private final TransactionType type;

    public Transaction(String transactionId, String playerId, double amount, TransactionType type) {
        this.transactionId = transactionId;
        this.playerId = playerId;
        this.amount = amount;
        this.type = type;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String playerId() {
        return playerId;
    }

    public double getAmount() {
        return amount;
    }

    public TransactionType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(transactionId, that.transactionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId);
    }
}
