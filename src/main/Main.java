package main;

import UI.ApplicantDashBoard;
import UI.HDBOfficerDashBoard;
import controller.UserController;
import java.util.Scanner;
import model.Applicant;
import model.HDBManager;
import model.HDBOfficer;
import model.User;
import repository.ApplicationRepository;
import repository.ProjectRepository;
import repository.UserRepository;

public class Main {
    public static void main(String[] args) {
        runSystem();
    }
    
    public static void runSystem() {
        // Initialize repositories and load CSV files.
        UserRepository userRepository = new UserRepository();
        userRepository.loadOfficers("data/OfficerList.csv");
        userRepository.loadManagers("data/ManagerList.csv");
        userRepository.loadApplicants("data/ApplicantList.csv");
        
        ProjectRepository projectRepository = new ProjectRepository();
        projectRepository.loadProjects("data/ProjectList.csv", userRepository.getManagers());
        
        ApplicationRepository applicationRepository = new ApplicationRepository();
        
        UserController userController = new UserController(userRepository);
        boundary.LoginBoundary loginBoundary = new boundary.LoginBoundary(userController);
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.println("\n=== Welcome to the BTO Management System ===");
            System.out.println("Enter 'exit' as NRIC to quit.");
            User loggedInUser = loginBoundary.displayLogin();
            if(loggedInUser == null) {
                System.out.println("Login failed or exit command entered.");
                break;
            }
            if(loggedInUser.getRole().equals("Applicant")) {
                Applicant applicant = (Applicant) loggedInUser;
                controller.ProjectController projectController =
                        new controller.ProjectController(projectRepository, applicationRepository, applicant);
                // Pass projectRepository into EnquiryController for project lookup.
                controller.EnquiryController enquiryController =
                        new controller.EnquiryController(applicant, projectRepository);
                ApplicantDashBoard applicantDashBoard =
                        new ApplicantDashBoard(applicant, projectController, enquiryController, userController);
                applicantDashBoard.run();
            } else if(loggedInUser.getRole().equals("HDBOfficer")) {
                HDBOfficer officer = (HDBOfficer) loggedInUser;
                HDBOfficerDashBoard officerDashBoard =
                        new HDBOfficerDashBoard(officer, userController, projectRepository, applicationRepository);
                officerDashBoard.run();
            } else if(loggedInUser.getRole().equals("HDBManager")) {
                HDBManager manager = (HDBManager) loggedInUser;
                // For Manager enquiry control, pass projectRepository to allow viewing enquiries by project.
                controller.EnquiryController enquiryController = new controller.EnquiryController(projectRepository);
                UI.HDBManagerDashBoard managerDashBoard =
                        new UI.HDBManagerDashBoard(manager, userController, projectRepository, applicationRepository, userRepository, enquiryController);
                managerDashBoard.run();
            }
            
            System.out.print("\nReturn to login prompt? (Y to log in again, any other key to exit): ");
            String ans = scanner.nextLine().trim();
            if(!ans.equalsIgnoreCase("Y")) {
                System.out.println("Exiting system.");
                break;
            }
        }
    }
}
