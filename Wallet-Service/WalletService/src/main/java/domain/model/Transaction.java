package domain.model;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * This class represents a transaction in the system.
 * <p>
 * A transaction has a unique identifier, player identifier,
 * an amount, and a transaction type.
 */
public class Transaction {

    private Long id;
    private final Long playerId;
    private final String transactionCode;
    private final BigDecimal amount;
    private final TransactionType type;

    public Transaction(String transactionCode, Long playerId, BigDecimal amount, TransactionType type) {
        this.transactionCode = transactionCode;
        this.playerId = playerId;
        this.amount = amount;
        this.type = type;
    }

    /**
     * Unique identifier of the transaction.
     *
     * @return Unique identifier of the transaction.
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Get the player identifier who made the transaction.
     *
     * @return Player identifier.
     */
    public Long getPlayerId() {
        return playerId;
    }

    /**
     * Get the transaction amount.
     *
     * @return Transaction amount.
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Get the transaction type.
     *
     * @return Transaction type.
     */
    public TransactionType getType() {
        return type;
    }

    public String getTransactionCode() {
        return transactionCode;
    }

    /**
     * Override the equals method to compare transactions based on their unique identifiers.
     *
     * @param o Object to compare.
     * @return true if objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(id, that.id);
    }

    /**
     * Override the hashCode method to compute the hash code based on the unique transaction identifier.
     *
     * @return Hash code of the object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}