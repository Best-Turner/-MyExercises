package domain.model;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final String id;
    private final String name;
    private final String password;
    private double balance;
    private final List<Transaction> transactionHistory;

    public Player(String id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
        balance = 0.0;
        transactionHistory = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public List<Transaction> getTransactionHistory() {
        return transactionHistory;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void addTransaction(Transaction transaction) {
        transactionHistory.add(transaction);
    }

}
