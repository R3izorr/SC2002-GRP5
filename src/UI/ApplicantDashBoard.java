package UI;

import boundary.EnquiryBoundary;
import controller.EnquiryController;
import controller.ProjectController;
import controller.UserController;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import model.Applicant;
import model.Application;
import model.BTOProject;
import utils.FilterSettings;

public class ApplicantDashBoard {
    private Applicant applicant; 
    private ProjectController projectController; 
    private EnquiryController enquiryController;
    private UserController userController; 
    private Scanner scanner;
    private EnquiryBoundary enquiryBoundary;
    private FilterSettings filterSettings; 

    public ApplicantDashBoard(Applicant applicant, ProjectController projectController,
        EnquiryController enquiryController, UserController userController) {
        this.applicant = applicant;
        this.projectController = projectController;
        this.enquiryController = enquiryController;
        this.userController = userController;
        this.scanner = new Scanner(System.in);
        this.enquiryBoundary = new EnquiryBoundary(enquiryController);
        this.filterSettings = new FilterSettings(); // default filter settings
    }

    public void run() {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n=== Applicant Dashboard ===");
            System.out.println("1. View Available Projects (Filtered)");
            System.out.println("2. Update Filter Settings");
            System.out.println("3. Apply for a Project");
            System.out.println("4. View Application Status");
            System.out.println("5. Withdraw Application");
            System.out.println("6. Request Flat Booking");
            System.out.println("7. Submit Enquiry");
            System.out.println("8. Edit Enquiry");
            System.out.println("9. Delete Enquiry");
            System.out.println("10. View My Enquiries");
            System.out.println("11. Change Password");
            System.out.println("12. Logout");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
            switch (choice) {
                case 1:
                    viewFilteredProjects();
                    break;
                case 2:
                    updateFilterSettings();
                    break;
                case 3:
                    System.out.print("Enter the project number to apply: ");
                    int projectChoice = scanner.nextInt();
                    if (projectChoice < 1 || projectChoice > projectController.getVisibleProjects().size()) {
                        System.out.println("Invalid project number. Please try again.");
                        continue;
                    }
                    scanner.nextLine();
                    System.out.print("Enter flat type (2-Room/3-Room): ");
                    String flatType = scanner.nextLine();
                    boolean applied = projectController.applyForProject(projectChoice, flatType);
                    if (applied) {
                    System.out.println("Application submitted successfully.");
                    }
                    break;
                case 4:
                    Application app = projectController.getApplication();
                    if (app != null) {
                    System.out.println("\n=== Your Application Details ===");
                    System.out.println("---------------------------------");
                    System.out.printf("%-15s: %s%n", "Project ID", app.getProject().getProjectId());
                    System.out.printf("%-15s: %s%n", "Project Name", app.getProject().getProjectName());
                    System.out.printf("%-15s: %s%n", "Flat Type", app.getFlatType());
                    System.out.printf("%-15s: %s%n", "Status", app.getStatus());
                    System.out.println("---------------------------------");
                    } else {
                    System.out.println("No application found.");
                    }
                    break;
                case 5:
                    projectController.withdrawApplication();
                     break;
                case 6:
                    projectController.requestBooking();
                    break;
                case 7:
                    enquiryBoundary.submitEnquiry();
                    break;
                case 8:
                    enquiryBoundary.editEnquiry();
                    break;
                case 9:
                    enquiryBoundary.deleteEnquiry();
                    break;
                case 10:
                    enquiryBoundary.displayEnquiries();
                    break;
                case 11:
                    System.out.print("Enter new password: ");
                    String newPassword = scanner.nextLine();
                    userController.changePassword(applicant, newPassword);
                    System.out.println("Password changed successfully.");
                    System.out.println("Please re-login.");
                    System.out.println("Logging out...");
                    exit = true;
                    break;
                case 12:
                    System.out.println("Logging out...");
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

      private void viewFilteredProjects() {
        List<BTOProject> projects = new ArrayList<>(projectController.getVisibleProjects());
        // Apply neighborhood filter if set.
        if (filterSettings.getNeighborhood() != null && !filterSettings.getNeighborhood().isEmpty()) {
            projects.removeIf(proj -> !proj.getNeighborhood().equalsIgnoreCase(filterSettings.getNeighborhood()));
        }
        // Apply flat type filter if set (show projects with available units for that flat type).
        if (filterSettings.getFlatType() != null && !filterSettings.getFlatType().isEmpty()) {
            if (filterSettings.getFlatType().equalsIgnoreCase("2-Room")) {
                projects.removeIf(proj -> proj.getUnits2Room() <= 0);
            } else if (filterSettings.getFlatType().equalsIgnoreCase("3-Room")) {
                projects.removeIf(proj -> proj.getUnits3Room() <= 0);
            }
        }
        // Sort alphabetically if required.
        if (filterSettings.isSortAlphabetical()) {
            projects.sort((p1, p2) -> p1.getProjectName().compareToIgnoreCase(p2.getProjectName()));
        }
        System.out.println("=== Available Projects (Filtered) ===");
        if (projects.isEmpty()) {
            System.out.println("No projects match your filter criteria.");
        } else {
            for (BTOProject proj : projects) {
                System.out.println(proj.toStringForApplicant());
            }
        }
    }
    
    // Allows the user to update filter settings; these settings persist.
    private void updateFilterSettings() {
        System.out.print("Enter Neighborhood filter (leave empty for no filter): ");
        String neighborhood = scanner.nextLine().trim();
        if (neighborhood.isEmpty()) {
            filterSettings.setNeighborhood(null);
        } else {
            filterSettings.setNeighborhood(neighborhood);
        }
        System.out.print("Enter Flat Type filter (2-Room/3-Room, leave empty for no filter): ");
        String flatType = scanner.nextLine().trim();
        if (flatType.isEmpty()) {
            filterSettings.setFlatType(null);
        } else {
            filterSettings.setFlatType(flatType);
        }
        // Here you could add an option to toggle sort order, but default is alphabetical.
        System.out.println("Filter settings updated.");
    }
}
