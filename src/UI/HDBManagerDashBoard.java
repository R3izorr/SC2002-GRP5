package UI;

import controller.EnquiryController;
import controller.UserController;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import utils.FilterSettings;

public class HDBManagerDashBoard {
    private HDBManager manager;
    private UserController userController;
    private ProjectRepository projectRepository;
    private ApplicationRepository applicationRepository;
    private UserRepository userRepository;
    private EnquiryController enquiryController;
    private Scanner scanner;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private FilterSettings assignedProjectFilterSettings; // filter settings for assigned projects

    public HDBManagerDashBoard(HDBManager manager, UserController userController,
                               ProjectRepository projectRepository, ApplicationRepository applicationRepository,
                               UserRepository userRepository, EnquiryController enquiryController) {
        this.manager = manager;
        this.userController = userController;
        this.projectRepository = projectRepository;
        this.applicationRepository = applicationRepository;
        this.userRepository = userRepository;
        this.enquiryController = enquiryController;
        this.assignedProjectFilterSettings = new FilterSettings();
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
            System.out.println("5. View Assigned Project (Filtered)");
            System.out.println("6. Filter Setting");
            System.out.println("7. Manage Applicant Applications");
            System.out.println("8. Manage HDB Officer Registrations");
            System.out.println("9. View/Reply to Enquiries");
            System.out.println("10. Generate Report for Booked Applicants");
            System.out.println("11. Change Password");
            System.out.println("12. Logout");
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
                    viewAssignedProjects();
                    break;
                case 6:
                    updateAssignedProjectFilterSettings();
                    break;
                case 7:
                    manageApplicantApplications();
                    break;
                case 8:
                    manageOfficerRegistrations();
                    break;
                case 9:
                    viewAndReplyEnquiries();
                    break;
                case 10:
                    generateReport();
                    break;
                case 11:
                    System.out.print("Enter new password: ");
                    String newPassword = scanner.nextLine();
                    userController.changePassword(manager, newPassword);
                    System.out.println("Password changed successfully.");
                    System.out.println("Please re-login.");
                    System.out.println("Logging out...");
                    exit = true;
                    break;
                case 12:
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
            System.out.print("Enter Application Opening Date (dd/MM/yyyy): ");
            String openDateStr = scanner.nextLine();
            System.out.print("Enter Application Closing Date (dd/MM/yyyy): ");
            String closeDateStr = scanner.nextLine();

             // Parse the dates.
            java.util.Date openDate = dateFormat.parse(openDateStr);
            java.util.Date closeDate = dateFormat.parse(closeDateStr);

            // Check for overlapping application periods.
            // Manager can only handle one project in a given application period.
            boolean overlapFound = false;
            for (BTOProject proj : manager.getManagedProjects()) {
                // Overlap condition: newOpen <= existingClose AND newClose >= existingOpen.
                if (!openDate.after(proj.getApplicationClose()) && !closeDate.before(proj.getApplicationOpen())) {
                    overlapFound = true;
                    System.out.println("Cannot create project. You already have a project (" + proj.getProjectName() +
                            ") with an overlapping application period (" + proj.getApplicationOpen() + " to " +
                            proj.getApplicationClose() + ").");
                    break;
                }
            }
            if (overlapFound) {
                return;
            }

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
            System.out.println("\n=== Edit Project Options ===");
            System.out.println("1. Project Name");
            System.out.println("2. Neighborhood");
            System.out.println("3. 2-Room Units");
            System.out.println("4. 2-Room Price");
            System.out.println("5. 3-Room Units");
            System.out.println("6. 3-Room Price");
            System.out.println("7. Application Opening Date");
            System.out.println("8. Application Closing Date");
            System.out.println("9. Officer Slots");
            System.out.println("10. Back to Menu");
            System.out.print("\nChoose an option: ");
            int editOption = scanner.nextInt();
            scanner.nextLine();
            switch(editOption) {
                case 1:
                    System.out.print("Enter new Project Name: ");
                    String newName = scanner.nextLine();
                    selected.setProjectName(newName);
                    break;
                case 2:
                    System.out.print("Enter new Neighborhood: ");
                    String newNeighborhood = scanner.nextLine();
                    selected.setNeighborhood(newNeighborhood);
                    break;
                case 3:
                    System.out.print("Enter new number of 2-Room units: ");
                    int newUnits2 = scanner.nextInt();
                    selected.setUnits2Room(newUnits2);
                    break;
                case 4:
                    System.out.print("Enter new selling price for 2-Room: ");
                    float newPrice2 = scanner.nextFloat();
                    selected.setSellingPrice2Room(newPrice2);
                    break;
                case 5:
                    System.out.print("Enter new number of 3-Room units: ");
                    int newUnits3 = scanner.nextInt();
                    selected.setUnits3Room(newUnits3);
                    break;
                case 6:
                    System.out.print("Enter new selling price for 3-Room: ");
                    float newPrice3 = scanner.nextFloat();
                    selected.setSellingPrice3Room(newPrice3);
                    break;
                case 7:
                    System.out.print("Enter new Application Opening Date (dd/MM/yyyy): ");
                    String openDateStr = scanner.nextLine();
                    try {
                        selected.setApplicationOpen(dateFormat.parse(openDateStr));
                    } catch (ParseException e) {
                        System.out.println("Invalid date format. Please try again.");
                    }
                    break;
                case 8:
                    System.out.print("Enter new Application Closing Date (dd/MM/yyyy): ");
                    String closeDateStr = scanner.nextLine();
                    try {
                        selected.setApplicationClose(dateFormat.parse(closeDateStr));
                    } catch (ParseException e) {
                        System.out.println("Invalid date format. Please try again.");
                    }
                    break;
                case 9:
                    System.out.print("Enter new Available HDB Officer Slots (max 10): ");
                    int slots = scanner.nextInt();
                    selected.setOfficerSlots(slots);
                    break;
                case 10: break;
                default:
                    System.out.println("Invalid option.");
            }
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
        boolean exit = false;
        while (!exit) {
            System.out.println("\n=== Manage Applicant Applications ===");
            List<Application> pendingApps = new ArrayList<>();
            for (Application app : applicationRepository.getApplications()){
                if (manager.getManagedProjects().contains(app.getProject()) &&
                    app.getStatus() == Application.Status.PENDING) {
                    pendingApps.add(app);
                }
            }
            if (pendingApps.isEmpty()){
                System.out.println("No pending applications found.");
                exit = true;
                continue;
            } else {
                for (int i = 0; i < pendingApps.size(); i++){
                    Application app = pendingApps.get(i);
                    System.out.println((i+1) + ". Applicant NRIC: " + app.getApplicant().getNric() +
                        " | Project: " + app.getProject().getProjectName() +
                        " | Flat Type: " + app.getFlatType());
                }
                System.out.println("Enter the application number to process (or 0 to exit): ");
                int choice = scanner.nextInt();
                scanner.nextLine();
                if (choice == 0) {
                    exit = true;
                    continue;
                }
                if (choice < 1 || choice > pendingApps.size()){
                    System.out.println("Invalid selection.");
                    continue;
                }
                Application selectedApp = pendingApps.get(choice-1);
                System.out.println("Selected Application: Applicant NRIC: " + selectedApp.getApplicant().getNric() +
                    " | Project: " + selectedApp.getProject().getProjectName() +
                    " | Flat Type: " + selectedApp.getFlatType());
                System.out.println("Enter A to Approve, R to Reject, or E to exit: ");
                String decision = scanner.nextLine().trim();
                if(decision.equalsIgnoreCase("E")){
                    continue;
                }
                if (decision.equalsIgnoreCase("A")){
                    if (selectedApp.getFlatType().equalsIgnoreCase("2-Room")){
                        if (selectedApp.getProject().getUnits2Room() > 0){
                            selectedApp.setStatus(Application.Status.SUCCESSFUL);
                            selectedApp.getProject().setUnits2Room(selectedApp.getProject().getUnits2Room()-1);
                            System.out.println("Application approved (SUCCESSFUL).");
                        } else {
                            System.out.println("Insufficient 2-Room units. Cannot approve.");
                        }
                    } else if (selectedApp.getFlatType().equalsIgnoreCase("3-Room")){
                        if (selectedApp.getProject().getUnits3Room() > 0){
                            selectedApp.setStatus(Application.Status.SUCCESSFUL);
                            selectedApp.getProject().setUnits3Room(selectedApp.getProject().getUnits3Room()-1);
                            System.out.println("Application approved (SUCCESSFUL).");
                        } else {
                            System.out.println("Insufficient 3-Room units. Cannot approve.");
                        }
                    } else {
                        System.out.println("Invalid flat type in application.");
                    }
                } else if (decision.equalsIgnoreCase("R")){
                    selectedApp.setStatus(Application.Status.UNSUCCESSFUL);
                    System.out.println("Application rejected.");
                } else {
                    System.out.println("Invalid decision.");
                }
            }
        }
    }
    
