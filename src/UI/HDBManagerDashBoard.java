package UI;

import controller.EnquiryController;
import controller.UserController;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;
import model.Application;
import model.BTOProject;
import model.Enquiry;
import model.HDBManager;
import model.HDBOfficer;
import repository.ApplicationRepository;
import repository.ProjectRepository;
import repository.UserRepository;

public class HDBManagerDashBoard {
    private HDBManager manager;
    private UserController userController;
    private ProjectRepository projectRepository;
    private ApplicationRepository applicationRepository;
    private UserRepository userRepository;
    private EnquiryController enquiryController;
    private Scanner scanner;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public HDBManagerDashBoard(HDBManager manager, UserController userController,
                               ProjectRepository projectRepository, ApplicationRepository applicationRepository,
                               UserRepository userRepository, EnquiryController enquiryController) {
        this.manager = manager;
        this.userController = userController;
        this.projectRepository = projectRepository;
        this.applicationRepository = applicationRepository;
        this.userRepository = userRepository;
        this.enquiryController = enquiryController;
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n=== HDB Manager Dashboard ===");
            System.out.println("1. Create New BTO Project Listing");
            System.out.println("2. Edit/Delete Project Listing");
            System.out.println("3. Toggle Project Visibility");
            System.out.println("4. View All Projects");
            System.out.println("5. Manage Applicant Applications");
            System.out.println("6. Manage HDB Officer Registrations");
            System.out.println("7. View/Reply to Enquiries");
            System.out.println("8. Generate Report for Booked Applicants");
            System.out.println("9. Change Password");
            System.out.println("10. Logout");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch(choice) {
                case 1:
                    createProject();
                    break;
                case 2:
                    editOrDeleteProject();
                    break;
                case 3:
                    toggleProjectVisibility();
                    break;
                case 4:
                    viewAllProjects();
                    break;
                case 5:
                    manageApplicantApplications();
                    break;
                case 6:
                    manageOfficerRegistrations();
                    break;
                case 7:
                    viewAndReplyEnquiries();
                    break;
                case 8:
                    generateReport();
                    break;
                case 9:
                    System.out.print("Enter new password: ");
                    String newPassword = scanner.nextLine();
                    userController.changePassword(manager, newPassword);
                    System.out.println("Password changed successfully.");
                    break;
                case 10:
                    exit = true;
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }
    
    private void createProject() {
        try {
            System.out.print("Enter Project Name: ");
            String name = scanner.nextLine();
            System.out.print("Enter Neighborhood: ");
            String neighborhood = scanner.nextLine();
            System.out.print("Enter number of 2-Room units: ");
            int units2 = scanner.nextInt();
            System.out.print("Enter selling price for 2-Room: ");
            float price2 = scanner.nextFloat();
            System.out.print("Enter number of 3-Room units: ");
            int units3 = scanner.nextInt();
            System.out.print("Enter selling price for 3-Room: ");
            float price3 = scanner.nextFloat();
            scanner.nextLine();
            System.out.print("Enter Application Opening Date (M/d/yyyy): ");
            String openDateStr = scanner.nextLine();
            System.out.print("Enter Application Closing Date (M/d/yyyy): ");
            String closeDateStr = scanner.nextLine();
            System.out.print("Enter Available HDB Officer Slots (max 10): ");
            int slots = scanner.nextInt();
            scanner.nextLine();
            BTOProject newProject = new BTOProject(name, neighborhood, price2, units2, price3, units3,
                    dateFormat.parse(openDateStr), dateFormat.parse(closeDateStr), manager, slots, true);
            projectRepository.getProjects().add(newProject);
            manager.addManagedProject(newProject);
            System.out.println("Project created successfully.");
        } catch (ParseException e) {
            System.out.println("Date parse error. Project creation failed.");
        }
    }
    
    private void editOrDeleteProject() {
        List<BTOProject> projects = manager.getManagedProjects();
        if(projects.isEmpty()){
            System.out.println("No projects to manage.");
            return;
        }
        System.out.println("Your Projects:");
        for (int i = 0; i < projects.size(); i++) {
            System.out.println((i+1) + ". " + projects.get(i).toStringForManagerOfficer());
        }
        System.out.print("Enter project number to edit/delete: ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        if(choice < 1 || choice > projects.size()){
            System.out.println("Invalid selection.");
            return;
        }
        BTOProject selected = projects.get(choice - 1);
        System.out.println("1. Edit Project");
        System.out.println("2. Delete Project");
        System.out.print("Choose an option: ");
        int option = scanner.nextInt();
        scanner.nextLine();
        if(option == 1){
            System.out.print("Enter new Neighborhood: ");
            String neighborhood = scanner.nextLine();
            System.out.print("Enter new number of 2-Room units: ");
            int units2 = scanner.nextInt();
            System.out.print("Enter new number of 3-Room units: ");
            int units3 = scanner.nextInt();
            scanner.nextLine();
            selected.setVisible(selected.isVisible()); // keep current visibility
            // For simplicity, allow editing only neighborhood and unit counts.
            // (In a real system, more fields can be edited.)
            System.out.println("Project updated.");
        } else if(option == 2){
            projects.remove(selected);
            projectRepository.getProjects().remove(selected);
            System.out.println("Project deleted.");
        } else {
            System.out.println("Invalid option.");
        }
    }
    
    private void toggleProjectVisibility() {
        List<BTOProject> projects = manager.getManagedProjects();
        if(projects.isEmpty()){
            System.out.println("No projects available.");
            return;
        }
        System.out.println("Your Projects:");
        for (int i = 0; i < projects.size(); i++) {
            System.out.println((i+1) + ". " + projects.get(i).toStringForManagerOfficer());
        }
        System.out.print("Enter project number to toggle visibility: ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        if(choice < 1 || choice > projects.size()){
            System.out.println("Invalid selection.");
            return;
        }
        BTOProject selected = projects.get(choice - 1);
        selected.setVisible(!selected.isVisible());
        System.out.println("Project visibility toggled. Now: " + (selected.isVisible() ? "Visible" : "Not Visible"));
    }
    
    private void viewAllProjects() {
        System.out.println("=== All Projects ===");
        for(BTOProject proj : projectRepository.getProjects()){
            System.out.println(proj.toStringForManagerOfficer());
        }
    }
    
    private void manageApplicantApplications() {
        System.out.println("=== Pending Applicant Applications ===");
        List<Application> apps = applicationRepository.getApplications();
        boolean found = false;
        for(Application app : apps) {
            if(manager.getManagedProjects().contains(app.getProject()) && app.getStatus() == model.Application.Status.PENDING) {
                System.out.println("Applicant NRIC: " + app.getApplicant().getNric() +
                        " | Applicant Name: " + app.getApplicant().getName() +
                        " | Project: " + app.getProject().getProjectName() +
                        " | Flat Type: " + app.getFlatType());
                System.out.print("Approve (A) or Reject (R) this application? ");
                String decision = scanner.nextLine();
                if(decision.equalsIgnoreCase("A")) {
                    // Check flat availability and update status.
                    if(app.getFlatType().equalsIgnoreCase("2-Room") && app.getProject().getUnits2Room() > 0) {
                        app.setStatus(model.Application.Status.SUCCESSFUL);
                        app.getProject().setUnits2Room(app.getProject().getUnits2Room()-1);
                        System.out.println("Application approved (SUCCESSFUL).");
                    } else if(app.getFlatType().equalsIgnoreCase("3-Room") && app.getProject().getUnits3Room() > 0) {
                        app.setStatus(model.Application.Status.SUCCESSFUL);
                        app.getProject().setUnits3Room(app.getProject().getUnits3Room()-1);
                        System.out.println("Application approved (SUCCESSFUL).");
                    } else {
                        System.out.println("Insufficient flat units. Cannot approve.");
                    }
                } else if(decision.equalsIgnoreCase("R")){
                    app.setStatus(model.Application.Status.UNSUCCESSFUL);
                    System.out.println("Application rejected.");
                }
                found = true;
            }
        }
        if(!found){
            System.out.println("No pending applications found.");
        }
    }
    
    private void manageOfficerRegistrations() {
        System.out.println("=== Pending HDB Officer Registrations ===");
        List<HDBOfficer> officers = userRepository.getOfficers();
        boolean found = false;
        for(HDBOfficer off : officers) {
            if(off.getRegistrationStatus().equals("PENDING") &&
               off.getAssignedProject() != null &&
               manager.getManagedProjects().contains(off.getAssignedProject())) {
                System.out.println("Officer NRIC: " + off.getNric() +
                        " | Officer Name: " + off.getName() +
                        " | Project: " + off.getAssignedProject().getProjectName());
                System.out.print("Approve (A) or Reject (R) registration? ");
                String decision = scanner.nextLine();
                if(decision.equalsIgnoreCase("A")) {
                    off.setRegistrationStatus("APPROVED");
                    System.out.println("Registration approved.");
                    off.getAssignedProject().setOfficerSlots(off.getAssignedProject().getOfficerSlots()-1);
                    off.setRegistered(true);
                    off.getAssignedProject().addOfficers(off);
                } else if(decision.equalsIgnoreCase("R")){
                    off.setRegistrationStatus("REJECTED");
                    off.setAssignedProject(null);
                    off.setRegistered(false);
                    System.out.println("Registration rejected.");
                }
                found = true;
            }
        }
        if(!found) {
            System.out.println("No pending officer registrations.");
        }
    }
    
    private void viewAndReplyEnquiries() {
        System.out.print("Enter project ID to view its enquiries: ");
        int projId = scanner.nextInt();
        scanner.nextLine();
        List<Enquiry> enquiries = enquiryController.getEnquiriesForProject(projId);
        if(enquiries.isEmpty()) {
            System.out.println("No enquiries found for this project.");
            return;
        }
        System.out.println("=== Enquiries for Project ID " + projId + " ===");
        for(Enquiry e : enquiries) {
            System.out.println(e.toString());
        }
        System.out.print("Enter Enquiry ID to reply (or 0 to skip): ");
        int eid = scanner.nextInt();
        scanner.nextLine();
        if(eid == 0) return;
        System.out.print("Enter your reply: ");
        String reply = scanner.nextLine();
        enquiryController.editEnquiry(eid, reply);
        System.out.println("Reply submitted.");
    }
    
    private void generateReport() {
        System.out.println("=== Report: Booked Applicants ===");
        boolean found = false;
        for(Application app : applicationRepository.getApplications()) {
            if(manager.getManagedProjects().contains(app.getProject()) &&
               app.getStatus() == model.Application.Status.BOOKED) {
                System.out.println("----- Receipt -----");
                System.out.println("Applicant NRIC: " + app.getApplicant().getNric());
                System.out.println("Age: " + app.getApplicant().getAge());
                System.out.println("Marital Status: " + app.getApplicant().getMaritalStatus());
                System.out.println("Flat Type: " + app.getFlatType());
                System.out.println("Project: " + app.getProject().toStringForManagerOfficer());
                System.out.println("-------------------");
                found = true;
            }
        }
        if(!found) {
            System.out.println("No booked applications found.");
        }
    }
}