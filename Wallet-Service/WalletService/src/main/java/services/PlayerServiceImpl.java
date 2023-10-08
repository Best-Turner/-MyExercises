package services;

import domain.model.Player;
import domain.model.Transaction;
import infrastructure.PlayerRepository;
import infrastructure.PlayerRepositoryImpl;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;

    public PlayerServiceImpl() {
        playerRepository = new PlayerRepositoryImpl();

    }

    @Override
    public boolean performPlayerRegistration(String playerName, String playerPassword) {
        String userId = generateUserID(playerName, playerPassword);
        if (!playerRepository.exist(userId)) {
            playerRepository.savePlayer(userId, new Player(userId, playerName, playerPassword));
            return true;
        }
        return false;
    }

    @Override
    public String performPlayerAuthentication(String playerName, String playerPassword) {
        String key = generateUserID(playerName, playerPassword);
        if (playerRepository.exist(key)) {
            return key;
        }
        return null;
    }

    @Override
    public double getBalance(String playerId) {
        return playerRepository.getCurrentBalance(playerId);
    }

    @Override
    public Player getPlayer(String playerId) {
        return playerRepository.getPlayer(playerId);
    }

    @Override
    public StringBuilder getPlayerTransactionHistory(String playerId) {
        StringBuilder builder = new StringBuilder();
        List<Transaction> transactionHistory = playerRepository.getPlayerTransactions(playerId);
        for (Transaction transaction : transactionHistory) {
            builder
                    .append("ID transaction : " + transaction.getTransactionId() + "\n")
                    .append("Amount : " + transaction.getAmount() + "\n")
                    .append("Type : " + transaction.getType().name() + "\n");
        }
        return builder;
    }

    @Override
    public boolean updateBalance(Transaction transaction, String typeOperation) {
        Player player = playerRepository.getPlayer(transaction.playerId());
        return typeOperation.equals("DEBIT") ? deductAmount(player, transaction) : addAmount(player, transaction);
    }

    private boolean addAmount(Player player, Transaction transaction) {
        double incomingAmount = transaction.getAmount();
        if (incomingAmount >= 0) {
            player.setBalance(player.getBalance() + incomingAmount);
            player.addTransaction(transaction);
            return true;
        }
        return false;
    }

    private boolean deductAmount(Player player, Transaction transaction) {
        double currentBalance = player.getBalance();
        double incomingAmount = transaction.getAmount();
        if (currentBalance >= incomingAmount) {
            player.setBalance(currentBalance - incomingAmount);
            player.addTransaction(transaction);
            return true;
        }
        return false;
    }

    private String generateUserID(String username, String password) {
        try {
            String data = username + password;
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(data.getBytes("UTF-8"));

            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xFF & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.substring(0, 10);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
