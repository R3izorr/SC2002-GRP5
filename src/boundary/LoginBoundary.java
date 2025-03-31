package boundary;

import controller.UserController;
import java.util.Scanner;
import model.User;
import utils.InputValidator;

public class LoginBoundary { 
    private UserController userController; 
    private Scanner scanner;

    public LoginBoundary(UserController userController) {
        this.userController = userController;
        this.scanner = new Scanner(System.in);
    }
    
    public User displayLogin() {
        while (true) {
            System.out.println("=== BTO Management System Login ===");
            System.out.println("1.Login");
            System.out.println("2.Exit");
            System.out.print("Select an option: ");
            int option = scanner.nextInt();
            scanner.nextLine(); // Consume the leftover newline character
            if (option == 2) {
                System.out.println("Exiting the system. Goodbye!");
                return null;
            }
            System.out.print("Enter NRIC: ");
            String nric = scanner.nextLine().trim();
            while (!InputValidator.isValidNRIC(nric)) {
                System.out.println("Invalid NRIC format. Please try again.");
                System.out.print("Enter NRIC: ");
                nric = scanner.nextLine().trim();
            }
            System.out.print("Enter Password: ");
            String password = scanner.nextLine().trim();
        
            User user = userController.login(nric, password);
            if (user == null) {
                System.out.println("Login failed. Please check your credentials.");
            } else {
                System.out.println("Login successful. Welcome, " + user.getRole() + " " + user.getNric());
                return user;
            }
        }
    }
}