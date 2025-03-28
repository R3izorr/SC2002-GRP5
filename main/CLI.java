package main;

import controller.ApplicationManager;
import controller.ProjectManager;
import controller.UserManager;
import model.User;
import model.Applicant;
import model.HDBOfficer;
import controller.EnquiryManager;

import java.util.Scanner;

public class CLI {
    private final UserManager userManager;
    private final ProjectManager projectManager;
    private final ApplicationManager applicationManager;
    private final Scanner scanner;
    private final EnquiryManager enquiryManager;

    public CLI() {
        userManager = new UserManager();
        projectManager = new ProjectManager();
        applicationManager = new ApplicationManager();
        enquiryManager = new EnquiryManager(); 
        scanner = new Scanner(System.in);
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
            case APPLICANT -> new ApplicantDashboard(projectManager, applicationManager, scanner)
                                    .launch((Applicant) user);
            case HDB_OFFICER -> new OfficerDashboard(projectManager, applicationManager, enquiryManager, scanner)
                                    .launch((HDBOfficer) user);
        
            default -> System.out.println("Dashboard for this role is not implemented yet.");
        }
    }
}
