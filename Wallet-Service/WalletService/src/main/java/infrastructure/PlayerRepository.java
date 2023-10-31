package infrastructure;

import domain.model.Player;
import domain.model.Transaction;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public interface PlayerRepository {

    /**
     * The PlayerRepository interface defines methods for interacting with player-related data.
     * This interface provides a contract for classes that will implement access and manipulation of data
     * for players in the domain.
     *
     * @see Player
     * @see Transaction
     */

    /**
     * Saves a player with the specified key.
     *
     * @param name     The player to be saved.
     * @param password The player to be saved.
     */
    void savePlayer(String name, String password) throws SQLException;

    /**
     * Checks the existence of a player with the given player ID.
     *
     * @param playerName     The player name to check.
     * @param playerPassword The player password to check.
     * @return true if the player exists, otherwise false.
     */
    boolean exist(String playerName, String playerPassword) throws SQLException;

    /**
     * Gets the current balance of a player identified by their ID.
     *
     * @param playerId The player's ID.
     * @return The current balance of the player.
     */
    BigDecimal getCurrentBalance(Long playerId) throws SQLException;

    /**
     * Gets the player object associated with the specified player ID.
     *
     * @param playerId The player's ID.
     * @return The player object.
     */
    Player getPlayer(Long playerId) throws SQLException;

    /**
     * Gets a list of transactions associated with the specified player ID.
     *
     * @param playerId The player's ID.
     * @return The list of player transactions.
     */
    List<Transaction> getPlayerTransactions(Long playerId) throws SQLException;

    /**
     * Returns the number of saved players in the repository.
     *
     * @return The number of players in the repository.
     */
    int getCountPlayers() throws SQLException;

    Long getPlayerIdByNameAndPassword(String playername, String playerPassword) throws SQLException;

    void updateBalance(Long id, BigDecimal newBalance) throws SQLException;
}
