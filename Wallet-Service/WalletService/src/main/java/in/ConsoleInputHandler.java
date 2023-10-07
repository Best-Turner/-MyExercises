package in;

import domain.model.Player;
import services.PlayerService;
import services.TransactionService;

import java.util.Scanner;

public class ConsoleInputHandler {
    private final Scanner scanner;
    private final PlayerService playerService;
    private final TransactionService transactionService;


    public ConsoleInputHandler(PlayerService playerService, TransactionService transactionService) {
        this.playerService = playerService;
        this.transactionService = transactionService;
        scanner = new Scanner(System.in);
    }


    
    public void registrationPlayer() {
        String name = readPlayerName();
        String password = readPassword();
        boolean successfully  = playerService.performPlayerRegistration(name, password);
        if (successfully){
            System.out.println("Player registered successfully");
        } else {
            System.out.println("Registration failed =(");
        }
    }
    public void authenticatePlayer() {
        String name = readPlayerName();
        String password = readPassword();
        Player player = playerService.performPlayerAuthentication(name, password);
        if (player != null) {
            System.out.println("Hello, " + player.getName());
        } else {
            System.out.println("Incorrect username or password! Please try again.");
        }

    }

    public void showBalance() {
    }

    private String readPlayerName() {
        System.out.println("Enter your name: ");
        return scanner.nextLine();
    }

    private String readPassword() {
        System.out.println("Enter your password: ");
        return scanner.nextLine();
    }

    private double readAmount() {
        System.out.println("Enter transaction amount");
        while(true) {
            try {
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid amount format. Please, try again");
            }
        }
    }




}
