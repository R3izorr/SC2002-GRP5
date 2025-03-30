package main;

import controller.ProjectManager;
import controller.ApplicationManager;
import controller.EnquiryManager;
import controller.ReportManager;
import model.HDBManager;
import model.Project;
import utils.FlatType;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class ManagerDashboard {
    private final ProjectManager projectManager;
    private final ApplicationManager applicationManager;
    private final EnquiryManager enquiryManager;
    private final ReportManager reportManager;
    private final Scanner scanner;

    public ManagerDashboard(ProjectManager projectManager,
                            ApplicationManager applicationManager,
                            EnquiryManager enquiryManager,
                            ReportManager reportManager,
                            Scanner scanner) {
        this.projectManager = projectManager;
        this.applicationManager = applicationManager;
        this.enquiryManager = enquiryManager;
        this.reportManager = reportManager;
        this.scanner = scanner;
    }

    public void launch(HDBManager manager) {
        while (true) {
            System.out.println("\n=== HDB Manager Dashboard ===");
            System.out.println("1. Create Project");
            System.out.println("2. Edit Project");
            System.out.println("3. Delete Project");
            System.out.println("4. Toggle Project Visibility");
            System.out.println("5. Approve Officer Registration");
            System.out.println("6. Approve/Reject Applications");
            System.out.println("7. Approve Withdrawal Requests");
            System.out.println("8. Reply to Enquiry");
            System.out.println("9. Generate Report");
            System.out.println("10. Logout");
            System.out.print("Enter choice: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> createProject(manager);
                case "2" -> editProject();
                case "3" -> deleteProject();
                case "4" -> toggleProjectVisibility();
                case "5" -> approveOfficerRegistration();                
                case "6" -> System.out.println("Approve/Reject applications - coming soon");
                case "7" -> System.out.println("Approve withdrawal - coming soon");
                case "8" -> System.out.println("Reply to enquiry - coming soon");
                case "9" -> System.out.println("Generate report - coming soon");
                case "10" -> {
                    System.out.println("Logging out...");
                    return;
                }
                default -> System.out.println("Invalid input.");
            }
        }
    }
    private void createProject(HDBManager manager) {
        try {
            System.out.print("Enter project name: ");
            String name = scanner.nextLine().trim();
    
            System.out.print("Enter project location: ");
            String location = scanner.nextLine().trim();
    
            System.out.print("Enter number of 2-Room units: ");
            int twoRoom = Integer.parseInt(scanner.nextLine().trim());
    
            System.out.print("Enter number of 3-Room units: ");
            int threeRoom = Integer.parseInt(scanner.nextLine().trim());
    
            System.out.print("Enter application open date (YYYY-MM-DD): ");
            LocalDate openDate = LocalDate.parse(scanner.nextLine().trim());
    
            System.out.print("Enter application close date (YYYY-MM-DD): ");
            LocalDate closeDate = LocalDate.parse(scanner.nextLine().trim());
    
            System.out.print("Enter number of officer registration slots: ");
            int officerSlots = Integer.parseInt(scanner.nextLine().trim());
    
            Map<FlatType, Integer> units = new HashMap<>();
            units.put(FlatType.TWO_ROOM, twoRoom);
            units.put(FlatType.THREE_ROOM, threeRoom);
    
            projectManager.createProject(name, location, openDate, closeDate, manager, officerSlots, units);
            System.out.println("Project created successfully.");
        } catch (Exception e) {
            System.out.println("Invalid input. Project creation failed.");
        }
    }
    private void editProject() {
        var projects = projectManager.getAllProjects();
        if (projects.isEmpty()) {
            System.out.println("No projects to edit.");
            return;
        }
    
        System.out.println("\nAvailable Projects:");
        for (int i = 0; i < projects.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, projects.get(i).getName());
        }
    
        System.out.print("Select project number: ");
        try {
            int index = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (index < 0 || index >= projects.size()) {
                System.out.println("Invalid selection.");
                return;
            }
    
            Project selected = projects.get(index);
            System.out.println("Editing Project: " + selected.getName());
    
            System.out.print("New 2-Room unit count (" + selected.getUnitCount().get(FlatType.TWO_ROOM) + "): ");
            int twoRoom = Integer.parseInt(scanner.nextLine().trim());
    
            System.out.print("New 3-Room unit count (" + selected.getUnitCount().get(FlatType.THREE_ROOM) + "): ");
            int threeRoom = Integer.parseInt(scanner.nextLine().trim());
    
            System.out.print("New officer slots (" + selected.getAvailableOfficerSlots() + "): ");
            int officerSlots = Integer.parseInt(scanner.nextLine().trim());
    
            Map<FlatType, Integer> updatedUnits = new HashMap<>();
            updatedUnits.put(FlatType.TWO_ROOM, twoRoom);
            updatedUnits.put(FlatType.THREE_ROOM, threeRoom);
    
            projectManager.editProject(selected, updatedUnits, officerSlots);
            System.out.println("Project updated successfully.");
    
        } catch (Exception e) {
            System.out.println("Error during project editing.");
        }
    }
    private void deleteProject() {
        var projects = projectManager.getAllProjects();
        if (projects.isEmpty()) {
            System.out.println("No projects available to delete.");
            return;
        }
    
        System.out.println("Select a project to delete:");
        for (int i = 0; i < projects.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, projects.get(i).getName());
        }
    
        try {
            int index = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (index < 0 || index >= projects.size()) {
                System.out.println("Invalid selection.");
                return;
            }
    
            Project selected = projects.get(index);
            projectManager.deleteProject(selected);
            System.out.println("Project deleted successfully.");
        } catch (Exception e) {
            System.out.println("Error deleting project.");
        }
    }
    private void toggleProjectVisibility() {
        var projects = projectManager.getAllProjects();
        if (projects.isEmpty()) {
            System.out.println("No projects to update.");
            return;
        }
    
        System.out.println("Select a project to toggle visibility:");
        for (int i = 0; i < projects.size(); i++) {
            Project p = projects.get(i);
            System.out.printf("%d. %s (Currently: %s)%n", i + 1, p.getName(), p.isVisible() ? "Visible" : "Hidden");
        }
    
        try {
            int index = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (index < 0 || index >= projects.size()) {
                System.out.println("Invalid selection.");
                return;
            }
    
            Project selected = projects.get(index);
            projectManager.toggleProjectVisibility(selected);
            System.out.println("Project visibility updated.");
        } catch (Exception e) {
            System.out.println("Error toggling visibility.");
        }
    }
    private void approveOfficerRegistration() {
        var projects = projectManager.getAllProjects();
        if (projects.isEmpty()) {
            System.out.println("No projects available.");
            return;
        }
    
        System.out.println("Select a project to approve officer for:");
        for (int i = 0; i < projects.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, projects.get(i).getName());
        }
    
        try {
            int index = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (index < 0 || index >= projects.size()) {
                System.out.println("Invalid selection.");
                return;
            }
    
            Project selected = projects.get(index);
            var pending = selected.getPendingOfficer(); // assumes there's a list or queue
            if (pending == null) {
                System.out.println("No pending officer registration.");
                return;
            }
    
            projectManager.approveOfficerRegistration(pending, selected);
            System.out.println("Officer approved for project: " + selected.getName());
        } catch (Exception e) {
            System.out.println("Error approving officer.");
        }
    }
            
    

    

}

