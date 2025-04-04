package main;

import controller.UserController;
import java.util.Scanner;

import model.user.Applicant;
import model.user.HDBManager;
import model.user.HDBOfficer;
import model.user.User;
import repository.ApplicationRepository;
import repository.ProjectRepository;
import repository.UserRepository;
import system.ApplicantDashBoard;
import system.HDBOfficerDashBoard;

public class App {
    public static void runSystem() {
        // Initialize repositories and load CSV files
        UserRepository userRepository = new UserRepository("data/ApplicantList.csv", "data/OfficerList.csv", "data/ManagerList.csv");

        userRepository.loadOfficers("data/OfficerList.csv");
        userRepository.loadManagers("data/ManagerList.csv");
        userRepository.loadApplicants("data/ApplicantList.csv");
        
        ProjectRepository projectRepository = new ProjectRepository("data/ProjectList.csv");
        projectRepository.loadProjects("data\\ProjectList.csv", userRepository.getManagers(), userRepository.getOfficers());
        
        ApplicationRepository applicationRepository = new ApplicationRepository();
        
        UserController userController = new UserController(userRepository);
        boundary.LoginBoundary loginBoundary = new boundary.LoginBoundary(userController);
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.println("\n=== Welcome to the BTO Management System ===");
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
                        new ApplicantDashBoard(applicant, projectController, projectRepository, enquiryController, userController);
                applicantDashBoard.run();
            } else if(loggedInUser.getRole().equals("HDBOfficer")) {
                HDBOfficer officer = (HDBOfficer) loggedInUser;
                controller.ProjectController projectController =
                        new controller.ProjectController(projectRepository, applicationRepository, officer);
                HDBOfficerDashBoard officerDashBoard =
                        new HDBOfficerDashBoard(officer, userController, projectRepository, applicationRepository, projectController);
                officerDashBoard.run();
            } else if(loggedInUser.getRole().equals("HDBManager")) {
                HDBManager manager = (HDBManager) loggedInUser;
                // For Manager enquiry control, pass projectRepository to allow viewing enquiries by project.
                controller.EnquiryController enquiryController = new controller.EnquiryController(projectRepository);
                system.HDBManagerDashBoard managerDashBoard =
                        new system.HDBManagerDashBoard(manager, userController, projectRepository, applicationRepository, userRepository, enquiryController);
                managerDashBoard.run();
            }
        }
        scanner.close();
    }
    public static void main(String[] args) {
        runSystem();
    }
}

