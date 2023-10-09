package infrastructure;


import domain.model.Player;
import domain.model.Transaction;
import domain.model.TransactionType;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class PlayerRepositoryTest {

    private PlayerRepository repository;
    private Player player;

    @Before
    public void setUp() throws Exception {
        repository = new PlayerRepositoryImpl();
        player = new Player("1", "Ivan", "123");
    }

    @Test
    public void savePlayer() {
        assertNull(repository.getPlayer("1"));
        repository.savePlayer("1", player);
        assertEquals(player, repository.getPlayer("1"));
    }

    @Test
    public void whenAdd100PlayersThenSizeBecome100() {
        assertEquals(0, repository.getCountPlayers());
        for (int i = 0; i < 100; i++) {
            repository.savePlayer("key" + i, player);
        }
        assertEquals(100, repository.getCountPlayers());
    }

    @Test
    public void exist() {
        repository.savePlayer("1", player);
        assertTrue(repository.exist("1"));
    }

    @Test
    public void doesNotExist() {
        assertEquals(0, repository.getCountPlayers());
        repository.savePlayer("1", player);
        assertEquals(1, repository.getCountPlayers());
        assertFalse(repository.exist("2"));
    }

    @Test
    public void getCurrentBalance() {
        repository.savePlayer("1", player);
        assertEquals(0.0, repository.getCurrentBalance("1"), 0.0);
    }

    @Test
    public void getPlayer() {
        repository.savePlayer("1", player);
        assertEquals(player, repository.getPlayer("1"));
        assertNull(repository.getPlayer("2"));
    }

    @Test
    public void whenReceiveNonExistentTransaction() {
        assertEquals(0, repository.getCountPlayers());
        assertNull(repository.getPlayer("1"));
    }


    @Test
    public void getPlayerTransactions() {
        List<Transaction> fakeTransaction = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Transaction transaction =
                    new Transaction("id" + i, "playerId" + i, 100, TransactionType.CREDIT);
            fakeTransaction.add(transaction);
            player.addTransaction(transaction);
        }
        repository.savePlayer("1", player);
        assertEquals(fakeTransaction, repository.getPlayerTransactions("1"));
    }
}