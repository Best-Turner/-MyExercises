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
        //String userId = generateUserID(playerName, playerPassword);
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
        String typeOperation =
                transaction.getType().name().equalsIgnoreCase("CREDIT") ? "CREDIT" : "DEBIT";
        Player player;
        boolean operationCompleted = false;
        try {
            player = playerRepository.getPlayer(transaction.getPlayerId());

            operationCompleted = switch (typeOperation) {
                case "CREDIT" -> addAmount(player, transaction);
                case "DEBIT" -> deductAmount(player, transaction);
                default -> operationCompleted;
            };

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return operationCompleted;

    }


    private boolean addAmount(Player player, Transaction transaction) {
        BigDecimal incomingAmount = transaction.getAmount();
        int comparisonResult = incomingAmount.compareTo(BigDecimal.ZERO);

        if (comparisonResult > 0) {
            BigDecimal currentBalance = player.getBalance();
            BigDecimal newBalance = currentBalance.add(incomingAmount);
            try {
                //transactionService.makeTransaction(player.getId(), )
                playerRepository.updateBalance(player.getId(), newBalance);
                return true;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            //player.addTransaction(transaction);
        }
        return false;
    }

    private boolean deductAmount(Player player, Transaction transaction) {
        BigDecimal currentBalance = player.getBalance();
        BigDecimal incomingAmount = transaction.getAmount();
        int comparisonResult = currentBalance.compareTo(incomingAmount);

        if (comparisonResult >= 0) {
            BigDecimal newBalance = currentBalance.subtract(incomingAmount);
            try {
                playerRepository.updateBalance(player.getId(), newBalance);
                return true;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            //player.addTransaction(transaction);
        }
        return false;
    }

    @Override
    public long getPlayerId(String name, String password) {
        try {
            return playerRepository.getPlayerIdByNameAndPassword(name, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

//    private String generateUserID(String username, String password) {
//        try {
//            String data = username + password;
//            MessageDigest md = MessageDigest.getInstance("SHA-256");
//            byte[] hashBytes = md.digest(data.getBytes(StandardCharsets.UTF_8));
//
//            StringBuilder hexString = new StringBuilder();
//            for (byte b : hashBytes) {
//                String hex = Integer.toHexString(0xFF & b);
//                if (hex.length() == 1) {
//                    hexString.append('0');
//                }
//                hexString.append(hex);
//            }
//            return hexString.substring(0, 10);
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
}
