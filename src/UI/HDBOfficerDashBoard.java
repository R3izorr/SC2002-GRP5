package UI;

import controller.EnquiryController;
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
    // For enquiry handling, we use an EnquiryController that can look up enquiries by project.
    private EnquiryController enquiryController;
    
    public HDBOfficerDashBoard(HDBOfficer officer, UserController userController,
                               ProjectRepository projectRepository, ApplicationRepository applicationRepository) {
        this.officer = officer;
        this.userController = userController;
        this.projectRepository = projectRepository;
        this.applicationRepository = applicationRepository;
        this.scanner = new Scanner(System.in);
        // For officer-level enquiry management, no current applicant is provided.
        this.enquiryController = new EnquiryController(projectRepository);
    }
    
    public void run() {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n=== HDB Officer Dashboard ===");
            System.out.println("1. View Assigned Project Details");
            System.out.println("2. Register for a New Project");
            System.out.println("3. Apply for a Project (as Applicant) [if not handling one]");
            System.out.println("4. View Registration Status");
            System.out.println("5. Process Flat Booking for an Applicant");
            System.out.println("6. Generate Receipt for Booked Applicants");
            System.out.println("7. View/Reply to Enquiries for Your Project");
            System.out.println("8. Change Password");
            System.out.println("9. Logout");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
            switch(choice) {
                case 1:
                    viewAssignedProjects();
                    break;
                case 2:
                    registerForNewProject();
                    break;
                case 3:
                    applyForProjectAsApplicant();
                    break;
                case 4:
                    viewRegistrationStatus();
                    break;
                case 5:
                    processFlatBooking();
                    break;
                case 6:
                    generateReceipts();
                    break;
                case 7:
                    viewAndReplyEnquiries();
                    break;
                case 8:
                    System.out.print("Enter new password: ");
                    String newPassword = scanner.nextLine();
                    userController.changePassword(officer, newPassword);
                    System.out.println("Password changed successfully.");
                    System.out.println("Please re-login.");
                    System.out.println("Logging out...");
                    exit = true;
                    break;
                case 9:
                    System.out.println("Logging out...");
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }
    
    private void viewAssignedProjects() {
        List<BTOProject> projects = officer.getAssignedProjects();
        if (projects.isEmpty()) {
            System.out.println("You have no approved project registrations.");
        } else {
            System.out.println("=== Your Approved Projects ===");
            for (BTOProject proj : projects) {
                System.out.println(proj.toStringForManagerOfficer());
            }
        }
    }
    
    private void registerForNewProject() {
        List<BTOProject> projects = projectRepository.getProjects();
        if (projects.isEmpty()){
            System.out.println("No projects available.");
            return;
        }
        System.out.println("=== All Projects (Manager/Officer View) ===");
        for (BTOProject proj : projects) {
            System.out.println(proj.toStringForManagerOfficer());
        }
        System.out.print("Enter project ID to register for: ");
        int projId = scanner.nextInt();
        scanner.nextLine();
        BTOProject selected = projectRepository.getProjectById(projId);
        if (selected == null) {
            System.out.println("Invalid project ID.");
            return;
        }
        // Check if officer already applied as Applicant for this project.
        if (officer.getApplication() != null && officer.getApplication().getProject().getProjectId() == projId) {
            System.out.println("You have already applied for this project as an applicant.");
            return;
        }
        // Check for overlapping approved projects.
        if (officer.hasOverlappingApprovedProject(selected)) {
            System.out.println("You are already registered for a project with an overlapping application period.");
            return;
        }
        // Check if already pending registration.
        boolean alreadyPending = officer.getPendingRegistrations().stream()
                                    .anyMatch(p -> p.getProjectId() == projId);
        if (alreadyPending) {
            System.out.println("You have already submitted a registration for this project.");
            return;
        }
        officer.addPendingRegistration(selected);
        System.out.println("Registration for project \"" + selected.getProjectName() + "\" submitted. Awaiting manager approval.");
    }
    
    // Allow officer (as applicant) to apply if they haven't applied yet.
    private void applyForProjectAsApplicant() {
        if (officer.getApplication() != null) {
            System.out.println("You have already applied for a project.");
            return;
        }
        List<BTOProject> visibleProjects = projectRepository.getProjects();
        System.out.println("=== Available Projects (Applicant View) ===");
        for (BTOProject proj : visibleProjects) {
            System.out.println(proj.toStringForApplicant());
        }
        System.out.print("Enter project ID to apply: ");
        int projId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter flat type (2-Room/3-Room): ");
        String flatType = scanner.nextLine();
        controller.ProjectController projectController =
                new controller.ProjectController(projectRepository, applicationRepository, null);
        // Here, pass the officer as the applicant.
        boolean applied = projectController.applyForProjectByUser(projId, flatType, officer);
        if (applied) {
            System.out.println("Application submitted successfully.");
        }
    }
    
    private void viewRegistrationStatus() {
        System.out.println("=== Your Registration Status ===");
        System.out.println("Approved Projects:");
        for (BTOProject proj : officer.getAssignedProjects()) {
            System.out.println(proj.toStringForManagerOfficer());
        }
        System.out.println("Pending Registrations:");
        for (BTOProject proj : officer.getPendingRegistrations()) {
            System.out.println(proj.toStringForManagerOfficer());
        }
    }
    
    // Process flat booking: only process if the officer is handling a project.
    private void processFlatBooking() {
        if (officer.getAssignedProjects().isEmpty()) {
            System.out.println("You are not approved for any project.");
            return;
        }
        System.out.print("Enter the project ID for which you want to process booking: ");
        int projId = scanner.nextInt();
        scanner.nextLine();
        // Check that the project is among officer's approved projects.
        boolean isHandling = officer.getAssignedProjects().stream().anyMatch(p -> p.getProjectId() == projId);
        if (!isHandling) {
            System.out.println("You are not approved for this project.");
            return;
        }
        // Retrieve successful applications for that project.
        List<Application> BookingApps = new java.util.ArrayList<>();
        for (Application app : applicationRepository.getApplications()) {
            if (app.getProject().getProjectId() == projId &&
                app.getStatus() == Application.Status.BOOKING) {
                BookingApps.add(app);
            }
        }
        if (BookingApps.isEmpty()) {
            System.out.println("No request for flat booking found");
            return;
        }
        System.out.println("=== Request Booking Flat for Project ID " + projId + " ===");
        for (int i = 0; i < BookingApps.size(); i++) {
            Application app = BookingApps.get(i);
            System.out.println((i + 1) + ". Applicant NRIC: " + app.getApplicant().getNric() +
                                 " | Applicant Name: " + app.getApplicant().getName() +
                               " | Flat Type Requested: " + app.getFlatType());
        }
        while (true) {
            System.out.print("Enter the number of the application to process booking (or 0 to cancel): ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            if (choice == 0) {
                System.out.println("Booking process cancelled.");
                return;
            }
            if (choice < 1 || choice > BookingApps.size()) {
                System.out.println("Invalid selection.");
                continue;
            }
            Application targetApp = BookingApps.get(choice - 1);
            String flatType = targetApp.getFlatType();
            BTOProject proj = targetApp.getProject();
            if (flatType.equalsIgnoreCase("2-Room")) {
                if (proj.getUnits2Room() <= 0) {
                    System.out.println("No 2-Room units available.");
                    return;
                }
                proj.setUnits2Room(proj.getUnits2Room() - 1);
            } else if (flatType.equalsIgnoreCase("3-Room")) {
                if (proj.getUnits3Room() <= 0) {
                    System.out.println("No 3-Room units available.");
                    return;
                }
                proj.setUnits3Room(proj.getUnits3Room() - 1);
            } else {
                System.out.println("Invalid flat type in application.");
                return;
            }
            targetApp.setStatus(Application.Status.BOOKED);
            System.out.println("Flat booking processed successfully for applicant " + targetApp.getApplicant().getNric() +
                            ". Application status updated to BOOKED.");
        }
    }
    
    // Generate receipts for all booked applications for the officer's assigned project.
    private void generateReceipts() {
        System.out.print("Enter project ID to generate receipts for: ");
        int projId = scanner.nextInt();
        scanner.nextLine();
        System.out.println("=== Receipts for Booked Applicants for Project ID " + projId + " ===");
        boolean found = false;
        for (Application app : applicationRepository.getApplications()) {
            if (app.getProject().getProjectId() == projId &&
                app.getStatus() == Application.Status.BOOKED) {
                System.out.println("----- Receipt -----");
                System.out.println("Applicant NRIC: " + app.getApplicant().getNric());
                System.out.println("Age: " + app.getApplicant().getAge());
                System.out.println("Marital Status: " + app.getApplicant().getMaritalStatus());
                System.out.println("Flat Type Booked: " + app.getFlatType());
                System.out.println("Project ID: " + app.getProject().getProjectId());
                System.out.println("Project Name: " + app.getProject().getProjectName());
                System.out.println("-------------------");
                found = true;
            }
        }
        if (!found) {
            System.out.println("No booked applications found for that project.");
        }
    }
    
    // Only allow viewing and replying to enquiries for the project the officer is registered for.
    private void viewAndReplyEnquiries() {
        if (officer.getAssignedProjects().isEmpty()) {
            System.out.println("You are not approved for any project, so no enquiries to view.");
            return;
        }
        // For simplicity, let the officer select one of their projects.
        System.out.println("Your Approved Projects:");
        for (BTOProject proj : officer.getAssignedProjects()) {
            System.out.println(proj.toStringForManagerOfficer());
        }
        System.out.print("Enter the project ID for which to view enquiries: ");
        int projId = scanner.nextInt();
        scanner.nextLine();
        // Check that the project is in officer's approved list.
        boolean handling = officer.getAssignedProjects().stream().anyMatch(p -> p.getProjectId() == projId);
        if (!handling) {
            System.out.println("You are not approved for this project.");
            return;
        }
        List<Enquiry> enquiries = enquiryController.getEnquiriesForProject(projId);
        if (enquiries.isEmpty()) {
            System.out.println("No enquiries found for this project.");
            return;
        }
        System.out.println("=== Enquiries for Project ID " + projId + " ===");
        for (Enquiry e : enquiries) {
            System.out.println(e.toString());
        }
        System.out.print("Enter Enquiry ID to reply (or 0 to skip): ");
        int eid = scanner.nextInt();
        scanner.nextLine();
        if (eid == 0)
            return;
        System.out.print("Enter your reply: ");
        String reply = scanner.nextLine();
        enquiryController.editEnquiry(eid, reply);
        System.out.println("Reply submitted.");
    }
    
}