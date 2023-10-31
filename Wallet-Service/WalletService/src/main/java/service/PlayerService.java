package service;

import domain.model.Player;
import domain.model.Transaction;

import java.math.BigDecimal;
import java.util.List;

/**
 * The `PlayerService` interface provides methods for managing players and their transactions.
 */

public interface PlayerService {

    /**
     * Registers a player with the specified name and password.
     *
     * @param playerName     Player's name.
     * @param playerPassword Player's password.
     * @return true if registration is successful, otherwise false.
     */
    boolean performPlayerRegistration(String playerName, String playerPassword);

    /**
     * Authenticates a player with the specified username and password.
     *
     * @param username Player's username.
     * @param password User's password.
     * @return Authentication userID or null if authentication fails.
     */
    Long performPlayerAuthentication(String username, String password);

    /**
     * Gets the balance of a player by their identifier.
     *
     * @param playerId Player's identifier.
     * @return Player's balance.
     */
    BigDecimal getBalance(Long playerId);

    /**
     * Gets player information by their identifier.
     *
     * @param playerId Player's identifier.
     * @return A Player object representing the player.
     */
    Player getPlayer(Long playerId);

    /**
     * Retrieves the transaction history of a player by their identifier.
     *
     * @param playerId Player's identifier.
     * @return A list of Transaction objects representing the player's transaction history.
     */
    List<Transaction> getPlayerTransactionHistory(Long playerId);

    /**
     * Updates the player's balance based on the transaction and operation type.
     *
     * @param transaction Transaction to update the balance.
     * @return true if the update is successful, otherwise false.
     */
    boolean updateBalance(Transaction transaction);

    long getPlayerId(String name, String password);
}