    private void manageOfficerRegistrations() {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n=== Manage HDB Officer Registrations ===");
            List<PendingRegistration> pendingRegs = new ArrayList<>();
            for (HDBOfficer off : userRepository.getOfficers()){
                for (BTOProject proj : off.getPendingRegistrations()){
                    if (manager.getManagedProjects().contains(proj)){
                        pendingRegs.add(new PendingRegistration(off, proj));
                    }
                }
            }
            if (pendingRegs.isEmpty()){
                System.out.println("No pending officer registrations for your projects.");
                exit = true;
                continue;
            } else {
                for (int i = 0; i < pendingRegs.size(); i++){
                    PendingRegistration reg = pendingRegs.get(i);
                    System.out.println((i+1) + ". Officer NRIC: " + reg.officer.getNric() +
                        " | Pending Registration for Project: " + reg.project.getProjectName());
                }
                System.out.println("Enter the registration number to process (or 0 to exit): ");
                int choice = scanner.nextInt();
                scanner.nextLine();
                if (choice == 0) {
                    exit = true;
                    continue;
                }
                if (choice < 1 || choice > pendingRegs.size()){
                    System.out.println("Invalid selection.");
                    continue;
                }
                PendingRegistration selected = pendingRegs.get(choice-1);
                System.out.println("Selected Registration: Officer NRIC: " + selected.officer.getNric() +
                    " | Project: " + selected.project.toStringForManagerOfficer());
                System.out.println("Enter A to Approve, R to Reject, or E to exit: ");
                String decision = scanner.nextLine().trim();
                if(decision.equalsIgnoreCase("E")){
                    continue;
                }
                if (decision.equalsIgnoreCase("A")){
                    selected.officer.addAssignedProject(selected.project);
                    selected.officer.getPendingRegistrations().remove(selected.project);
                    System.out.println("Registration approved.");
                    selected.project.addOfficers(selected.officer);
                    selected.project.setOfficerSlots(selected.project.getOfficerSlots()-1);
                } else if (decision.equalsIgnoreCase("R")){
                    selected.officer.getPendingRegistrations().remove(selected.project);
                    System.out.println("Registration rejected.");
                } else {
                    System.out.println("Invalid decision.");
                }
            }
        }
    }
    
     // Helper inner class to hold pending registration information.
    private class PendingRegistration {
        public HDBOfficer officer;
        public BTOProject project;
        public PendingRegistration(HDBOfficer officer, BTOProject project){
            this.officer = officer;
            this.project = project;
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
        boolean exit = false;
        while (!exit) {
            System.out.println("\n=== Generate Report for Booked Applicants ===");
            List<BTOProject> managed = manager.getManagedProjects();
            if (managed.isEmpty()){
                System.out.println("You have no managed projects.");
                return;
            }
            System.out.println("Select a project to generate report:");
            for (int i = 0; i < managed.size(); i++){
                System.out.println((i+1) + ". " + managed.get(i).toStringForManagerOfficer());
            }
            System.out.println("Enter project number (or 0 to exit): ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            if (choice == 0) {
                exit = true;
                continue;
            }
            if (choice < 1 || choice > managed.size()){
                System.out.println("Invalid selection.");
                continue;
            }
            BTOProject selectedProj = managed.get(choice-1);
            // Gather all booked applications for the selected project.
            List<Application> bookedApps = new ArrayList<>();
            for (Application app : applicationRepository.getApplications()){
                if (app.getProject().getProjectId() == selectedProj.getProjectId() &&
                    app.getStatus() == Application.Status.BOOKED) {
                    bookedApps.add(app);
                }
            }
            if (bookedApps.isEmpty()){
                System.out.println("No booked applications found for this project.");
            } else {
                // Ask the manager if they want to apply filters.
                System.out.println("Would you like to apply a filter?");
                System.out.println("1. No Filter");
                System.out.println("2. Filter by Marital Status");
                System.out.println("3. Filter by Flat Type");
                System.out.println("4. Filter by Both Marital Status and Flat Type");
                System.out.print("Choose an option: ");
                int filterChoice = scanner.nextInt();
                scanner.nextLine();
                List<Application> filteredApps = new ArrayList<>(bookedApps);
                if (filterChoice == 2) {
                    System.out.print("Enter Marital Status to filter by (e.g., Married, Single): ");
                    String maritalStatus = scanner.nextLine().trim();
                    filteredApps.removeIf(app -> !app.getApplicant().getMaritalStatus().equalsIgnoreCase(maritalStatus));
                } else if (filterChoice == 3) {
                    System.out.print("Enter Flat Type to filter by (2-Room or 3-Room): ");
                    String flatType = scanner.nextLine().trim();
                    filteredApps.removeIf(app -> !app.getFlatType().equalsIgnoreCase(flatType));
                } else if (filterChoice == 4) {
                    System.out.print("Enter Marital Status to filter by (e.g., Married, Single): ");
                    String maritalStatus = scanner.nextLine().trim();
                    System.out.print("Enter Flat Type to filter by (2-Room or 3-Room): ");
                    String flatType = scanner.nextLine().trim();
                    filteredApps.removeIf(app -> 
                        !app.getApplicant().getMaritalStatus().equalsIgnoreCase(maritalStatus) ||
                        !app.getFlatType().equalsIgnoreCase(flatType));
                }
                if (filteredApps.isEmpty()){
                    System.out.println("No booked applications found after applying the filter.");
                } else {
                    model.Report report = new model.Report(filteredApps);
                    System.out.println(report.toString());
                }
            }
            System.out.print("Generate report for another project? (Y/N): ");
            String again = scanner.nextLine().trim();
            if (!again.equalsIgnoreCase("Y")){
                exit = true;
            }
        }
    }
    
    private void viewAssignedProjects() {
        List<BTOProject> projects = new ArrayList<>(manager.getManagedProjects());
        // Apply neighborhood filter if set.
        if (assignedProjectFilterSettings.getNeighborhood() != null &&
            !assignedProjectFilterSettings.getNeighborhood().isEmpty()) {
            projects.removeIf(proj -> !proj.getNeighborhood().equalsIgnoreCase(assignedProjectFilterSettings.getNeighborhood()));
        }
        // Apply flat type filter if set: filter projects with available units of that flat type.
        if (assignedProjectFilterSettings.getFlatType() != null &&
            !assignedProjectFilterSettings.getFlatType().isEmpty()) {
            if (assignedProjectFilterSettings.getFlatType().equalsIgnoreCase("2-Room")) {
                projects.removeIf(proj -> proj.getUnits2Room() <= 0);
            } else if (assignedProjectFilterSettings.getFlatType().equalsIgnoreCase("3-Room")) {
                projects.removeIf(proj -> proj.getUnits3Room() <= 0);
            }
        }
        // Sort alphabetically if required.
        if (assignedProjectFilterSettings.isSortAlphabetical()) {
            projects.sort((p1, p2) -> p1.getProjectName().compareToIgnoreCase(p2.getProjectName()));
        }
        System.out.println("=== Your Assigned Projects (Filtered) ===");
        if (projects.isEmpty()) {
            System.out.println("No projects match your filter criteria.");
        } else {
            for (BTOProject proj : projects) {
                System.out.println(proj.toStringForManagerOfficer());
            }
        }
    }

    private void updateAssignedProjectFilterSettings() {
        System.out.print("Enter Neighborhood filter for assigned projects (leave empty for no filter): ");
        String neighborhood = scanner.nextLine().trim();
        if (neighborhood.isEmpty()) {
            assignedProjectFilterSettings.setNeighborhood(null);
        } else {
            assignedProjectFilterSettings.setNeighborhood(neighborhood);
        }
        System.out.print("Enter Flat Type filter for assigned projects (2-Room/3-Room, leave empty for no filter): ");
        String flatType = scanner.nextLine().trim();
        if (flatType.isEmpty()) {
            assignedProjectFilterSettings.setFlatType(null);
        } else {
            assignedProjectFilterSettings.setFlatType(flatType);
        }
        System.out.println("Assigned project filter settings updated.");
    }
    
}