package system.service.manager;

import ui.AbstractMenu;
import ui.Prompt;
import java.util.ArrayList;
import java.util.List;
import model.Application;
import model.BTOProject;
import model.Report;
import model.HDBManager;
import repository.ApplicationRepository;

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
            for (int i = 0; i < managed.size(); i++){
                System.out.println((i+1) + ". " + managed.get(i).toStringForManagerOfficer());
            }
        }
        System.out.println("Enter project number to generate report (or 'b' to go back): ");
    }
    
    @Override
    public void handleInput() {
        String input = Prompt.prompt("Your choice: ");
        if(input.equalsIgnoreCase("b")){
            exit();
            return;
        }
        int choice;
        try {
            choice = Integer.parseInt(input);
        } catch(NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }
        List<BTOProject> managed = manager.getManagedProjects();
        if(choice < 1 || choice > managed.size()){
            System.out.println("Invalid selection.");
            return;
        }
        BTOProject selectedProj = managed.get(choice - 1);
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
