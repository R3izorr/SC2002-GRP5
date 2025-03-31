package UI;

import boundary.EnquiryBoundary;
import controller.EnquiryController;
import controller.ProjectController;
import controller.UserController;
import java.util.Scanner;
import model.Applicant;
import model.Application;

public class ApplicantDashBoard {
    private Applicant applicant; 
    private ProjectController projectController; 
    private EnquiryController enquiryController;
    private UserController userController; 
    private Scanner scanner;
    private EnquiryBoundary enquiryBoundary;

    public ApplicantDashBoard(Applicant applicant, ProjectController projectController,
        EnquiryController enquiryController, UserController userController) {
        this.applicant = applicant;
        this.projectController = projectController;
        this.enquiryController = enquiryController;
        this.userController = userController;
        this.scanner = new Scanner(System.in);
        this.enquiryBoundary = new EnquiryBoundary(enquiryController);
    }

    public void run() {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n=== Applicant Dashboard ===");
            System.out.println("1. View Available Projects");
            System.out.println("2. Apply for a Project");
            System.out.println("3. View Application Status");
            System.out.println("4. Withdraw Application");
            System.out.println("5. Request Flat Booking");
            System.out.println("6. Submit Enquiry");
            System.out.println("7. Edit Enquiry");
            System.out.println("8. Delete Enquiry");
            System.out.println("9. View My Enquiries");
            System.out.println("10. Change Password");
            System.out.println("11. Logout");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
            switch (choice) {
                case 1:
                    projectController.getVisibleProjects().forEach(project -> System.out.println(project.toStringForApplicant()));
                    break;
                case 2:
                    System.out.print("Enter the project number to apply: ");
                    int projectChoice = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter flat type (2-Room/3-Room): ");
                    String flatType = scanner.nextLine();
                    boolean applied = projectController.applyForProject(projectChoice, flatType);
                    if (applied) {
                    System.out.println("Application submitted successfully.");
                    }
                    break;
                case 3:
                    Application app = projectController.getApplication();
                    if (app != null) {
                    System.out.println("Your Application Details:");
                    System.out.println(app.getProject().toString());
                    System.out.println("Flat Type: " + app.getFlatType());
                    System.out.println("Status: " + app.getStatus());
                    } else {
                    System.out.println("No application found.");
                    }
                    break;
                case 4:
                    projectController.withdrawApplication();
                     break;
                case 5:
                    projectController.requestBooking();
                    break;
                case 6:
                    enquiryBoundary.submitEnquiry();
                    break;
                case 7:
                    enquiryBoundary.editEnquiry();
                    break;
                case 8:
                    enquiryBoundary.deleteEnquiry();
                    break;
                case 9:
                    enquiryBoundary.displayEnquiries();
                    break;
                case 10:
                    System.out.print("Enter new password: ");
                    String newPassword = scanner.nextLine();
                    userController.changePassword(applicant, newPassword);
                    System.out.println("Password changed successfully.");
                    System.out.println("Please re-login.");
                    System.out.println("Logging out...");
                    exit = true;
                    break;
                case 11:
                    System.out.println("Logging out...");
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}
