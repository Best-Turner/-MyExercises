import app.WalletService;
import app.in.ConsoleInputHandler;
import infrastructure.TransactionRepository;
import infrastructure.TransactionRepositoryImpl;
import services.PlayerService;
import services.PlayerServiceImpl;
import services.TransactionService;
import services.TransactionServiceImpl;

public class Main {

    public static void main(String[] args) {
        PlayerService playerService = new PlayerServiceImpl();
        TransactionRepository transactionRepository = new TransactionRepositoryImpl();
        TransactionService transactionService = new TransactionServiceImpl(playerService, transactionRepository);
        ConsoleInputHandler consoleInputHandler = new ConsoleInputHandler(playerService, transactionService);
        WalletService walletService = new WalletService(consoleInputHandler);
        walletService.startWork();
    }
}