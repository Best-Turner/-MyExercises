
import app.ConsoleInputHandler;
import infrastructure.PlayerRepository;
import infrastructure.PlayerRepositoryImpl;
import infrastructure.TransactionRepository;
import infrastructure.TransactionRepositoryImpl;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import service.PlayerService;
import service.PlayerServiceImpl;
import service.TransactionService;
import service.TransactionServiceImpl;
import util.DBConnector;

import java.sql.Connection;

/**
 * The `WalletService` class serves as the entry point to the application for managing player wallets.
 *
 * @author [Alexander]
 * @version 1.0
 * @since [10/10/2023]
 */
public class Main {
    /**
     * The entry point to the application.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {

        try {
//            Connection connection = DriverManager.getConnection(
//                    "jdbc:postgresql://localhost:5432/jdbc",
//                    "postgres",
//                    "password");
            Connection connection = DBConnector.getConnection();
            Database database =
                    DatabaseFactory.getInstance()
                            .findCorrectDatabaseImplementation(new JdbcConnection(connection));

            Liquibase liquibase =
                    new Liquibase("db/changelog/main-changelog.xml",
                            new ClassLoaderResourceAccessor(),
                            database);

            liquibase.update();
            System.out.println("Миграции успешно выполнены!");

            // Creating a service for working with players
            PlayerRepository playerRepository = new PlayerRepositoryImpl(connection);
            // Creating a transaction repository
            TransactionRepository transactionRepository = new TransactionRepositoryImpl(connection);

            PlayerService playerService = new PlayerServiceImpl(playerRepository, transactionRepository);
            // Creating a service for performing transactions using the player service and repository
            TransactionService transactionService = new TransactionServiceImpl(playerService, transactionRepository);
            // Creating a console input handler that uses player and transaction services
            ConsoleInputHandler consoleInputHandler = new ConsoleInputHandler(playerService, transactionService);

            // Infinite loop for handling user input
            while (true) {
                consoleInputHandler.start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
