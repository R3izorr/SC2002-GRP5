package system.service.officer;

import entity.model.Application;
import entity.model.ApplicationStatus;
import entity.model.BTOProject;
import entity.model.Receipt;
import entity.user.HDBOfficer;
import java.util.ArrayList;
import java.util.List;
import ui.AbstractMenu;
import ui.Prompt;

public class GenerateReceiptService extends AbstractMenu {
    private HDBOfficer officer;
    private List<Application> applications;
    
    public GenerateReceiptService(HDBOfficer officer, List<Application> applications) {
        this.officer = officer;
        this.applications = applications;
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
        if (officer.getAssignedProjects().isEmpty()) {
            while (true) {
                String back = Prompt.prompt("Type 'b' to go back: ");
                if(back.equalsIgnoreCase("b")){
                    exit();
                    return;
                    }
                else {
                    System.out.println("Invalid input");
                }
            }
        }

        else {
            List<Application> bookedApps = new ArrayList<>();
            for(BTOProject proj : officer.getAssignedProjects()){
                for(Application app : applications){
                    if(app.getProject().getProjectId() == proj.getProjectId() &&
                    app.getStatus() == ApplicationStatus.BOOKED) {
                        bookedApps.add(app);
                    }
                }
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
                exit();
                return;
            }
            if(choice < 1 || choice > bookedApps.size()){
                System.out.println("Invalid selection.");
                return;
            }
            else {
                Application targetApp = bookedApps.get(choice - 1);
                Receipt receipt = new Receipt(targetApp);
                System.out.println("\nGenerated Receipt:");
                System.out.println(receipt.toString());
            }
            String back = Prompt.prompt("Type 'b' to go back: ");
            if(back.equalsIgnoreCase("b")){
                exit();
            }
        }
    }
}
