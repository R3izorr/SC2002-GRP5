package system.service.manager;

import java.util.ArrayList;
import java.util.List;
import model.Application;
import model.BTOProject;
import model.Report;
import model.user.HDBManager;
import repository.ApplicationRepository;
import ui.AbstractMenu;
import ui.Prompt;

public class GenerateReportService extends AbstractMenu {
    private HDBManager manager;
    private ApplicationRepository applicationRepository;
    
    public GenerateReportService(HDBManager manager, ApplicationRepository applicationRepository) {
        this.manager = manager;
        this.applicationRepository = applicationRepository;
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
        for(Application app : applicationRepository.getApplications()){
            if(app.getProject().getProjectId() == selectedProj.getProjectId() &&
               app.getStatus() == model.Application.Status.BOOKED) {
                bookedApps.add(app);
            }
        }
        if(bookedApps.isEmpty()){
            System.out.println("No booked applications found for this project.");
        } else {
            // Ask if manager wants to apply a filter.
            String applyFilter = Prompt.prompt("Would you like to apply a filter? (Y/N): ");
            if(applyFilter.equalsIgnoreCase("Y")) {
                System.out.println("Select filter option:");
                System.out.println("1. Filter by Marital Status");
                System.out.println("2. Filter by Flat Type");
                System.out.println("3. Filter by both Marital Status and Flat Type");
                int filterOption = Integer.parseInt(Prompt.prompt("Enter option: "));
                if(filterOption == 1) {
                    String status = Prompt.prompt("Enter Marital Status to filter by (e.g., Married, Single): ");
                    bookedApps.removeIf(app -> !app.getApplicant().getMaritalStatus().equalsIgnoreCase(status));
                } else if(filterOption == 2) {
                    String flatType = Prompt.prompt("Enter Flat Type to filter by (2-Room or 3-Room): ");
                    bookedApps.removeIf(app -> !app.getFlatType().equalsIgnoreCase(flatType));
                } else if(filterOption == 3) {
                    String status = Prompt.prompt("Enter Marital Status to filter by: ");
                    String flatType = Prompt.prompt("Enter Flat Type to filter by (2-Room or 3-Room): ");
                    bookedApps.removeIf(app -> 
                        !app.getApplicant().getMaritalStatus().equalsIgnoreCase(status) ||
                        !app.getFlatType().equalsIgnoreCase(flatType));
                } else {
                    System.out.println("Invalid filter option; no filter applied.");
                }
            }
            if(bookedApps.isEmpty()){
                System.out.println("No booked applications found after applying the filter.");
            } else {
                Report report = new Report(bookedApps);
                System.out.println(report.toString());
            }
        }
        String back = Prompt.prompt("Type 'b' to go back: ");
        if(back.equalsIgnoreCase("b")){
            exit();
        }
    }
}
