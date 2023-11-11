package app;


import domain.model.Transaction;
import domain.model.TransactionType;
import exception.DuplicateTransactionIdException;
import service.PlayerService;
import service.TransactionService;
import util.AuditLogger;

import java.math.BigDecimal;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * A class that handles user input from the console and manages the application's logic.
 * <p>
 * This class is the main part of the console interface of the application and facilitates interaction
 * between the user and the system. It provides options for user registration, authentication, account management
 * for players, as well as conducting transactions and viewing transaction history.
 */
public class ConsoleInputHandler {

    private static final String COMMAND_REGISTRATION = "1 - Registration account";
    private static final String COMMAND_AUTHENTICATE = "2 - Login to account";
    private static final String COMMAND_CREDIT = "1 - CREDIT";
    private static final String COMMAND_DEBIT = "2 - DEBIT";
    private static final String COMMAND_BALANCE = "1 - Get balance";
    private static final String COMMAND_CHANGE_BALANCE = "2 - Change balance";
    private static final String COMMAND_HISTORY_TRANSACTIONS = "3 - Get history transaction";
    private static final String COMMAND_EXIT = "4 - Exit";
    private static final String COMMAND_WRONG_ANSWER = "This command does not exist!!\n";
    private static final String COMMAND_STOP = "0";
    private final Scanner scanner;
    private final PlayerService playerService;
    private final TransactionService transactionService;


    public ConsoleInputHandler(PlayerService playerService, TransactionService transactionService) {
        this.playerService = playerService;
        this.transactionService = transactionService;
        scanner = new Scanner(System.in);
    }


    public void start() {
        AuditLogger.log("The program has started");

        int commandAnswer = -1;

        while (commandAnswer != 0) {
            try {

                System.out.println(COMMAND_REGISTRATION);
                System.out.println(COMMAND_AUTHENTICATE);
                commandAnswer = scanner.nextInt();

                scanner.nextLine();
                switch (commandAnswer) {
                    case 1:
                        AuditLogger.log("The user entered the command " + COMMAND_REGISTRATION);
                        registrationPlayer();
                        break;
                    case 2:
                        AuditLogger.log("The user entered the command " + COMMAND_AUTHENTICATE);
                        Long authenticatePlayer = authenticatePlayer();
                        if (authenticatePlayer != null) {
                            menuForAuthenticatePlayer(authenticatePlayer);
                        }
                        break;
                    case 0:
                        commandAnswer = 0;
                        return;
                    default:
                        AuditLogger.log("The user entered an incorrect command");
                        System.out.println(COMMAND_WRONG_ANSWER);
                }
            } catch (InputMismatchException ex) {
                scanner.nextLine();
                AuditLogger.log("The user entered an incorrect command format");
                System.out.println(("Incorrect format command. Please repeat"));
            }
        }
    }


    private void registrationPlayer() {
        AuditLogger.log("Entered the registration command");
        String name = readPlayerName();
        String password = readPassword();
        boolean successfully = playerService.performPlayerRegistration(name, password);
        if (successfully) {
            AuditLogger.log("The user has been successfully registered");
            System.out.println("Player registered successfully");
        } else {
            AuditLogger.log("The user was not registered");
            System.out.println("Registration failed =( Account already exists. Please log in.");
        }
    }

    private Long authenticatePlayer() {
        AuditLogger.log("User attempting authentication");
        String name = readPlayerName();
        String password = readPassword();
        Long playerId = playerService.performPlayerAuthentication(name, password);
        if (playerId != null) {
            AuditLogger.log("User logged into the account with the name " + name);
            System.out.println("Hello, " + name);
            return playerId;
        } else {
            AuditLogger.log("User could not log into the account");
            System.out.println("Incorrect username or password! Please try again or register.");
            return null;
        }
    }

    private String readPlayerName() {
        System.out.println("Enter your name: ");
        String name = scanner.nextLine();
        AuditLogger.log("Entered name " + name);
        return name;
    }

