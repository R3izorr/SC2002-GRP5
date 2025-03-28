package main;
import model.Applicant;
import controller.UserManager;
import model.User;
import controller.ProjectManager;
import model.Project;
import java.util.Scanner;



public class CLI {
    private final UserManager userManager;
    private final Scanner scanner;
    private final ProjectManager projectManager;

    public CLI() {
        userManager = new UserManager();
        projectManager = new ProjectManager(); // new
        scanner = new Scanner(System.in);
    }
    private void viewProjects() {
        System.out.println("\n=== Available Projects ===");
        var visible = projectManager.filterVisibleProjects();
        if (visible.isEmpty()) {
            System.out.println("No projects available.");
            return;
        }
        for (Project p : visible) {
            System.out.println(p.getDetails());
        }
    }

    public void start() {
        while (true) {
            System.out.println("===== BTO Management System =====");
            System.out.println("1. Login");
            System.out.println("2. Exit");
            System.out.print("Enter choice: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> handleLogin();
                case "2" -> {
                    System.out.println("Exiting system.");
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void handleLogin() {
        System.out.print("Enter NRIC: ");
        String nric = scanner.nextLine().trim();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine().trim();
    
        User user = userManager.authenticate(nric, password);
    
        if (user != null) {
            System.out.println("Login successful. Role: " + user.getRole());
            displayDashboard(user);
        } else {
            System.out.println("Invalid NRIC or password.");
        }
    }
    private void displayDashboard(User user) {
        switch (user.getRole()) {
            case APPLICANT -> handleApplicantDashboard((Applicant) user);
            default -> System.out.println("Dashboard for this role is not implemented yet.");
        }
    }
    private void handleApplicantDashboard(Applicant applicant) {
        while (true) {
            System.out.println("\n=== Applicant Dashboard ===");
            System.out.println("1. View Eligible Projects");
            System.out.println("2. Apply for Project");
            System.out.println("3. View Application Status");
            System.out.println("4. Withdraw Application");
            System.out.println("5. Logout");
            System.out.print("Enter choice: ");
            String choice = scanner.nextLine().trim();
    
            switch (choice) {
                case "1" -> System.out.println("View Projects - Coming soon");
                case "2" -> System.out.println("Apply for Project - Coming soon");
                case "3" -> System.out.println("View Application Status - Coming soon");
                case "4" -> System.out.println("Withdraw Application - Coming soon");
                case "5" -> {
                    System.out.println("Logging out...");
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }
    
    
}
