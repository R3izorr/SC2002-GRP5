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
            Report report = new Report(bookedApps);
            System.out.println(report.toString());
        }
        String back = Prompt.prompt("Type 'b' to go back: ");
        if(back.equalsIgnoreCase("b")){
            exit();
        }
    }
}