    private String readPassword() {
        System.out.println("Enter your password: ");
        String password = scanner.nextLine();
        AuditLogger.log("Entered password");
        return password;
    }

    private void showHistory(Long playerId) {
        AuditLogger.log("User with id = " + playerId + " requested transaction history");
        StringBuilder builder = new StringBuilder();
        List<Transaction> transactionHistory = playerService.getPlayerTransactionHistory(playerId);
        if (transactionHistory.isEmpty()) {
            System.out.println("Transaction list is empty");
        } else {
            for (Transaction transaction : transactionHistory) {
                builder.append("Transaction code : ").append(transaction.getTransactionCode())
                        .append("\n").append("Amount : ").append(transaction.getAmount())
                        .append("\n").append("Type : ")
                        .append(transaction.getType().name()).append("\n");
            }

            System.out.println(builder);
        }
    }

    private void incomingDataForCreatingTransaction(Long playerId) throws DuplicateTransactionIdException {
        System.out.println(COMMAND_CREDIT);
        System.out.println(COMMAND_DEBIT);
        String commandCreditOrDebit = scanner.nextLine();
        TransactionType type;
        if (commandCreditOrDebit.equals("1")) {
            type = TransactionType.CREDIT;
            AuditLogger.log("User with id = " + playerId + " requested a DEBIT transaction");
        } else if (commandCreditOrDebit.equals("2")) {
            type = TransactionType.DEBIT;
            AuditLogger.log("User with id = " + playerId + " requested a CREDIT transaction");
        } else {
            System.out.println(COMMAND_WRONG_ANSWER);
            AuditLogger.log("User with id = " + playerId + " selected a non-existent command");
            return;
        }
        System.out.println("Enter the amount of money: ");
        try {
            BigDecimal amount = BigDecimal.valueOf(scanner.nextDouble());
            AuditLogger.log("User with id = " + playerId + " entered the transaction amount");
            boolean success = transactionService.makeTransaction(playerId, amount, type);
            if (success) {
                AuditLogger.log("Transaction successful for user with id " + playerId);
            } else {
                AuditLogger.log("Transaction was not successful");
            }
        } catch (NumberFormatException e) {
            AuditLogger.log("Transaction was not successful");
            scanner.nextLine();
            System.out.println("Invalid format! Please try again.");
        }
    }

    private void menuForAuthenticatePlayer(Long playerId) {
        int responseCommand;
        boolean authPlayer = true;
        while (authPlayer) {
            System.out.println(COMMAND_BALANCE);
            System.out.println(COMMAND_CHANGE_BALANCE);
            System.out.println(COMMAND_HISTORY_TRANSACTIONS);
            System.out.println(COMMAND_EXIT);

            responseCommand = scanner.nextInt();
            scanner.nextLine();
            switch (responseCommand) {
                case 1:
                    AuditLogger.log("User with id = " + playerId + " selected the command " + COMMAND_BALANCE);
                    BigDecimal balance = playerService.getBalance(playerId);
                    System.out.println("Your balance = " + balance);
                    AuditLogger.log("Information received");
                    break;
                case 2:
                    try {
                        AuditLogger.log("User with id = " + playerId + " selected the command " + COMMAND_CHANGE_BALANCE);
                        incomingDataForCreatingTransaction(playerId);
                    } catch (DuplicateTransactionIdException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 3:
                    AuditLogger.log("User with id = " + playerId + " selected the command " + COMMAND_HISTORY_TRANSACTIONS);
                    showHistory(playerId);
                    AuditLogger.log("Information received");
                    break;
                case 4:
                    AuditLogger.log("User with id = " + playerId + " exited the account");
                    authPlayer = false;
                    break;
                default:
                    AuditLogger.log("User entered an incorrect command");
                    System.out.println(COMMAND_WRONG_ANSWER);
            }
        }
    }
}
