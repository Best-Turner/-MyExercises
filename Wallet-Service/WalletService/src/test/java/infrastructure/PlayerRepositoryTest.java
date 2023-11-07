package infrastructure;


import domain.model.Player;
import domain.model.Transaction;
import domain.model.TransactionType;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@Testcontainers

public class PlayerRepositoryTest {
    private final static String DB_NAME = "testdb";
    private final static String USERNAME = "testuser";
    private final static String PASSWORD = "testpass";


    @Container
    @ClassRule
    public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:13")).withDatabaseName(DB_NAME).withUsername(USERNAME).withPassword(PASSWORD).waitingFor(Wait.forLogMessage(".*database system is ready to accept connections.*\\n", 2));


    @Before
    public void setUp() throws Exception {
        postgresContainer.start();
        try (Connection connection = getConnection()) {
            connection.setAutoCommit(true);
            String createSchema = "CREATE SCHEMA IF NOT EXISTS model";
            String createTableSQL = "CREATE TABLE IF NOT EXISTS model.player " + "(id SERIAL PRIMARY KEY," + " name VARCHAR(255)," + " password VARCHAR(255)," + " balance DECIMAL);";

            try (PreparedStatement createSchemaStatement = connection.prepareStatement(createSchema); PreparedStatement createTableStatement = connection.prepareStatement(createTableSQL)) {
                createSchemaStatement.execute();
                createTableStatement.execute();
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void savePlayerTest() throws SQLException {

        String getCountPlayersFromDBSql = "SELECT count(*) FROM model.player";

        try (Connection connection = getConnection()) {
            int expected = getCountPlayers(connection, getCountPlayersFromDBSql);
            assertEquals(0, expected);
            String insertPlayer = "INSERT INTO model.player (name, password, balance) VALUES('user', 123, 100);";
            PreparedStatement preparedStatement = connection.prepareStatement(insertPlayer);
            assertEquals(1, preparedStatement.executeUpdate());
            assertEquals(1, getCountPlayers(connection, getCountPlayersFromDBSql));
        }

    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(postgresContainer.getJdbcUrl(), USERNAME, PASSWORD);
    }

    private int getCountPlayers(Connection connection, String sql) throws SQLException {
        int countPlayer;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            countPlayer = resultSet.getInt(1);
        }
        return countPlayer;
    }
}


//
//    @Test
//    public void savePlayer() throws SQLException {
//        assertNull(repository.getPlayer(1L));
//        repository.savePlayer(NAME, PASSWORD);
//        assertEquals(player, repository.getPlayer(1L));
//    }
//
//    @Test
//    public void whenAdd100PlayersThenSizeBecome100() throws SQLException {
//        assertEquals(0, repository.getCountPlayers());
//        for (int i = 0; i < 100; i++) {
//            repository.savePlayer(NAME, PASSWORD);
//        }
//        assertEquals(100, repository.getCountPlayers());
//    }
//
//    @Test
//    public void exist() throws SQLException {
//        repository.savePlayer(NAME, PASSWORD);
//        assertTrue(repository.exist(player.getName(), player.getPassword()));
//    }
//
//    @Test
//    public void doesNotExist() throws SQLException {
//        assertEquals(0, repository.getCountPlayers());
//        repository.savePlayer(NAME, PASSWORD);
//        assertEquals(1, repository.getCountPlayers());
//        assertFalse(repository.exist("name", "password"));
//    }
//
//    @Test
//    public void getCurrentBalance() throws SQLException {
//        repository.savePlayer(NAME, PASSWORD);
//        assertEquals(BigDecimal.ZERO, repository.getCurrentBalance(1L));
//    }
//
//    @Test
//    public void getPlayer() throws SQLException {
//        repository.savePlayer(NAME, PASSWORD);
//        assertEquals(player, repository.getPlayer(1L));
//        assertNull(repository.getPlayer(2L));
//    }
//
//    @Test
//    public void whenReceiveNonExistentTransaction() throws SQLException {
//        assertEquals(0, repository.getCountPlayers());
//        assertNull(repository.getPlayer(1L));
//    }
//
//
//    @Test
//    public void getPlayerTransactions() throws SQLException {
//        List<Transaction> fakeTransaction = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            Transaction transaction =
//                    new Transaction("code " + i, 1L, new BigDecimal(100), TransactionType.CREDIT);
//            fakeTransaction.add(transaction);
//            player.addTransaction(transaction);
//        }
//        repository.savePlayer(NAME, PASSWORD);
//        assertEquals(fakeTransaction, repository.getPlayerTransactions(1L));
//    }

