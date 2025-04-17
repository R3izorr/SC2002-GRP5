package system.service.manager;

import java.util.ArrayList;
import java.util.List;
import model.Application;
import model.ApplicationStatus;
import model.user.HDBManager;
import repository.ApplicationRepository;
import ui.AbstractMenu;
import ui.Prompt;

public class ManageApplicantApplicationsService extends AbstractMenu {
    private HDBManager manager;
    private ApplicationRepository applicationRepository;
    
    public ManageApplicantApplicationsService(HDBManager manager, ApplicationRepository applicationRepository) {
        this.manager = manager;
        this.applicationRepository = applicationRepository;
    }
    
    @Override
    public void display() {
        System.out.println("\n=== Manage Applicant Applications ===");
        List<Application> pendingApps = new ArrayList<>();
        for (Application app : applicationRepository.getApplications()){
            if(manager.getManagedProjects().contains(app.getProject()) &&
               app.getStatus() == ApplicationStatus.PENDING) {
                pendingApps.add(app);
            }
        }
        if(pendingApps.isEmpty()){
            System.out.println("No pending applications found.");
        } else {
            for (int i = 0; i < pendingApps.size(); i++){
                Application app = pendingApps.get(i);
                System.out.println((i+1) + ". Applicant Name: " + app.getApplicant().getName() +
                        " | Project: " + app.getProject().getProjectName() +
                        " | Flat Type: " + app.getFlatType());
            }
        }
        System.out.println("Enter application number to process (or 'b' to go back): ");
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
        List<Application> pendingApps = new ArrayList<>();
        for (Application app : applicationRepository.getApplications()){
            if(manager.getManagedProjects().contains(app.getProject()) &&
               app.getStatus() == ApplicationStatus.PENDING) {
                pendingApps.add(app);
            }
        }
        if(choice < 1 || choice > pendingApps.size()){
            System.out.println("Invalid selection.");
            return;
        }
        Application selectedApp = pendingApps.get(choice - 1);
        System.out.println("Selected Application: Applicant Name: " + selectedApp.getApplicant().getName() +
                " | Project: " + selectedApp.getProject().getProjectName() +
                " | Flat Type: " + selectedApp.getFlatType());
        String decision = Prompt.prompt("Enter A to Approve, R to Reject (or 'b' to cancel): ");
        if(decision.equalsIgnoreCase("b")){
            return;
        }
        if(decision.equalsIgnoreCase("A")){
            if(selectedApp.getFlatType().equalsIgnoreCase("2-Room")){
                if(selectedApp.getProject().getUnits2Room() > 0){
                    selectedApp.setStatus(ApplicationStatus.SUCCESSFUL);
                    System.out.println("Application approved (SUCCESSFUL).");
                    applicationRepository.saveApplications();
                } else {
                    System.out.println("Insufficient 2-Room units. Cannot approve.");
                }
            } else if(selectedApp.getFlatType().equalsIgnoreCase("3-Room")){
                if(selectedApp.getProject().getUnits3Room() > 0){
                    selectedApp.setStatus(ApplicationStatus.SUCCESSFUL);
                    System.out.println("Application approved (SUCCESSFUL).");
                    applicationRepository.saveApplications();
                } else {
                    System.out.println("Insufficient 3-Room units. Cannot approve.");
                }
            } else {
                System.out.println("Invalid flat type in application.");
            }
        } else if(decision.equalsIgnoreCase("R")){
            selectedApp.setStatus(ApplicationStatus.UNSUCCESSFUL);
            System.out.println("Application rejected.");
        } else {
            System.out.println("Invalid decision.");
        }
        String back = Prompt.prompt("Type 'b' to go back: ");
        if(back.equalsIgnoreCase("b")){
            exit();
        }
    }
}
