package service;

import domain.model.Player;
import domain.model.Transaction;
import infrastructure.PlayerRepository;
import infrastructure.TransactionRepository;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

/**
 * The `PlayerServiceImpl` class implements the `PlayerService` interface and provides methods for managing players.
 */
public class PlayerServiceImpl implements PlayerService {

    private final TransactionRepository transactionRepository;

    /**
     * Player repository for accessing player data.
     */
    private final PlayerRepository playerRepository;
    //private final TransactionService transactionService;

    /**
     * Default constructor initializes the player repository.
     */
    public PlayerServiceImpl(PlayerRepository playerRepository, TransactionRepository transactionRepository1) {
        this.playerRepository = playerRepository;

        this.transactionRepository = transactionRepository1;
    }

    @Override
    public boolean performPlayerRegistration(String playerName, String playerPassword) {
        try {
            if (!playerRepository.exist(playerName, playerPassword)) {
                playerRepository.savePlayer(playerName, playerPassword);
                return true;
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Long performPlayerAuthentication(String playerName, String playerPassword) {
        try {
            if (playerRepository.exist(playerName, playerPassword)) {
                return playerRepository.getPlayerIdByNameAndPassword(playerName, playerPassword);
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public BigDecimal getBalance(Long playerId) {
        try {
            return playerRepository.getCurrentBalance(playerId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Player getPlayer(Long playerId) {
        try {
            return playerRepository.getPlayer(playerId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Transaction> getPlayerTransactionHistory(Long playerId) {
        try {
            return playerRepository.getPlayerTransactions(playerId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean updateBalance(Transaction transaction) {
        String typeOperation = transaction.getType().name();
        boolean operationCompleted = false;
        try {
            long playerId = transaction.getPlayerId();

            operationCompleted = switch (typeOperation) {
                case "CREDIT" -> addAmount(playerId, transaction);
                case "DEBIT" -> deductAmount(playerId, transaction);
                default -> operationCompleted;
            };

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return operationCompleted;

    }


    private boolean addAmount(long playerId, Transaction transaction) throws SQLException {
        BigDecimal incomingAmount = transaction.getAmount();
        int comparisonResult = incomingAmount.compareTo(BigDecimal.ZERO);

        if (comparisonResult > 0) {
            BigDecimal currentBalance = playerRepository.getCurrentBalance(playerId);
            BigDecimal newBalance = currentBalance.add(incomingAmount);

            playerRepository.updateBalance(playerId, newBalance);
            return true;
        }
        return false;
    }

    private boolean deductAmount(long playerId, Transaction transaction) throws SQLException {
        BigDecimal incomingAmount = transaction.getAmount();
        int comparisonResult = incomingAmount.compareTo(BigDecimal.ZERO);
        if (comparisonResult >= 0) {
            BigDecimal currentBalance = playerRepository.getCurrentBalance(playerId);
            if (currentBalance.compareTo(incomingAmount) >= 0) {
                BigDecimal newBalance = currentBalance.subtract(incomingAmount);
                playerRepository.updateBalance(playerId, newBalance);
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    @Override
    public Long getPlayerId(String name, String password) {
        try {
            return playerRepository.getPlayerIdByNameAndPassword(name, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
