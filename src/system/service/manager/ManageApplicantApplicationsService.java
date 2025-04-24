package system.service.manager;

import controller.ApplicationController;
import controller.NotificationController;
import entity.model.Application;
import entity.model.ApplicationStatus;
import entity.model.BTOProject;
import entity.user.HDBManager;
import java.util.ArrayList;
import java.util.List;
import ui.AbstractMenu;
import ui.Prompt;

public class ManageApplicantApplicationsService extends AbstractMenu {
    private HDBManager manager;
    private ApplicationController applicationController;
    private NotificationController notificationController;
    
    public ManageApplicantApplicationsService(HDBManager manager, ApplicationController applicationController, NotificationController notificationController) {
        this.manager = manager;
        this.applicationController = applicationController;
        this.notificationController = notificationController;
    }
    
    @Override
    public void display() {
        System.out.println("\n=== Manage Applicant Applications ===");
        List<Application> pendingApps = new ArrayList<>();
        for(BTOProject project : manager.getManagedProjects()){
            for(Application app : applicationController.getApplicationByProjectId(project.getProjectId())){
                if(app.getStatus() == ApplicationStatus.PENDING){
                    pendingApps.add(app);
                }
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
        for(BTOProject project : manager.getManagedProjects()){
            for(Application app : applicationController.getApplicationByProjectId(project.getProjectId())){
                if(app.getStatus() == ApplicationStatus.PENDING){
                    pendingApps.add(app);
                }
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
                    applicationController.updateApplication();
                    notificationController.send(selectedApp.getApplicant().getNric(), "Your application for a 2-Room flat has been APPROVED.");
                } else {
                    System.out.println("Insufficient 2-Room units. Cannot approve.");
                }
            } else if(selectedApp.getFlatType().equalsIgnoreCase("3-Room")){
                if(selectedApp.getProject().getUnits3Room() > 0){
                    selectedApp.setStatus(ApplicationStatus.SUCCESSFUL);
                    System.out.println("Application approved (SUCCESSFUL).");
                    applicationController.updateApplication();
                    notificationController.send(selectedApp.getApplicant().getNric(), "Your application for a 3-Room flat has been APPROVED.");
                } else {
                    System.out.println("Insufficient 3-Room units. Cannot approve.");
                }
            } else {
                System.out.println("Invalid flat type in application.");
            }
        } else if(decision.equalsIgnoreCase("R")){
            selectedApp.setStatus(ApplicationStatus.UNSUCCESSFUL);
            System.out.println("Application rejected.");
            applicationController.updateApplication();
            notificationController.send(selectedApp.getApplicant().getNric(), "Your application for a flat has been REJECTED.");
        } else {
            System.out.println("Invalid decision.");
        }
        String back = Prompt.prompt("Type 'b' to go back: ");
        if(back.equalsIgnoreCase("b")){
            exit();
        }
    }
}
