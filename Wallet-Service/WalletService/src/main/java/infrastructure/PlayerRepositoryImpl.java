package infrastructure;

import domain.model.Player;
import domain.model.Transaction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerRepositoryImpl implements PlayerRepository {
    private final Map<String, Player> playersDB = new HashMap<String, Player>();


    @Override
    public void savePlayer(String key, Player player) {
        playersDB.put(key, player);
    }

    @Override
    public boolean exist(String playerId) {
        return playersDB.containsKey(playerId);
    }

    @Override
    public double getCurrentBalance(String playerId) {
        return playersDB.get(playerId).getBalance();
    }

    @Override
    public Player getPlayer(String playerId) {
        return playersDB.get(playerId);
    }

    @Override
    public List<Transaction> getPlayerTransactions(String playerId) {
        return playersDB.get(playerId).getTransactionHistory();
    }

}
