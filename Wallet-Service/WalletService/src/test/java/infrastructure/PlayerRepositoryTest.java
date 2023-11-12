package infrastructure;


import domain.model.Player;
import domain.model.Transaction;
import domain.model.TransactionType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@Testcontainers
public class PlayerRepositoryTest {
    private final static String DB_NAME = "testDB";
    private final static String USERNAME = "testUser";
    private final static String PASSWORD = "testPass";
    @Mock
    private PlayerRepository playerRepository;

    private Connection connection;


    @Container
    public static PostgreSQLContainer<?> postgresContainer =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"))
                    .withDatabaseName(DB_NAME)
                    .withUsername(USERNAME)
                    .withPassword(PASSWORD)
                    .waitingFor(Wait.forLogMessage(".*database system is ready to accept connections.*\\n", 2));


    @Before
    public void setUp() throws Exception {

        postgresContainer.start();
        try {
            connection = getConnection();
            connection.setAutoCommit(true);
            playerRepository = new PlayerRepositoryImpl(connection);
            String createSchema = "CREATE SCHEMA IF NOT EXISTS model";
            String createTableSQL = "CREATE TABLE IF NOT EXISTS model.player " +
                    "(id SERIAL PRIMARY KEY," +
                    " name VARCHAR(255)," +
                    " password VARCHAR(255)," + " balance DECIMAL);";
            try (PreparedStatement createSchemaStatement = connection.prepareStatement(createSchema); PreparedStatement createTableStatement = connection.prepareStatement(createTableSQL)) {
                createSchemaStatement.execute();
                createTableStatement.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @After
    public void tearDown() throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("DROP table model.player")) {
            preparedStatement.execute();
        } finally {
            connection.close();
        }
    }

    @Test
    public void savePlayerTest() throws SQLException {
        int actual = playerRepository.getCountPlayers();
        assertEquals(0, actual);
        playerRepository.savePlayer(USERNAME, PASSWORD);
        assertEquals(1, playerRepository.getCountPlayers());

    }

    @Test
    public void whenAdd100PlayersThenSizeBecome100() throws SQLException {
        assertEquals(0, playerRepository.getCountPlayers());
        for (int i = 0; i < 100; i++) {
            playerRepository.savePlayer(USERNAME + i, PASSWORD + i);
        }
        assertEquals(100, playerRepository.getCountPlayers());

    }

    @Test
    public void exist() throws SQLException {
        assertEquals(0, playerRepository.getCountPlayers());
        playerRepository.savePlayer(USERNAME, PASSWORD);
        assertTrue(playerRepository.exist(USERNAME, PASSWORD));

    }

    @Test
    public void doesNotExist() throws SQLException {
        assertEquals(0, playerRepository.getCountPlayers());
        playerRepository.savePlayer(USERNAME, PASSWORD);
        assertEquals(1, playerRepository.getCountPlayers());
        assertFalse(playerRepository.exist(USERNAME + "1", PASSWORD));

    }


    @Test
    public void getCurrentBalance() throws SQLException {
        playerRepository.savePlayer(USERNAME, PASSWORD);
        assertEquals(BigDecimal.ZERO, playerRepository.getCurrentBalance(1L));

    }

    @Test
    public void getPlayer() throws SQLException {
        assertEquals(0, playerRepository.getCountPlayers());
        Player player = new Player(1L, USERNAME, PASSWORD);
        playerRepository.savePlayer(USERNAME, PASSWORD);
        assertEquals(1, playerRepository.getCountPlayers());
        assertEquals(player, playerRepository.getPlayer(1L));
        assertNull(playerRepository.getPlayer(2L));

    }

    @Test
    public void whenReceiveNonExistentTransaction() throws SQLException {
        assertEquals(0, playerRepository.getCountPlayers());
        assertNull(playerRepository.getPlayer(1L));

    }


    @Test
    public void getPlayerTransactionsTest() throws SQLException {
        MockitoAnnotations.initMocks(this);
        List<Transaction> fakeTransaction = new ArrayList<>();
        Player player = new Player(1L, USERNAME, PASSWORD);
        for (int i = 0; i < 10; i++) {
            Transaction transaction = new Transaction("code " + i, 1L, new BigDecimal(100), TransactionType.CREDIT);
            fakeTransaction.add(transaction);
            player.addTransaction(transaction);
        }
        when(playerRepository.getPlayerTransactions(1L)).thenReturn(fakeTransaction);
        playerRepository.savePlayer(USERNAME, PASSWORD);
        verify(playerRepository, never()).getPlayerTransactions(1L);
        assertEquals(fakeTransaction, playerRepository.getPlayerTransactions(1L));
        verify(playerRepository, times(1)).getPlayerTransactions(1L);

    }

    @Test
    public void whenChangeBalanceThenBalanceMustBeChange() throws SQLException {
        assertEquals(0, playerRepository.getCountPlayers());
        playerRepository.savePlayer(USERNAME, PASSWORD);
        assertEquals(1, playerRepository.getCountPlayers());
        assertEquals(BigDecimal.ZERO, playerRepository.getCurrentBalance(1L));
        playerRepository.updateBalance(1L, BigDecimal.valueOf(100));
        BigDecimal expected = new BigDecimal(100);
        assertEquals(expected, playerRepository.getCurrentBalance(1L));
    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(postgresContainer.getJdbcUrl(), USERNAME, PASSWORD);
    }
}