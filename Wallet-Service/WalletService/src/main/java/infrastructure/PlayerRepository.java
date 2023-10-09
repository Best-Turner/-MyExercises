package infrastructure;

import domain.model.Player;
import domain.model.Transaction;

import java.util.List;

public interface PlayerRepository {

    void savePlayer(String key, Player player);

    boolean exist(String playerId);

    double getCurrentBalance(String playerId);

    Player getPlayer(String playerId);

    List<Transaction> getPlayerTransactions(String playerId);
    int getCountPlayers();

}
