package domain.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The class represents a player in the system.
 * <p>
 * A player has a unique identifier (ID), a name, a password,
 * a balance of funds, and a transaction history.
 */
public class Player {
    /**
     * The unique identifier of the player.
     */
    private final Long id;
    /**
     * The name of the player.
     */
    private final String name;
    /**
     * The password of the player.
     */
    private final String password;
    /**
     * The balance of the player's funds.
     */
    private BigDecimal balance;
    /**
     * The list of player's transactions.
     */
    private final List<Transaction> transactionHistory;

    /**
     * Constructor for the Player class.
     *
     * @param id
     * @param name     The name of the player.
     * @param password The password of the player.
     */
    public Player(Long id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
        balance = new BigDecimal(0);
        transactionHistory = new ArrayList<>();
    }

    /**
     * Get the unique identifier of the player.
     *
     * @return The unique identifier of the player.
     */
    public Long getId() {
        return id;
    }

    /**
     * Get the name of the player.
     *
     * @return The name of the player.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the password of the player.
     *
     * @return The password of the player.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Get the list of player's transactions.
     *
     * @return The list of player's transactions.
     */
    public List<Transaction> getTransactionHistory() {
        return transactionHistory;
    }

    /**
     * Get the balance of the player's funds.
     *
     * @return The balance of the player's funds.
     */
    public BigDecimal getBalance() {
        return balance;
    }

    /**
     * Set the balance of the player's funds.
     *
     * @param balance The new balance of the player's funds.
     */
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }


    /**
     * Add a transaction to the player's transaction history.
     *
     * @param transaction The transaction to add.
     */
    public void addTransaction(Transaction transaction) {
        transactionHistory.add(transaction);
    }

    /**
     * Override the equals method to compare players based on their unique identifiers.
     *
     * @param o The object to compare.
     * @return true if the objects are equal, otherwise false.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(id, player.id);
    }

    /**
     * Override the hashCode method to calculate the hash code based on the player's unique identifier.
     *
     * @return The hash code of the object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}