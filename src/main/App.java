package main;

import boundary.LoginBoundary;
import controller.*;
import entity.model.*;
import entity.user.*;
import java.util.Scanner;
import repository.*;
import repository.modelRepository.*;
import repository.userRepository.*;
import system.ApplicantDashBoard;
import system.HDBManagerDashBoard;
import system.HDBOfficerDashBoard;

public class App implements ISystem {
    /* The core application */
    private boolean isInitialized = false;
    private static final String APPLICANT_CSV   = "data\\ApplicantList.csv";
    private static  final String OFFICER_CSV     = "data\\OfficerList.csv";
    private static final String MANAGER_CSV     = "data\\ManagerList.csv";
    private static final String PROJECT_CSV     = "data\\ProjectList.csv";
    private static final String APPLICATION_CSV = "data\\ApplicationList.csv";
    private static final String ENQUIRY_CSV     = "data\\EnquiryList.csv";
    private static final String NOTIFICATION_CSV = "data\\NotifcationList.csv";

    // Repositories (DIP: depend on interfaces)
    private IRepository<Applicant>              applicantRepo;
    private IRepository<HDBOfficer>             officerRepo;
    private IRepository<HDBManager>             managerRepo;
    private ICRUDRepository<BTOProject>         projectRepo;
    private ICRUDRepository<Application>        applicationRepo;
    private ICRUDRepository<Enquiry>            enquiryRepo;
    private ICRUDRepository<Notification>      notificationRepo; // Not used in this version

    // Controllers (DIP: depend on interfaces)
    private UserController    userController;
    private ProjectController projectController;
    private ApplicationController applicationController;
    private EnquiryController enquiryController;
    private NotificationController notificationController; 

    @Override
    public void initialize() {
        if (isInitialized) return;

        //Instantiate repos (low-level modules) against interfaces and load data
        applicantRepo   = new ApplicantRepository(APPLICANT_CSV);
        officerRepo     = new HDBOfficerRepository(OFFICER_CSV);
        managerRepo     = new HDBManagerRepository(MANAGER_CSV);
        applicantRepo.load();
        officerRepo.load();
        managerRepo.load();
        projectRepo     = new ProjectRepository(PROJECT_CSV, managerRepo.getAll(), officerRepo.getAll());
        projectRepo.load();
        applicationRepo = new ApplicationRepository(APPLICATION_CSV, applicantRepo.getAll(), officerRepo.getAll(), projectRepo.getAll());
        applicationRepo.load();
        enquiryRepo     = new EnquiryRepository(ENQUIRY_CSV, applicantRepo.getAll(),projectRepo.getAll());
        enquiryRepo.load();
        notificationRepo = new NotificationRepository(NOTIFICATION_CSV);
        notificationRepo.load();        
        // Instantiate controllers (high-level modules) against interfaces
        userController    = new UserController(applicantRepo, officerRepo, managerRepo);
        notificationController = new NotificationController(notificationRepo);
        isInitialized = true;
    }
    
    @Override
    public void run() {
        if (!isInitialized) initialize();
        LoginBoundary loginBoundary = new LoginBoundary(userController);
        Scanner scanner = new Scanner(System.in);

        while (true) {
            User loggedInUser = loginBoundary.displayLogin();
            if (loggedInUser == null) {
                System.out.println("Login failed or exit command entered.");
                break;
            }
            switch (loggedInUser.getRole()) {
                case "Applicant":
                    projectController = new ProjectController(projectRepo, applicationRepo, (Applicant) loggedInUser);
                    applicationController = new ApplicationController(applicationRepo, (Applicant) loggedInUser);
                    enquiryController = new EnquiryController(projectRepo, enquiryRepo, (Applicant) loggedInUser);
                    ApplicantDashBoard applicantDashBoard = new ApplicantDashBoard((Applicant) loggedInUser, projectController, applicationController, enquiryController, userController, notificationController);
                    applicantDashBoard.run();
                    break;
                case "HDBOfficer":
                    projectController = new ProjectController(projectRepo, applicationRepo, (HDBOfficer) loggedInUser);
                    applicationController = new ApplicationController(applicationRepo, (HDBOfficer) loggedInUser);
                    enquiryController = new EnquiryController(projectRepo, enquiryRepo);
                    HDBOfficerDashBoard officerDashBoard = new HDBOfficerDashBoard((HDBOfficer) loggedInUser, projectController, applicationController, enquiryController, userController, notificationController);
                    officerDashBoard.run();
                    break;
                case "HDBManager":
                    projectController = new ProjectController(projectRepo, applicationRepo);
                    applicationController = new ApplicationController(applicationRepo);
                    enquiryController = new EnquiryController(projectRepo, enquiryRepo);
                    HDBManagerDashBoard managerDashBoard = new HDBManagerDashBoard((HDBManager) loggedInUser, projectController, applicationController, enquiryController, userController, notificationController);
                    managerDashBoard.run();
                    break;
                default:
                    System.out.println("Unknown user role. Exiting.");
                    return;
            }
        }
        scanner.close();
    }
}

