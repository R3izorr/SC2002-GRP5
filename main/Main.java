package main;

import controller.UserManager;
import model.User;

import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final UserManager userManager = new UserManager();

    public static void main(String[] args) {
        boolean running = true;

        while (running) {
            printMainMenu();
            String choice = scanner.nextLine().trim();

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

        scanner.close();
    }

    private static void printMainMenu() {
        System.out.println("\n=== BTO Management System ===");
        System.out.println("1. Login");
        System.out.println("2. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void handleLogin() {
        System.out.print("Enter NRIC: ");
        String nric = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        User user = userManager.authenticate(nric, password);
        if (user != null) {
            System.out.println("✅ Login successful!");
            System.out.println("Welcome, " + user.getRole());
            // Placeholder for role-specific dashboard
        } else {
            System.out.println("❌ Invalid NRIC or password. Please try again.");
        }
    }
}
