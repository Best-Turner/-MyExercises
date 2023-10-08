package app.in;

import domain.model.TransactionType;
import exception.DuplicateTransactionIdException;
import services.PlayerService;
import services.TransactionService;
import util.AuditLogger;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ConsoleInputHandler {
    private final Scanner scanner;
    private final PlayerService playerService;
    private final TransactionService transactionService;
    private static final String COMMAND_REGISTRATION = "1 - Registration account";
    private static final String COMMAND_AUTHENTICATE = "2 - Login to account";
    private static final String COMMAND_CREDIT = "1 - CREDIT";
    private static final String COMMAND_DEBIT = "2 - DEBIT";
    private static final String COMMAND_BALANCE = "1 - Get balance";
    private static final String COMMAND_CHANGE_BALANCE = "2 - Change balance";
    private static final String COMMAND_HISTORY_TRANSACTIONS = "3 - Get history transaction";
    private static final String COMMAND_EXIT = "4 - Exit";
    private static final String COMMAND_WRONG_ANSWER = "This command does not exist!!\n";


    public ConsoleInputHandler(PlayerService playerService, TransactionService transactionService) {
        this.playerService = playerService;
        this.transactionService = transactionService;
        scanner = new Scanner(System.in);
    }


    public void start() {
        AuditLogger.log("Программа начала работать");

        while (true) {
            try {
                int commandAnswer = 0;

                System.out.println(COMMAND_REGISTRATION);
                System.out.println(COMMAND_AUTHENTICATE);
                commandAnswer = scanner.nextInt();

                scanner.nextLine();
                switch (commandAnswer) {
                    case 1:
                        AuditLogger.log("Пользователь ввел команду " + COMMAND_REGISTRATION);
                        registrationPlayer();
                        break;
                    case 2:
                        AuditLogger.log("Пользователь ввел команду " + COMMAND_AUTHENTICATE);
                        String authenticatePlayer = authenticatePlayer();
                        if (authenticatePlayer != null) {
                            menuForAuthenticatePlayer(authenticatePlayer);
                        }
                        break;
                    default:
                        AuditLogger.log("Пользователь ввел не верную комманду");
                        System.out.println(COMMAND_WRONG_ANSWER);
                }
            } catch (InputMismatchException ex) {
                scanner.nextLine();
                AuditLogger.log("Пользователь ввел не верный формат суммы");
                System.out.println(("Incorrect format command. Please repeat"));
            }
        }
    }


    private void registrationPlayer() {
        AuditLogger.log("Ввел команду на регистрацию");
        String name = readPlayerName();
        String password = readPassword();
        boolean successfully = playerService.performPlayerRegistration(name, password);
        if (successfully) {
            AuditLogger.log("Пользователь успешно зарегистрирован");
            System.out.println("Player registered successfully");
        } else {
            AuditLogger.log("Пользователь не зарегистрирован");
            System.out.println("Registration failed =( Account already exists. Please log in.");
        }
    }

    private String authenticatePlayer() {
        AuditLogger.log("Пользователь пытается проити аутентификацию");
        String name = readPlayerName();
        String password = readPassword();
        String playerId = playerService.performPlayerAuthentication(name, password);
        if (playerId != null) {
            AuditLogger.log("Пользователь зашел в аккаунт под имнем " + name);
            System.out.println("Hello, " + name);
            return playerId;
        } else {
            AuditLogger.log("Пользователь не смог войти в аккаунт");
            System.out.println("Incorrect username or password! Please try again or register.");
            return null;
        }

    }

    private String readPlayerName() {
        System.out.println("Enter your name: ");
        String name = scanner.nextLine();
        AuditLogger.log("Ввел имя " + name);
        return name;
    }

    private String readPassword() {
        System.out.println("Enter your password: ");
        String password = scanner.nextLine();
        AuditLogger.log("Ввел пароль");
        return password;
    }

    private void showHistory(String playerId) {
        AuditLogger.log("Пользователь запросил истрию транзакций");
        System.out.println(playerService.getPlayerTransactionHistory(playerId));

    }

    private void incomingDataForCreatingTransaction(String playerId) throws DuplicateTransactionIdException {
        System.out.println(COMMAND_CREDIT);
        System.out.println(COMMAND_DEBIT);
        String responseType = scanner.nextLine();
        TransactionType type;
        if (responseType.equals("1")) {
            type = TransactionType.CREDIT;
            AuditLogger.log("Пользователь запросил транзакцию - DEBIT");
        } else if (responseType.equals("2")) {
            type = TransactionType.DEBIT;
            AuditLogger.log("Пользователь запросил транзакцию - CREDIT");
        } else {
            System.out.println(COMMAND_WRONG_ANSWER);
            AuditLogger.log("Пользователь выбрал не существующую комманду");
            return;
        }
        System.out.println("Enter amount money: ");
        try {
            double amount = scanner.nextDouble();
            AuditLogger.log("Пользователь ввел сумму для транзакции");
            boolean access = transactionService.makeTransaction(playerId, amount, type);
            AuditLogger.log("Транзакция успешна");

        } catch (NumberFormatException e) {
            AuditLogger.log("Транзакция не успешна");
            scanner.nextLine();
            System.out.println("Invalid format! Please try again.");
        }
    }


    private void menuForAuthenticatePlayer(String playerId) {
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
                    AuditLogger.log("Пользователь выбрал команду " + COMMAND_BALANCE);
                    double balance = playerService.getBalance(playerId);
                    System.out.println("You balance = " + balance);
                    AuditLogger.log("Информация получена");
                    break;
                case 2:
                    try {
                        AuditLogger.log("Пользователь выбрал команду " + COMMAND_CHANGE_BALANCE);
                        incomingDataForCreatingTransaction(playerId);
                    } catch (DuplicateTransactionIdException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 3:
                    AuditLogger.log("Пользователь выбрал комамнду " + COMMAND_HISTORY_TRANSACTIONS);
                    showHistory(playerId);
                    AuditLogger.log("Информация получена");
                    break;
                case 4:
                    AuditLogger.log("Пользователь вышел из аккаунта");
                    authPlayer = false;
                    break;
                default:
                    AuditLogger.log("Пользователь ввел не верную комманду");
                    System.out.println(COMMAND_WRONG_ANSWER);
            }
        }
    }
}
