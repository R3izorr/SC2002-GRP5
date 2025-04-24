package system.service.manager;

import entity.model.Application;
import entity.model.BTOProject;
import entity.model.Report;
import entity.user.HDBManager;
import java.util.ArrayList;
import java.util.List;
import ui.AbstractMenu;
import ui.Prompt;

public class GenerateReportService extends AbstractMenu {
    private HDBManager manager;
    private List<Application> applications;
    
    public GenerateReportService(HDBManager manager, List<Application> applications) {
        this.manager = manager;
        this.applications = applications;
    }
    
    @Override
    public void display() {
        System.out.println("\n=== Generate Report for Booked Applicants ===");
        List<BTOProject> managed = manager.getManagedProjects();
        if(managed.isEmpty()){
            System.out.println("You have no managed projects.");
        } else {
            // Display managed projects using manager's display method.
            System.out.println(manager.displayManagedProject());
        }
        System.out.println("Enter project ID to generate report (or 'b' to go back): ");
    }
    
    @Override
    public void handleInput() {
        String input = Prompt.prompt("Your choice: ");
        if(input.equalsIgnoreCase("b")){
            exit();
            return;
        }
        int projectId;
        try {
            projectId = Integer.parseInt(input);
        } catch(NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }
        List<BTOProject> managed = manager.getManagedProjects();
        BTOProject selectedProj = null;
        for (BTOProject project : managed) {
            if (project.getProjectId() == projectId) {
                selectedProj = project;
                break;
            }
        }
        if (selectedProj == null) {
            System.out.println("Invalid project ID.");
            return;
        }
        List<Application> bookedApps = new ArrayList<>();
        for(Application app : applications) {
            if(app.getProject().getProjectId() == selectedProj.getProjectId() &&
               app.getStatus() == entity.model.ApplicationStatus.BOOKED) {
                bookedApps.add(app);
            }
        }
        if(bookedApps.isEmpty()){
            System.out.println("No booked applications found for this project.");
        } else {
            // Ask if manager wants to apply a filter.
            Report report = new Report(bookedApps);
            System.out.println(report.toString());
            String applyFilter = Prompt.prompt("Would you like to apply a filter? (Y/N): ");
            if(applyFilter.equalsIgnoreCase("Y")) {
                System.out.println("Select filter option:");
                System.out.println("1. Filter by Marital Status");
                System.out.println("2. Filter by Flat Type");
                System.out.println("3. Filter by both Marital Status and Flat Type");
                try {
                    int filterOption = Integer.parseInt(Prompt.prompt("Enter option: "));
                    switch (filterOption) {
                        case 1 -> {
                            String status = Prompt.prompt("Enter Marital Status to filter by (Single or Married): ");
                            bookedApps.removeIf(app -> !app.getApplicant().getMaritalStatus().equalsIgnoreCase(status));
                        }
                        case 2 -> {
                            String flatType = Prompt.prompt("Enter Flat Type to filter by (2-Room or 3-Room): ");
                            bookedApps.removeIf(app -> !app.getFlatType().equalsIgnoreCase(flatType));
                        }
                        case 3 -> {
                            String status = Prompt.prompt("Enter Marital Status to filter by (Single or Married): ");
                            String flatType = Prompt.prompt("Enter Flat Type to filter by (2-Room or 3-Room): ");
                            bookedApps.removeIf(app -> 
                                !app.getApplicant().getMaritalStatus().equalsIgnoreCase(status) ||
                                !app.getFlatType().equalsIgnoreCase(flatType));
                        }
                        default -> System.out.println("Invalid filter option; no filter applied.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input; no filter applied.");
                }
            }
            if(bookedApps.isEmpty()){
                System.out.println("No booked applications found after applying the filter.");
            } else {
                report = new Report(bookedApps);
                System.out.println("Filtered Report:");
                System.out.println(report.toString());
            }
        }
        String back = Prompt.prompt("Type 'b' to go back: ");
        if(back.equalsIgnoreCase("b")){
            exit();
        }
    }
}
