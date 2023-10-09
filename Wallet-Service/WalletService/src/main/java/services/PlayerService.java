package services;

import domain.model.Player;
import domain.model.Transaction;

public interface PlayerService {

    boolean performPlayerRegistration(String playerName, String playerPassword);

    String performPlayerAuthentication(String username, String password);

    double getBalance(String playerId);

    Player getPlayer(String playerId);

    StringBuilder getPlayerTransactionHistory(String playerId);

    boolean updateBalance(Transaction transaction, String typeOperation);

}
