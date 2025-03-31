package UI;

import controller.UserController;
import java.util.List;
import java.util.Scanner;
import model.Application;
import model.BTOProject;
 import model.Enquiry;
 import model.HDBOfficer;
 import repository.ApplicationRepository;
 import repository.ProjectRepository;

public class HDBOfficerDashBoard {
    private HDBOfficer officer;
    private UserController userController; 
    private ProjectRepository projectRepository;
    private ApplicationRepository applicationRepository; 
    private Scanner scanner;
    public HDBOfficerDashBoard(HDBOfficer officer, UserController userController,
                           ProjectRepository projectRepository, ApplicationRepository applicationRepository) {
        this.officer = officer;
        this.userController = userController;
        this.projectRepository = projectRepository;
        this.applicationRepository = applicationRepository;
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n=== HDB Officer Dashboard ===");
            System.out.println("1. View Assigned Project Details");
            System.out.println("2. Register for a Project");
            System.out.println("3. View Registration Status");
            System.out.println("4. Reply to Enquiries");
            System.out.println("5. Generate Receipt for Booked Applicants");
            System.out.println("6. Change Password");
            System.out.println("7. Logout");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
            switch (choice) {
                case 1:
                    if(officer.getAssignedProject() != null) {
                        System.out.println("Assigned Project Details:");
                        System.out.println(officer.getAssignedProject().toString());
                    } else {
                        System.out.println("No project assigned.");
                    }
                    break;
                case 2:
                    registerForProject();
                    break;
                case 3:
                    System.out.println("Registration Status: " + officer.getRegistrationStatus());
                    if(officer.getAssignedProject() != null) {
                        System.out.println("Assigned Project: " + officer.getAssignedProject().toString());
                    }
                    break;
                case 4:
                    replyToEnquiries();
                    break;
                case 5:
                    generateReceipts();
                    break;
                case 6:
                    System.out.print("Enter new password: ");
                    String newPassword = scanner.nextLine();
                    userController.changePassword(officer, newPassword);
                    System.out.println("Password changed successfully.");
                    break;
                case 7:
                    exit = true;
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void registerForProject() {
        // List all projects (regardless of visibility) for registration purposes.
        List<BTOProject> projects = projectRepository.getProjects();
        System.out.println("=== All BTO Projects ===");
        for (int i = 0; i < projects.size(); i++) {
            System.out.println((i+1) + ". " + projects.get(i).toString());
        }
        System.out.print("Enter the project number to register: ");
        int projectChoice = scanner.nextInt();
        scanner.nextLine();
        if(projectChoice < 1 || projectChoice > projects.size()) {
            System.out.println("Invalid selection.");
            return;
        }
        // Check if already registered.
        if(officer.getAssignedProject() != null) {
            System.out.println("You are already registered for a project.");
            return;
        }
        BTOProject selectedProject = projects.get(projectChoice - 1);
        // For simplicity, we set registration as PENDING.
        officer.setAssignedProject(selectedProject);
        officer.setRegistrationStatus("PENDING");
        officer.setRegistered(true);
        System.out.println("Registration submitted. Awaiting approval from HDB Manager.");
    }

    private void replyToEnquiries() {
        if(officer.getAssignedProject() == null) {
            System.out.println("No assigned project to view enquiries for.");
            return;
        }
        System.out.println("=== Enquiries for Your Project ===");
        List<Enquiry> allEnquiries = repository.EnquiryRepository.getEnquiries();
        boolean found = false;
        for (Enquiry enquiry : allEnquiries) {
            // For demo purposes, we check if the enquiry's project equals the officer's assigned project.
            if(enquiry.getProject() != null &&
                    enquiry.getProject().getProjectName().equals(officer.getAssignedProject().getProjectName())) {
                System.out.println(enquiry.toString());
                found = true;
            }
        }
        if(!found) {
            System.out.println("No enquiries found for your project.");
            return;
        }
        System.out.print("Enter Enquiry ID to reply: ");
        int enquiryId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter your reply: ");
        String reply = scanner.nextLine();
        boolean updated = false;
        for (Enquiry enquiry : allEnquiries) {
            if(enquiry.getEnquiryId() == enquiryId) {
                enquiry.setReply(reply);
                updated = true;
                System.out.println("Reply submitted.");
                break;
            }
        }
        if(!updated) {
            System.out.println("Enquiry ID not found.");
        }
    }

    private void generateReceipts() {
        if(officer.getAssignedProject() == null) {
            System.out.println("No assigned project.");
            return;
        }
        System.out.println("=== Receipt for Booked Applicants ===");
        List<Application> applications = applicationRepository.getApplications();
        boolean found = false;
        for (Application app : applications) {
            if(app.getProject().getProjectName().equals(officer.getAssignedProject().getProjectName()) &&
                    app.getStatus() == Application.Status.BOOKED) {
                System.out.println("----- Receipt -----");
                System.out.println("Applicant Name/NRIC: " + app.getApplicant().getNric());
                System.out.println("Age: " + app.getApplicant().getAge());
                System.out.println("Marital Status: " + app.getApplicant().getMaritalStatus());
                System.out.println("Flat Type Booked: " + app.getFlatType());
                System.out.println("Project Details: " + app.getProject().toString());
                System.out.println("-------------------");
                found = true;
            }
        }
        if(!found) {
            System.out.println("No booked applicants found for your project.");
        }
    }
}
