package services;

import domain.model.TransactionType;
import domain.model.Player;

public interface PlayerService {

    boolean performPlayerRegistration(String playerName, String playerPassword);

    Player performPlayerAuthentication(String username, String password);

    double getBalance(Player player);

    void makeTransaction(Player player, TransactionType type, double amount);

    void getPlayerTransactionHistory(Player player);
}
