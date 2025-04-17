package system.service.officer;

import java.util.ArrayList;
import java.util.List;
import model.Application;
import model.ApplicationStatus;
import model.BTOProject;
import model.Receipt;
import model.user.HDBOfficer;
import repository.ApplicationRepository;
import ui.AbstractMenu;
import ui.Prompt;

public class GenerateReceiptService extends AbstractMenu {
    private HDBOfficer officer;
    private ApplicationRepository applicationRepository;
    
    public GenerateReceiptService(HDBOfficer officer, ApplicationRepository applicationRepository) {
        this.officer = officer;
        this.applicationRepository = applicationRepository;
    }
    
    @Override
    public void display() {
        System.out.println("\n=== Generate Receipt for Booked Applicants ===");
        if(officer.getAssignedProjects().isEmpty()){
            System.out.println("You are not approved for any projects."); 
        }
    }
    
    @Override
    public void handleInput() {
        List<Application> bookedApps = new ArrayList<>();
        for(BTOProject proj : officer.getAssignedProjects()){
            for(Application app : applicationRepository.getApplications()){
                if(app.getProject().getProjectId() == proj.getProjectId() &&
                   app.getStatus() == ApplicationStatus.BOOKED) {
                    bookedApps.add(app);
                }
            }
        }
        if(bookedApps.isEmpty()){
            System.out.println("No booked applications found for your projects.");
            return;
        }
        System.out.println("=== List of Booked Applications ===");
        for(int i = 0; i < bookedApps.size(); i++){
            Application app = bookedApps.get(i);
            System.out.println((i+1) + ". Applicant Name: " + app.getApplicant().getName() +
                               " | Project: " + app.getProject().getProjectName() +
                               " | Flat Type: " + app.getFlatType());
        }
        int choice = Prompt.promptInt("Enter the number of the application to generate receipt (or 0 to cancel): ");
        if(choice == 0){
            System.out.println("Receipt generation cancelled.");
            return;
        }
        if(choice < 1 || choice > bookedApps.size()){
            System.out.println("Invalid selection.");
            return;
        }
        Application targetApp = bookedApps.get(choice - 1);
        Receipt receipt = new Receipt(targetApp);
        System.out.println("\nGenerated Receipt:");
        System.out.println(receipt.toString());
        String back = Prompt.prompt("Type 'b' to go back: ");
        if(back.equalsIgnoreCase("b")){
            exit();
        }
    }
}
