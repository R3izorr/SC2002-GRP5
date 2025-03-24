package main;

import controller.UserManager;
import model.User;
import utils.InputValidator;


import java.util.Scanner;

public class Main {
    //initiate scanner for option
    private static final Scanner sc = new Scanner(System.in);
    private static final UserManager userManager = new UserManager();

    public static void main(String[] args) {
        boolean running = true;

        while (running) {
            printMainMenu();
            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1":
                    handleLogin();
                    break;
                case "2":
                    System.out.println("Exiting... Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid input. Please enter 1 or 2.");
            }
        }

        sc.close();
    }

    private static void printMainMenu() 
    {
        System.out.println("\n=== BTO Management System ===");
        System.out.println("1. Login");
        System.out.println("2. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void handleLogin() {
        System.out.print("Enter NRIC: ");
        String nric = sc.nextLine().toUpperCase();
    
        if (!InputValidator.isValidNRIC(nric)) {
            System.out.println(" Invalid NRIC format. Format must be S/T + 7 digits + 1 letter (e.g., S1234567A).");
            return;
        }
    
        System.out.print("Enter Password: ");
        String password = sc.nextLine();
    
        User user = userManager.authenticate(nric, password);
        if (user != null) {
            System.out.println("Login successful!");
            System.out.println("Welcome, " + user.getRole());
        } else {
            System.out.println(" Invalid NRIC or password. Please try again.");
        }
    }
}
