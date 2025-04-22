package system.service.manager;

import controller.NotificationController;
import controller.ProjectController;
import entity.model.BTOProject;
import entity.user.HDBManager;
import entity.user.HDBOfficer;
import java.util.ArrayList;
import java.util.List;
import ui.AbstractMenu;
import ui.Prompt;

public class ManageOfficerRegistrationsService extends AbstractMenu {
    private HDBManager manager;
    private List<HDBOfficer> officers;
    private ProjectController projectController;
    private NotificationController notificationController;
  
    
    public ManageOfficerRegistrationsService(HDBManager manager, List<HDBOfficer> officers, ProjectController projectController, 
            NotificationController notificationController) {
        this.manager = manager;
        this.officers = officers;
        this.projectController = projectController;
        this.notificationController = notificationController;
    }
    
    @Override
    public void display() {
        System.out.println("\n=== Manage HDB Officer Registrations ===");
        List<PendingRegistration> pendingRegs = new ArrayList<>();
        for (HDBOfficer off : officers){
            for (BTOProject proj : off.getPendingRegistrations()){
                if(manager.getManagedProjects().contains(proj)){
                    pendingRegs.add(new PendingRegistration(off, proj));
                }
            }
        }
        if(pendingRegs.isEmpty()){
            System.out.println("No pending officer registrations for your projects.");
        } else {
            for (int i = 0; i < pendingRegs.size(); i++){
                PendingRegistration reg = pendingRegs.get(i);
                System.out.println((i+1) + ". Officer Name: " + reg.officer.getName() +
                        " | Pending Registration for Project: " + reg.project.getProjectName() + "(ID: " + reg.project.getProjectId() + ")" +
                        " | Remaining Slots: " + reg.project.getOfficerSlots()
                        );
            }
        }
        System.out.println("Enter registration number to process (or 'b' to go back): ");
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
        List<PendingRegistration> pendingRegs = new ArrayList<>();
        for (HDBOfficer off : officers){
            for (BTOProject proj : off.getPendingRegistrations()){
                if(manager.getManagedProjects().contains(proj)){
                    pendingRegs.add(new PendingRegistration(off, proj));
                }
            }
        }
        if(choice < 1 || choice > pendingRegs.size()){
            System.out.println("Invalid selection.");
            return;
        }
        PendingRegistration selected = pendingRegs.get(choice - 1);
        System.out.println("Selected Registration: Officer Name: " + selected.officer.getName() +
                        " | Pending Registration for Project: " + selected.project.getProjectName() + 
                        "(ID: " + selected.project.getProjectId() + ")");
        String decision = Prompt.prompt("Enter A to Approve, R to Reject (or 'b' to cancel): ");
        if(decision.equalsIgnoreCase("b")){
            return;
        }
        if(decision.equalsIgnoreCase("A")){
            selected.officer.addAssignedProject(selected.project);
            selected.officer.getPendingRegistrations().remove(selected.project);
            selected.project.addOfficers(selected.officer);
            selected.project.setOfficerSlots(selected.project.getOfficerSlots() - 1);
            projectController.updateProject();
            System.out.println("Registration approved.");
            // Notify the officer
            String message = "Your registration for project %s (ID: %d) has been approved.".formatted(
                    selected.project.getProjectName(),
                    selected.project.getProjectId()
            );
            notificationController.send(selected.officer.getNric(), message);

        } else if(decision.equalsIgnoreCase("R")){
            selected.officer.getPendingRegistrations().remove(selected.project);
            System.out.println("Registration rejected.");
            // Notify the officer
            String message = "Your registration for project %s (ID: %d) has been rejected.".formatted(
                    selected.project.getProjectName(),
                    selected.project.getProjectId()
            );
            notificationController.send(selected.officer.getNric(), message);
        } else {
            System.out.println("Invalid decision.");
        }
        String back = Prompt.prompt("Type 'b' to go back: ");
        if(back.equalsIgnoreCase("b")){
            exit();
        }
    }
    
    private class PendingRegistration {
        public HDBOfficer officer;
        public BTOProject project;
        
        public PendingRegistration(HDBOfficer officer, BTOProject project){
            this.officer = officer;
            this.project = project;
        }
    }
}
