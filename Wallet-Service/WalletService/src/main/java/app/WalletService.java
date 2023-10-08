package app;

import app.in.ConsoleInputHandler;
import domain.model.Transaction;


import java.util.HashSet;
import java.util.Set;

public class WalletService {
    private final Set<Transaction> transactions;
    private final ConsoleInputHandler consoleInputHandler;

    public WalletService(ConsoleInputHandler consoleInputHandler) {
        this.consoleInputHandler = consoleInputHandler;
        transactions = new HashSet<>();
    }

    public void startWork() {
        while (true) {
            consoleInputHandler.start();
        }
    }
}
