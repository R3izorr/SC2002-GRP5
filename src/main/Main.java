package main;

import UI.*;
import boundary.LoginBoundary;
import controller.*;
import java.util.Scanner;
import model.*;
import repository.*;

public class Main {
    public static void main(String[] args) { 
    // Initialize repositories and load CSV data (order: OfficerList, ManagerList, ApplicantList, then ProjectList)
        UserRepository userRepository = new UserRepository(); 
        userRepository.loadOfficers("data/OfficerList.csv"); 
        userRepository.loadManagers("data/ManagerList.csv"); 
        userRepository.loadApplicants("data/ApplicantList.csv");

        ProjectRepository projectRepository = new ProjectRepository();
        projectRepository.loadProjects("data/ProjectList.csv", userRepository.getManagers());

        ApplicationRepository applicationRepository = new ApplicationRepository();

        // Initialize UserController.
        UserController userController = new UserController(userRepository);
        LoginBoundary loginBoundary = new LoginBoundary(userController);
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.println("\n=== Welcome to the BTO Management System ===");
            System.out.println("Enter 'exit' as NRIC to quit the system.");
            
            User loggedInUser = loginBoundary.displayLogin();
            if (loggedInUser == null) {
                // Login failed or user typed 'exit'
                System.out.println("Returning to login prompt.");
                continue;
            }
            
            // Dispatch to the appropriate dashboard based on the user's role.
            if (loggedInUser.getRole().equals("Applicant")) {
                Applicant applicant = (Applicant) loggedInUser;
                controller.ProjectController projectController =
                        new controller.ProjectController(projectRepository, applicationRepository, applicant);
                controller.EnquiryController enquiryController =
                        new controller.EnquiryController(applicant);
                ApplicantDashBoard applicantDashBoard =
                        new ApplicantDashBoard(applicant, projectController, enquiryController, userController);
                applicantDashBoard.run();
            } else if (loggedInUser.getRole().equals("HDBOfficer")) {
                HDBOfficer officer = (HDBOfficer) loggedInUser;
                HDBOfficerDashBoard officerDashBoard =
                        new HDBOfficerDashBoard(officer, userController, projectRepository, applicationRepository);
                officerDashBoard.run();
            } else if (loggedInUser.getRole().equals("HDBManager")) {
                HDBManager manager = (HDBManager) loggedInUser;
                HDBManagerDashBoard managerDashBoard =
                        new HDBManagerDashBoard(manager, userController, projectRepository, applicationRepository, userRepository);
                managerDashBoard.run();
            }
            
            // When a user logs out from their dashboard, ask if they want to log in again.
            System.out.print("\nReturn to login prompt? (Y to log in again, any other key to exit): ");
            String ans = scanner.nextLine().trim();
            if (!ans.equalsIgnoreCase("Y")) {
                System.out.println("Exiting system.");
                break;
            }
        }
    }
}
