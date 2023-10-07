package services;


import domain.model.Player;
import domain.model.TransactionType;

public class PlayerServiceImpl implements PlayerService {

    @Override
    public boolean performPlayerRegistration(String playerName, String playerPassword) {
        return false;
    }

    @Override
    public Player performPlayerAuthentication(String username, String password) {
        return null;
    }

    @Override
    public double getBalance(Player player) {
        return 0;
    }

    @Override
    public void makeTransaction(Player player, TransactionType type, double amount) {

    }

    @Override
    public void getPlayerTransactionHistory(Player player) {

    }
}
