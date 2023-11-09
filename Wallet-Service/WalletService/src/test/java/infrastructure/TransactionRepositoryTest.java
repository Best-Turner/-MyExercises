package infrastructure;

import domain.model.Transaction;
import domain.model.TransactionType;
import exception.DuplicateTransactionIdException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@Testcontainers

public class TransactionRepositoryTest {
    private final static String DB_NAME = "testDB";
    private final static String USERNAME = "testUser";
    private final static String PASSWORD = "testPass";
    private TransactionRepository transactionRepository;
    private Transaction transaction;
    private Connection connection;

    @Container
    public static final PostgreSQLContainer<?> container =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:13"))
                    .withDatabaseName(DB_NAME)
                    .withUsername(USERNAME)
                    .withPassword(PASSWORD);

    @Before
    public void setUp() throws Exception {
        container.start();
        connection = DriverManager.getConnection(container.getJdbcUrl(), container.getUsername(), container.getPassword());
        transactionRepository = new TransactionRepositoryImpl(connection);
        String createSchemaSql = "CREATE SCHEMA IF NOT EXISTS model";
        String createTypeSql = "CREATE TYPE typeTr AS ENUM('CREDIT', 'DEBIT');";
        String createTableTransactionSql = "CREATE TABLE IF NOT EXISTS model.transaction " +
                "(id SERIAL PRIMARY KEY, player_id bigint, transactionCode varchar(255), amount DECIMAL, transactionType typeTr);";
        try (PreparedStatement createSchemaStatement = connection.prepareStatement(createSchemaSql);
             PreparedStatement createTypeStatement = connection.prepareStatement(createTypeSql);
             PreparedStatement createTableStatement = connection.prepareStatement(createTableTransactionSql)) {
            createSchemaStatement.execute();
            createTypeStatement.execute();
            createTableStatement.execute();
        }

        transaction = new Transaction("1", 1L, new BigDecimal(100), TransactionType.CREDIT);

    }

    @After
    public void tearDown() throws SQLException {
        String dropTableSql = "DROP TABLE model.transaction";
        String dropTypeSql = "DROP TYPE typeTr";
        try (Statement dropTableStatement = connection.createStatement();
             Statement dropTypeStatement = connection.createStatement()) {
            dropTableStatement.execute(dropTableSql);
            dropTypeStatement.execute(dropTypeSql);
        } finally {
            connection.close();
        }
    }

    @Test
    public void saveTransaction() throws DuplicateTransactionIdException, SQLException {
        assertEquals(0, transactionRepository.getCountTransaction());
        transactionRepository.saveTransaction(transaction);
        assertEquals(1, transactionRepository.getCountTransaction());
    }


    @Test
    public void getTransactionsListByPlayerId() throws SQLException {
        assertEquals(0, transactionRepository.getCountTransaction());
        List<Transaction> transactionList = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            transaction = new Transaction("test" + i, 1L, BigDecimal.valueOf(10 * i), TransactionType.CREDIT);
            transactionList.add(transaction);
            transactionRepository.saveTransaction(transaction);
        }
        assertEquals(5, transactionRepository.getCountTransaction());

        assertEquals(transactionList, transactionRepository.getTransactionsByPlayerId(1L));
    }


    @Test
    public void getTransactionByTransactionCodeTest() throws SQLException {
        assertEquals(0, transactionRepository.getCountTransaction());
        transactionRepository.saveTransaction(transaction);
        assertEquals(transaction, transactionRepository.getTransactionByTransactionCode(transaction.getTransactionCode()));
    }

    @Test
    public void whenTransactionCodeNotExistThenReturnNull() throws SQLException {
        assertEquals(0, transactionRepository.getCountTransaction());
        transactionRepository.saveTransaction(transaction);
        assertEquals(1, transactionRepository.getCountTransaction());
        assertNull(transactionRepository.getTransactionByTransactionCode("2"));
    }


//    @Test
//    public void whenTransactionByIdNotExistReturnFalse() throws DuplicateTransactionIdException, SQLException {
//        assertEquals(0, repository.getCountTransaction());
//        repository.saveTransaction(transaction);
//        assertFalse(repository.existById(2L));
//    }
}