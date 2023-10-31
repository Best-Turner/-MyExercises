package infrastructure;


import domain.model.Player;
import domain.model.Transaction;
import domain.model.TransactionType;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class PlayerRepositoryTest {


    @ClassRule
    public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:13"))
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass")
            .waitingFor(Wait.forLogMessage(".*database system is ready to accept connections.*\\n", 2));
    private PlayerRepository repository;
    private final static String NAME = "Ivan";
    private final static String PASSWORD = "Ivan";
    private Player player;

    @Before
    public void setUp() throws Exception {
        repository = new PlayerRepositoryImpl();
        //player = new Player(id, NAME, PASSWORD);
    }

    @Test
    public void savePlayer() throws SQLException {
        assertNull(repository.getPlayer(1L));
        repository.savePlayer(NAME, PASSWORD);
        assertEquals(player, repository.getPlayer(1L));
    }

    @Test
    public void whenAdd100PlayersThenSizeBecome100() throws SQLException {
        assertEquals(0, repository.getCountPlayers());
        for (int i = 0; i < 100; i++) {
            repository.savePlayer(NAME, PASSWORD);
        }
        assertEquals(100, repository.getCountPlayers());
    }

    @Test
    public void exist() throws SQLException {
        repository.savePlayer(NAME, PASSWORD);
        assertTrue(repository.exist(player.getName(), player.getPassword()));
    }

    @Test
    public void doesNotExist() throws SQLException {
        assertEquals(0, repository.getCountPlayers());
        repository.savePlayer(NAME, PASSWORD);
        assertEquals(1, repository.getCountPlayers());
        assertFalse(repository.exist("name", "password"));
    }

    @Test
    public void getCurrentBalance() throws SQLException {
        repository.savePlayer(NAME, PASSWORD);
        assertEquals(BigDecimal.ZERO, repository.getCurrentBalance(1L));
    }

    @Test
    public void getPlayer() throws SQLException {
        repository.savePlayer(NAME, PASSWORD);
        assertEquals(player, repository.getPlayer(1L));
        assertNull(repository.getPlayer(2L));
    }

    @Test
    public void whenReceiveNonExistentTransaction() throws SQLException {
        assertEquals(0, repository.getCountPlayers());
        assertNull(repository.getPlayer(1L));
    }


    @Test
    public void getPlayerTransactions() throws SQLException {
        List<Transaction> fakeTransaction = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Transaction transaction =
                    new Transaction("code " + i, 1L, new BigDecimal(100), TransactionType.CREDIT);
            fakeTransaction.add(transaction);
            player.addTransaction(transaction);
        }
        repository.savePlayer(NAME, PASSWORD);
        assertEquals(fakeTransaction, repository.getPlayerTransactions(1L));
    }
}